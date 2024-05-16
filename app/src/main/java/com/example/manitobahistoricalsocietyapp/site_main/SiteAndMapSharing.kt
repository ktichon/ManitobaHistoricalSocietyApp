package com.example.manitobahistoricalsocietyapp.site_main

import android.location.Location
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSite
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotos
import com.example.manitobahistoricalsocietyapp.map.DisplayMap
import com.example.manitobahistoricalsocietyapp.site_details.DisplayFullSiteDetails
import com.example.manitobahistoricalsocietyapp.site_details.DisplayStatePreviewParameterProvider
import com.example.manitobahistoricalsocietyapp.site_details.turnLatLongIntoLocation
import com.example.manitobahistoricalsocietyapp.state_classes.SiteDisplayState
import com.example.manitobahistoricalsocietyapp.ui.theme.ManitobaHistoricalSocietyAppTheme
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

@Composable
fun DisplaySiteAndMapViewport(
    //Map parameters
    cameraPositionState: CameraPositionState,
    allSites: List<HistoricalSite>,
    onClusterItemClick: (HistoricalSite)  -> Unit,
    locationEnabled: Boolean = false,

    //Site Details parameters
    currentSite: HistoricalSite?,
    displayState: SiteDisplayState,
    onClickChangeDisplayState: (SiteDisplayState) -> Unit,
    currentSiteTypes: List<String>,
    userLocation: LatLng,
    currentSitePhotos: List<SitePhotos>,
    currentSiteSourcesList: List<String>,
   // siteDetailsScrollState: ScrollState,

    modifier: Modifier = Modifier


) {
    Column(modifier = modifier) {

        val siteWeight = if(displayState == SiteDisplayState.FullSite) 49f else 1f
        val animationLengthInMilliSeconds = 200

        //Always display map, as we do not want it to leave the composition
        //If it leaves the composition, all the markers would need to be re-added to the map, and that takes time
        //This is the code for the map. However, maps don't show up on preview.
        DisplayMap(
            cameraPositionState = cameraPositionState,
            sites = allSites,
            onClusterItemClick = onClusterItemClick,
            locationEnabled = locationEnabled,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )

        //Instead, there is a placeholder image so we can check how things look on the preview
       /* Icon(imageVector = Icons.Default.AccountBox,
            contentDescription = "Map Place Holder",
            modifier = Modifier
                .background(Color.Cyan)
                .fillMaxSize()
                .weight(1f))*/

        AnimatedVisibility(
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
            if (currentSite != null) {
                DisplayFullSiteDetails(
                    site = currentSite,
                    displayState = displayState,
                    onClickChangeDisplayState = onClickChangeDisplayState,
                    siteTypes = currentSiteTypes,
                    userLocation = userLocation,
                    allSitePhotos = currentSitePhotos,
                    sourcesList = currentSiteSourcesList,
                    //scrollState = siteDetailsScrollState,
                    modifier = Modifier
                        .fillMaxSize()

                )
            }
        }


        /*if (displayState == SiteDisplayState.HalfSite || displayState == SiteDisplayState.FullSite )
        {


        }*/



    }
    
}
class DisplayStateWithMapPreviewParameterProvider : PreviewParameterProvider<SiteDisplayState>{
    override val values: Sequence<SiteDisplayState>
        get() = sequenceOf(SiteDisplayState.HalfSite, SiteDisplayState.FullSite, SiteDisplayState.FullMap)
}

@Preview
@Composable
private fun PreviewSiteAndMap(
    @PreviewParameter(DisplayStateWithMapPreviewParameterProvider::class) displayState :SiteDisplayState
) {
    ManitobaHistoricalSocietyAppTheme {
        Surface {

            val testSite = HistoricalSite(3817, "Odd Fellows Home","4025 Roblin Boulevard", 3, 49.86902, -97.26729, "MB", "Winnipeg", "By June 1916, the <a href=\"http://www.mhs.mb.ca/docs/organization/ioof.shtml\">Independent Order of Odd Fellows</a> began searching in the greater <a href=\"http://www.mhs.mb.ca/docs/municipalities/winnipeg.shtml\">Winnipeg</a> area for property on which to built a home for elderly members and their spouses, as well as orphaned children of deceased members. These large grounds in the <a href=\"http://www.mhs.mb.ca/docs/municipalities/charleswood.shtml\">Rural Municipality of Charleswood</a> were selected and a contest was held for the design plans. The winning entry was drawn up by Winnipeg architect <a href=\"http://www.mhs.mb.ca/docs/people/russell_jhg.shtml\">John Hamilton Gordon Russell</a>. It called for a structure measuring 120 feet by 66 feet, costing \$30,000 to \$40,000, with capacity for around 40 beds. Excavation was to begin by the fall of 1917. However, construction did not proceed and, over the following years, additional funds were raised to build a larger facility.<br><br>Construction on the present building began in 1922, with site preparation and excavation work underway by the spring. A cornerstone-laying ceremony officiated by Grand Secretary <a href=\"http://www.mhs.mb.ca/docs/people/deering_bd.shtml\">Benjamin Draper Deering</a> was held on 15 July. It was to be the second IOOF Home in Canada (the first being located in Toronto) and one of 57 such Homes across North America. Once completed, it would measure 140 feet by 30 feet, with two south wings (each 24 feet by 30 feet), and a joint dining room and kitchen (measuring 33 feet by 56 feet). Accomodation was provided for 70 people with additional undeveloped capacity for orphans in the attic. The two-storey structure cost around \$125,000 with another \$25,000 in furnishings and equipment. In the basement, in addition to the steam heating plant, there were two large playrooms for orphans along with two corresponding rear (north side) entrances to the building labeled “Girls” and “Boys.” A school was later operated at the site.<br><br>The facility was opened officially on 13 March 1923 at a ceremony attended by some 850 people. It was dedicated by Lucian J. Eastin (IOOF Grand Sire of St. Joseph, Missouri) and Lieutenant-Governor <a href=\"http://www.mhs.mb.ca/docs/people/aikins_jam.shtml\">James Albert Manning Aikins</a> addressed the crowd.<br><br>In 1997, the facility was closed following withdrawal of government funding. The building was given a \$2 million renovation, resulting in seven studio apartments and 25 single bedroom suites. In April 2001, it reopened as an assisted living facility known as Assiniboine Links. A private residential subdivision was later constructed between the building and the Assiniboine River.<br><br>The building became a <a href=\"http://www.mhs.mb.ca/docs/sites/municipal.shtml\">municipally-designated heritage site</a> in January 2023.<br><br>", "http://www.mhs.mb.ca/docs/sites/oddfellowshome.shtml", "Charleswood, year=1923, arc=Russell, mat=bricks, oddfellows, photo=2020, des=2023", "2024-05-06 14:35:26")
            val siteTypes = listOf("Building", "Museum or Archives")
            val userLocation = LatLng(49.8555836, -97.2888901)

            val photo1 = SitePhotos(140229,3817,  "3817_oddfellowshome2_1715023463.jpg", 600, 422,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome2.jpg", "<strong>Architect’s drawing of the Odd Fellows Home</strong> (1922)<br/><a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\">Manitoba Free Press</a>, 15 July 1922, page 48.", "2024-05-06 14:24:23"  )
            val photo2 = SitePhotos(140229,3817,  "3817_oddfellowshome4_1715023463.jpg", 600, 250,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome4.jpg", "<strong>Odd Fellows Home</strong> (1923)<br/><a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\">Winnipeg Tribune</a>, 13 March 1923, page 2.","2024-05-06 14:24:23"  )
            val photo3 = SitePhotos(140229,3817,  "3817_oddfellowshome3_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome3.jpg", "<strong>Odd Fellows Home</strong> (no date)<br/>\n" + "<em>Source:</em> Jack Hardman","2024-05-06 14:24:23"  )
            val photo4 = SitePhotos(140229,3817,  "3817_oddfellowshome1_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome1.jpg", "<strong>Odd Fellows Home</strong> (May 2011)<br/>\n" + "<em>Source:</em> <a href=\"http://www.mhs.mb.ca/docs/people/goldsborough_lg.shtml\">Gordon Goldsborough</a>","2024-05-06 14:24:23"  )
            val photo5 = SitePhotos(140229,3817,  "3817_oddfellowshome5_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome5.jpg", "<strong>Rear view of the Odd Fellows Home with Girls entrance</strong> (December 2020)<br/>\n" + "<em>Source:</em> <a href=\"http://www.mhs.mb.ca/docs/people/goldsborough_lg.shtml\">Gordon Goldsborough</a>","2024-05-06 14:24:23"  )
            val photo6 = SitePhotos(140229,3817,  "3817_oddfellowshome6_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome6.jpg", "<strong>Rear view of the Odd Fellows Home with Boys entrance</strong> (December 2020)<br/>\n" + "<em>Source:</em> <a href=\"http://www.mhs.mb.ca/docs/people/goldsborough_lg.shtml\">Gordon Goldsborough</a>","2024-05-06 14:24:23"  )
            val photo7 = SitePhotos(140229,3817,  "3817_oddfellowshome7_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome7.jpg", "<strong>Remaining fence post of the Odd Fellows Home grounds</strong> (December 2020)<br/>\n" + "<em>Source:</em> <a href=\"http://www.mhs.mb.ca/docs/people/goldsborough_lg.shtml\">Gordon Goldsborough</a>","2024-05-06 14:24:23"  )
            val photo8 = SitePhotos(140229,3817,  "3817_oddfellowshome8_1715023463.jpg", 600, 400,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome8.jpg", "<strong>Front view of the Odd Fellows Home</strong> (May 2020)<br/>\n" + "<em>Source:</em> <a href=\"http://www.mhs.mb.ca/docs/people/penner_g.shtml\">George Penner</a> ","2024-05-06 14:24:23"  )
            val allSitePhotos = listOf(photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8)
            val uriHandler = LocalUriHandler.current

            val souce1 = "“Tenders wanted,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 7 June 1916, page 2."
            val souce2 = "“Fraternal notes,” <a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\"><em>Winnipeg Tribune</em></a>, 1 July 1916, page 13. "
            val souce3 = "“500 Odd Fellows at anniversary service,” <a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\"><em>Winnipeg Tribune</em></a>, 30 April 1917, page 5."
            val souce4 = "“Tenders called for Odd Fellows Home,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 10 September 1917, page 8."
            val souce5 = "“Lodge News [Independent order of Odd Fellows],” <a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\"><em>Winnipeg Tribune</em></a>, 19 January 1918, page 23."
            val souce6 = "“Tenders,” <a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\"><em>Winnipeg Tribune</em></a>, 28 February 1921, page 2. "
            val souce7 = "“News of bazaars,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 19 November 1921, page 9. "
            val souce8 = "“Odd Fellows’ Grand Lodge installation,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 10 March 1922, page 7."
            val souce9 = "“Plans for a \$100,000 Odd Fellows Home,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 5 April 1922, page 8."
            val souce10 = "“Proposed Odd Fellows’ Home for Charleswood,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\"><em>Manitoba Free Press</em></a>, 5 March 1921, page 18."
            val sourcesList = listOf(souce1, souce2, souce3, souce4, souce5, souce6, souce7, souce8, souce9, souce10)
            val cameraPositionState = CameraPositionState()
            val scrollState = rememberScrollState()
            DisplaySiteAndMapViewport(
                currentSite = testSite,
                displayState = displayState,
                onClickChangeDisplayState = {},
                currentSiteTypes = siteTypes,
                userLocation = userLocation,
                currentSitePhotos = allSitePhotos,
                currentSiteSourcesList = sourcesList,
                //siteDetailsScrollState = scrollState,

                cameraPositionState = cameraPositionState,

                allSites = listOf(testSite),
                onClusterItemClick = {},
                modifier = Modifier
                    .height(1000.dp)
                    .width(500.dp)
                )

                //modifier = Modifier.padding(5.dp)

        }
    }
    
}