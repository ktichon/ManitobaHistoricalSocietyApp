package com.example.manitobahistoricalsocietyapp.site_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSite
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSiteRepository
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotos
import com.example.manitobahistoricalsocietyapp.state_classes.SiteDisplayState
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricalSiteViewModel @Inject internal constructor(
    private val  historicalSiteRepository: HistoricalSiteRepository
) : ViewModel(){


    private val _displayState = MutableStateFlow(SiteDisplayState.FullMap)
    val displayState: StateFlow<SiteDisplayState> = _displayState.asStateFlow()

    private val _allHistoricalSites = MutableStateFlow<List<HistoricalSite>>(emptyList())
    val allHistoricalSites = _allHistoricalSites.asStateFlow()

    private val _currentSite:MutableStateFlow<HistoricalSite?>  = MutableStateFlow(null)
    val currentSite = _currentSite.asStateFlow()

    private val _siteTypes = MutableStateFlow<List<String>>(emptyList())
    val siteTypes = _siteTypes.asStateFlow()

    private val _sitePhotos = MutableStateFlow<List<SitePhotos>>(emptyList())
    val sitePhotos = _sitePhotos.asStateFlow()

    private val _siteSources = MutableStateFlow<List<String>>(emptyList())
    val siteSources = _siteSources.asStateFlow()

    private val _locationEnabled = MutableStateFlow(false)
    val locationEnabled = _locationEnabled.asStateFlow()

    //Default location is the Manitoba Museum
    private val _currentUserLocation = MutableStateFlow(LatLng(49.9000253, -97.1386276))
    val currentUserLocation = _currentUserLocation.asStateFlow()


    //private val db : HistoricalSiteDatabase= HistoricalSiteDatabase.getDatabase(context)

    /*init {
        viewModelScope.launch {
            historicalSiteRepository.getAllSites().collect{
                _allHistoricalSites.value = it
            }
        }

    }*/

    suspend fun getAllHistoricalSites()
    {
        _allHistoricalSites.value = historicalSiteRepository.getAllSites()
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

    fun newSiteSelected(newSite: HistoricalSite){
        _currentSite.value = newSite
        updateSiteDisplayState(SiteDisplayState.HalfSite)

        /*viewModelScope.launch {
            _currentSiteState.value.siteTypes =  db.siteTypeDao().getAllTypesForSite(newSite.id)
            _currentSiteState.value.sitePhotos = db.sitePhotosDao().getPhotosForSite(newSite.id)
            _currentSiteState.value.siteSources = db.siteSourceDao().getOnlySourceInfoForSite(newSite.id)
        }*/
    }
}