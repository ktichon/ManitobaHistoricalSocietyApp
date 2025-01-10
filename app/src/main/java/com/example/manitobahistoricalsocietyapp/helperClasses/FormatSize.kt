package com.example.manitobahistoricalsocietyapp.helperClasses

import kotlin.math.roundToInt

class FormatSize {
    companion object{
        fun getDpFromPercent(percent: Float, total: Int): Int{

            val result = (total * percent.toDouble())
            return result.roundToInt()
        }
    }
}