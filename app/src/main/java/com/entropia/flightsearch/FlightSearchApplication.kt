package com.entropia.flightsearch

import android.app.Application
import com.entropia.flightsearch.data.FlightSearchDatabase

class FlightSearchApplication : Application() {
    val database: FlightSearchDatabase by lazy { FlightSearchDatabase.getDatabase(this) }
}