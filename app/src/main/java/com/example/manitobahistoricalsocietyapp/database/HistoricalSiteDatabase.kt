package com.example.manitobahistoricalsocietyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.manitobahistoricalsocietyapp.database.historicalSite.HistoricalSite
import com.example.manitobahistoricalsocietyapp.database.historicalSite.HistoricalSiteDao
import com.example.manitobahistoricalsocietyapp.database.sitePhotos.SitePhotos
import com.example.manitobahistoricalsocietyapp.database.sitePhotos.SitePhotosDao
import com.example.manitobahistoricalsocietyapp.database.siteSource.SiteSource
import com.example.manitobahistoricalsocietyapp.database.siteSource.SiteSourceDao
import com.example.manitobahistoricalsocietyapp.database.siteTypes.SiteType
import com.example.manitobahistoricalsocietyapp.database.siteTypes.SiteTypeDao
import com.example.manitobahistoricalsocietyapp.database.siteTypes.SiteWithType
import com.example.manitobahistoricalsocietyapp.database.siteTypes.SiteWithTypeDao
import kotlin.concurrent.Volatile


@Database(
    entities = [HistoricalSite::class, SitePhotos::class, SiteSource::class, SiteType::class, SiteWithType::class],
    version = 1,
    exportSchema = false
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

        fun getDatabase(context: Context): HistoricalSiteDatabase{
            return Instance?: synchronized(this){
                Room.databaseBuilder(context, HistoricalSiteDatabase::class.java, "historicalSiteData.db")
                    .fallbackToDestructiveMigration()
                    .createFromAsset("historicalSiteData.db")
                    .build()
                    .also { Instance = it }
            }
        }


        /*fun getInstance(context: Context): HistoricalSiteDatabase? {
            if (INSTANCE == null) {
                synchronized(HistoricalSiteDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = databaseBuilder(
                            context.applicationContext,
                            HistoricalSiteDatabase::class.java, "historicalSiteDatabase.db"
                        )
                            .fallbackToDestructiveMigration()
                            .createFromAsset("historicalSiteData.db")
                            .build()
                    }
                }
            }
            return INSTANCE
        }*/
    }
}
