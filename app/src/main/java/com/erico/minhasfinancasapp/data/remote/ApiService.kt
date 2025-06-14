// ApiService.kt

package com.erico.minhasfinancasapp.data.remote

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    // Adicione esta função
    @FormUrlEncoded
    @POST("token")
    suspend fun login(
        @Field("username") email: String,
        @Field("password") senha: String
    ): Response<Map<String, String>> // Receberemos uma resposta com o token
}