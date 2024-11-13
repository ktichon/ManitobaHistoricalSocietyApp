package com.example.manitobahistoricalsocietyapp.database.SitePhotos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SitePhotosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sitePhotos: SitePhotos)

    @Delete
    suspend fun delete(sitePhotos: SitePhotos)

    @Update
    suspend fun update(sitePhotos: SitePhotos)

    /*@Query("SELECT * FROM sitePhotos")
    suspend  fun getAllSitePhotos(): List<SitePhotos>*/

    @Query("SELECT * FROM sitePhotos WHERE siteId = :siteId ")
    fun getPhotosForSite(siteId: Int): Flow<List<SitePhotos>>
}