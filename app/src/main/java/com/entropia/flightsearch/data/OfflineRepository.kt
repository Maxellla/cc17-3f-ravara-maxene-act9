package com.entropia.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineRepository(
    private val airportDao: AirportDao,
    private val favoriteDao: FavoriteDao
) : FlightSearchRepository {
    override fun getAirportByInputStream(input: String): Flow<List<Airport>> =
        airportDao.getAirportByName(input)

    override fun getAirportByCode(input: String): Airport? = airportDao.getAirportByCode(input)

    override fun getAllDestinationAirportsStream(currentId: Int): Flow<List<Airport>> =
        airportDao.getAllByPassengers(currentId)

    override suspend fun addFavorite(favorite: Favorite) = favoriteDao.addFavorite(favorite)

    override suspend fun removeFavorite(favorite: Favorite) = favoriteDao.removeFavorite(favorite)
    override fun getAllFavorites(): Flow<List<Favorite>> = favoriteDao.getAll()
    override suspend fun getFavorite(
        departureAirport: Airport,
        destinationAirport: Airport
    ): Favorite? =
        favoriteDao.getFavorite(departureAirport, destinationAirport)

}