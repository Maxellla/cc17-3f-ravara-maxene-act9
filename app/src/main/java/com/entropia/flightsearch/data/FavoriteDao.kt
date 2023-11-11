package com.entropia.flightsearch.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite ORDER BY departure_code ASC")
    fun getAll(): Flow<List<Favorite>>
}