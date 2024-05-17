package com.example.manitobahistoricalsocietyapp.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manitobahistoricalsocietyapp.R
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSiteClusterItem
import com.example.manitobahistoricalsocietyapp.ui.theme.ClusterLessThan100
import com.example.manitobahistoricalsocietyapp.ui.theme.ClusterLessThan1000
import com.example.manitobahistoricalsocietyapp.ui.theme.ClusterLessThan20
import com.example.manitobahistoricalsocietyapp.ui.theme.ClusterLessThan50
import com.example.manitobahistoricalsocietyapp.ui.theme.ClusterLessThan500
import com.example.manitobahistoricalsocietyapp.ui.theme.ClusterMax
import com.example.manitobahistoricalsocietyapp.ui.theme.ManitobaHistoricalSocietyAppTheme
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.clustering.view.ClusterRenderer
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun CustomClusterRenderer(
    sites: List<HistoricalSiteClusterItem>,
    onClusterItemClick: (id:Int)  -> Unit,
    ) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    //Sets the algorithm for clustering. Using NonHierarchicalViewBasedAlgorithm which only loads items that would be on the screen
    val clusterManager = rememberClusterManager<HistoricalSiteClusterItem>()
    clusterManager?.setAlgorithm(
        NonHierarchicalViewBasedAlgorithm(
            screenWidth.value.toInt(),
            screenHeight.value.toInt()
        ),
    )

    //Sets what to display for the Cluster and Cluster Item
    val renderer = rememberClusterRenderer(
        clusterContent ={cluster ->
                        ClusterCircleContent(numOfItems = cluster.size)
        } ,
        clusterItemContent = {
                             ClusterItemContent(Modifier.size(50.dp))
        },
        clusterManager = clusterManager
    )

    //Use SideEffect to set onClick
    SideEffect {
        clusterManager?: return@SideEffect
        clusterManager.setOnClusterItemClickListener {
            onClusterItemClick(it.id)
            false
        }
    }
    //SideEffect to set renderer
    SideEffect {
        if (clusterManager?.renderer != renderer) {
            clusterManager?.renderer = renderer ?: return@SideEffect
        }
    }

    if (clusterManager != null) {
        Clustering(
            items = sites,
            clusterManager = clusterManager,
        )
    }


}

@Composable
fun ClusterCircleContent(
    //colour: Color,
    numOfItems: Int,
    modifier: Modifier = Modifier) {

    val colour = when(numOfItems){
        in 0..20 -> ClusterLessThan20
        in 21..50 -> ClusterLessThan50
        in 51..100 -> ClusterLessThan100
        in 101..500 -> ClusterLessThan500
        in 501..1000 -> ClusterLessThan1000
        else -> ClusterMax
    }
    val size = when(numOfItems){
        in 0..20 -> 40.dp
        in 21..50 -> 45.dp
        in 51..100 -> 50.dp
        in 101..500 -> 55.dp
        in 501..1000 -> 60.dp
        else -> 65.dp
    }




    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = colour,
        contentColor = Color.White,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                "%,d".format(numOfItems),
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,

            )
        }
    }
}

@Composable
fun ClusterItemContent( modifier: Modifier = Modifier,) {
    Image(
        painter = painterResource(id = R.drawable.site_location_marker),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        //colorFilter = ColorFilter.tint(color = ),
        modifier = modifier
        )
    
}




class ClusterCircleWithSizesPreviewProvider : PreviewParameterProvider<Int> {
    override val values: Sequence<Int>
        get() = sequenceOf(10, 35, 75, 150, 720, 8045)
}
@Preview
@Composable
private fun PreviewClusterCircle(
    @PreviewParameter(ClusterCircleWithSizesPreviewProvider::class) numOfItems :Int
) {
    ManitobaHistoricalSocietyAppTheme {
        ClusterCircleContent(
            numOfItems = numOfItems,
            //modifier = Modifier.size(50.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewClusterItem() {
    ClusterItemContent(Modifier.size(40.dp))

}