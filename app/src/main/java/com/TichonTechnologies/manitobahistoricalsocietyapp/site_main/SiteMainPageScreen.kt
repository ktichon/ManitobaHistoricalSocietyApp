package com.TichonTechnologies.manitobahistoricalsocietyapp.site_main

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.TichonTechnologies.manitobahistoricalsocietyapp.site_scaffolding.LoadingScreen
import com.TichonTechnologies.manitobahistoricalsocietyapp.site_scaffolding.SiteMainPageContent
import com.TichonTechnologies.manitobahistoricalsocietyapp.storage_classes.SiteDisplayState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


@Composable
fun SiteMainPageScreen(
    viewModel: HistoricalSiteViewModel = viewModel(),
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,

) {

    val navController = rememberNavController()

    //Display state of screen
    //Controls the size the DisplayMap, DisplayFullSiteDetails, and Legend Composables, and other less drastic changes
    val displayState by viewModel.displayState.collectAsState()
    //Used to let the camera know that a new padding has been added to the map, and so center the camera
    val newMapUpdate by viewModel.newMapUpdate.collectAsState()
    //Used to center the camera at location without waiting for the current site data to load, and display name if it is the first site selected and the current site hasn't loaded yet
    val currentlySelectedClusterItem by viewModel.currentlySelectedClusterItem.collectAsState()


    //All site clusterItems that should be displayed on the map
    val allSiteClusterItems by viewModel.allHistoricalSiteClusterItems.collectAsState()


    //Current Site
    //Used to let DisplayFullSiteDetails know that a new site has been selected, so that it can ensure that all of its scrollable are scrolled to the top
    val renderNewSite by viewModel.renderNewSite.collectAsState()
    //The currently selected site
    val currentSite by viewModel.currentSite.collectAsState()
    //Other info about the selected site
    val siteTypes by viewModel.siteTypes.collectAsState()
    val sitePhotos by viewModel.sitePhotos.collectAsState()
    val siteSources by viewModel.siteSources.collectAsState()


    //User Location
    //Whether the user has allowed the app access to their location
    val locationEnabled by viewModel.locationEnabled.collectAsState()
    //Current user location
    val currentUserLocation by viewModel.currentUserLocation.collectAsState()
    //Controls if the camera should follow the user location. Currently only used to center the camera on the user on app load
    val followUserLocation by viewModel.followUserLocation.collectAsState()
    //Location Provider, used to get user location
    val locationProvider by remember {
        mutableStateOf(LocationServices.getFusedLocationProviderClient(context))
    }


    //Search Bar
    //Stores if the search bar is active or not
    val searchActive by viewModel.searchActive.collectAsState()
    //The text used to search
    val searchQuery by viewModel.searchQuery.collectAsState()
    //All sites where either the name or address contains the searchQuery
    val searchedSitesList by viewModel.searchedSiteList.collectAsState()
    //Used to let the camera know if the site was selected by searchbar (if so then zoom) or not
    val siteSelectedFromSearch by viewModel.siteSelectedFromSearch.collectAsState()
    //used to un-focus from search bar
    val focusManager = LocalFocusManager.current


    //Stores if the loading should be visible or not
    var showLoadingScreen by remember { mutableStateOf(true) }

    //Camera
    //Camera zoom level on map load
    val startingZoomLevel = 16f
    //Camera zoom level when a site is searched. Since searching on a site doesn't trigger a onMarkerClick event, we want zoom in close enough that it is obvious which site is the result
    val searchZoomLevel = 18f
    //Camera animation time
    val cameraAnimationDurationMs = 400
    //coroutineScope used to launch animations
    val coroutineScope = rememberCoroutineScope()
    //Controls the camera position state
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(currentUserLocation, startingZoomLevel)
    }



    //Requesting Location Permissions
    //used to make sure it keeps checking if permissions are granted until they are either granted or denied.
    //It allows us to avoid having to close the app completely and reopening it after enabling permissions
    var askedForPermission by remember { mutableStateOf(false) }
    if (!askedForPermission){
        //Request location permissions, and storing the value in currentSiteState
        RequestLocationPermission(
            onPermissionGranted = {
                viewModel.updateLocationEnabled(true)
                askedForPermission = true
            },
            onPermissionDenied = {viewModel.updateLocationEnabled(false)
                askedForPermission = true
            },
            onPermissionsRevoked = {
                askedForPermission = false
            }
        )

    }


    //If location enable is set to true, continually get and update user location
    if (locationEnabled  ){
        GetUserLocation(
            onNewLocation = {newLocation ->
                viewModel.updateUserLocation(newLocation)
                newLocation?.let{
                    if (displayState == SiteDisplayState.FullMap && followUserLocation ){
                        if (showLoadingScreen){
                            cameraPositionState.position =CameraPosition.fromLatLngZoom(newLocation, startingZoomLevel)
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
    } else if (askedForPermission){


        SiteMainPageContent(
            navController = navController,
            cameraPositionState = cameraPositionState,
            allSites = allSiteClusterItems,
            onSiteSelected = { siteSelected, searched ->
                viewModel.updateCurrentlySelectedClusterItem(siteSelected)
                viewModel.updateSiteSelectedFromSearch(searched)
                viewModel.newSiteSelected(siteSelected.id)
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
            renderNewSite = renderNewSite,
            updateRenderNewSite = {
                    newRender -> viewModel.updateRenderNewSite(newRender)
            },

            //Search bar
            searchQuery = searchQuery,
            onQueryChange = {query -> viewModel.updateSearchQuery(query) },
            searchedSites = searchedSitesList,
            searchActive = searchActive,
            onActiveChange = { toggle -> viewModel.updateSearchActive(toggle)},
            removeFocus = {focusManager.clearFocus()},

            //Map Padding
            newMapUpdate = newMapUpdate,
            currentlySelectedClusterItem = currentlySelectedClusterItem,
            onClusterClicked = {
                coroutineScope.launch{
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, cameraPositionState.position.zoom + 1), cameraAnimationDurationMs)
                }
            },
            centerCamera = {
                //When the padding on the map changes, this will center the map onto the new smaller display port

                //When going from FullMap to HalfSite, no animation. We just want the map centered around the new site
                if (displayState == SiteDisplayState.HalfSite){
                    //If searched, zoom in to site. Else use the current zoom level
                    if(siteSelectedFromSearch){
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(currentlySelectedClusterItem.position, searchZoomLevel)
                    } else{
                        coroutineScope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.newLatLng(currentlySelectedClusterItem.position), cameraAnimationDurationMs)
                        }

                    }

                }
                //When going from HalfSite or FullSite to FullMap, animate the move to center
                else if (displayState == SiteDisplayState.FullMap) {
                    coroutineScope.launch{
                        cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPositionState.position), cameraAnimationDurationMs)
                    }

                }


                viewModel.updateNewMapUpdate(false)
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
    onPermissionsRevoked: () -> Unit
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
        if (permissionsToRequest.isNotEmpty())
            permissionState.launchMultiplePermissionRequest()

        // Execute callbacks based on permission status.
        if (allPermissionsRevoked) {
            onPermissionsRevoked()
        } else {
            if (permissionState.allPermissionsGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }
    // Fall back to ensure that if all permissions are granted, call on permission granted
    if (permissionState.allPermissionsGranted)
        onPermissionGranted()
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




