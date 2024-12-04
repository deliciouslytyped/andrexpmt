package com.example.comp.model.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.comp.model.game.concept.owner.Ownable
import com.example.comp.model.game.concept.owner.Owner
import io.github.oshai.kotlinlogging.KotlinLogging

// Game board spaces that can hold a tile
open class LetterSocketModel : ViewModel(), Owner<LetterTileModel> {
    var tile by mutableStateOf<LetterTileModel?>(null)
    var label by mutableStateOf<String?>(null)
    override fun canAccept(t: Ownable<LetterTileModel>): Boolean {
        return tile == null
    }

    override fun accept(tileModel: Ownable<LetterTileModel>): Boolean {
        tile = tileModel.self()
        return true //TODO actually check
    }

    override fun canRelease(ownable: Ownable<LetterTileModel>): Boolean {
        return tile == ownable.self() //TODO is this correct?  / tile != null? / should throw some exceptions sanity checks
    }

    override fun release(tileModel: Ownable<LetterTileModel>): Boolean {
        assert(tileModel == tile)
        tile = null

        var logger = KotlinLogging.logger {}
        logger.debug { "remove tilesocket: ${tileModel.self().label} $tileModel from $this" }
        return true
    }
}