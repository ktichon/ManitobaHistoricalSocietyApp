package com.example.manitobahistoricalsocietyapp.helperClasses

import kotlin.math.roundToInt

class FormatSize {
    companion object{
        fun getDpFromPercent(percent: Int, total: Int): Int{

            val result = (total * (percent.toDouble()/100))
            return result.roundToInt()
        }
    }
}