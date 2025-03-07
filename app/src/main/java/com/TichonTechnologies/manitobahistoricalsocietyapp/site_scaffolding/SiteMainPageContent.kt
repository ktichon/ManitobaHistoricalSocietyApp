package com.TichonTechnologies.manitobahistoricalsocietyapp.site_scaffolding

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.TichonTechnologies.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSite
import com.TichonTechnologies.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSiteClusterItem
import com.TichonTechnologies.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotos
import com.TichonTechnologies.manitobahistoricalsocietyapp.helperClasses.DistanceAwayFromSite
import com.TichonTechnologies.manitobahistoricalsocietyapp.helperClasses.FormatSite
import com.TichonTechnologies.manitobahistoricalsocietyapp.helperClasses.FormatSize
import com.TichonTechnologies.manitobahistoricalsocietyapp.info_pages.AboutPage
import com.TichonTechnologies.manitobahistoricalsocietyapp.map.DisplayMap
import com.TichonTechnologies.manitobahistoricalsocietyapp.navigation.AboutDestination
import com.TichonTechnologies.manitobahistoricalsocietyapp.navigation.MapDestination
import com.TichonTechnologies.manitobahistoricalsocietyapp.site_ui.DisplayFullSiteDetails
import com.TichonTechnologies.manitobahistoricalsocietyapp.storage_classes.SiteDisplayState
import com.TichonTechnologies.manitobahistoricalsocietyapp.ui.theme.AppTheme
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteMainPageContent(
    navController: NavHostController,
    cameraPositionState: CameraPositionState,
    allSites: List<HistoricalSiteClusterItem>,
    onSiteSelected: (siteClusterItem: HistoricalSiteClusterItem, searched: Boolean)  -> Unit,
    onClusterClicked: (LatLng) -> Unit,
    locationEnabled: Boolean,

    //Site Details parameters
    currentSite: HistoricalSite,
    currentlySelectedClusterItem: HistoricalSiteClusterItem,
    displayState: SiteDisplayState,
    onClickChangeDisplayState: (SiteDisplayState) -> Unit,
    currentSiteTypes: List<String>,
    userLocation: LatLng,
    currentSitePhotos: List<SitePhotos>,
    currentSiteSourcesList: List<String>,
    renderNewSite: Boolean,
    updateRenderNewSite: (Boolean) -> Unit,


    //Site Search Parameters
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    searchedSites: List<HistoricalSiteClusterItem>,
    searchActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    removeFocus: () -> Unit,


    //MapPadding
    newMapUpdate: Boolean,
    centerCamera: () -> Unit,


    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {SnackbarHostState()}
    val legendState = rememberModalBottomSheetState()
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val displayedItemSize = FormatSize.getDpFromPercent(displayState.mapBottomPaddingPercent, screenHeightDp).dp

  /*  var showLegend by remember { mutableStateOf(false) }*/

    NavHost(navController = navController, startDestination = MapDestination.route) {

        composable(route = AboutDestination.route){
            AboutPage(
                navigateToRoute = {route ->
                navController.navigateSingleTopTo(route)
            },
                onBackClick = {
                    navController.popBackStack()
                }
                )


        }

        composable(route = MapDestination.route){
            Scaffold(
                topBar = {
                        SiteMainPageTopBar(
                            onClickChangeDisplayState = onClickChangeDisplayState,
                            displayState = displayState,
                            searchQuery = searchQuery,
                            onQueryChange = onQueryChange,
                            searchActive = searchActive,
                            onActiveChange = onActiveChange,
                            removeFocus = removeFocus,
                            navigateToRoute = {route ->
                                navController.navigateSingleTopTo(route)
                            })

                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                },
                modifier = modifier
            ) {innerPadding ->
                Box(modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()

                ){
                    DisplayMap(
                        cameraPositionState = cameraPositionState,
                        sites = allSites,
                        onSiteSelected = onSiteSelected,
                        onClusterClicked = onClusterClicked,
                        //currentlySelectedClusterItem = currentlySelectedClusterItem,
                        locationEnabled = locationEnabled,
                        mapPadding = PaddingValues(bottom = displayedItemSize),
                        newMapUpdate = newMapUpdate,
                        centerCamera = centerCamera,
                        modifier = Modifier
                            .fillMaxSize()
                        //The background colour just makes it more visible on preview.
                        //.background(Color.Cyan)
                    )

                    if (displayState == SiteDisplayState.HalfSite || displayState == SiteDisplayState.FullSite )
                    {
                        DisplayFullSiteDetails(
                            site = currentSite,
                            currentlySelectedClusterItem = currentlySelectedClusterItem,
                            displayState = displayState,
                            onClickChangeDisplayState = onClickChangeDisplayState,
                            siteTypes = currentSiteTypes,
                            userLocation = userLocation,
                            allSitePhotos = currentSitePhotos,
                            sourcesList = currentSiteSourcesList,
                            renderNewSite = renderNewSite,
                            updateRenderNewSite = updateRenderNewSite,
                            displayErrorMessage = {message ->
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = message,
                                        duration = SnackbarDuration.Short
                                    )
                                }},
                            modifier = Modifier
                                .height(
                                    displayedItemSize
                                )
                                .fillMaxWidth()
                                .align(Alignment.BottomEnd)

                        )
                    }

                    /*AnimatedVisibility(
                    visible = (displayState == SiteDisplayState.HalfSite || displayState == SiteDisplayState.FullSite) and (currentSite != null),
                    modifier = Modifier.weight(siteWeight),
                    enter = slideInVertically (
                        //slide in from bottom
                        initialOffsetY = {
                            it / 2
                        },
                        animationSpec = tween(durationMillis = animationLengthInMilliSeconds, easing = LinearOutSlowInEasing)
                    ),
                     //slide out from bottom
                    exit = slideOutVertically(
                        targetOffsetY = {
                            it / 2
                        },
                        animationSpec = tween(durationMillis = animationLengthInMilliSeconds, easing = LinearOutSlowInEasing)
                    ),

                ) {

                }*/


                    //Display legend text
                    if ( displayState == SiteDisplayState.FullMap){
                        DisplayLegendCard(
                            onCardClick = {
                                onClickChangeDisplayState(SiteDisplayState.MapWithLegend)
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(5.dp)

                        )
                    }

                    if (displayState == SiteDisplayState.MapWithLegend){
                        ModalBottomSheet(
                            onDismissRequest = {
                                onClickChangeDisplayState(SiteDisplayState.FullMap)

                            },
                            sheetState = legendState,
                            //For some reason, updating my gradle dependencies caused this to not work anymore.
                            //Instead of the bottom sheet appearing on the bottom, it went straight to the top of the app instead.
                            //The height is now determined by the LegendBottomSheet
                            //modifier = Modifier.height(displayedItemSize)
                        )
                        {
                            LegendBottomSheet(
                                modifier = Modifier.height(
                                    //The bottom sheet is slightly bigger that the legend, which was covering up the google logo.
                                    //To compensate for that, I have multiplied the height by 0.85.
                                    //Hopefully, this will stop it from covering up the google logo
                                    (FormatSize.getDpFromPercent(displayState.mapBottomPaddingPercent, screenHeightDp) * 0.85).dp )

                            )
                        }

                    }

                    if (searchActive){
                        DisplayAppbarSearchResults(
                            searchedSites = searchedSites,
                            onSiteSelected = onSiteSelected,
                            userLocation = userLocation,
                            onActiveChange = onActiveChange,
                            removeFocus = removeFocus,
                            modifier = Modifier.fillMaxSize()
                        )


                    }

                }

            }

        }

    }









}



//Displays while we are loading all the historical sites from the viewmodel
@Composable
fun LoadingScreen(
    waitOn: suspend () -> Unit,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
        ){
        val currentOnCompeted by rememberUpdatedState( onCompleted)

        LaunchedEffect(Unit) {
            waitOn()
            currentOnCompeted()
        }


        Text(text = "Loading Manitoba Historical Sites",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        CircularProgressIndicator(

            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth = 5.dp,
            modifier = Modifier
                .width(100.dp)
                .padding(10.dp),
        )





    }
}

@Composable
fun DisplayAppbarSearchResults(
    searchedSites: List<HistoricalSiteClusterItem>,
    onSiteSelected: (siteClusterItem: HistoricalSiteClusterItem, searched: Boolean) -> Unit,
    userLocation: LatLng,
    onActiveChange: (Boolean) -> Unit,
    removeFocus: () -> Unit,

    modifier: Modifier = Modifier) {

    LazyColumn(
        //verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(5.dp),
        modifier = modifier.background(MaterialTheme.colorScheme.secondaryContainer)

    ) {



        items(searchedSites){foundSite ->

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    /*.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )*/
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .clickable {
                        onSiteSelected(foundSite, true)
                        onActiveChange(false)
                        removeFocus()

                    }


            ){
                Column (
                    modifier = Modifier.weight(1f)
                ){
                    Text(
                        text = foundSite.name,
                        maxLines =  2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = FormatSite.formatAddress(foundSite),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(
                        text = DistanceAwayFromSite.getDisplayDistance(userLocation, LatLng(foundSite.latitude, foundSite.longitude)),
                        style = MaterialTheme.typography.bodyLarge
                    )

                }

            }
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )


        }

    }
    
}

//LaunchSingleTop ensures that only one copy of a given destination will be on top of the backstack.
//Stops from creating multiple copies of the same if the user clicks the button multiple times
//Adding it as a helper method to the NavController so that we don't need to do
//navController.navigate(route) { launchSingleTop = true }
//everytime
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route){
        //Pop up to the start destination of the graph to avoid building large stack on backstack
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ){
            saveState = true
        }


        //Only one copy of destination
        launchSingleTop = true

        //restores state that was previously saved by popUpTo (with ID)
        restoreState = true
    }


class DisplayStateWithMapPreviewParameterProvider : PreviewParameterProvider<SiteDisplayState> {
    override val values: Sequence<SiteDisplayState>
        get() = sequenceOf(SiteDisplayState.HalfSite, SiteDisplayState.FullSite, SiteDisplayState.FullMap, SiteDisplayState.MapWithLegend)
}
@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun PreviewHistoricalSiteHomeContent(
    @PreviewParameter(DisplayStateWithMapPreviewParameterProvider::class) displayState :SiteDisplayState
) {
    AppTheme {
        Surface {

            val testSite = HistoricalSite(3817, "Odd Fellows Home","4025 Roblin Boulevard", 3, 49.86902, -97.26729, "Winnipeg","MB",  "By June 1916, the <a href=\"http://www.mhs.mb.ca/docs/organization/ioof.shtml\">Independent Order of Odd Fellows</a> began searching in the greater <a href=\"http://www.mhs.mb.ca/docs/municipalities/winnipeg.shtml\">Winnipeg</a> area for property on which to built a home for elderly members and their spouses, as well as orphaned children of deceased members. These large grounds in the <a href=\"http://www.mhs.mb.ca/docs/municipalities/charleswood.shtml\">Rural Municipality of Charleswood</a> were selected and a contest was held for the design plans. The winning entry was drawn up by Winnipeg architect <a href=\"http://www.mhs.mb.ca/docs/people/russell_jhg.shtml\">John Hamilton Gordon Russell</a>. It called for a structure measuring 120 feet by 66 feet, costing \$30,000 to \$40,000, with capacity for around 40 beds. Excavation was to begin by the fall of 1917. However, construction did not proceed and, over the following years, additional funds were raised to build a larger facility.<br><br>Construction on the present building began in 1922, with site preparation and excavation work underway by the spring. A cornerstone-laying ceremony officiated by Grand Secretary <a href=\"http://www.mhs.mb.ca/docs/people/deering_bd.shtml\">Benjamin Draper Deering</a> was held on 15 July. It was to be the second IOOF Home in Canada (the first being located in Toronto) and one of 57 such Homes across North America. Once completed, it would measure 140 feet by 30 feet, with two south wings (each 24 feet by 30 feet), and a joint dining room and kitchen (measuring 33 feet by 56 feet). Accomodation was provided for 70 people with additional undeveloped capacity for orphans in the attic. The two-storey structure cost around \$125,000 with another \$25,000 in furnishings and equipment. In the basement, in addition to the steam heating plant, there were two large playrooms for orphans along with two corresponding rear (north side) entrances to the building labeled “Girls” and “Boys.” A school was later operated at the site.<br><br>The facility was opened officially on 13 March 1923 at a ceremony attended by some 850 people. It was dedicated by Lucian J. Eastin (IOOF Grand Sire of St. Joseph, Missouri) and Lieutenant-Governor <a href=\"http://www.mhs.mb.ca/docs/people/aikins_jam.shtml\">James Albert Manning Aikins</a> addressed the crowd.<br><br>In 1997, the facility was closed following withdrawal of government funding. The building was given a \$2 million renovation, resulting in seven studio apartments and 25 single bedroom suites. In April 2001, it reopened as an assisted living facility known as Assiniboine Links. A private residential subdivision was later constructed between the building and the Assiniboine River.<br><br>The building became a <a href=\"http://www.mhs.mb.ca/docs/sites/municipal.shtml\">municipally-designated heritage site</a> in January 2023.<br><br>", "", "http://www.mhs.mb.ca/docs/sites/oddfellowshome.shtml", "Charleswood, year=1923, arc=Russell, mat=bricks, oddfellows, photo=2020, des=2023", "2024-05-06 14:35:26")
            val siteTypes = listOf("Building", "Museum or Archives")
            val userLocation = LatLng(49.8555836, -97.2888901)

            val clusterItem =  HistoricalSiteClusterItem(3817, "Odd Fellows Home","4025 Roblin Boulevard", 3, 49.86902, -97.26729, "Winnipeg")

            val photo1 = SitePhotos(140229,3817,  "3817_oddfellowshome2_1715023463.jpg", 600, 422,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome2.jpg", "<strong>Architect’s drawing of the Odd Fellows Home</strong> (1922)<br/><a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\">Manitoba Free Press</a>, 15 July 1922, page 48.", "","2024-05-06 14:24:23"  )
            val photo2 = SitePhotos(140229,3817,  "3817_oddfellowshome4_1715023463.jpg", 600, 250,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome4.jpg", "<strong>Odd Fellows Home</strong> (1923)<br/><a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\">Winnipeg Tribune</a>, 13 March 1923, page 2.", "","2024-05-06 14:24:23"  )
            val photo3 = SitePhotos(140229,3817,  "3817_oddfellowshome3_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome3.jpg", "<strong>Odd Fellows Home</strong> (no date)<br/>\n" + "<em>Source:</em> Jack Hardman", "","2024-05-06 14:24:23"  )
            val photo4 = SitePhotos(140229,3817,  "3817_oddfellowshome1_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome1.jpg", "<strong>Odd Fellows Home</strong> (May 2011)<br/>\n" + "<em>Source:</em> <a href=\"http://www.mhs.mb.ca/docs/people/goldsborough_lg.shtml\">Gordon Goldsborough</a>","","2024-05-06 14:24:23"  )
            val photo5 = SitePhotos(140229,3817,  "3817_oddfellowshome5_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome5.jpg", "<strong>Rear view of the Odd Fellows Home with Girls entrance</strong> (December 2020)<br/>\n" + "<em>Source:</em> <a href=\"http://www.mhs.mb.ca/docs/people/goldsborough_lg.shtml\">Gordon Goldsborough</a>","","2024-05-06 14:24:23"  )
            val photo6 = SitePhotos(140229,3817,  "3817_oddfellowshome6_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome6.jpg", "<strong>Rear view of the Odd Fellows Home with Boys entrance</strong> (December 2020)<br/>\n" + "<em>Source:</em> <a href=\"http://www.mhs.mb.ca/docs/people/goldsborough_lg.shtml\">Gordon Goldsborough</a>","","2024-05-06 14:24:23"  )
            val photo7 = SitePhotos(140229,3817,  "3817_oddfellowshome7_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome7.jpg", "<strong>Remaining fence post of the Odd Fellows Home grounds</strong> (December 2020)<br/>\n" + "<em>Source:</em> <a href=\"http://www.mhs.mb.ca/docs/people/goldsborough_lg.shtml\">Gordon Goldsborough</a>","","2024-05-06 14:24:23"  )
            val photo8 = SitePhotos(140229,3817,  "3817_oddfellowshome8_1715023463.jpg", 600, 400,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome8.jpg", "<strong>Front view of the Odd Fellows Home</strong> (May 2020)<br/>\n" + "<em>Source:</em> <a href=\"http://www.mhs.mb.ca/docs/people/penner_g.shtml\">George Penner</a> ","","2024-05-06 14:24:23"  )
            val allSitePhotos = listOf(photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8)
            //val uriHandler = LocalUriHandler.current

            val source1 = "“Tenders wanted,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 7 June 1916, page 2."
            val source2 = "“Fraternal notes,” <a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\"><em>Winnipeg Tribune</em></a>, 1 July 1916, page 13. "
            val source3 = "“500 Odd Fellows at anniversary service,” <a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\"><em>Winnipeg Tribune</em></a>, 30 April 1917, page 5."
            val source4 = "“Tenders called for Odd Fellows Home,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 10 September 1917, page 8."
            val source5 = "“Lodge News [Independent order of Odd Fellows],” <a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\"><em>Winnipeg Tribune</em></a>, 19 January 1918, page 23."
            val source6 = "“Tenders,” <a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\"><em>Winnipeg Tribune</em></a>, 28 February 1921, page 2. "
            val source7 = "“News of bazaars,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 19 November 1921, page 9. "
            val source8 = "“Odd Fellows’ Grand Lodge installation,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 10 March 1922, page 7."
            val source9 = "“Plans for a \$100,000 Odd Fellows Home,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 5 April 1922, page 8."
            val source10 = "“Proposed Odd Fellows’ Home for Charleswood,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 5 March 1921, page 18."
            val sourcesList = listOf(source1, source2, source3, source4, source5, source6, source7, source8, source9, source10)
            val cameraPositionState = CameraPositionState()
            //val scrollState = rememberScrollState()
            val searchedSites = listOf(clusterItem, clusterItem,clusterItem,clusterItem,clusterItem,clusterItem)

            val navController = rememberNavController()
           // val legendState = rememberModalBottomSheetState()
            SiteMainPageContent(
                navController = navController,
                currentSite = testSite,
                displayState = displayState,
                onClickChangeDisplayState = {},
                currentSiteTypes = siteTypes,
                userLocation = userLocation,
                currentSitePhotos = allSitePhotos,
                currentSiteSourcesList = sourcesList,
                //siteDetailsScrollState = scrollState,

                cameraPositionState = cameraPositionState,

                allSites = emptyList(),
                onSiteSelected = { _, _ ->  },
                /*siteDetailsScrollState = scrollState,
                photosPagerState = rememberPagerState {
                    allSitePhotos.size
                },*/
                updateRenderNewSite = {},

                searchQuery = "",
                onQueryChange = {},
                searchedSites = searchedSites,
                searchActive = false,
                onActiveChange = {},
                //onSearchSiteSelected = {},
                removeFocus = {},
                /*mapPadding = PaddingValues(40.dp),
                //updateMapPadding = {},
                screenHeightDp = LocalConfiguration.current.screenHeightDp,*/
                centerCamera = {},
                newMapUpdate = false,
                locationEnabled = false,
                renderNewSite = false,
                currentlySelectedClusterItem = clusterItem,
                onClusterClicked = {},
                modifier = Modifier
                    .height(1000.dp)
                    .width(500.dp)
            )

            //modifier = Modifier.padding(5.dp)

        }
    }

}

@Preview
@Composable
private fun PreviewLoadingScreen() {
    AppTheme {
        Surface {
            LoadingScreen(waitOn = {},
                onCompleted = {  },
                modifier = Modifier
                    .height(1000.dp)
                    .width(500.dp)

            )
        }
    }
}

@Preview
@Composable
private fun PreviewAppBarSearchResults() {
    AppTheme {
        Surface {
            val clusterItem =  HistoricalSiteClusterItem(3817, "Odd Fellows Home","4025 Roblin Boulevard", 3, 49.86902, -97.26729, "Winnipeg")

            val searchedSites = listOf(clusterItem, clusterItem,clusterItem,clusterItem,clusterItem,clusterItem)
            DisplayAppbarSearchResults(
                searchedSites = searchedSites,
                onSiteSelected= { _, _ ->  },
                onActiveChange = {},
                removeFocus = {},
                userLocation = LatLng(49.8555836, -97.2888901)
            )
        }
    }
}


