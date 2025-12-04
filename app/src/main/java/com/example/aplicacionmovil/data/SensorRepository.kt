package com.example.aplicacionmovil.data

import com.example.aplicacionmovil.data.remote.HttpClient
import com.example.aplicacionmovil.data.remote.SensorApi
import com.example.aplicacionmovil.data.remote.dto.SensorDataDto

class SensorRepository(
    private val api: SensorApi = HttpClient.sensorApi
) {

    suspend fun getSensorData(): Result<SensorDataDto> {
        return try {
            val data = api.getSensorData()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
