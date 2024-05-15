package com.example.manitobahistoricalsocietyapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Need to have one class the extends Application with the annotation @HiltAndroidApp to use hilt
@HiltAndroidApp
class ManitobaHistoricalSocietyApp : Application()