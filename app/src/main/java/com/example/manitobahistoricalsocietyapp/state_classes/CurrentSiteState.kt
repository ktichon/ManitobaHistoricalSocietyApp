package com.example.manitobahistoricalsocietyapp.state_classes

import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSite
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotos
import com.example.manitobahistoricalsocietyapp.database.SiteSource.SiteSource


//Class that holds the current state for the HistoricalSite Home page
data class CurrentSiteState (
    var displayState: SiteDisplayState = SiteDisplayState.FullMap,
    var allHistoricalSites: List<HistoricalSite> = emptyList(),
    var currentSite: HistoricalSite? = null,
    var siteTypes: List<String> = emptyList(),
    var sitePhotos: List<SitePhotos> = emptyList(),
    var siteSources: List<String> = emptyList(),
    )