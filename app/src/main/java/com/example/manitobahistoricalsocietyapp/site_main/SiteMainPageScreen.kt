package com.example.manitobahistoricalsocietyapp.site_main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
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

@OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
@Composable
fun SiteMainPageScreen(
    viewModel: HistoricalSiteViewModel = viewModel(),
    context: Context = LocalContext.current,


    modifier: Modifier = Modifier
) {
    val displayState by viewModel.displayState.collectAsState()
    val currentSite by viewModel.currentSite.collectAsState()

    val siteTypes by viewModel.siteTypes.collectAsState()
    val sitePhotos by viewModel.sitePhotos.collectAsState()
    val siteSources by viewModel.siteSources.collectAsState()
    val locationEnabled by viewModel.locationEnabled.collectAsState()
    val currentUserLocation by viewModel.currentUserLocation.collectAsState()

    val allSiteClusterItems by viewModel.allHistoricalSiteClusterItems.collectAsState()

    //Used to let composable know that a new site has been selected, so that we can scroll to top
    val newSiteSelected by viewModel.newSiteSelected.collectAsState()
    //var newSiteSelected by remember { mutableStateOf(false) }

    var showLoadingScreen by remember { mutableStateOf(true) }

    val locationProvider by remember {
        mutableStateOf(LocationServices.getFusedLocationProviderClient(context))
    }



    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(currentUserLocation, 16f)
    }



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




    //Only show map if all the sites are loaded
    if (showLoadingScreen && allSiteClusterItems.isEmpty()){
        LoadingScreen(
            waitOn = { viewModel.getAllHistoricalSites() },
            onCompleted = { showLoadingScreen = false
                cameraPositionState.position = CameraPosition.fromLatLngZoom(currentUserLocation, 16f)
                          },
            modifier = modifier
        )
    } else {


        SiteMainPageContent(
            cameraPositionState = cameraPositionState,
            allSites = allSiteClusterItems,
            onClusterItemClick = { id -> viewModel.newSiteSelected(id)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(currentSite.getPosition(), cameraPositionState.position.zoom)
            },
            currentSite = currentSite,
            displayState = displayState,
            onClickChangeDisplayState = {newState ->
                viewModel.updateSiteDisplayState(newState) },
            currentSiteTypes = siteTypes,
            userLocation =  currentUserLocation,
            locationEnabled = locationEnabled,
            currentSitePhotos = sitePhotos,
            currentSiteSourcesList = siteSources,
            newSiteSelected = newSiteSelected,
            updateNewSiteSelected = {
                    selected -> viewModel.updateSiteSelected(selected)
            },
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

    DisposableEffect(locationProvider) {
        locationProvider.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        onDispose {
            locationProvider.removeLocationUpdates(locationCallback)
        }
    }




}




