package com.example.manitobahistoricalsocietyapp.site_main

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSite
import com.example.manitobahistoricalsocietyapp.database.HistoricalSiteDatabase
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotos
import com.example.manitobahistoricalsocietyapp.database.SiteSource.SiteSource
import com.example.manitobahistoricalsocietyapp.database.SiteTypes.SiteType
import com.example.manitobahistoricalsocietyapp.state_classes.CurrentSiteState
import com.example.manitobahistoricalsocietyapp.state_classes.SiteDisplayState
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoricalSiteViewModel(context: Context) : ViewModel(){
    private val _currentSiteState = MutableStateFlow(CurrentSiteState())
    val currentSiteState: StateFlow<CurrentSiteState> = _currentSiteState.asStateFlow()
    private val db : HistoricalSiteDatabase= HistoricalSiteDatabase.getDatabase(context)

    init {
        //Load all sites when the view model is initiated
        viewModelScope.launch {
            _currentSiteState.value.allHistoricalSites = db.manitobaHistoricalSiteDao().getAllSites()
        }
    }

    fun updateSiteDisplayState(newState: SiteDisplayState){
        _currentSiteState.value.displayState = newState
    }

    fun newSiteSelected(newSite: HistoricalSite){
        _currentSiteState.value.currentSite = newSite
        updateSiteDisplayState(SiteDisplayState.HalfSite)

        viewModelScope.launch {
            _currentSiteState.value.siteTypes =  db.siteTypeDao().getAllTypesForSite(newSite.id)
            _currentSiteState.value.sitePhotos = db.sitePhotosDao().getPhotosForSite(newSite.id)
            _currentSiteState.value.siteSources = db.siteSourceDao().getOnlySourceInfoForSite(newSite.id)
        }
    }
}