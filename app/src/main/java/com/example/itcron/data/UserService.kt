package com.example.itcron.data

import com.example.itcron.data.model.UserModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface UserService {

    @GET("/users")
    fun getAllUsers(
        @Query("since") since: Int,
        @Query("per_page") per_page: Int
    ): Single<List<UserModel>>

    @GET("/users/{username}")
    fun getUser(@Path("username") username: String): Single<UserModel>

}