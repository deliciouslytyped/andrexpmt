package com.example.comp.model.game.concept.owner

import androidx.annotation.CheckResult
import io.github.oshai.kotlinlogging.KotlinLogging

interface Ownable<T> { // TODO Should T be the base type, or Owner<T>?
    //TODO should owner var be private?
    fun getOwner(): Owner<T> //TODO instead of this, transfer owner?
    fun setOwner(newOwner: Owner<T>)

    fun self(): T { //TODO enforce
        return this as T
    }

    //TODO note this isnt an owner, it doesnt own itself. (TODO I guess it could?)
    // It just needs to know its parent so we can move it around when dragging and dropping //TODO other way to do this?
    @CheckResult
    fun move(newOwner: Owner<T>): Boolean { //TODO this code is too distributed
        val logger = KotlinLogging.logger {}
        logger.debug { "attempt move, canacept:${newOwner.canAccept(this)} canrelease:${getOwner().canRelease(this)}, owner:${getOwner()}" }
        logger.debug { "newowner ${newOwner}" }

        if(getOwner().moveTo(newOwner, this)){
            setOwner(newOwner) //TODO this is a mess?
            logger.debug { "final owner ${getOwner()} {}" }
            return true
        } else {
            logger.debug { "final owner ${getOwner()} {}" }
            return false
        }
    }
}