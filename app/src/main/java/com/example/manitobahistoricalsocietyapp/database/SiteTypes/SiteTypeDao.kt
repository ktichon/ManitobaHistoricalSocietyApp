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
    suspend fun getAllSiteType(): List<SiteType>

    @Query(
        "SELECT siteType.type FROM siteType " +
                "INNER JOIN siteWithType ON siteWithType.siteTypeId = siteType.id" +
                " WHERE siteId = :siteId "
    )
    fun getAllTypesForSite(siteId: Int): Flow<List<String>>

}

