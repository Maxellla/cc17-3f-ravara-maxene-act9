package com.entropia.flightsearch.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {

    @Query("SELECT * FROM airport WHERE iata_code LIKE :input+'%' OR name LIKE '%'+ :input + '%'")
    fun getAirportByName(input: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport ORDER BY passengers DESC")
    fun getAllByPassengers(): Flow<List<Airport>>
}