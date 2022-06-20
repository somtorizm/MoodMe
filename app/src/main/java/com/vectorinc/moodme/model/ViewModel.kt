package com.vectorinc.moodme.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {
    private val _name = MutableLiveData<Int>()
    val name: LiveData<Int> = _name

    fun selectButton(){
        _name.value = 0
    }
    fun selectButton1(){
        _name.value = 1
    }

}