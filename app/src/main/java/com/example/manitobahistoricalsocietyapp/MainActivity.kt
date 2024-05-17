package com.example.manitobahistoricalsocietyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.manitobahistoricalsocietyapp.site_main.SiteMainPageScreen
import com.example.manitobahistoricalsocietyapp.ui.theme.ManitobaHistoricalSocietyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ManitobaHistoricalSocietyAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    SiteMainPageScreen()
                }

               // GoogleMapClustering()



                    /*val db = HistoricalSiteDatabase.getDatabase(applicationContext)
                    *//*val allSites : List<HistoricalSite> by db.manitobaHistoricalSiteDao().getAllSites().collectAsState(
                        emptyList()
                    )*//*

                    val startingLatLong = LatLng(49.8555836, -97.2888901)
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(startingLatLong, 16f)
                    }*/



                    //Greeting(allSites.size.toString())
                    /*DisplayMap(
                        cameraPositionState = cameraPositionState,
                        sites = allSites,
                        onClusterItemClick = {Toast.makeText(this@MainActivity, "Clicked on site " + it.name, Toast.LENGTH_SHORT).show()},
                        modifier = Modifier.fillMaxSize()
                        )*/

            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Found $name! Sites",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ManitobaHistoricalSocietyAppTheme {
        Greeting("Android")
    }
}
