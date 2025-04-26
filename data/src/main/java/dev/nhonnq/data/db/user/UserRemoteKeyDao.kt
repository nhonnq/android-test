package dev.nhonnq.data.db.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.nhonnq.data.entities.UserRemoteKeyDbData

@Dao
interface UserRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRemoteKey(keys: UserRemoteKeyDbData)

    @Query("SELECT * FROM users_remote_keys WHERE id=:id")
    suspend fun getRemoteKeyByUserId(id: Int): UserRemoteKeyDbData?

    @Query("DELETE FROM users_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM users_remote_keys WHERE id = (SELECT MAX(id) FROM users_remote_keys)")
    suspend fun getLastRemoteKey(): UserRemoteKeyDbData?
}