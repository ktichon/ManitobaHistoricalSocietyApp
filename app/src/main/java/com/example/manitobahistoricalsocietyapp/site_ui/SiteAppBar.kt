package com.example.manitobahistoricalsocietyapp.site_ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSiteClusterItem
import com.example.manitobahistoricalsocietyapp.helperClasses.DistanceAwayFromSite
import com.example.manitobahistoricalsocietyapp.storage_classes.SiteDisplayState
import com.example.manitobahistoricalsocietyapp.ui.theme.AppTheme
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteMainPageTopBar(
    //Parameters for closing a site
    onClickChangeDisplayState: (SiteDisplayState) -> Unit,
    displayState: SiteDisplayState,

    //Parameters for the search bar
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    searchedSites: List<HistoricalSiteClusterItem>,
    searchActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onSiteSelected: (Int) -> Unit,
    userLocation: LatLng,

    modifier: Modifier = Modifier
) {

    TopAppBar(
        navigationIcon = {

            /*AnimatedVisibility(
                visible = (displayState != SiteDisplayState.FullMap ),
                enter = slideInHorizontally(
                    initialOffsetX = {fullWidth -> -fullWidth },
                    animationSpec = tween(1000)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = {fullWidth -> -fullWidth },
                    animationSpec = tween(1000)
                )
            ) {
                IconButton(
                    onClick = { onClickChangeDisplayState(SiteDisplayState.FullMap) }
                ){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier.size(40.dp)
                    )

                }
            }*/
            //On click show full map
            if (displayState != SiteDisplayState.FullMap){
                IconButton(
                    onClick = { onClickChangeDisplayState(SiteDisplayState.FullMap) }
                ){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(40.dp)


                    )

                }

            }
            //However if it is already full map then display nothing
            else{
                // I have the blank icon button to keep the padding the same
                // Changed the blank icon to be search
                IconButton(onClick = {  }) {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(40.dp)


                    )

                    /*Spacer(modifier = Modifier
                        .size(40.dp))*/
                }

            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        title = {

            BasicSearchBar(
                searchQuery = searchQuery,
                onQueryChange = onQueryChange,
                searchedSites = searchedSites,
                searchActive = searchActive,
                onActiveChange = onActiveChange,
                onSiteSelected = onSiteSelected,
                userLocation = userLocation,
                modifier = Modifier.padding(7.dp)
            )

        },
        modifier = modifier.animateContentSize(
            tween(500)

        )
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicSearchBar(
    searchActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    searchedSites: List<HistoricalSiteClusterItem>,

    onSiteSelected: (Int) -> Unit,
    userLocation: LatLng,
    modifier: Modifier = Modifier,
    //Maybe implement this later, which would send to a new activity
    onSearch: ((String) -> Unit) = {},
) {
    SearchBar(
        //Search State parameters
        query = searchQuery,
        onQueryChange = { query -> onQueryChange(query) },
        onSearch = onSearch,
        active = searchActive,
        onActiveChange = onActiveChange,

        placeholder = {
            Text(
                text = "Search for Historical Sites...",
                modifier = Modifier.padding(0.dp)
                )},
        /*leadingIcon = {
            Icon(imageVector = Icons.Rounded.Search,
                contentDescription = null
            )
        },*/
        //shape = RectangleShape,
        modifier = modifier.offset(y = (-5).dp)

    )
    {


    }

    
}


@Preview
@Composable
private fun PreviewBasicSearchBar() {
    AppTheme {
        Surface {
            SiteMainPageTopBar(
                displayState = SiteDisplayState.FullSite,
                onClickChangeDisplayState = {},
                searchQuery = "",
                onQueryChange = {},
                searchedSites = emptyList(),
                searchActive = false,
                onActiveChange = {},
                onSiteSelected = {},
                userLocation = LatLng(49.9000253, -97.1386276),

            )
        }
    }

}

@Preview
@Composable
private fun PreviewAppBar() {
    AppTheme {
        Surface {

            val siteClusterItem1 = HistoricalSiteClusterItem(2,2,"Site Number 1", "11 Bowhill Lane", "Winnipeg", 0.0,0.0)
            val siteClusterItem2 = HistoricalSiteClusterItem(2,2,"Site Number 2", "11 Bowhill Lane", "Winnipeg", 0.0,0.0)
            val siteClusterItem3 = HistoricalSiteClusterItem(2,2,"Site Number 3", "11 Bowhill Lane", "Winnipeg", 0.0,0.0)
            val siteClusterItem4 = HistoricalSiteClusterItem(2,2,"Site Number 4", "11 Bowhill Lane", "Winnipeg", 0.0,0.0)
            val siteClusterItem5 = HistoricalSiteClusterItem(2,2,"Site Number 5", "11 Bowhill Lane", "Winnipeg", 0.0,0.0)
            BasicSearchBar(
                searchQuery = "",
                onQueryChange = {},
                searchedSites = listOf(siteClusterItem1, siteClusterItem2, siteClusterItem3, siteClusterItem4, siteClusterItem5),
                searchActive = true,
                onActiveChange = {},
                onSiteSelected = {},
                userLocation = LatLng(49.9000253, -97.1386276),
            )
        }
    }
}

