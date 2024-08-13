package com.example.manitobahistoricalsocietyapp.database.SiteTypes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy


@Dao
interface SiteWithTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(siteWithType: SiteWithType)

    @Delete
    suspend fun delete(siteWithType: SiteWithType)

    /*@Query("SELECT * FROM SiteWithType")
    suspend fun getAllSiteWithType(): List<SiteWithType>

    @Query("SELECT * FROM SiteWithType WHERE site_id = :siteId ")
    suspend fun getAllSiteWithTypesForSite(siteId: Int): List<SiteWithType>*/
}
