package com.example.manitobahistoricalsocietyapp.database.HistoricalSite

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoricalSiteRepository @Inject constructor(private  val historicalSiteDao: HistoricalSiteDao){
    suspend fun getAllSites() = historicalSiteDao.getAllSites()
    fun getHistoricalSite(siteID: Int) = historicalSiteDao.getHistoricalSite(siteID)

    companion object{
        @Volatile private var instance: HistoricalSiteRepository? = null

        fun getInstance(historicalSiteDao: HistoricalSiteDao) =
            instance?: synchronized(this){
                instance?: HistoricalSiteRepository(historicalSiteDao).also { instance = it }
            }


    }
}