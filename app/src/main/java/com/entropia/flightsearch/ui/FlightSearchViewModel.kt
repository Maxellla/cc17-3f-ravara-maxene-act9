package com.entropia.flightsearch.ui

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
import com.entropia.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FlightSearchViewModel(
    private val repository: FlightSearchRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var flightSearchUi by mutableStateOf(FlightSearchUi())
        private set

    var favoriteUi by mutableStateOf(FavoriteUi())
        private set

    var userInput by mutableStateOf("")
        private set

    fun updateUserInput(input: String) {
        userInput = input
    }

    fun updateCurrentAirport(airport: Airport?) {
        flightSearchUi = flightSearchUi.copy(
            currentAirport = airport
        )
    }

    init {
        runBlocking { userInput=userPreferencesRepository.inputString.first() }
        getSearchResultsList(userInput)
    }
    fun getSearchResultsList(input: String) {
        viewModelScope.launch {
            flightSearchUi = flightSearchUi.copy(
                suggestedAirportList = repository.getAirportByInputStream("%$input%")
                    .filterNotNull()
                    .first()
            )
        }

    }

    fun clearSearchResultsList() {
        updateInputPreferences("")
        flightSearchUi = flightSearchUi.copy(
            suggestedAirportList = emptyList(),
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
            if (repository.getFavorite(
                    favorite.departureAirport, favorite.destinationAirport
                ) == null
            ) {
                addFavorite(favorite)
            } else {
                removeFavorite(
                    repository.getFavorite(
                        favorite.departureAirport, favorite.destinationAirport
                    )!!
                )
            }
            updateFavorites()
        }
    }

    private fun addFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.addFavorite(favorite)
        }
    }


    private fun removeFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.removeFavorite(favorite)
        }
    }

    fun updateFavorites() {
        viewModelScope.launch {
            favoriteUi = favoriteUi.copy(
                favorites = repository.getAllFavorites().filterNotNull().first()
            )
        }

    }

    fun isFavorite(departureCode: String, destinationCode: String): Boolean {
        if (favoriteUi.favorites.isNotEmpty()) {
            favoriteUi.favorites.forEach { favorite ->
                if (favorite.departureAirport.iataCode == departureCode && favorite.destinationAirport.iataCode == destinationCode) return true
            }
        }
        return false
    }

    fun updateInputPreferences(input: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveInputString(inputString = input)
        }
    }


    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
                val repository = OfflineRepository(
                    application.database.airportDao(), application.database.favoriteDao()
                )
                FlightSearchViewModel(repository, application.userPreferencesRepository)
            }
        }
    }
}

data class FlightSearchUi(
    val currentAirport: Airport? = null,
    val suggestedAirportList: List<Airport> = emptyList(),
    val destinationAirportList: List<Airport> = emptyList(),
)

data class FavoriteUi(
    val favorites: List<Favorite> = listOf(),
)