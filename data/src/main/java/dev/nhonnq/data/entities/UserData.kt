package dev.nhonnq.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.nhonnq.domain.entities.IUser
import dev.nhonnq.domain.entities.UserEntity

@Entity(tableName = "users")
data class UserData(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    override val login: String,
    override val avatarUrl: String?,
    override val htmlUrl: String?,
    var location: String? = null,
    var followers: Int? = null,
    var following: Int? = null,
    var blog: String? = null,
) : IUser

fun UserData.toDomain() = UserEntity(
    login = login,
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl,
    location = location,
    followers = followers,
    following = following,
    blog = blog,
)