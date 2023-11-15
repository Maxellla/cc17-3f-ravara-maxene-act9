package com.entropia.flightsearch.ui

import androidx.lifecycle.ViewModel
import com.entropia.flightsearch.data.Airport
import com.entropia.flightsearch.data.Favorite

class FlightSearchViewModel : ViewModel() {



}

data class FlightSearchUi(
    val currentAirport: Airport?=null,
    val flightList: List<Airport> = listOf(),
    val favoriteList: MutableList<Favorite> = mutableListOf()
)