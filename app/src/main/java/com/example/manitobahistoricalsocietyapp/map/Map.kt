package com.example.manitobahistoricalsocietyapp.map

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSiteClusterItem
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties

@Composable
fun DisplayMap(

    cameraPositionState: CameraPositionState,
    sites: List<HistoricalSiteClusterItem>,
    onSiteSelected: (siteClusterItem: HistoricalSiteClusterItem)  -> Unit,
    locationEnabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    //If the system is in dark mode, use dark map. Else use light map
    /*val mapProperties by remember {
        mutableStateOf(MapProperties(
            isMyLocationEnabled = locationEnabled,
            mapStyleOptions = MapStyleOptions(if (isSystemInDarkTheme()) mapStylingNight else mapStylingDay)
        ))
    }*/


    GoogleMap(
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = locationEnabled,
            //If the system is in dark mode, use dark map. Else use light map
            mapStyleOptions = MapStyleOptions(if (isSystemInDarkTheme()) mapStylingNight else mapStylingDay)
        ),
        modifier = modifier
    ){
        CustomClusterRenderer(sites = sites, onSiteSelected = onSiteSelected)
    }

}


val mapStylingDay ="""
   [
     {
       "featureType": "administrative",
       "elementType": "geometry",
       "stylers": [
         {
           "visibility": "off"
         }
       ]
     },
     {
       "featureType": "administrative.country",
       "elementType": "geometry.stroke",
       "stylers": [
         {
           "visibility": "on"
         }
       ]
     },
     {
       "featureType": "administrative.locality",
       "elementType": "geometry.stroke",
       "stylers": [
         {
           "visibility": "on"
         }
       ]
     },
     {
       "featureType": "administrative.province",
       "elementType": "geometry.stroke",
       "stylers": [
         {
           "visibility": "on"
         }
       ]
     },
     {
       "featureType": "poi",
       "stylers": [
         {
           "visibility": "off"
         }
       ]
     },
     {
       "featureType": "road",
       "elementType": "labels.icon",
       "stylers": [
         {
           "visibility": "off"
         }
       ]
     },
     {
       "featureType": "transit",
       "stylers": [
         {
           "visibility": "off"
         }
       ]
     }
   ]
""".trimIndent()

val mapStylingNight = """
    [
      {
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#242f3e"
          }
        ]
      },
      {
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#746855"
          }
        ]
      },
      {
        "elementType": "labels.text.stroke",
        "stylers": [
          {
            "color": "#242f3e"
          }
        ]
      },
      {
        "featureType": "administrative",
        "elementType": "geometry",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "administrative",
        "elementType": "geometry.stroke",
        "stylers": [
          {
            "color": "#d59563"
          }
        ]
      },
      {
        "featureType": "administrative.country",
        "elementType": "geometry.stroke",
        "stylers": [
          {
            "color": "#d59563"
          },
          {
            "visibility": "on"
          }
        ]
      },
      {
        "featureType": "administrative.locality",
        "elementType": "geometry.stroke",
        "stylers": [
          {
            "color": "#d59563"
          },
          {
            "visibility": "on"
          }
        ]
      },
      {
        "featureType": "administrative.locality",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#d59563"
          }
        ]
      },
      {
        "featureType": "administrative.province",
        "elementType": "geometry.stroke",
        "stylers": [
          {
            "color": "#d59563"
          },
          {
            "visibility": "on"
          }
        ]
      },
      {
        "featureType": "poi",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "poi",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#d59563"
          }
        ]
      },
      {
        "featureType": "poi.park",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#263c3f"
          }
        ]
      },
      {
        "featureType": "poi.park",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#6b9a76"
          }
        ]
      },
      {
        "featureType": "road",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#38414e"
          }
        ]
      },
      {
        "featureType": "road",
        "elementType": "geometry.stroke",
        "stylers": [
          {
            "color": "#212a37"
          }
        ]
      },
      {
        "featureType": "road",
        "elementType": "labels.icon",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "road",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#9ca5b3"
          }
        ]
      },
      {
        "featureType": "road.highway",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#746855"
          }
        ]
      },
      {
        "featureType": "road.highway",
        "elementType": "geometry.stroke",
        "stylers": [
          {
            "color": "#1f2835"
          }
        ]
      },
      {
        "featureType": "road.highway",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#f3d19c"
          }
        ]
      },
      {
        "featureType": "transit",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "transit",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#2f3948"
          }
        ]
      },
      {
        "featureType": "transit.station",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#d59563"
          }
        ]
      },
      {
        "featureType": "water",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#17263c"
          }
        ]
      },
      {
        "featureType": "water",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#515c6d"
          }
        ]
      },
      {
        "featureType": "water",
        "elementType": "labels.text.stroke",
        "stylers": [
          {
            "color": "#17263c"
          }
        ]
      }
    ]
    """.trimIndent()










