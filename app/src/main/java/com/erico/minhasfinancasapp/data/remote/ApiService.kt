package com.erico.minhasfinancasapp.data.remote

import com.erico.minhasfinancasapp.data.model.Transacao // Importe o novo modelo
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET // Importe o GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("token")
    suspend fun login(
        @Field("username") email: String,
        @Field("password") senha: String
    ): Response<Map<String, String>>

    // ADICIONE ESTE NOVO ENDPOINT
    @GET("transacoes")
    suspend fun getTransacoes(): Response<List<Transacao>>
}