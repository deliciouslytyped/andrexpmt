package com.example.comp.ui.menus

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.comp.model.owner.DummyOwner
import com.example.comp.model.game.LetterTileModel
import com.example.comp.model.index.oldSampleLetter
import com.example.comp.ui.game.LetterTile
import com.example.comp.ui.game.tileSize
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.sqrt

class TileAnimationObject(val index: Int, val gridSize: Int, val screenHeight: Float, val screenWidth: Float, val letterGen : (() -> LetterTileModel), val density: Density) {
    var tileModel = letterGen()
    var duration = 3000
    var scale = jitteredGrid(index, 0.5f, 3f, 2f, 1f)
    var timeStartOffset = jitteredGrid(index, 500f, 3000f, 1f, 0f).toInt()
    var yOffsetStart = with(density) { -(tileSize.dp.toPx() * scale * sqrt(2f)) }
    var yOffset = Animatable(yOffsetStart)
    val yOffsetDp
        get() = with(density) { yOffset.value.toDp() }
    var xOffset = jitteredGrid(index, screenWidth/gridSize*3, screenWidth, 1f, 0f)
    val xOffsetDp
        get() = with(density) { xOffset.toDp() }
    var rotationStart = random() % 360
    var rotation = Animatable(rotationStart)
    fun random(): Float {
        return Random().nextInt().absoluteValue.toFloat()
    }

    fun jitteredGrid(index: Int, gridScale: Float, gridSize: Float, jitterScale: Float, offset: Float): Float {
        val gridpos = (index * gridScale) % gridSize
        val jitter = Random().nextInt() % gridScale * jitterScale
        return offset + (gridpos + jitter).absoluteValue
    }

    fun reinit(){
        yOffset = Animatable(yOffsetStart)
        xOffset = jitteredGrid(index, screenWidth/gridSize*3, screenWidth, 1f, 0f)
        timeStartOffset = jitteredGrid(index, 500f, 3000f, 1f,0f).toInt()
        scale = jitteredGrid(index, 0.5f, 3f, 2f, 1f)
        tileModel = letterGen()
    }

    suspend fun launchRotation(): AnimationResult<Float, AnimationVector1D> {
        return rotation.animateTo(
            targetValue = rotation.value + 360,
            animationSpec = infiniteRepeatable(
                animation = tween(1000 + Random().nextInt().absoluteValue % 5000)
            )
        )
    }

    suspend fun launchYTranslation(): AnimationResult<Float, AnimationVector1D> {
        return yOffset.animateTo(
            targetValue = screenHeight,
            /*animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = duration, easing = LinearEasing),
                initialStartOffset = StartOffset(offsetMillis = timeStartOffset)
            )*/
            animationSpec = tween(
                durationMillis = duration,
                easing = LinearEasing,
                delayMillis = timeStartOffset)
        )
    }

    suspend fun run(scope: (suspend (suspend CoroutineScope.() -> Unit) -> Unit)){
        scope {
            launch { while(isActive) {
                launchRotation()
            } }
            launch { while(isActive) {
                launchYTranslation()
                reinit()
            } }
        }
    }
}

@Composable
fun HomeAnimationArea() {
    val dummyOwner = remember { DummyOwner<LetterTileModel>() }
    //TODO particle source?
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenHeight = with(LocalDensity.current) { maxHeight.toPx() }
        val screenWidth = with(LocalDensity.current) { maxWidth.toPx() }
        val count = 16
        val letterGen = { LetterTileModel(oldSampleLetter(), dummyOwner) }//TODO I swear this thing keeps giving the same value for neighboring letters
        (0..<count).forEach { i ->
            val density = LocalDensity.current
            val ao = remember { TileAnimationObject(i, count, screenHeight, screenWidth, letterGen, density) }
            LaunchedEffect(Unit){
                ao.run(::coroutineScope)
            }
            LetterTile(
                modifier = Modifier
                    .offset(x = ao.xOffsetDp, y = ao.yOffsetDp)
                    .rotate(ao.rotation.value)
                    .scale(ao.scale),
                model = ao.tileModel,
                draggable = false
            )
        }
    }
}

