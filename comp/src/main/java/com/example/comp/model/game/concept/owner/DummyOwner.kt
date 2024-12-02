package com.example.comp.model.game.concept.owner

class DummyOwner<T> : Owner<T> {

    override fun accept(mdoel: Ownable<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun canAccept(t: Ownable<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun canRelease(ownable: Ownable<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun release(tileModel: Ownable<T>): Boolean {
        TODO("Not yet implemented")
    }
}