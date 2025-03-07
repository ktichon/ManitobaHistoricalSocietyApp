package com.TichonTechnologies.manitobahistoricalsocietyapp.database.SitePhotos

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SitePhotosRepository @Inject constructor(private  val sitePhotosDao: SitePhotosDao) {

    fun getPhotosForSite(siteID: Int) = sitePhotosDao.getPhotosForSite(siteID)

    companion object{
        @Volatile private var instance: SitePhotosRepository? = null

        fun getInstance(sitePhotosDao: SitePhotosDao) =
            instance?: synchronized(this){
                instance?: SitePhotosRepository(sitePhotosDao).also { instance = it }
            }


    }
}