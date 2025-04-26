package dev.nhonnq.data.api

import dev.nhonnq.domain.entities.UserEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int,
    ): List<UserEntity>

    @GET("users/{login}")
    suspend fun getUserDetails(@Path("login") loginUserName: String): UserEntity
}