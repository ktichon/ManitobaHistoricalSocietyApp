package com.example.manitobahistoricalsocietyapp.database.SiteTypes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface SiteTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(siteType: SiteType)

    @Delete
    suspend fun delete(siteType: SiteType)

    @Update
    suspend fun update(siteType: SiteType)

    @Query("SELECT * FROM siteType")
    fun getAllSiteType(): Flow<List<SiteType>>

    @Query(
        "SELECT siteType.* FROM siteType " +
                "INNER JOIN siteWithType ON siteWithType.site_type_id = siteType.site_type_id" +
                " WHERE site_id = :siteId "
    )
    fun getAllSiteTypesForSite(siteId: Int): Flow<List<SiteType>>

}

