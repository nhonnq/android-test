package dev.nhonnq.domain.entities

import com.google.gson.annotations.SerializedName

data class UserEntity(
    @SerializedName("login") override val login: String,
    @SerializedName("avatar_url") override val avatarUrl: String?,
    @SerializedName("html_url") override val htmlUrl: String?,
    @SerializedName("location") val location: String? = null,
    @SerializedName("followers") val followers: Int? = null,
    @SerializedName("following") val following: Int? = null,
    @SerializedName("blog") val blog: String? = null,
) : IUser