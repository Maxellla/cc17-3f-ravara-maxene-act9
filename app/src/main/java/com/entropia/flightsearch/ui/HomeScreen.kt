package com.entropia.flightsearch.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.entropia.flightsearch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: FlightSearchViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = {
        FlightSearchTopAppBar(title = stringResource(id = R.string.app_name))
    }) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                viewModel = viewModel,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
            )
            if (viewModel.flightSearchUi.currentAirport == null && viewModel.userInput != "") {
                SearchResultList(
                    viewModel = viewModel,
                    modifier = Modifier.padding(
                        dimensionResource(id = R.dimen.padding_small)
                    )
                )
            }

            if (viewModel.flightSearchUi.currentAirport != null) {
                Text(
                    text = stringResource(id = R.string.flights_from) + " " +  viewModel.flightSearchUi.currentAirport!!.iataCode,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                )
                RouteList(
                    destinationList = viewModel.flightSearchUi.destinationAirportList,
                    departureAirport = viewModel.flightSearchUi.currentAirport!!,
                    viewModel = viewModel
                )
            } else if (viewModel.userInput == "") {
                viewModel.updateFavorites()
                Text(
                    text = stringResource(id = R.string.favorite_routes),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                )
                FavoriteList(favorites = viewModel.favoriteUi.favorites, viewModel = viewModel)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchTopAppBar(title: String, modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        colors =TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary)
    )
}