package com.example.comp.ui.game

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp.dnd.DropItem
import com.example.comp.model.game.LetterBoardSocketModel
import com.example.comp.model.game.LetterSocketModel
import com.example.comp.model.game.LetterTileModel
import com.example.comp.ui.theme.game.*

//TODO visuals similar to tile, create common TileLike?
@Composable
fun LetterSocket(modifier: Modifier = Modifier, model: LetterSocketModel, dropEnabled: Boolean = true) {
    DropItem<LetterTileModel> { isCurrentDropTarget, tile ->
        if ( tile != null && model.tile == null && dropEnabled){
            LaunchedEffect(key1 = isCurrentDropTarget, key2 = tile) {
                tile.move(model) //TODO figure out how to IOC this or something
            }
        }
        SocketContent(modifier, model, isCurrentDropTarget, dropEnabled)
    }
}

@Composable
fun SocketContent(modifier: Modifier = Modifier, model: LetterSocketModel, isCurrentDropTarget : Boolean, dropEnabled: Boolean){
    Box(
        modifier
            .width(50.dp)
            .height(50.dp)
            .aspectRatio(1f) // See lazyhorizontalgrid usage
            .border(3.dp, creamSocketBorderInner)
            .border(2.dp, creamSocketBorderOuter)
            .background(
                if (isCurrentDropTarget && dropEnabled)
                    dropColor
                else (if (model.label != null) labelColor
                    else socketBackground)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (model.tile == null) {
            model.label?.let { label ->
                Text(text = label,
                    fontSize = if(label.length == 2) 30.sp else 20.sp)
            }
        } else {
            model.tile?.let { tileModel ->
                LetterTile(
                    model = tileModel,
                    draggable = false )
            }
        }
    }
}

//TODO cant figure out how to handle drag and drop
@Composable
fun LetterBoardSocket(modifier: Modifier = Modifier, model: LetterBoardSocketModel){
    Box {
        LetterSocket(modifier, model, dropEnabled = !model.burned.value)
        Box(modifier = Modifier.background(Color(0x95FF5900)))
    }
}