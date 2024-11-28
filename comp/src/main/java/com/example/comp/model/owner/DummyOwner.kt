package com.example.comp.model.owner

class DummyOwner<T> : Owner<T> {
    override fun move(newOwner: Owner<T>, model: T) {
        TODO("Not yet implemented")
    }

    override fun accept(mdoel: T) {
        TODO("Not yet implemented")
    }

    override fun canAccept(t: T): Boolean {
        TODO("Not yet implemented")
    }
}