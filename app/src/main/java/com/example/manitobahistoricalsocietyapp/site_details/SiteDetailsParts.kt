package com.example.manitobahistoricalsocietyapp.site_details

import android.text.method.LinkMovementMethod
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.View.TEXT_ALIGNMENT_TEXT_START
import android.widget.TextView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.manitobahistoricalsocietyapp.R
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotos
import com.example.manitobahistoricalsocietyapp.database.SiteSource.SiteSource
import com.example.manitobahistoricalsocietyapp.state_classes.SiteDisplayState
import com.example.manitobahistoricalsocietyapp.ui.theme.ManitobaHistoricalSocietyAppTheme
import kotlin.math.roundToInt

@Composable
fun DisplaySiteTitle(
    name: String,
    displayState: SiteDisplayState,
    onClickChangeDisplayState: (SiteDisplayState) -> Unit,
    modifier: Modifier = Modifier
) {

    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
    ){
        Text(
            text = name,
            maxLines = if (displayState == SiteDisplayState.HalfSite) 2 else 10,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier
                .weight(1f)
                .padding(vertical = 10.dp, horizontal = 2.dp)


        )

        //Changes the icon, content description, and on click depending on state
        val newDisplayStateOnClick = if(displayState == SiteDisplayState.HalfSite) SiteDisplayState.FullSite else SiteDisplayState.HalfSite
        val icon =  if(displayState == SiteDisplayState.HalfSite) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
        val contentDescription = if(displayState == SiteDisplayState.HalfSite) "Show More Info" else "Show Less Info"


        IconButton(onClick = { onClickChangeDisplayState(newDisplayStateOnClick) },
            modifier =  modifier.align(Alignment.Top)
        ) {
            Icon(imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(50.dp))
        }
    }

}

@Composable
fun DisplaySiteBasicInfo(
    siteTypes: List<String>,
    fullAddress: String,
    /*siteLocation: Location,
    userLocation: Location,*/
    metersFromUser: Float,
    modifier: Modifier = Modifier
    ) {
    val types = siteTypes.joinToString(separator = "/")
    val displayDistance = displayDistance(metersFromUser)
    Column (
        modifier = modifier
    ){
        Text(text = "$types, $displayDistance",
            style = MaterialTheme.typography.bodyLarge,)
        Text(text = fullAddress)

        
        


    }
}

//Displays the text for the distance from user
private fun displayDistance(metersFromUser: Float): String{

        //When meters is greater than 100 km
    val displayDistance = if (metersFromUser > 100000) (metersFromUser/1000).roundToInt().toString() + " km"
        //When meters is greater than 10 km
        else if (metersFromUser > 10000)  "%.1f".format (metersFromUser/1000) + " km"
        //When meters is greater than 1 km
        else if (metersFromUser >= 1000) "%.2f".format (metersFromUser/1000) + " km"
        //When meters is less than 1 km
        else metersFromUser.roundToInt().toString() + " m"

    return "$displayDistance away"
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplaySitePhoto(
    photoIndex: Int,
    totalNumberOfPhotos: Int,
    sitePhoto: SitePhotos,
    uriHandler: UriHandler,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        SubcomposeAsyncImage(
            model = sitePhoto.url,
            contentDescription = sitePhoto.name,
            loading = {
                CircularProgressIndicator()
            },
            contentScale = ContentScale.Fit,
            error ={ AsyncImage(model = sitePhoto.url, contentDescription = sitePhoto.name, error = painterResource(id = R.drawable.error) )}            ,
            modifier = modifier
                //.fillMaxWidth().wrapContentHeight(
                .size(height = (sitePhoto.height / 2).dp, width = (sitePhoto.width / 2).dp)
                //.aspectRatio(sitePhoto.width.toFloat() / sitePhoto.height.toFloat())
                .height(sitePhoto.height.dp)
                //.size(10.dp)
                // .fillMaxWidth()
                //.wrapContentHeight()
                //.defaultMinSize(50.dp)
                .padding(5.dp)
                .combinedClickable(
                    onClick = { },
                    onLongClick = {
                        sitePhoto.url?.let { uriHandler.openUri(it) }
                    },
                    onLongClickLabel = sitePhoto.url
                )

        )

            


        sitePhoto.info?.let {
            GetAndroidViewWithStyle(textStyle = MaterialTheme.typography.bodyMedium, text = sitePhoto.info, textAlignment = TEXT_ALIGNMENT_CENTER, modifier = modifier.align(Alignment.CenterHorizontally))
        }

        Text(
            text = "$photoIndex/$totalNumberOfPhotos",
            modifier = modifier.align(Alignment.End)
        )

    }

}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplaySitePhotos(
    photos: List<SitePhotos>,
    uriHandler: UriHandler,
    modifier: Modifier = Modifier
) {
    val pageState = rememberPagerState{photos.size}

    /*LazyRow(
        contentPadding = PaddingValues(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        state = state,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
        modifier = modifier.fillMaxWidth()
    )*/
    Column {
        HorizontalPager(
            state = pageState,
            contentPadding = PaddingValues(10.dp),
            modifier = modifier.fillMaxWidth()


        )
        {
                index ->
            DisplaySitePhoto(photoIndex = index + 1, totalNumberOfPhotos = photos.size, sitePhoto = photos[index], uriHandler = uriHandler )


        }

        //This is the code that puts a row of circles to show how many photos there are
        Row(
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.End)
                .padding(bottom = 5.dp),
            horizontalArrangement = Arrangement.Center
        ){
            repeat(pageState.pageCount){pageNum ->
                val colour = if(pageState.pageCount == pageNum) Color.DarkGray else Color.LightGray
                Box(modifier = Modifier
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(colour)
                    .size(15.dp)
                )

            }
        }


    }
}

//Displays if the are no photos for the site
@Composable
fun DisplayNoPhotos(
    uriHandler: UriHandler,
    modifier: Modifier = Modifier

) {
    val annotatedString = buildAnnotatedString {
        append("We have no photos for this site. If you have one in your personal collection and can provide a copy, please contact us at ")

        pushStringAnnotation(tag = "photo_email", annotation = "photos@mhs.mb.ca")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append("photos@mhs.mb.ca")
        }
        pop()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(5.dp)) {
        ClickableText(text = annotatedString, style = MaterialTheme.typography.bodyLarge.merge( TextStyle(
            textAlign = TextAlign.Center)
        ), onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "photo_email", start = offset, end = offset).firstOrNull()?.let {
                uriHandler.openUri(it.item)
            }
        },)
    }
}

@Composable
fun DisplaySiteDescription(
    siteInfo:String,
    modifier: Modifier = Modifier
) {
    GetAndroidViewWithStyle(textStyle = MaterialTheme.typography.bodyLarge, text = siteInfo, textAlignment = TEXT_ALIGNMENT_TEXT_START, modifier = modifier)

}





@Composable
fun DisplaySiteSources(
    sourceList: List<SiteSource>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = modifier
        ) {
            Text(
                text ="Sources:",
                style = MaterialTheme.typography.titleLarge
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            items(sourceList){ source ->
                source.info?.let { GetAndroidViewWithStyle(textStyle = MaterialTheme.typography.bodyLarge, text = it, textAlignment = TEXT_ALIGNMENT_TEXT_START ) }
                
            }
        }


    }
    
}

@Composable
fun DisplayLinkToHistoricalSocietyPage(siteUrl: String, uriHandler: UriHandler, modifier: Modifier = Modifier) {
    Text(
        text = "Click here to go to the Manitoba Historical Society webpage for this site!",
        style = MaterialTheme.typography.titleLarge.merge(TextStyle(textDecoration = TextDecoration.Underline)),
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier =modifier.clickable { uriHandler.openUri(siteUrl) }
    )
    
}


//Helper function that sets the style of text when I need to preserve the HTML contents
//Such as links or <br>
@Composable
fun GetAndroidViewWithStyle(
    textStyle: TextStyle,
    text:String,
    textAlignment: Int,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = {context -> TextView(context)},
        update = {
            it.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
            it.movementMethod = LinkMovementMethod.getInstance()
            it.textSize = textStyle.fontSize.value
            it.textAlignment = textAlignment
            // it.letterSpacing = textStyle.letterSpacing.value
        }
    )
}






//Sample values used for testing
private const val longName:String = "Ebenezer Evangelical Lutheran Church / Bethel African Methodist Episcopal Church / First Norwegian Baptist Church / German Full Gospel Church / Indian Metis Holiness Chapel / Vietnamese Mennonite Church"
private const val description = "By June 1916, the <a href=\"http://www.mhs.mb.ca/docs/organization/ioof.shtml\">Independent Order of Odd Fellows</a> began searching in the greater <a href=\"http://www.mhs.mb.ca/docs/municipalities/winnipeg.shtml\">Winnipeg</a> area for property on which to built a home for elderly members and their spouses, as well as orphaned children of deceased members. These large grounds in the <a href=\"http://www.mhs.mb.ca/docs/municipalities/charleswood.shtml\">Rural Municipality of Charleswood</a> were selected and a contest was held for the design plans. The winning entry was drawn up by Winnipeg architect <a href=\"http://www.mhs.mb.ca/docs/people/russell_jhg.shtml\">John Hamilton Gordon Russell</a>. It called for a structure measuring 120 feet by 66 feet, costing \$30,000 to \$40,000, with capacity for around 40 beds. Excavation was to begin by the fall of 1917. However, construction did not proceed and, over the following years, additional funds were raised to build a larger facility.<br><br>Construction on the present building began in 1922, with site preparation and excavation work underway by the spring. A cornerstone-laying ceremony officiated by Grand Secretary <a href=\"http://www.mhs.mb.ca/docs/people/deering_bd.shtml\">Benjamin Draper Deering</a> was held on 15 July. It was to be the second IOOF Home in Canada (the first being located in Toronto) and one of 57 such Homes across North America. Once completed, it would measure 140 feet by 30 feet, with two south wings (each 24 feet by 30 feet), and a joint dining room and kitchen (measuring 33 feet by 56 feet). Accomodation was provided for 70 people with additional undeveloped capacity for orphans in the attic. The two-storey structure cost around \$125,000 with another \$25,000 in furnishings and equipment. In the basement, in addition to the steam heating plant, there were two large playrooms for orphans along with two corresponding rear (north side) entrances to the building labeled “Girls” and “Boys.” A school was later operated at the site.<br><br>The facility was opened officially on 13 March 1923 at a ceremony attended by some 850 people. It was dedicated by Lucian J. Eastin (IOOF Grand Sire of St. Joseph, Missouri) and Lieutenant-Governor <a href=\"http://www.mhs.mb.ca/docs/people/aikins_jam.shtml\">James Albert Manning Aikins</a> addressed the crowd.<br><br>In 1997, the facility was closed following withdrawal of government funding. The building was given a \$2 million renovation, resulting in seven studio apartments and 25 single bedroom suites. In April 2001, it reopened as an assisted living facility known as Assiniboine Links. A private residential subdivision was later constructed between the building and the Assiniboine River.<br><br>The building became a <a href=\"http://www.mhs.mb.ca/docs/sites/municipal.shtml\">municipally-designated heritage site</a> in January 2023.<br><br>"
@Preview
@Composable
private fun PreviewSiteTitleHalfSite () {
    ManitobaHistoricalSocietyAppTheme {
        Surface {
            DisplaySiteTitle(name = longName, displayState = SiteDisplayState.HalfSite, onClickChangeDisplayState = {})
        }
    }
}

@Preview
@Composable
private fun PreviewSiteTitleFullSite () {
    ManitobaHistoricalSocietyAppTheme {
        Surface {
            DisplaySiteTitle(name = longName, displayState = SiteDisplayState.FullSite, onClickChangeDisplayState = {})
        }
    }
}

@Preview
@Composable
private fun PreviewSiteBasicInfo () {
    ManitobaHistoricalSocietyAppTheme {
        Surface {
            DisplaySiteBasicInfo(
                siteTypes = listOf("Building", "Museum or Archives"),
                fullAddress = "333 Alexander Avenue, Winnipeg",
                metersFromUser = 10567f
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSitePhoto()
{
    ManitobaHistoricalSocietyAppTheme {
        Surface {
            val photo1 = SitePhotos(140229,3817,  "3817_oddfellowshome2_1715023463.jpg", 600, 422,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome2.jpg", "<strong>Architect’s drawing of the Odd Fellows Home</strong> (1922)<br/><a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\">Manitoba Free Press</a>, 15 July 1922, page 48.", "2024-05-06 14:24:23"  )
            DisplaySitePhoto(photoIndex = 1, totalNumberOfPhotos = 3, sitePhoto = photo1, LocalUriHandler.current )
        }
    }
}

@Preview
@Composable
private fun PreviewSitePhotos()
{
    ManitobaHistoricalSocietyAppTheme {
        Surface {
            val photo1 = SitePhotos(140229,3817,  "3817_oddfellowshome2_1715023463.jpg", 600, 422,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome2.jpg", "<strong>Architect’s drawing of the Odd Fellows Home</strong> (1922)<br/><a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\">Manitoba Free Press</a>, 15 July 1922, page 48.", "2024-05-06 14:24:23"  )
            val photo2 = SitePhotos(140229,3817,  "3817_oddfellowshome2_1715023463.jpg", 600, 250,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome4.jpg", "<strong>Odd Fellows Home</strong> (1923)<br/><a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\">Winnipeg Tribune</a>, 13 March 1923, page 2.", "2024-05-06 14:24:23"  )
            val photo3 = SitePhotos(140229,3817,  "3817_oddfellowshome2_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome3.jpg", "<strong>Odd Fellows Home</strong> (no date)<br/>\n" +
                    "<em>Source:</em> Jack Hardman", "2024-05-06 14:24:23"  )

            val bunchOfPhotos :List<SitePhotos> = listOf(photo1, photo2, photo3)
            DisplaySitePhotos(photos = bunchOfPhotos, LocalUriHandler.current)
        }
    }
}

@Preview
@Composable
fun PreviewNoPhotos() {
    ManitobaHistoricalSocietyAppTheme {
        Surface {
            DisplayNoPhotos(LocalUriHandler.current)
        }
    }
}


@Preview
@Composable
fun PreviewSiteDescription() {
    ManitobaHistoricalSocietyAppTheme {
        Surface {
            DisplaySiteDescription(siteInfo = description )
        }
    }
}

@Preview
@Composable
fun PreviewSiteSources() {
    ManitobaHistoricalSocietyAppTheme {
        Surface {
            val source1 = SiteSource(1,2,"<a href=\"http://www.gov.mb.ca/chc/hrb/mun/m053.html\" target=\"_blank\">St. Andrews United Church, NE4-13-6 EPM Garson</a>, Manitoba Historic Resources Branch.", "")
            val source2 = SiteSource(2,2,"<em>One Hundred Years in the History of the Rural Schools of Manitoba: Their Formation, Reorganization and Dissolution (1871-1971)</em> by <a href=\"http://www.mhs.mb.ca/docs/people/perfect_mb.shtml\">Mary B. Perfect</a>, MEd thesis, University of Manitoba, April 1978.", "")
            val source3 = SiteSource(3,2,"“Goodbye, Miss Chips,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\">Winnipeg Free Press</a>, 26 May 1962, page 3.", "")
            val source4 = SiteSource(4,2,"This page was prepared by <a href=\"http://www.mhs.mb.ca/docs/people/penner_g.shtml\">George Penner</a>, <a href=\"http://www.mhs.mb.ca/docs/people/kuzina_r.shtml\">Rose Kuzina</a>, and <a href=\"http://www.mhs.mb.ca/docs/people/goldsborough_lg.shtml\">Gordon Goldsborough</a>.", "")
            val source5 = SiteSource(5,2,"<a href=\"/info/links/lac_cef.shtml\" target=\"_blank\">Attestation papers, Canadian Expeditionary Force</a> [John Amos Comba], Library and Archives Canada.", "")
            DisplaySiteSources(sourceList = listOf(source1, source2, source3, source4, source5))
        }
    }
}

@Preview
@Composable
fun PreviewSiteLink() {
    ManitobaHistoricalSocietyAppTheme {
        Surface {
            DisplayLinkToHistoricalSocietyPage(siteUrl = "http://www.mhs.mb.ca/docs/sites/oddfellowshome.shtml", uriHandler = LocalUriHandler.current )
        }
    }
}