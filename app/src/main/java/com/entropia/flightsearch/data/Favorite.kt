package com.entropia.flightsearch.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "favorite")
@TypeConverters(AirportConverter::class)
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "departure_code")
    val departureAirport: Airport,
    @ColumnInfo(name = "destination_code")
    val destinationAirport: Airport
)


