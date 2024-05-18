package com.example.manitobahistoricalsocietyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSite
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSiteDao
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotos
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotosDao
import com.example.manitobahistoricalsocietyapp.database.SiteSource.SiteSource
import com.example.manitobahistoricalsocietyapp.database.SiteSource.SiteSourceDao
import com.example.manitobahistoricalsocietyapp.database.SiteTypes.SiteType
import com.example.manitobahistoricalsocietyapp.database.SiteTypes.SiteTypeDao
import com.example.manitobahistoricalsocietyapp.database.SiteTypes.SiteWithType
import com.example.manitobahistoricalsocietyapp.database.SiteTypes.SiteWithTypeDao
import kotlin.concurrent.Volatile


@Database(
    entities = [HistoricalSite::class, SitePhotos::class, SiteSource::class, SiteType::class, SiteWithType::class],
    version = 1,
    exportSchema = false,


)
abstract class HistoricalSiteDatabase : RoomDatabase() {
    abstract fun manitobaHistoricalSiteDao(): HistoricalSiteDao
    abstract fun sitePhotosDao(): SitePhotosDao
    abstract fun siteSourceDao(): SiteSourceDao
    abstract fun siteTypeDao(): SiteTypeDao
    abstract fun siteWithTypeDao(): SiteWithTypeDao

    companion object {
        @Volatile
        private var Instance: HistoricalSiteDatabase? = null

        fun getInstance(context: Context): HistoricalSiteDatabase{
            return Instance?: synchronized(this){
                Room.databaseBuilder(context, HistoricalSiteDatabase::class.java, "historicalSiteData.db")
                    .fallbackToDestructiveMigration()
                    .createFromAsset("historicalSiteData.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
