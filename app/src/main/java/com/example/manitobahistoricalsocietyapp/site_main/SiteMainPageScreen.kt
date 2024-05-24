package com.example.manitobahistoricalsocietyapp.site_main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manitobahistoricalsocietyapp.site_ui.LoadingScreen
import com.example.manitobahistoricalsocietyapp.site_ui.SiteMainPageContent
import com.example.manitobahistoricalsocietyapp.storage_classes.SiteDisplayState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun SiteMainPageScreen(
    viewModel: HistoricalSiteViewModel = viewModel(),
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,

) {
    val displayState by viewModel.displayState.collectAsState()
    val currentSite by viewModel.currentSite.collectAsState()

    val siteTypes by viewModel.siteTypes.collectAsState()
    val sitePhotos by viewModel.sitePhotos.collectAsState()
    val siteSources by viewModel.siteSources.collectAsState()
    val locationEnabled by viewModel.locationEnabled.collectAsState()
    val currentUserLocation by viewModel.currentUserLocation.collectAsState()
    val followUserLocation by viewModel.followUserLocation.collectAsState()

    val allSiteClusterItems by viewModel.allHistoricalSiteClusterItems.collectAsState()

    //Used to let composable know that a new site has been selected, so that we can scroll to top
    val newSiteSelected by viewModel.newSiteSelected.collectAsState()

    //Used for search bar
    val searchActive by viewModel.searchActive.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchedSitesList by viewModel.searchedSiteList.collectAsState()


    var showLoadingScreen by remember { mutableStateOf(true) }

    val locationProvider by remember {
        mutableStateOf(LocationServices.getFusedLocationProviderClient(context))
    }

    //used to un-focus from search bar
    val focusManager = LocalFocusManager.current

    val STARTING_ZOOM_LEVEL = 16f
    val SEARCH_ZOOM_LEVEL = 18f



    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(currentUserLocation, STARTING_ZOOM_LEVEL)
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


        GetUserLocation(
            onNewLocation = {newLocation ->
                viewModel.updateUserLocation(newLocation)
                newLocation?.let{
                    if (displayState == SiteDisplayState.FullMap && followUserLocation ){
                        if (showLoadingScreen){
                            cameraPositionState.position =CameraPosition.fromLatLngZoom(newLocation, STARTING_ZOOM_LEVEL)
                        } else {
                            cameraPositionState.move(CameraUpdateFactory.newLatLng(newLocation)) //= CameraPosition.fromLatLngZoom(newLocation, cameraPositionState.position.zoom)
                        }
                        viewModel.updateFollowUserLocation(false)
                    }

            }


                /*viewModel.updateFollowUserLocation(false)*/
                            },
            locationProvider = locationProvider)
    }





    //Only show map if all the sites are loaded
    if (showLoadingScreen && allSiteClusterItems.isEmpty()){
        LoadingScreen(
            waitOn = { viewModel.getAllHistoricalSites() },
            onCompleted = { showLoadingScreen = false
                //cameraPositionState.position = CameraPosition.fromLatLngZoom(currentUserLocation, 18f)
                          },
            modifier = modifier
        )
    } else {


        SiteMainPageContent(
            cameraPositionState = cameraPositionState,
            allSites = allSiteClusterItems,
            onSiteSelected = { siteSelected, searched ->
                viewModel.newSiteSelected(siteSelected.id)
                //If searched, zoom in to site. Else use the current zoom level
                if(searched){
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(siteSelected.position, SEARCH_ZOOM_LEVEL)
                } else
                    cameraPositionState.move(CameraUpdateFactory.newLatLng(siteSelected.position))

            },
            currentSite = currentSite,
            displayState = displayState,
            onClickChangeDisplayState = {newState ->
                viewModel.updateSiteDisplayState(newState)
                focusManager.clearFocus()
                                        },
            currentSiteTypes = siteTypes,
            userLocation =  currentUserLocation,
            locationEnabled = locationEnabled,
            currentSitePhotos = sitePhotos,
            currentSiteSourcesList = siteSources,
            newSiteSelected = newSiteSelected,
            updateNewSiteSelected = {
                    selected -> viewModel.updateSiteSelected(selected)
            },

            //Search bar
            searchQuery = searchQuery,
            onQueryChange = {query -> viewModel.updateSearchQuery(query) },
            searchedSites = searchedSitesList,
            searchActive = searchActive,
            onActiveChange = { toggle -> viewModel.updateSearchActive(toggle)},
            removeFocus = {focusManager.clearFocus()}

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
fun GetUserLocation(
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




