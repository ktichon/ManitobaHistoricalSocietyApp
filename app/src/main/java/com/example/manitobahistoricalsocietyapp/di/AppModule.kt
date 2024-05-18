package com.example.manitobahistoricalsocietyapp.di

import android.content.Context
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSiteDao
import com.example.manitobahistoricalsocietyapp.database.HistoricalSiteDatabase
import com.example.manitobahistoricalsocietyapp.database.SitePhotos.SitePhotosDao
import com.example.manitobahistoricalsocietyapp.database.SiteSource.SiteSourceDao
import com.example.manitobahistoricalsocietyapp.database.SiteTypes.SiteTypeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context) : HistoricalSiteDatabase  {
        return HistoricalSiteDatabase.getInstance(context)
    }

    @Provides
    fun provideHistoricalSiteDao(appDatabase: HistoricalSiteDatabase) : HistoricalSiteDao{
        return appDatabase.manitobaHistoricalSiteDao()
    }

    @Provides
    fun provideSitePhotosDao(appDatabase: HistoricalSiteDatabase) : SitePhotosDao{
        return appDatabase.sitePhotosDao()
    }
    @Provides
    fun provideSiteSourcesDao(appDatabase: HistoricalSiteDatabase) : SiteSourceDao{
        return appDatabase.siteSourceDao()
    }
    @Provides
    fun provideSiteTypesDao(appDatabase: HistoricalSiteDatabase) : SiteTypeDao{
        return appDatabase.siteTypeDao()
    }

}