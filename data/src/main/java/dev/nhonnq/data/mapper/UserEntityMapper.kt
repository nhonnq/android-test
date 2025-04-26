package dev.nhonnq.data.mapper

import dev.nhonnq.data.entities.UserData
import dev.nhonnq.domain.entities.UserEntity

fun UserEntity.toDb() = UserData(
    id = null,
    login = login,
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl,
    location = location,
    followers = followers,
    following = following,
    blog = blog,
)