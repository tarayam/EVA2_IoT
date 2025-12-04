package com.example.aplicacionmovil.data.remote
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
object HttpClient {

    // ⚠️ IMPORTANTE: CAMBIA ESTO DEPENDIENDO DE CÓMO PRUEBES ⚠️
    // Emulador Android Studio -> 10.0.2.2
    // Celular real -> IP local, por ejemplo "http://192.168.1.50:3000/"
    private const val BASE_URL = "http://34.193.64.16:3000/"
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttp: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    // 👉 NUEVO: API de sensores
    val sensorApi: SensorApi = retrofit.create(SensorApi::class.java)
    // 👉 NUEVO: API de usuarios
    val userApi: UserApi = retrofit.create(UserApi::class.java)
}
