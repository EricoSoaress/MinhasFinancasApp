package com.erico.minhasfinancasapp.data.remote

import com.erico.minhasfinancasapp.data.model.NovaTransacao
import com.erico.minhasfinancasapp.data.model.Transacao
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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

    @POST("transacoes")
    suspend fun criarTransacao(@Body novaTransacao: NovaTransacao): Response<Transacao>

}