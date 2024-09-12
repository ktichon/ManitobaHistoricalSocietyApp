package com.example.manitobahistoricalsocietyapp.helperClasses

import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSite
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSiteClusterItem

//Nicely Formats the site to display info. Putting this all in one place for easy access
class FormatSite {
    companion object{
        //Formats the address to display both the address (if it exists) and the municipality for HistoricalSiteClusterItems
        fun formatAddress(site: HistoricalSiteClusterItem): String{
            return (if (site.address.isNullOrBlank()) "" else site.address+ ", ") + site.municipality
        }
        //Same method but for HistoricalSite objects
        fun formatAddress(site: HistoricalSite): String{
            return (if (site.address.isNullOrBlank()) "" else site.address +", ") + site.municipality
        }
    }
}