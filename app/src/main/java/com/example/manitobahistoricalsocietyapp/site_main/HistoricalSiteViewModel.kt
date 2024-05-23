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
    private val siteSourceRepository: SiteSourceRepository
) : ViewModel(){


    private val _displayState = MutableStateFlow(SiteDisplayState.FullMap)
    val displayState: StateFlow<SiteDisplayState> = _displayState.asStateFlow()


    //Initialize with a blank site
    private val _currentSite:MutableStateFlow<HistoricalSite>  = MutableStateFlow(HistoricalSite(0, "","", 1, 0.0, 0.0, "", "", "", "https://www.mhs.ca/", "", ""))
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

    //Default location is the Manitoba Museum
    private val _currentUserLocation = MutableStateFlow(LatLng(49.9000253, -97.1386276))
    val currentUserLocation = _currentUserLocation.asStateFlow()

    private val _allHistoricalSiteClusterItems = MutableStateFlow<List<HistoricalSiteClusterItem>>(emptyList())
    val allHistoricalSiteClusterItems = _allHistoricalSiteClusterItems.asStateFlow()

    //Used to notify the scrollState to scroll to top when a new site is selected
    private val _newSiteSelected = MutableStateFlow(false)
    val newSiteSelected = _newSiteSelected.asStateFlow()


    //Search Variables
    private val _searchActive = MutableStateFlow(false)
    val searchActive = _searchActive.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private  val _searchedSitesList = MutableStateFlow<List<HistoricalSiteClusterItem>>(emptyList())
    val searchedSiteList = _searchedSitesList.asStateFlow()






    suspend fun getAllHistoricalSites()
    {
        _allHistoricalSiteClusterItems.value = historicalSiteRepository.getAllSiteClusterItems()
    }

    fun updateSiteDisplayState(newState: SiteDisplayState){
        _displayState.value = newState
    }

    fun updateLocationEnabled(enabled: Boolean)
    {
        _locationEnabled.value = enabled
    }

    fun updateUserLocation(newLocation: LatLng?){
        newLocation?.let { _currentUserLocation.value = newLocation }

    }

    fun updateSiteSelected(selected: Boolean)
    {
        _newSiteSelected.value = selected
    }

    fun newSiteSelected(siteId: Int){
        viewModelScope.launch {
            historicalSiteRepository.getHistoricalSite(siteId).collect{ _currentSite.value = it}
        }
        updateSiteSelected(true)
        updateSiteDisplayState(SiteDisplayState.HalfSite)
        viewModelScope.launch{sitePhotosRepository.getPhotosForSite(siteId).collect{ _sitePhotos.value = it}}
        viewModelScope.launch {   siteTypeRepository.getTypesForSite(siteId).collect{ _siteTypes.value = it} }
        viewModelScope.launch {   siteSourceRepository.getSourceInfoForSite(siteId).collect{ _siteSources.value = it} }
    }


    //Search

    fun updateSearchActive(searchActive: Boolean){
        _searchActive.value = searchActive

    }

    fun updateSearchQuery(query: String){
        _searchQuery.value = query
        if (_searchQuery.value.isBlank()){
            _searchedSitesList.value = emptyList()
        }
        else{
            _searchedSitesList.value = _allHistoricalSiteClusterItems.value.filter { site ->
                site.getNameAndAddress().uppercase().contains(query.trim().uppercase())

            }

        }


    }
}