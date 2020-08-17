package me.ycdev.android.demo.customviews.ui.layout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LayoutViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is layout Fragment"
    }
    val text: LiveData<String> = _text
}