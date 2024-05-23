package com.example.manitobahistoricalsocietyapp.site_ui

import android.text.method.LinkMovementMethod
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.View.TEXT_ALIGNMENT_TEXT_START
import android.widget.TextView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import com.example.manitobahistoricalsocietyapp.R
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotos
import com.example.manitobahistoricalsocietyapp.database.SiteSource.SiteSource
import com.example.manitobahistoricalsocietyapp.storage_classes.SiteDisplayState
import com.example.manitobahistoricalsocietyapp.ui.theme.AppTheme

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
    ){
        Text(
            text = name,
            maxLines = if (displayState == SiteDisplayState.HalfSite) 2 else 10,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge,

            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 2.dp)
        )

        //Changes the icon, content description, and on click depending on state
        val newDisplayStateOnClick = if(displayState == SiteDisplayState.HalfSite) SiteDisplayState.FullSite else SiteDisplayState.HalfSite
        val icon =  if(displayState == SiteDisplayState.HalfSite) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
        val contentDescription = if(displayState == SiteDisplayState.HalfSite) "Show More Info" else "Show Less Info"


        IconButton(onClick = { onClickChangeDisplayState(newDisplayStateOnClick) },
            modifier =  Modifier.align(Alignment.Top)
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
    distanceFromUser: String,
    modifier: Modifier = Modifier
    ) {
    val types = siteTypes.joinToString(separator = "/")
    Row (modifier = modifier) {
        Column{
            Text(text = "$types, $distanceFromUser",
                style = MaterialTheme.typography.bodyLarge,)
            Text(text = fullAddress,
                style = MaterialTheme.typography.bodyLarge,)
        }
    }

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
        //Should use the simple AsyncImage before I try and use the more complex stuff
        //As the more complex stuff (such as a loading wheel) might not be necessary
        AsyncImage(
            model = sitePhoto.url,
            contentDescription = sitePhoto.name,
            error = painterResource(id = R.drawable.error),
            placeholder = painterResource(id = R.drawable.photo_placeholder_outlined),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(height = (sitePhoto.height / 2).dp, width = (sitePhoto.width / 2).dp)
                .padding(5.dp)
                .combinedClickable(
                    onClick = { },
                    onLongClick = {
                        uriHandler.openUri(sitePhoto.url)
                    },
                    onLongClickLabel = sitePhoto.url
                )

        )

        sitePhoto.info?.let {
            GetAndroidViewWithStyle(
                text = sitePhoto.info,
                textStyle = MaterialTheme.typography.bodyMedium,
                textColour = MaterialTheme.colorScheme.onSurface,
                textAlignment = TEXT_ALIGNMENT_CENTER,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
            )
        }

        Text(
            text = "$photoIndex/$totalNumberOfPhotos",
            modifier = Modifier.align(Alignment.End)
        )

    }

}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplaySitePhotos(
    photos: List<SitePhotos>,
    uriHandler: UriHandler,
    pageState: PagerState,
    modifier: Modifier = Modifier
) {

    Row (
        modifier = modifier
    ){
        Column(

        ) {
            HorizontalPager(
                state = pageState,
                //contentPadding = PaddingValues(10.dp),
                modifier = Modifier.fillMaxWidth()


            )
            {
                    index ->
                DisplaySitePhoto(photoIndex = index + 1, totalNumberOfPhotos = photos.size, sitePhoto = photos[index], uriHandler = uriHandler )
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
    //One way to presive a link in a string
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)){
            append("We have no photos for this site. If you have one in your personal collection and can provide a copy, please contact us at ")
        }


        pushStringAnnotation(tag = "photo_email", annotation = "photos@mhs.mb.ca")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append("photos@mhs.mb.ca")
        }
        pop()
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ){
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
    Row(
        modifier = modifier
    ) {
        GetAndroidViewWithStyle(
            textStyle = MaterialTheme.typography.bodyLarge,
            textColour = MaterialTheme.colorScheme.onSurface,
            text = siteInfo,
            textAlignment = TEXT_ALIGNMENT_TEXT_START)
    }



}





@Composable
fun DisplaySiteSources(
    sourcesList: List<String>,
    modifier: Modifier = Modifier
) {
    Row (modifier = modifier){
        Column {
            Text(
                text = "Sources:",
                style = MaterialTheme.typography.titleLarge
            )
            if (sourcesList.isEmpty()){
                Text(
                    text = "There is no additional information about the sources used for this site",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                )
            } else{
                sourcesList.forEach {
                    GetAndroidViewWithStyle(
                        textStyle = MaterialTheme.typography.bodyLarge,
                        textColour = MaterialTheme.colorScheme.onSurface,
                        text = it,
                        textAlignment = TEXT_ALIGNMENT_TEXT_START,
                        Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                    )
                }

            }
        }

    }
}

@Composable
fun DisplaySiteLink(siteUrl: String, uriHandler: UriHandler, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth()
    ){
        Text(
            text = "Click here to go to the Manitoba Historical Society webpage for this site!",
            style = MaterialTheme.typography.titleLarge.merge(TextStyle(textDecoration = TextDecoration.Underline)),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable { uriHandler.openUri(siteUrl) }
        )
    }

    
}


//Helper function that sets the style of text when I need to preserve the HTML contents
//Such as links or <br>
@Composable
fun GetAndroidViewWithStyle(
    textStyle: TextStyle,
    textColour: Color,
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
            it.setTextColor(textColour.toArgb())
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
    AppTheme {
        Surface {
            DisplaySiteTitle(name = longName, displayState = SiteDisplayState.HalfSite, onClickChangeDisplayState = {})
        }
    }
}

@Preview
@Composable
private fun PreviewSiteTitleFullSite () {
    AppTheme {
        Surface {
            DisplaySiteTitle(name = longName, displayState = SiteDisplayState.FullSite, onClickChangeDisplayState = {})
        }
    }
}

@Preview
@Composable
private fun PreviewSiteBasicInfo () {
    AppTheme {
        Surface {
            DisplaySiteBasicInfo(
                siteTypes = listOf("Building", "Museum or Archives"),
                fullAddress = "333 Alexander Avenue, Winnipeg",
                distanceFromUser = "100 km away"
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSitePhoto()
{
    AppTheme {
        Surface {
            val photo1 = SitePhotos(140229,3817,  "3817_oddfellowshome2_1715023463.jpg", 600, 422,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome2.jpg", "<strong>Architect’s drawing of the Odd Fellows Home</strong> (1922)<br/><a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\">Manitoba Free Press</a>, 15 July 1922, page 48.", "2024-05-06 14:24:23"  )
            DisplaySitePhoto(photoIndex = 1, totalNumberOfPhotos = 3, sitePhoto = photo1, LocalUriHandler.current )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun PreviewSitePhotos()
{
    AppTheme {
        Surface {
            val photo1 = SitePhotos(140229,3817,  "3817_oddfellowshome2_1715023463.jpg", 600, 422,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome2.jpg", "<strong>Architect’s drawing of the Odd Fellows Home</strong> (1922)<br/><a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\">Manitoba Free Press</a>, 15 July 1922, page 48.", "2024-05-06 14:24:23"  )
            val photo2 = SitePhotos(140229,3817,  "3817_oddfellowshome2_1715023463.jpg", 600, 250,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome4.jpg", "<strong>Odd Fellows Home</strong> (1923)<br/><a href=\"http://www.mhs.mb.ca/docs/business/tribune.shtml\">Winnipeg Tribune</a>, 13 March 1923, page 2.", "2024-05-06 14:24:23"  )
            val photo3 = SitePhotos(140229,3817,  "3817_oddfellowshome2_1715023463.jpg", 600, 450,"http://www.mhs.mb.ca/docs/sites/images/oddfellowshome3.jpg", "<strong>Odd Fellows Home</strong> (no date)<br/>\n" +
                    "<em>Source:</em> Jack Hardman", "2024-05-06 14:24:23"  )

            val bunchOfPhotos :List<SitePhotos> = listOf(photo1, photo2, photo3)
            DisplaySitePhotos(photos = bunchOfPhotos, LocalUriHandler.current, rememberPagerState {
                bunchOfPhotos.size
            })
        }
    }
}

@Preview
@Composable
fun PreviewNoPhotos() {
    AppTheme {
        Surface {
            DisplayNoPhotos(LocalUriHandler.current)
        }
    }
}


@Preview
@Composable
fun PreviewSiteDescription() {
    AppTheme {
        Surface {
            DisplaySiteDescription(siteInfo = description )
        }
    }
}

@Preview
@Composable
fun PreviewSiteSources() {
    AppTheme {
        Surface {
            val source1 = SiteSource(1,2,"<a href=\"http://www.gov.mb.ca/chc/hrb/mun/m053.html\" target=\"_blank\">St. Andrews United Church, NE4-13-6 EPM Garson</a>, Manitoba Historic Resources Branch.", "").info
            val source2 = SiteSource(2,2,"<em>One Hundred Years in the History of the Rural Schools of Manitoba: Their Formation, Reorganization and Dissolution (1871-1971)</em> by <a href=\"http://www.mhs.mb.ca/docs/people/perfect_mb.shtml\">Mary B. Perfect</a>, MEd thesis, University of Manitoba, April 1978.", "").info
            val source3 = SiteSource(3,2,"“Goodbye, Miss Chips,” <a href=\"http://www.mhs.mb.ca/docs/business/freepress.shtml\">Winnipeg Free Press</a>, 26 May 1962, page 3.", "").info
            val source4 = SiteSource(4,2,"This page was prepared by <a href=\"http://www.mhs.mb.ca/docs/people/penner_g.shtml\">George Penner</a>, <a href=\"http://www.mhs.mb.ca/docs/people/kuzina_r.shtml\">Rose Kuzina</a>, and <a href=\"http://www.mhs.mb.ca/docs/people/goldsborough_lg.shtml\">Gordon Goldsborough</a>.", "").info
            val source5 = SiteSource(5,2,"<a href=\"/info/links/lac_cef.shtml\" target=\"_blank\">Attestation papers, Canadian Expeditionary Force</a> [John Amos Comba], Library and Archives Canada.", "").info
            DisplaySiteSources(sourcesList = listOf(source1, source2, source3, source4, source5))
        }
    }
}

@Preview
@Composable
fun PreviewSiteLink() {
    AppTheme {
        Surface {
            DisplaySiteLink(siteUrl = "http://www.mhs.mb.ca/docs/sites/oddfellowshome.shtml", uriHandler = LocalUriHandler.current )
        }
    }
}
