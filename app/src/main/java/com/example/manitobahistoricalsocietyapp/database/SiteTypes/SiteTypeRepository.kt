package com.example.manitobahistoricalsocietyapp.database.SiteTypes

import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotosDao
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotosRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SiteTypeRepository @Inject constructor(private  val siteTypeDao: SiteTypeDao) {
    fun getTypesForSite(siteID: Int) = siteTypeDao.getAllTypesForSite(siteID)

    companion object{
        @Volatile private var instance: SiteTypeRepository? = null

        fun getInstance(siteTypeDao: SiteTypeDao) =
            instance?: synchronized(this){
                instance?: SiteTypeRepository(siteTypeDao).also { instance = it }
            }


    }
}