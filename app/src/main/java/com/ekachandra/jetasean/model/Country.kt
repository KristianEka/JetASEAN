package com.ekachandra.jetasean.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country")
data class Country(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val image: Int,
    val title: String,
    val capital: String,
    val membership: String,
    val contribution: String,
)
