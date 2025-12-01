package com.example.aplicacionmovil.data.remote

import com.example.aplicacionmovil.data.remote.dto.SensorDataDto
import retrofit2.http.GET

interface SensorApi {

    @GET("iot/data")
    suspend fun getSensorData(): SensorDataDto
}
