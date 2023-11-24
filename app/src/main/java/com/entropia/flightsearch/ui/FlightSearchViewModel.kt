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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FlightSearchViewModel(private val repository: FlightSearchRepository) : ViewModel() {

    var flightSearchUi by mutableStateOf(FlightSearchUi())
        private set

    var favoriteUi by mutableStateOf(FavoriteUi())
        private set

    fun updateCurrentAirport(airport: Airport) {
        flightSearchUi = flightSearchUi.copy(
            currentAirport = airport
        )
    }


    fun getSearchResultsList(input: String) {
        viewModelScope.launch {
            flightSearchUi = flightSearchUi.copy(
                suggestedAirportList = repository.getAirportByInputStream(input).filterNotNull()
                    .first()
            )
        }

    }

    fun clearSearchResultsList() {
        flightSearchUi = flightSearchUi.copy(
            suggestedAirportList = emptyList()
        )
    }

    fun getAllDestinationAirports() {
        viewModelScope.launch {
            if (flightSearchUi.currentAirport != null) {
                flightSearchUi = flightSearchUi.copy(
                    destinationAirportList = repository.getAllDestinationAirportsStream(currentId = flightSearchUi.currentAirport!!.id)
                        .filterNotNull().first()
                )
            }
        }

    }

    fun addOrRemoveFavorite(favorite: Favorite) {
        viewModelScope.launch {
            if (repository.getFavorite(favorite.departureCode, favorite.destinationCode) == null) {
                addFavorite(favorite)
            } else {
                removeFavorite(repository.getFavorite(favorite.departureCode, favorite.destinationCode)!!)
            }
        }
    }

    fun addFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.addFavorite(favorite)
            favoriteUi = favoriteUi.copy(
                favorites = repository.getAllFavorites().filterNotNull().first()
            )
            Log.d("favorite", favoriteUi.favorites.toString())
        }
    }

    fun removeFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.removeFavorite(favorite)
            favoriteUi = favoriteUi.copy(
                favorites = repository.getAllFavorites().filterNotNull().first()
            )
        }
        Log.d("favorite", favoriteUi.favorites.toString())
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
                val repository = OfflineRepository(
                    application.database.airportDao(), application.database.favoriteDao()
                )
                FlightSearchViewModel(repository)
            }
        }
    }
}

data class FlightSearchUi(
    val currentAirport: Airport? = null,
    val suggestedAirportList: List<Airport> = emptyList(),
    val destinationAirportList: List<Airport> = emptyList()
)

data class FavoriteUi(
    val favorites: List<Favorite> = listOf()
)