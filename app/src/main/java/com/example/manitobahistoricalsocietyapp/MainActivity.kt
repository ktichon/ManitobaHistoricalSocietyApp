package com.example.manitobahistoricalsocietyapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSite
import com.example.manitobahistoricalsocietyapp.database.HistoricalSiteDatabase
import com.example.manitobahistoricalsocietyapp.map.DisplayMap
import com.example.manitobahistoricalsocietyapp.ui.theme.ManitobaHistoricalSocietyAppTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

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
                    val db = HistoricalSiteDatabase.getDatabase(applicationContext)
                    val allSites : List<HistoricalSite> by db.manitobaHistoricalSiteDao().getAllSites().collectAsState(
                        emptyList()
                    )

                    val startingLatLong = LatLng(49.8555836, -97.2888901)
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(startingLatLong, 16f)
                    }



                    Greeting(allSites.size.toString())
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
