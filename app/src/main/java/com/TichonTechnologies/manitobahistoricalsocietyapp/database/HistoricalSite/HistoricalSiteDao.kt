package com.TichonTechnologies.manitobahistoricalsocietyapp.database.HistoricalSite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoricalSiteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historicalSite: HistoricalSite)

    @Delete
    suspend fun delete(historicalSite: HistoricalSite)

    @Update
    suspend fun update( historicalSites: HistoricalSite)


    /*@Query("SELECT * FROM manitobaHistoricalSite  WHERE municipality = 'Winnipeg' LIMIT 100")
     suspend fun getAllSites(): List<HistoricalSite>*/

    @Query("SELECT id,  mainType, name, address, municipality, latitude, longitude FROM historicalSite ORDER BY name ASC ")
    suspend fun getAllSiteClusterItems(): List<HistoricalSiteClusterItem>

    @Query("SELECT  * FROM historicalSite WHERE id = :id ")
    fun getHistoricalSite(id: Int): Flow<HistoricalSite>

}