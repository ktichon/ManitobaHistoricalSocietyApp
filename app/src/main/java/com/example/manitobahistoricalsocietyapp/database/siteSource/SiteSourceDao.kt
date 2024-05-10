package com.example.manitobahistoricalsocietyapp.database.siteSource

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


    @Query("SELECT * FROM siteSource")
    fun getAllSources(): Flow<List<SiteSource>>

    @Query("SELECT * FROM siteSource WHERE site_id = :siteId ")
    fun getAllSiteSourcesForSite(siteId: Int): Flow<List<SiteSource>>

    //Only really need the SiteSource.info strings
    @Query("SELECT info FROM siteSource WHERE site_id = :siteId ")
    fun getOnlySourceInfoForSite(siteId: Int): Flow<List<String>>
}
