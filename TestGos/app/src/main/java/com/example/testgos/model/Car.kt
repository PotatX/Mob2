package com.example.testgos.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Car(
    @ColumnInfo(name = "carName") val carName: String,
    @ColumnInfo(name = "state") val carState: String,
    @ColumnInfo(name = "client") val client: String,
    @PrimaryKey(autoGenerate = true) val id: Int? = null)

enum class CarState {
    None,
    New,
    InUse,
    Deleted,
}