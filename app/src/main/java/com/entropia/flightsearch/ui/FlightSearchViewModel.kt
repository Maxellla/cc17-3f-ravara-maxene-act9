package com.entropia.flightsearch.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.entropia.flightsearch.data.Airport
import com.entropia.flightsearch.data.Favorite
import com.entropia.flightsearch.data.FlightSearchRepository

class FlightSearchViewModel(private val repository: FlightSearchRepository) : ViewModel() {

    var flightSearchUi by mutableStateOf(FlightSearchUi())
        private set




}

data class FlightSearchUi(
    val currentAirport: Airport? = null,
    val flightList: List<Airport> = listOf(),
    val favoriteList: MutableList<Favorite> = mutableListOf()
)