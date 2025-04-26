package dev.nhonnq.data.db.user

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.nhonnq.data.entities.UserData
import dev.nhonnq.data.entities.UserRemoteKeyDbData

@Database(
    entities = [UserData::class, UserRemoteKeyDbData::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userRemoteKeyDao(): UserRemoteKeyDao
}