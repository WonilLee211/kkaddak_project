package com.ssafy.kkaddak.data.remote.service

import com.ssafy.kkaddak.data.remote.datasource.base.BaseResponse
import com.ssafy.kkaddak.data.remote.datasource.user.LogoutRequest
import com.ssafy.kkaddak.data.remote.datasource.user.UserResponse
import com.ssafy.kkaddak.data.remote.datasource.user.WalletAccountRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UserApiService {
    @Multipart
    @POST("/api/v1/members/sign-up")
    suspend fun createUserInfo(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?
    ): BaseResponse<UserResponse>

    @GET("/api/v1/members/nicknames/{nickname}/exists")
    suspend fun checkDuplication(@Path("nickname") nickname: String): BaseResponse<Boolean>

    @POST("/api/v1/members/cancel-sign-up")
    suspend fun cancelSignUp(): BaseResponse<Boolean>

    @GET("/api/v1/members/member-image")
    suspend fun getUserProfile(): String?

    @POST("/api/v1/members/logout")
    suspend fun logout(@Body body: LogoutRequest): BaseResponse<Boolean>

    @POST("/api/v1/members/account")
    suspend fun registerAccount(@Body body: WalletAccountRequest): BaseResponse<Boolean>

    @POST("/api/v4/pay/exchange")
    suspend fun chargeCoin(@Body receiptId: String): BaseResponse<Boolean>
}