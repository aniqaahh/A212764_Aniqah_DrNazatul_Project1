package com.example.a212764_aniqah_drnazatul_project1.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf

// 1. Letak Review kat atas sekali
data class Review(
    val username: String,
    val comment: String,
    val rating: Float
)

// 2. Trail kena ada var rating & reviews list
data class Trail(
    val id: Int,
    val title: String,
    val location: String,
    val level: String,
    var rating: String,
    val length: String,
    val elevation: String,
    val time: String,
    val imageRes: Int,
    val mapRes: Int,
    val isSaved: MutableState<Boolean> = mutableStateOf(false),
    val reviews: SnapshotStateList<Review> = mutableStateListOf()// to know which trail to review
)