package com.TichonTechnologies.manitobahistoricalsocietyapp.database.SiteSource

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SiteSourceRepository @Inject constructor(private  val siteSourceDao: SiteSourceDao ){
    fun getSourceInfoForSite(siteID: Int) = siteSourceDao.getOnlySourceInfoForSite(siteID)

    companion object{
        @Volatile private var instance: SiteSourceRepository? = null

        fun getInstance(sourceDao: SiteSourceDao) =
            instance?: synchronized(this){
                instance?: SiteSourceRepository(sourceDao).also { instance = it }
            }


    }
}