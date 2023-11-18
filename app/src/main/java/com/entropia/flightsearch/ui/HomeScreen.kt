package com.entropia.flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entropia.flightsearch.R
import com.entropia.flightsearch.data.Airport
import com.entropia.flightsearch.ui.theme.FlightSearchTheme


@Composable
fun TestScreen(
    viewModel: FlightSearchViewModel,
    flightSearchUi: FlightSearchUi,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxSize()) {
        SearchBar(viewModel)
        SearchResultList(airports = flightSearchUi.suggestedAirportList, viewModel = viewModel)
    }
}

@Composable
fun SearchResultList(
    airports: List<Airport>, viewModel: FlightSearchViewModel, modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(airports) { airport ->
            AirportData(airport = airport,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.updateCurrentAirport(airport = airport) })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(viewModel: FlightSearchViewModel, modifier: Modifier = Modifier) {
    var value by remember {
        mutableStateOf("")
    }
    TextField(
        value = value,
        onValueChange = { newValue ->
            value = newValue
            viewModel.getSearchResultsList(newValue)
        },
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(id = R.string.enter_name),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 16.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "searchIcon"
            )
        },
        modifier = modifier.fillMaxWidth()
    )
}


@Composable
fun FlightCard(
    departureAirport: Airport,
    destinationAirport: Airport,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(
            topStart = 0f, bottomStart = 0f, bottomEnd = 0f, topEnd = 50f
        ), modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        ) {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
                Text(
                    text = stringResource(id = R.string.depart).uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AirportData(departureAirport)
                Text(
                    text = stringResource(id = R.string.arrive).uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                )
                AirportData(destinationAirport)
            }
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Add to Favorites",
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}

@Composable
private fun AirportData(airport: Airport, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.Bottom, modifier = modifier
    ) {
        Text(
            text = airport.iataCode, modifier = Modifier.padding(
                end = dimensionResource(
                    id = R.dimen.padding_small
                )
            ), style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = airport.name, style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
fun FlightCardPreview() {
    val departure = Airport(0, "Warsaw Chopin Airport", "WAW", 1)
    val destination = Airport(1, "Stockholm Arlanda Airport", "ARN", 2)
    FlightSearchTheme {
        FlightCard(departureAirport = departure, destinationAirport = destination, onClick = {})
    }

}