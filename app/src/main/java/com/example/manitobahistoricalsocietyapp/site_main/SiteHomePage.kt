package com.example.manitobahistoricalsocietyapp.site_main

import android.annotation.SuppressLint
import android.os.Looper
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.manitobahistoricalsocietyapp.Manifest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import org.w3c.dom.Text

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HistoricalSiteHome(
    viewModel: HistoricalSiteViewModel = HistoricalSiteViewModel(LocalContext.current),
    modifier: Modifier = Modifier
) {

    val currentSiteState by viewModel.currentSiteState.collectAsState()
    

    val manitobaMuseumCoordinates = LatLng(49.9000253, -97.1386276)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(manitobaMuseumCoordinates, 16f)
    }

    //Old code, now replaced by RequestLocationPermission
    /*val permissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    currentSiteState.locationEnabled = permissionState.allPermissionsGranted\
    var haveAskedForPermissions by rememberSaveable {
        mutableStateOf(false)
    }
    if (!permissionState.allPermissionsGranted && !haveAskedForPermissions){
          RequestPermissionAlert(
              onConfirmClick = {
                               permissionState.launchMultiplePermissionRequest()
                               haveAskedForPermissions = true
                               },
              onDismiss = {
                  haveAskedForPermissions = true
              })
      }*/

    //Request location permissions, and storing the value in currentSiteState
    RequestLocationPermission(
        onPermissionGranted = {
            currentSiteState.locationEnabled = true
                              },
        onPermissionDenied = {currentSiteState.locationEnabled = false}
    )

    //If location enable is set to true
    if (currentSiteState.locationEnabled ){
        getUserLocation(
            onNewLocation = {newLocation -> viewModel.updateUserLocation(newLocation)},
            locationProvider = viewModel.locationProvider)
    }





   Scaffold {innerPadding ->


       DisplaySiteAndMapViewport(
           cameraPositionState = cameraPositionState,
           allSites = currentSiteState.allHistoricalSites,
           onClusterItemClick = {
               newSite -> viewModel.newSiteSelected(newSite)
               cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(newSite.latitude, newSite.longitude), 16f)
                                },
           currentSite = currentSiteState.currentSite,
           displayState = currentSiteState.displayState,
           onClickChangeDisplayState = {newState -> viewModel.updateSiteDisplayState(newState)},
           currentSiteTypes = currentSiteState.siteTypes,
           userLocation = currentSiteState.currentUserLocation,
           locationEnabled = currentSiteState.locationEnabled,
           currentSitePhotos = currentSiteState.sitePhotos,
           currentSiteSourcesList = currentSiteState.siteSources,
           //siteDetailsScrollState = ,
           modifier = Modifier.padding(innerPadding)
       )

   }

}

/*@Composable
fun RequestPermissionAlert(
    onConfirmClick: ()-> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = {
                Text(text = "Enable Location Permissions?")
        },
        text = {
               Text(text = "This uses your location to show where you are in relation to the sites")
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = { onConfirmClick() }) {
                Text(text = "OK")

        } },

    )

}*/


//Asks for user permission
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted:  () -> Unit,
    onPermissionDenied: () -> Unit,
    //onPermissionsRevoked: () -> Unit
) {
    // Initialize the state for managing multiple location permissions.
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // Use LaunchedEffect to handle permissions logic when the composition is launched.
    LaunchedEffect(key1 = permissionState) {
        // Check if all previously granted permissions are revoked.
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        // Filter permissions that need to be requested.
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }

        // If there are permissions to request, launch the permission request.
        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()

        // Execute callbacks based on permission status.
        if (allPermissionsRevoked) {
            onPermissionDenied()
        } else {
            if (permissionState.allPermissionsGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }
}


//We check to see if they have permissions earlier
@Composable
@SuppressLint("MissingPermission")
fun getUserLocation(
    onNewLocation: (LatLng?) -> Unit,
    locationProvider: FusedLocationProviderClient,
){
    //update every 10 seconds
    val updateInterval: Long = 10000

    val locationCallback = object: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            locationResult.lastLocation?.let {
                val newLocation = LatLng(it.latitude, it.longitude)
                onNewLocation(newLocation)
            }
        }
    }

    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, updateInterval    ).build()

    DisposableEffect(Unit) {
        locationProvider.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        onDispose {
            locationProvider.removeLocationUpdates(locationCallback)
        }
    }




}

