package com.entropia.flightsearch.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.entropia.flightsearch.R
import com.entropia.flightsearch.data.Airport

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {

    TextField(
        value = "",
        onValueChange = {},
        singleLine = true,
        placeholder = { Text(text = stringResource(id = R.string.enter_name)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "searchIcon"
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun FlightCard(departureAirport: Airport, destinationAirport: Airport) {
    Card(
        shape = RoundedCornerShape(
            topStart = 0f,
            bottomStart = 0f,
            bottomEnd = 0f,
            topEnd = 50f
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        ) {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
                Text(text = stringResource(id = R.string.depart),)
                Row {
                    Text(
                        text = departureAirport.iataCode, modifier = Modifier.padding(
                            end = dimensionResource(
                                id = R.dimen.padding_small
                            )
                        )
                    )
                    Text(text = departureAirport.name)
                }
            }
            Icon(
                imageVector = Icons.Default.Star, contentDescription = "Add to Favorites",
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Preview
@Composable
fun FlightCardPreview() {
    val departure = Airport(0, "Warsaw Chopin Airport", "WAW", 1)
    val destination = Airport(1, "Stockholm Arlanda Airport", "ARN", 2)

    FlightCard(departureAirport = departure, destinationAirport = destination)
}