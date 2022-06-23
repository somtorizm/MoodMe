package com.vectorinc.moodme.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.ar.core.Session

class ViewModel : ViewModel() {
    private val _name = MutableLiveData<Int>()
    val name: LiveData<Int> = _name

    private val _delete = MutableLiveData<Int>()
    val delete: LiveData<Int> = _delete


    fun selectButton(){
        _name.value = 0

    }
    fun selectButton1(){
        _name.value = 1
    }

    fun deleteButton(){
        _delete.value = 0
    }

}