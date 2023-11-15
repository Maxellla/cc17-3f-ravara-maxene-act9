package com.entropia.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: Favorite)

    @Delete
    suspend fun removeFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite ORDER BY departure_code ASC")
    fun getAll(): Flow<List<Favorite>>

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun getFavorite(id: Int): Favorite
}