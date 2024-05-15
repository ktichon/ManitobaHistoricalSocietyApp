package com.example.manitobahistoricalsocietyapp.di

import android.content.Context
import androidx.room.Room
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSiteDao
import com.example.manitobahistoricalsocietyapp.database.HistoricalSiteDatabase
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

}