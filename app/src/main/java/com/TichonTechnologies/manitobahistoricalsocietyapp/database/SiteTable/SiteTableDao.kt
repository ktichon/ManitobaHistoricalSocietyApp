package com.TichonTechnologies.manitobahistoricalsocietyapp.database.SiteTable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface SiteTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(siteTable: SiteTable)

    @Delete
    suspend fun delete(siteTable: SiteTable)

    @Update
    suspend fun update(siteTable: SiteTable)


    @Query("SELECT * FROM siteTable WHERE siteId = :siteId ")
    fun getTablesForSite(siteId: Int): Flow<List<String>>
}
