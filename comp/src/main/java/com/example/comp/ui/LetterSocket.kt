package com.example.comp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp.dnd.DropItem
import com.example.comp.model.LetterSocketModel
import com.example.comp.model.LetterTileModel
import com.example.comp.ui.theme.game.creamSocketBorderInner
import com.example.comp.ui.theme.game.creamSocketBorderOuter
import com.example.comp.ui.theme.game.socketBackground

//TODO visuals similar to tile, create common TileLike?
@Composable
fun LetterSocket(modifier: Modifier = Modifier, model: LetterSocketModel) {
    DropItem<LetterTileModel> { isCurrentDropTarget, tile ->
        if (tile != null){
            LaunchedEffect(key1 = isCurrentDropTarget, key2 = tile) {
                tile.move(model) //TODO figure out how to IOC this or something
            }
        }
        Box(
            modifier
                .width(50.dp)
                .height(50.dp)
                .aspectRatio(1f) // See lazyhorizontalgrid usage
                .border(3.dp, creamSocketBorderInner)
                .border(2.dp, creamSocketBorderOuter)
                .background(if (isCurrentDropTarget) Color(0xFFFFFFFF) else socketBackground),
            contentAlignment = Alignment.Center
        ) {
            if (model.tile == null) {
                model.label?.let { label -> Text(label, fontSize = 30.sp) }
            } else {
                model.tile?.let { tileModel ->
                    LetterTile(
                        model = tileModel,
                        draggable = false )
                }
            }
        }
    }
}

@Preview
@Composable
fun LetterSocketPreviewEmpty() {
    val model = LetterSocketModel()
    LetterSocket(model = model)
}

@Preview
@Composable
fun LetterSocketPreviewWithLabel() {
    val model = LetterSocketModel()
    model.label = "+1"
    LetterSocket(model = model)
}

@Preview
@Composable
fun LetterSocketPreviewWithTile() {
    val model = LetterSocketModel()
    model.label = "+1"
    model.tile = LetterTileModel("A", owner = model)
    LetterSocket(model = model)
}