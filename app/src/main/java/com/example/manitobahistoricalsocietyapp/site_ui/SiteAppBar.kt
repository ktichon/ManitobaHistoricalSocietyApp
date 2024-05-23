package com.example.manitobahistoricalsocietyapp.site_ui


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.manitobahistoricalsocietyapp.storage_classes.SiteDisplayState
import com.example.manitobahistoricalsocietyapp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteMainPageTopBar(
    //Parameters for closing a site
    onClickChangeDisplayState: (SiteDisplayState) -> Unit,
    displayState: SiteDisplayState,

    //Parameters for the search bar
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    searchActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    removeFocus: () -> Unit,


    modifier: Modifier = Modifier
) {

    TopAppBar(
        navigationIcon = {

            //Close search if its active
            if (searchActive){
                TopBarNavIcon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Close Search",
                    //Clearing the focus should cause AppBarSearch to set call onActiveChange(false)
                    onClick = {
                        //Clearing the focus should cause AppBarSearch to set call onActiveChange(false)
                        removeFocus()
                        //Clear the search bar
                        onQueryChange("")
                    })
            }
            //On click show full map, only available if displayState is not FullMap
            else if (displayState != SiteDisplayState.FullMap){
                TopBarNavIcon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back to Map",
                    onClick = { onClickChangeDisplayState(SiteDisplayState.FullMap)})
            }
            //Default to a Search icon
            else{
                IconButton(onClick = {  }) {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(40.dp)


                    )
                }
                TopBarNavIcon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    onClick = {})

            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        title = {

            AppBarSearch(
                searchQuery = searchQuery,
                onQueryChange = onQueryChange,
                onActiveChange = onActiveChange,
                modifier = Modifier.padding(7.dp)
            )

        },
        modifier = modifier
    )

}

//Used to ensure consistency with the Icons on the top left of the app
@Composable
fun TopBarNavIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = { onClick()  }) {

        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = modifier.size(40.dp)
        )
    }

}


@Composable
fun AppBarSearch(
    onActiveChange: (Boolean) -> Unit,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    TextField(
        value = searchQuery,
        onValueChange = { query -> onQueryChange(query) },
        singleLine = true,
        shape = CircleShape, //RoundedCornerShape(20.dp),
        placeholder = { Text(text = "Search for Historical Sites...") },
        label = null,
        modifier = modifier.onFocusChanged {
            //Updates the focus state
            onActiveChange(it.isFocused)
        }

    )

    /*SearchBar(
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
        *//*leadingIcon = {
            Icon(imageVector = Icons.Rounded.Search,
                contentDescription = null
            )
        },*//*
        //shape = RectangleShape,
        modifier = modifier.offset(y = (-5).dp)

    )
    {


    }*/

    
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
              /*  searchedSites = emptyList(),*/
                searchActive = false,
                onActiveChange = {},
                removeFocus = {},
              /*  onSiteSelected = {},
                userLocation = LatLng(49.9000253, -97.1386276),*/

            )
        }
    }

}

@Preview
@Composable
private fun PreviewAppBar() {
    AppTheme {
        Surface {

            /*val siteClusterItem1 = HistoricalSiteClusterItem(2,2,"Site Number 1", "11 Bowhill Lane", "Winnipeg", 0.0,0.0)
            val siteClusterItem2 = HistoricalSiteClusterItem(2,2,"Site Number 2", "11 Bowhill Lane", "Winnipeg", 0.0,0.0)
            val siteClusterItem3 = HistoricalSiteClusterItem(2,2,"Site Number 3", "11 Bowhill Lane", "Winnipeg", 0.0,0.0)
            val siteClusterItem4 = HistoricalSiteClusterItem(2,2,"Site Number 4", "11 Bowhill Lane", "Winnipeg", 0.0,0.0)
            val siteClusterItem5 = HistoricalSiteClusterItem(2,2,"Site Number 5", "11 Bowhill Lane", "Winnipeg", 0.0,0.0)*/
            AppBarSearch(
                searchQuery = "",
                onQueryChange = {},
                /*searchedSites = listOf(siteClusterItem1, siteClusterItem2, siteClusterItem3, siteClusterItem4, siteClusterItem5),
                searchActive = true,*/
                onActiveChange = {},
                /*onSiteSelected = {},
                userLocation = LatLng(49.9000253, -97.1386276),*/
            )
        }
    }
}

