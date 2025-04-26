package dev.nhonnq.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_remote_keys")
data class UserRemoteKeyDbData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prevPage: Int?,
    val nextPage: Int?
)