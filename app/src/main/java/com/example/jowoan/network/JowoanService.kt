package com.example.jowoan.network

import com.example.jowoan.models.*
import com.example.jowoan.models.lesson.Lesson
import retrofit2.Call
import retrofit2.http.*

interface JowoanService {
    @GET("avatar")
    fun avatarGetAll(@Header("Token") token: String): Call<APIResponse<List<Avatar>>>

    @POST("user/emailSignUp")
    fun emailSignUp(@Body user: User): Call<APIResponse<User>>

    @POST("user/emailSignIn")
    fun emailSignIn(@Body user: User): Call<APIResponse<User>>

    @POST("user/tokenSignUp")
    fun tokenSignUp(@Body user: User): Call<APIResponse<User>>

    @POST("user/tokenSignIn")
    fun tokenSignIn(@Body user: User): Call<APIResponse<User>>

    @GET("practice")
    fun practiceGetAll(@Header("Token") token: String): Call<APIResponse<List<Practice>>>

    @PUT("user/{id}")
    fun userUpdate(
        @Header("Token") token: String,
        @Path("id") id: Int,
        @Body user: User
    ): Call<APIResponse<User>>

    @GET("user/{id}")
    fun userGet(
        @Header("Token") token: String,
        @Path("id") id: Int
    ): Call<APIResponse<User>>

    @GET("lesson")
    fun lessonGet(
        @Header("Token") token: String,
        @Query("subpractice_id") subpracticeID: String
    )
            : Call<APIResponse<List<Lesson>>>

    @GET("completion/{user_id}")
    fun completionGetAll(
        @Header("Token") token: String,
        @Path("user_id") userID: Int
    ): Call<APIResponse<List<Completion>>>

    @POST("completion")
    fun completionUpsert(
        @Header("Token") token: String,
        @Body completion: Completion
    ): Call<APIResponse<Completion>>

    @GET("activity")
    fun activityGetAll(
        @Header("Token") token: String,
        @Query("user_id") userID: Int
    ): Call<APIResponse<List<Activity>>>

    @POST("activity")
    fun activityCreate(
        @Header("Token") token: String,
        @Body activity: Activity
    ): Call<APIResponse<Activity>>

    @GET("search-user")
    fun searchUserByName(
        @Header("Token") token: String,
        @Query("keyword") keyword: String
    ): Call<APIResponse<List<User>>>

    @POST("buy-avatar/{user_id}/{avatar_id}")
    fun buyAvatar(
        @Header("Token") token: String,
        @Path("user_id") userID: Int,
        @Path("avatar_id") avatarID: Int
    ): Call<APIResponse<String>>

    // Friends
    @GET("friendship/{user_id}")
    fun friendGetAll(
        @Header("Token") token: String,
        @Path("user_id") userID: Int
    ): Call<APIResponse<List<User>>>

    @GET("friendship/{user_id}/requests")
    fun friendGetAllRequests(
        @Header("Token") token: String,
        @Path("user_id") userID: Int
    ): Call<APIResponse<List<User>>>

    @POST("friendship")
    fun friendRequest(
        @Header("Token") token: String,
        @Body friendship: Friendship
    ): Call<APIResponse<Friendship>>

    @PUT("friendship")
    fun friendAccept(
        @Header("Token") token: String,
        @Body friendship: Friendship
    ): Call<APIResponse<Friendship>>

    @HTTP(method = "DELETE", path = "friendship", hasBody = true)
    fun friendReject(
        @Header("Token") token: String,
        @Body friendship: Friendship
    ): Call<APIResponse<Friendship>>
}