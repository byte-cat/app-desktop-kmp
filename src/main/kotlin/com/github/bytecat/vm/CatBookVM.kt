package com.github.bytecat.vm

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.github.bytecat.contact.Cat

class CatBookVM {

    val myCat = mutableStateOf<Cat?>(null)
    val cats = mutableStateListOf<Cat>()

    fun addCat(cat: Cat) {
        if (cats.contains(cat)) {
            return
        }
        cats.add(cat)
    }

    fun removeCat(cat: Cat) {
        cats.remove(cat)
    }

}