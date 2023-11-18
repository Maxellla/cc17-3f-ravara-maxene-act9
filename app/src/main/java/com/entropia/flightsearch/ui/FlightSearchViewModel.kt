package com.entropia.flightsearch.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.entropia.flightsearch.FlightSearchApplication
import com.entropia.flightsearch.data.Airport
import com.entropia.flightsearch.data.Favorite
import com.entropia.flightsearch.data.FlightSearchRepository
import com.entropia.flightsearch.data.OfflineRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class FlightSearchViewModel(private val repository: FlightSearchRepository) : ViewModel() {

    var flightSearchUi by mutableStateOf(FlightSearchUi())
        private set

    fun updateCurrentAirport(airport: Airport) {
        flightSearchUi.copy(
            currentAirport = airport
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getSearchResultsList(input: String) {
        viewModelScope.launch {
            flightSearchUi.copy(
                //TODO
            )
            Log.d("airports", flightSearchUi.suggestedAirportList.toString())
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
                val repository = OfflineRepository(
                    application.database.airportDao(),
                    application.database.favoriteDao()
                )
                FlightSearchViewModel(repository)
            }
        }
    }
}

data class FlightSearchUi(
    val currentAirport: Airport? = null,
    val suggestedAirportList: List<Airport> = listOf()
)

data class FavoriteUi(
    val favorites: List<Favorite> = listOf()
)