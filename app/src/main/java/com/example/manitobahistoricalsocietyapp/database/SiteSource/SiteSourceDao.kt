package com.example.manitobahistoricalsocietyapp.database.SiteSource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface SiteSourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(siteSource: SiteSource)

    @Delete
    suspend fun delete(siteSource: SiteSource)

    @Update
    suspend fun update(siteSource: SiteSource)


    /*@Query("SELECT * FROM siteSource")
    suspend fun getAllSources(): List<SiteSource>

    @Query("SELECT * FROM siteSource WHERE site_id = :siteId ")
    suspend fun getAllSiteSourcesForSite(siteId: Int): List<SiteSource>*/

    //Only really need the SiteSource.info strings
    @Query("SELECT infoHTML FROM siteSource WHERE siteId = :siteId ")
    fun getOnlySourceInfoForSite(siteId: Int): Flow<List<String>>
}
