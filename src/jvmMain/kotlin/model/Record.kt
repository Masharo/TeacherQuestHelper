package model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Record(
    var name: MutableState<String> = mutableStateOf(""),
    val title: MutableState<String> = mutableStateOf(""),
    val answers: SnapshotStateList<Answer> = mutableStateListOf(),
    var oneTrueAnswer: MutableState<Boolean> = mutableStateOf(true)
)