package com.consumers.pitchcatalystassignment.model

data class ListModel(
    val title : String? = null,
    val body : String? = null,
    val createdAt : Long,
    val isCompleted : Boolean = false
)
