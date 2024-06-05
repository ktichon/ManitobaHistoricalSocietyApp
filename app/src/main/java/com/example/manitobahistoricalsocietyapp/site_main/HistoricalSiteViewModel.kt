package com.example.manitobahistoricalsocietyapp.site_main


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSite
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSiteClusterItem
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSiteRepository
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotos
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotosRepository
import com.example.manitobahistoricalsocietyapp.database.SiteSource.SiteSourceRepository
import com.example.manitobahistoricalsocietyapp.database.SiteTypes.SiteTypeRepository
import com.example.manitobahistoricalsocietyapp.helperClasses.DistanceAwayFromSite
import com.example.manitobahistoricalsocietyapp.storage_classes.SiteDisplayState
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricalSiteViewModel @Inject internal constructor(
    private val  historicalSiteRepository: HistoricalSiteRepository,
    private val siteTypeRepository: SiteTypeRepository,
    private val sitePhotosRepository: SitePhotosRepository,
    private val siteSourceRepository: SiteSourceRepository,
) : ViewModel(){


    private val _displayState = MutableStateFlow(SiteDisplayState.FullMap)
    val displayState: StateFlow<SiteDisplayState> = _displayState.asStateFlow()


    //Initialize with a blank site
    private val _currentSite:MutableStateFlow<HistoricalSite>  = MutableStateFlow(HistoricalSite(0, "","", 1, 49.9000253, -97.1386276, "", "", "", "https://www.mhs.ca/", "", ""))
    val currentSite = _currentSite.asStateFlow()

    private val _siteTypes = MutableStateFlow<List<String>>(emptyList())
    val siteTypes = _siteTypes.asStateFlow()

    private val _sitePhotos = MutableStateFlow<List<SitePhotos>>(emptyList())
    val sitePhotos = _sitePhotos.asStateFlow()

    private val _siteSources = MutableStateFlow<List<String>>(emptyList())
    val siteSources = _siteSources.asStateFlow()

    //Checks if the location permission has been enabled
    private val _locationEnabled = MutableStateFlow(false)
    val locationEnabled = _locationEnabled.asStateFlow()

    //Used to check if the camera follows the user location
    private val _followUserLocation = MutableStateFlow(true)
    val followUserLocation = _followUserLocation.asStateFlow()

    //Default location is the Manitoba Museum
    private val _currentUserLocation = MutableStateFlow(LatLng(49.9000253, -97.1386276))
    val currentUserLocation = _currentUserLocation.asStateFlow()

    private val _allHistoricalSiteClusterItems = MutableStateFlow<List<HistoricalSiteClusterItem>>(emptyList())
    val allHistoricalSiteClusterItems = _allHistoricalSiteClusterItems.asStateFlow()

    //Used to notify the scrollState to scroll to top when a new site is selected
    private val _renderNewSite = MutableStateFlow(false)
    val renderNewSite = _renderNewSite.asStateFlow()


    //Search Variables
    private val _searchActive = MutableStateFlow(false)
    val searchActive = _searchActive.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private  val _searchedSitesList = MutableStateFlow<List<HistoricalSiteClusterItem>>(emptyList())
    val searchedSiteList = _searchedSitesList.asStateFlow()
    //Lets the camera know if this was selected from search results or not
    private val _siteSelectedFromSearch = MutableStateFlow(false)
    val siteSelectedFromSearch = _siteSelectedFromSearch.asStateFlow()


    //Map padding variables
    //lets the app know that new map padding has been added
    private  val _newMapUpdate = MutableStateFlow(false)
    val newMapUpdate = _newMapUpdate.asStateFlow()

    //Stores the clusterItem so that the map can move to location without waiting for me to fetch the current item
    private val _lastSelectedClusterItem:MutableStateFlow<HistoricalSiteClusterItem>  = MutableStateFlow(
        HistoricalSiteClusterItem(0, 1,"", "", "", 49.9000253, -97.1386276)
    )
    val lastSelectedClusterItem = _lastSelectedClusterItem.asStateFlow()





    suspend fun getAllHistoricalSites()
    {
        _allHistoricalSiteClusterItems.value = historicalSiteRepository.getAllSiteClusterItems()
    }

    fun updateSiteDisplayState(newState: SiteDisplayState){
        _displayState.value = newState
        //Let the map know that we have updated the padding, to make sure we properly center the camera when state is changed to HalfSite or FullMap
        if (_displayState.value == SiteDisplayState.HalfSite || _displayState.value == SiteDisplayState.FullMap)
            updateNewMapUpdate(true)

    }

    fun updateLocationEnabled(enabled: Boolean)
    {
        _locationEnabled.value = enabled
    }

    fun updateFollowUserLocation(followUser: Boolean){
        _followUserLocation.value = followUser
    }

    fun updateUserLocation(newLocation: LatLng?){
        newLocation?.let { _currentUserLocation.value = newLocation }

    }

    fun updateRenderNewSite(newRender: Boolean)
    {
        _renderNewSite.value = newRender
    }

    fun newSiteSelected(siteId: Int){
        viewModelScope.launch {
            historicalSiteRepository.getHistoricalSite(siteId).collect{ _currentSite.value = it}
        }

        updateRenderNewSite(true)
        //Set the display to HalfSite
        updateSiteDisplayState(SiteDisplayState.HalfSite)

        viewModelScope.launch{sitePhotosRepository.getPhotosForSite(siteId).collect{ _sitePhotos.value = it}}
        viewModelScope.launch {   siteTypeRepository.getTypesForSite(siteId).collect{ _siteTypes.value = it} }
        viewModelScope.launch {   siteSourceRepository.getSourceInfoForSite(siteId).collect{ _siteSources.value = it} }
    }


    fun updateNewMapUpdate(newMapUpdate: Boolean){
        _newMapUpdate.value = newMapUpdate
    }

    fun updateSiteSelectedFromSearch(siteSelectedFromSearch: Boolean){
        _siteSelectedFromSearch.value = siteSelectedFromSearch
    }

    fun updateLastSelectedClusterItem(clusterItem: HistoricalSiteClusterItem){
        _lastSelectedClusterItem.value = clusterItem
    }




    //Search

    fun updateSearchActive(searchActive: Boolean){
        _searchActive.value = searchActive
        if (searchActive && _searchQuery.value.isBlank() ){
            _searchedSitesList.value = getNearestSites(
                maxSites = 20,
                availableSites = _allHistoricalSiteClusterItems.value,
                userLocation = _currentUserLocation.value
            )
        }

    }

    fun updateSearchQuery(query: String){
        _searchQuery.value = query.trim()
        if (_searchQuery.value.isBlank()){
            _searchedSitesList.value = getNearestSites(
                maxSites = 20,
                availableSites = _allHistoricalSiteClusterItems.value,
                userLocation = _currentUserLocation.value
            )
        }
        else{
            _searchedSitesList.value = _allHistoricalSiteClusterItems.value.filter { site ->
                site.getNameAndAddress().uppercase().contains(query.trim().uppercase())

            }

        }

    }

    //Gets the X closes sites to the user
    private fun getNearestSites(maxSites: Int, availableSites: List<HistoricalSiteClusterItem>, userLocation: LatLng ) : List<HistoricalSiteClusterItem>{
        val distanceSearchValues = arrayOf(200, 500, 1000, 5000, 10000, 20000)
        val foundSites = mutableListOf<HistoricalSiteClusterItem>()
        var previousSearchValue = 0


        for (searchValue in distanceSearchValues){
            val numberOfSitesToFind = maxSites - foundSites.size
            val searchSites = availableSites.filter { site ->
                DistanceAwayFromSite.getDistanceInMeters(userLocation, site.position)  < searchValue
                        //this is to ensure that the closest sites appear on the top of the list
                        && DistanceAwayFromSite.getDistanceInMeters(userLocation, site.position) > previousSearchValue
            }.take(numberOfSitesToFind)
            if (searchSites.isNotEmpty()){
                foundSites.addAll(searchSites.sortedBy { site -> DistanceAwayFromSite.getDistanceInMeters(userLocation, site.position)})
            }
            previousSearchValue = searchValue
            if (foundSites.size >= maxSites)
                break
        }
        return foundSites
    }
}