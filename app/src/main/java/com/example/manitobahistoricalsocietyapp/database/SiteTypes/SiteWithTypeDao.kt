package com.example.manitobahistoricalsocietyapp.database.SiteTypes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface SiteWithTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(siteWithType: SiteWithType)

    @Delete
    suspend fun delete(siteWithType: SiteWithType)

    @Query("SELECT * FROM SiteWithType")
    fun getAllSiteWithType(): Flow<List<SiteWithType>>

    @Query("SELECT * FROM SiteWithType WHERE site_id = :siteId ")
    fun getAllSiteWithTypesForSite(siteId: Int): Flow<List<SiteWithType>>
}
