package com.TichonTechnologies.manitobahistoricalsocietyapp.site_scaffolding


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField

import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.TichonTechnologies.manitobahistoricalsocietyapp.R
import com.TichonTechnologies.manitobahistoricalsocietyapp.helperClasses.GetTypeValues
import com.TichonTechnologies.manitobahistoricalsocietyapp.map.ClusterItemContent
import com.TichonTechnologies.manitobahistoricalsocietyapp.navigation.AboutDestination
import com.TichonTechnologies.manitobahistoricalsocietyapp.storage_classes.SiteDisplayState
import com.TichonTechnologies.manitobahistoricalsocietyapp.ui.theme.AppTheme

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
    navigateToRoute: (String) -> Unit,


    modifier: Modifier = Modifier
) {

    DisplayTopAppBar(
        topLeftIcon = {
            if(searchActive || !(displayState == SiteDisplayState.FullMap || displayState == SiteDisplayState.MapWithLegend)){
                TopBarNavIcon(
                    painter = painterResource(id = R.drawable.arrow_back_ios),
                    contentDescription = "Back",
                    onClick = {
                        //Close search if its active
                        if(searchActive){
                            //Clearing the focus should cause AppBarSearch to set call onActiveChange(false)
                            removeFocus()
                            //Clear the search bar
                            onQueryChange("")
                        }
                        else{
                            //Change the display state back to map
                            onClickChangeDisplayState(SiteDisplayState.FullMap)
                        }}
                    , modifier = Modifier
                        .size(50.dp)
                        .padding(start = 5.dp)
                )
            }

            else{
                TopBarNavIcon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    onClick = {},
                    modifier = Modifier.size(50.dp)
                )

            }
        },
        title = @Composable {
            AppBarSearch(
            searchQuery = searchQuery,
            onQueryChange = onQueryChange,
            onActiveChange = onActiveChange,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        )},
        navigateToRoute = navigateToRoute,
        modifier = modifier)

}

//Used to ensure consistency with the Icons on the top left of the app
@Composable
fun TopBarNavIcon(
    painter: Painter,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = { onClick()  }) {

        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = modifier
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarSearch(
    onActiveChange: (Boolean) -> Unit,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column {
        /*OutlinedTextField(
            value = searchQuery,
            onValueChange = { query -> onQueryChange(query) },
            singleLine = true,
            shape = CircleShape, //RoundedCornerShape(20.dp),
            placeholder = { Text(text = "Search for Historical Sites...") },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = modifier
                .onFocusChanged {
                    //Updates the focus state
                    onActiveChange(it.isFocused)
                }
        )*/
        //Created my own TextField so that I could set my own content padding
        BasicTextField(
            value = searchQuery,
            onValueChange = { query -> onQueryChange(query) },
            maxLines = 1,
            singleLine = true,
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),

            decorationBox = @Composable { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = searchQuery,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = { Text(text = "Search for Historical Sites...") },
                    colors = OutlinedTextFieldDefaults.colors(),
                    contentPadding = PaddingValues( horizontal = 15.dp, vertical = 10.dp),//OutlinedTextFieldDefaults.contentPadding(),
                    container = {
                        OutlinedTextFieldDefaults.ContainerBox(
                            enabled = true,
                            isError = false,
                            interactionSource = interactionSource,
                            colors = OutlinedTextFieldDefaults.colors(),
                            shape = CircleShape)
                    },
                )
            },
            modifier = modifier
                .onFocusChanged {
                    //Updates the focus state
                    onActiveChange(it.isFocused)
                }
        )

    }



   /* TextField(
        value = searchQuery,
        onValueChange = { query -> onQueryChange(query) },
        singleLine = true,
        shape = CircleShape, //RoundedCornerShape(20.dp),
        placeholder = { Text(text = "Search for Historical Sites...") },
        label = null,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .onFocusChanged {
            //Updates the focus state
            onActiveChange(it.isFocused)
        }

    )*/

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayTopAppBar(
    topLeftIcon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier
) {

    var showMenu by remember { mutableStateOf(false) }
    Surface(shadowElevation = 10.dp){
        TopAppBar(
            navigationIcon = topLeftIcon,
            title = title,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            ),
            actions = {
                TopBarNavIcon(
                    painter = painterResource(id = R.drawable.more_vert),
                    contentDescription = "More Items",
                    onClick = { showMenu = !showMenu })
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        onClick = { navigateToRoute(AboutDestination.route) },
                        text = { Text(text = AboutDestination.title) })


                }
            },
            modifier = modifier
        )

    }
    

    
}

@Composable
fun DisplayLegendCard(
    onCardClick : () -> Unit,

    modifier: Modifier = Modifier) {
    Card(
        onClick = onCardClick,
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onSurface),
        shape = CircleShape,
        modifier = modifier
    ) {
        Text(
            text = "Legend",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
        )

    }
    
}

@Composable
fun LegendBottomSheet(
   /* fullyExpanded: Boolean,
    updateExpandedState: (Boolean) -> Unit,*/
    modifier: Modifier = Modifier
) {
    val siteTypes = arrayOf(2,3,4,5,6,7)
    Column(modifier = modifier) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Legend",
                //textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)


        ) {
            items(siteTypes){siteType ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClusterItemContent(typeId = siteType)
                    Text(text = GetTypeValues.getTypeName(siteType))
                }

            }
        }


    }
}

@Preview
@Composable
private fun PreviewGenericAppBar() {
    AppTheme {
        Surface {
            DisplayTopAppBar(
                topLeftIcon = {TopBarNavIcon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    onClick = {},
                    modifier = Modifier.size(50.dp)
                )},
                title = {  AppBarSearch(
                    searchQuery = "",
                    onQueryChange = {},
                    /*searchedSites = listOf(siteClusterItem1, siteClusterItem2, siteClusterItem3, siteClusterItem4, siteClusterItem5),
                    searchActive = true,*/
                    onActiveChange = {},
                    /*onSiteSelected = {},
                    userLocation = LatLng(49.9000253, -97.1386276),*/
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) },
                navigateToRoute = {},
                modifier = Modifier)
        }
    }
    
}

@Preview
@Composable
private fun PreviewBasicSearchBar() {
    AppTheme {
        Surface {
            SiteMainPageTopBar(
                displayState = SiteDisplayState.FullMap,
                onClickChangeDisplayState = {},
                searchQuery = "test search",
                onQueryChange = {},
              /*  searchedSites = emptyList(),*/
                searchActive = false,
                onActiveChange = {},
                removeFocus = {},
                navigateToRoute = {},
                modifier = Modifier
              /*  onSiteSelected = {},
                userLocation = LatLng(49.9000253, -97.1386276),*/

            )
        }
    }

}

@Preview
@Composable
private fun PreviewBasicSearchBarBack() {
    AppTheme {
        Surface {
            SiteMainPageTopBar(
                displayState = SiteDisplayState.FullSite,
                onClickChangeDisplayState = {},
                searchQuery = "test search",
                onQueryChange = {},
                /*  searchedSites = emptyList(),*/
                searchActive = false,
                onActiveChange = {},
                removeFocus = {},
                /*  onSiteSelected = {},
                  userLocation = LatLng(49.9000253, -97.1386276),*/
                navigateToRoute = {}

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

@Preview
@Composable
private fun PreviewLegendBottomSheet() {
    AppTheme {
        Surface {
            LegendBottomSheet()
        }
    }

}

@Preview
@Composable
private fun PreviewDisplayLegendCard() {
    AppTheme {
        Surface {
            DisplayLegendCard({})
        }
    }
    
}

