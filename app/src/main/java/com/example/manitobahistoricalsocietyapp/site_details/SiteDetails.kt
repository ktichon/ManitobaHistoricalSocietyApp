package com.example.manitobahistoricalsocietyapp.site_details

import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
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
        val onIconClick = if(displayState == SiteDisplayState.HalfSite) onClickChangeDisplayState(SiteDisplayState.FullSite)  else onClickChangeDisplayState(SiteDisplayState.HalfSite)
        val icon =  if(displayState == SiteDisplayState.HalfSite) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
        val contentDescription = if(displayState == SiteDisplayState.HalfSite) "Show More Info" else "Show Less Info"


        IconButton(onClick = { onIconClick },
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

@Composable
fun DisplaySitePhoto(
    photoIndex: Int,
    totalNumberOfPhotos: Int,
    sitePhoto: SitePhotos,
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
            )

            


        sitePhoto.info?.let {
            AndroidView(
                modifier = modifier,
                factory = {context -> TextView(context)},
                update = {
                    it.text = HtmlCompat.fromHtml(sitePhoto.info, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    it.movementMethod = LinkMovementMethod.getInstance()
                }

            )
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
            DisplaySitePhoto(photoIndex = index + 1, totalNumberOfPhotos = photos.size, sitePhoto = photos[index])


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
                Log.d("Photo Email", it.item)
            }
        },)
    }
}






private const val longName:String = "Ebenezer Evangelical Lutheran Church / Bethel African Methodist Episcopal Church / First Norwegian Baptist Church / German Full Gospel Church / Indian Metis Holiness Chapel / Vietnamese Mennonite Church"
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
            DisplaySitePhoto(photoIndex = 1, totalNumberOfPhotos = 3, sitePhoto = photo1 )
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
            DisplaySitePhotos(photos = bunchOfPhotos)
        }
    }
}

@Preview
@Composable
fun PreviewNoPhotos() {
    ManitobaHistoricalSocietyAppTheme {
        Surface {
            DisplayNoPhotos()
        }
    }
    
}