package com.example.manitobahistoricalsocietyapp.site_main

import android.annotation.SuppressLint
import android.os.Looper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
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
    viewModel: HistoricalSiteViewModel = viewModel(),


    modifier: Modifier = Modifier
) {
    val displayState by viewModel.displayState.collectAsState()
    val currentSite by viewModel.currentSite.collectAsState()
    val allSites by viewModel.allHistoricalSites.collectAsState()
    val siteTypes by viewModel.siteTypes.collectAsState()
    val sitePhotos by viewModel.sitePhotos.collectAsState()
    val siteSources by viewModel.siteSources.collectAsState()
    val locationEnabled by viewModel.locationEnabled.collectAsState()
    val currentUserLocation by viewModel.currentUserLocation.collectAsState()

    //viewModel.getAllHistoricalSites()


    val locationProvider = LocationServices.getFusedLocationProviderClient(LocalContext.current)

    

    //val manitobaMuseumCoordinates = LatLng(49.9000253, -97.1386276)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(currentUserLocation, 16f)
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
            viewModel.updateLocationEnabled(true)
                              },
        onPermissionDenied = {viewModel.updateLocationEnabled(false)}
    )

    //If location enable is set to true
    if (locationEnabled  ){
        getUserLocation(
            onNewLocation = {newLocation -> viewModel.updateUserLocation(newLocation)},
            locationProvider = locationProvider)
    }


    var showLoadingScreen by remember { mutableStateOf(true) }

    //Only show map if all the sites are loaded
    if (showLoadingScreen && allSites.isEmpty()){
        LoadingScreen(
            waitOn = { viewModel.getAllHistoricalSites() },
            onCompleted = { showLoadingScreen = false },
            modifier = modifier
        )
    } else {

        Scaffold(
            modifier = modifier
        ) {innerPadding ->


            DisplaySiteAndMapViewport(
                cameraPositionState = cameraPositionState,
                allSites = allSites,
                onClusterItemClick = {
                        newSite -> viewModel.newSiteSelected(newSite)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(newSite.latitude, newSite.longitude), 16f)
                },
                currentSite = currentSite,
                displayState = displayState,
                onClickChangeDisplayState = {newState -> viewModel.updateSiteDisplayState(newState)},
                currentSiteTypes = siteTypes,
                userLocation =  currentUserLocation,
                locationEnabled = locationEnabled,
                currentSitePhotos = sitePhotos,
                currentSiteSourcesList = siteSources,
                //siteDetailsScrollState = ,
                modifier = Modifier.padding(innerPadding)
            )

        }

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

    DisposableEffect(locationProvider) {
        locationProvider.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        onDispose {
            locationProvider.removeLocationUpdates(locationCallback)
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
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        val currentOnCompeted by rememberUpdatedState( onCompleted)

        LaunchedEffect(Unit) {
            waitOn()
            currentOnCompeted()
        }

        Text(text = "Loading App Data ...",
            style = MaterialTheme.typography.titleLarge)

    }


}

