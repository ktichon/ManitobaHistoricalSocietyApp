package com.TichonTechnologies.manitobahistoricalsocietyapp.database.SiteTable

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SiteTableRepository @Inject constructor(private  val siteTableDao: SiteTableDao){
    fun getTablesForSite(siteID: Int) = siteTableDao.getTablesForSite(siteID)

    companion object{
        @Volatile private var instance: SiteTableRepository? = null

        fun getInstance(tableDao: SiteTableDao) =
            instance?: synchronized(this){
                instance?: SiteTableRepository(tableDao).also { instance = it }
            }


    }
}