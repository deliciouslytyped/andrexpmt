package com.example.comp.dnd

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import io.github.oshai.kotlinlogging.KotlinLogging

internal class DraggableComposableInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dataToDrop by mutableStateOf<Any?>(null)
}

internal class MultiDragInfo {
    var activeDrags by mutableStateOf(mapOf<PointerId,DraggableComposableInfo>()) //TODO mutablestatelistof?
}

internal val LocalDragTargetInfo = compositionLocalOf { MultiDragInfo() } //TODO correct init?

//TODO of course multidrag enables all kinds of shenanigans, like duplicating tiles if you dont remove them from the source or whatever, for example
// For example, the tile stack is not a stack of tiles, and only accounts for one drag at time.
//TODO might as well implement a physics engine and go 3d lol....
// Finger release order reshuffling of shelf results in changing dragged composables models as well? :P


//TODO I still dont 100% understand how this works, regarding the .invoke() and content()s probably,
// but it seems like the way this works is that DraggableScreen is responsible for drawing the draggable area,
// and exposes the ambient LocalDragTargetInfo,
// while DraggableComposable manages the state of a draggable through its event callbacks and the ambient state variable
// (this is consistent with state hoisting?)
// (TODO Im still a bit funky on the viewmodel) / isnt there something that there will only be one viewmodel?
    //TODO yeah this is kind of broken
// DropItem also works by being in the ambient scope of the DraggableScreen, the LocalDragTargetInfo is what connects them.
// So basically the way this whole thing works is mutating some ambient state.

// Multitouch implemented with help from sonnet, there are some doubts as to the correctness of various parts of the implementation, including cleanup of items in the drag map.

@Composable
fun DraggableScreen(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val state = remember { MultiDragInfo() }
    CompositionLocalProvider(
        LocalDragTargetInfo provides state
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            content()
            state.activeDrags.values.forEach { dragInfo -> // Only had to wrap the single-case with a forEach to do multi
                if (dragInfo.isDragging) {
                    var targetSize by remember { mutableStateOf(IntSize.Zero) }
                    Box(modifier = Modifier
                        .graphicsLayer {
                            val offset = (dragInfo.dragPosition + dragInfo.dragOffset)
                            scaleX = 1.3f
                            scaleY = 1.3f
                            alpha = if (targetSize == IntSize.Zero) 0f else .9f
                            translationX = offset.x.minus(targetSize.width / 2)
                            translationY = offset.y.minus(targetSize.height / 2)
                        }
                        .onGloballyPositioned {
                            targetSize = it.size
                        }
                    ) {
                        dragInfo.draggableComposable?.invoke()
                    }
                }
            }
        }
    }
}

@Composable
fun <T> DraggableComposable(
    modifier: Modifier = Modifier,
    dataToDrop: T,
    model: DraggableModel,
    dragVisual: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit),
) {

    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    val multiDragState = LocalDragTargetInfo.current

    Box(modifier = modifier
        .onGloballyPositioned {
            currentPosition = it.localToWindow(Offset.Zero)
        }
        .multiFingerDrag { pointers ->
            pointers.forEach { change ->
                // Get or create DraggableComposableInfo for this pointer
                val dragInfo = multiDragState.activeDrags[change.id] ?: DraggableComposableInfo().also { newDragInfo ->
                    // Initialize new drag
                    model.startDragging() //TODO we arent even using the model...
                    newDragInfo.dataToDrop = dataToDrop
                    newDragInfo.isDragging = true
                    newDragInfo.dragPosition = currentPosition + change.position
                    newDragInfo.draggableComposable = dragVisual ?: content
                    multiDragState.activeDrags += (change.id to newDragInfo)
                }

                if (change.pressed) {
                    // Update existing drag
                    val positionChange = change.position - change.previousPosition
                    dragInfo.dragOffset += positionChange
                } else {
                    // End this drag
                    model.stopDragging()
                    dragInfo.isDragging = false
                    dragInfo.dragOffset = Offset.Zero
                    //multiDragState.activeDrags -= change.id // Moved cleaning up finished drop to droptarget, hopefully its correct? (pointer id reuse?)
                }
            }
        }) {
        content()
    }
}

@Composable
fun <T> DropTarget(
    modifier: Modifier = Modifier,
    content: @Composable (BoxScope.(isInBound: Boolean, data: T?) -> Unit)
) {
    val logger = KotlinLogging.logger {}
    val multiDragState = LocalDragTargetInfo.current
    var isHover by remember { mutableStateOf(false) }
    var currentDropData by remember { mutableStateOf<T?>(null) } //TODO need some kind of protocol for only using this up once

    //TODO hacked together moving this out of the ongloballypositioned
    var myRect by remember { mutableStateOf<Rect?>(null) }
    isHover = multiDragState.activeDrags.values
        .any { dragInfo ->
            dragInfo.isDragging && myRect?.contains(dragInfo.dragPosition + dragInfo.dragOffset) ?: false
        }
    currentDropData = ;
    /*val activeTarget = multiDragState.activeDrags.entries
        .firstOrNull { (_, dragInfo) ->
            !dragInfo.isDragging && (myRect?.contains(dragInfo.dragPosition + dragInfo.dragOffset) ?: false)
        }
    currentDropData = if (activeTarget != null) {
        activeTarget.let { (id, completedDrag) ->
            logger.debug { "setting drop data ${completedDrag.dataToDrop}" }
            val droppedData = completedDrag.dataToDrop as T?
            // Clean up after getting the data
            multiDragState.activeDrags -= id
            droppedData
        }
    } else null*/

    Box(modifier = modifier.onGloballyPositioned {
        it.boundsInWindow().let { rect ->
            myRect = rect
        }
    }) {
        content(isHover, currentDropData) //I dont like this approach, need to rearchitect probably
        currentDropData = null
    }
}

// sonnet
fun Modifier.multiFingerDrag(
    onDrag: (List<PointerInputChange>) -> Unit
) = this.pointerInput(Unit) {
    awaitEachGesture {
        // Wait for first touch
        awaitFirstDown(requireUnconsumed = false)

        // Track all pointers that are down
        do {
            val event = awaitPointerEvent()
            val changes = event.changes

            // Report all active pointers
            onDrag(changes)

            // Consume the changes
            changes.forEach { it.consume() }

        } while (changes.any { it.pressed })
    }
}

/* TODO put in a test or something

// Example usage:
Box(
    modifier = Modifier
        .size(200.dp)
        .multiFingerDrag { pointers ->
            // Calculate average movement
            val avgDx = pointers.map { it.positionChange().x }.average().toFloat()
            val avgDy = pointers.map { it.positionChange().y }.average().toFloat()

            // Use the movement values
            offsetX += avgDx
            offsetY += avgDy
        }
)

*/