package com.glivion.plasticdiary.contracts

interface StatusCallbacks<T> {
    fun onComplete() {}

    fun onComplete(data: T) {}

    fun onFailure() {}

    fun onFailure(data: T) {}


}