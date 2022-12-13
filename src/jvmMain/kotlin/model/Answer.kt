package model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Answer(
    var title: MutableState<String> = mutableStateOf(""),
    val isTrue: MutableState<Boolean> = mutableStateOf(false)
)
