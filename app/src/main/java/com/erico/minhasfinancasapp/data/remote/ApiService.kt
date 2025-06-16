package com.erico.minhasfinancasapp.data.remote

import com.erico.minhasfinancasapp.data.model.NovaTransacao
import com.erico.minhasfinancasapp.data.model.Transacao
import com.erico.minhasfinancasapp.data.model.NovoUsuario
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("token")
    suspend fun login(
        @Field("username") email: String,
        @Field("password") senha: String
    ): Response<Map<String, String>>

    @GET("transacoes")
    suspend fun getTransacoes(): Response<List<Transacao>>

    @POST("transacoes")
    suspend fun criarTransacao(@Body novaTransacao: NovaTransacao): Response<Transacao>

    @GET("transacoes/{id}")
    suspend fun getTransacaoPorId(@Path("id") id: Int): Response<Transacao>

    @PUT("transacoes/{id}")
    suspend fun atualizarTransacao(
        @Path("id") id: Int,
        @Body transacao: NovaTransacao
    ): Response<Transacao>

    @DELETE("transacoes/{id}")
    suspend fun deletarTransacao(@Path("id") id: Int): Response<Unit>

    @POST("usuarios")
    suspend fun criarUsuario(@Body novoUsuario: NovoUsuario): Response<Unit>
}