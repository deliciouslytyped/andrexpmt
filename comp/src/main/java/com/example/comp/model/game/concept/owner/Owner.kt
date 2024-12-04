package com.example.comp.model.game.concept.owner

// Implements tracking ownership of "tokens" (i.e. like tiles on the game board.)

// In practice, the drag manager must know the token, and the drag target,

// Implements capability to transfer LetterTileModel between owners while only having a reference to the tile and the new owner.
// Used to implement the object passing part of drag and drop.


//TODO move this note somewhere actually findable
// So the way this works is the draggable area wraps the tokens that can be dragged and exposes drag state via a composition local,
// DropTargets (currently) keep track of whether they are the currently overlapping element,
// and when dragging is released, the drop target sees that dragging is no longer happening, and invokes its content() with the dragged data
// the content() then launches an effect (TODO isnt this racy?) calling tile.move(target).
// the move operation should communicate with the target Owner<> object to change its owner to the target object, receiving some sort of failable Result,
// The Ownable object keeps track of its current owner as well, so that communication can happen through the ownable instead of spiderwebing around all possible owners?

interface Owner<T> {
    /*
    Must not have sideeffects on failure.
     */
    @Deprecated("TODO, not deprecated, but use move() on tile instead of the owners")
    fun moveTo(newOwner: Owner<T>, ownable: Ownable<T>): Boolean { //TODO synchronized or whatever? (should be doing everything on the ui thread anyway though?)
        if(newOwner.canAccept(ownable) and canRelease(ownable)){
            assert(release(ownable))
            assert(newOwner.accept(ownable))
            return true
        }
        return false
    }
    fun canAccept(ownable: Ownable<T>): Boolean
    fun accept(ownable: Ownable<T>): Boolean //TODO change return type to some generic result type?

    fun canRelease(ownable: Ownable<T>): Boolean

    fun release(tileModel: Ownable<T>): Boolean
}
