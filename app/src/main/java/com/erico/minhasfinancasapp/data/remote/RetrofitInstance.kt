package com.erico.minhasfinancasapp.data.remote

import android.content.Context
import com.erico.minhasfinancasapp.data.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// O nome do objeto permanece o mesmo
object RetrofitInstance {

    private const val BASE_URL = "http://10.0.0.119:8000/" // Use o seu IP real

    // A MUDANÇA ESTÁ AQUI: Criamos uma função que constrói e retorna o Retrofit
    fun getRetrofitInstance(context: Context): Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = AuthInterceptor(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

// O Interceptor continua aqui
class AuthInterceptor(private val context: Context) : okhttp3.Interceptor {
    private val userPreferencesRepository = UserPreferencesRepository(context)

    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val token = runBlocking {
            userPreferencesRepository.authToken.first()
        }

        val request = chain.request().newBuilder()
        token?.let {
            request.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(request.build())
    }
}