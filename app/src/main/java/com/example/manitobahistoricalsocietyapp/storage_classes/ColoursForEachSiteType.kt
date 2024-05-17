package com.example.manitobahistoricalsocietyapp.storage_classes

import androidx.compose.ui.graphics.Color
import com.example.manitobahistoricalsocietyapp.ui.theme.Green
import com.example.manitobahistoricalsocietyapp.ui.theme.Orange
import com.example.manitobahistoricalsocietyapp.ui.theme.Purple
import com.example.manitobahistoricalsocietyapp.ui.theme.Rose
import com.example.manitobahistoricalsocietyapp.ui.theme.Teal200
import com.example.manitobahistoricalsocietyapp.ui.theme.Yellow

//Holds the colours for each main site type
class ColoursForEachSiteType {
    companion object{

        private val colourForTypeMap = mapOf(
            //1 is featured site, and no site has that as a main type

            //2 is Museum or Archives
            2 to Teal200,
            //3 is Building
            3 to Rose,
            //4 is Monument
            4 to Orange,
            //5 is Cemetery
            5 to Purple,
            //6 is Location
            6 to Green,
            //7 is Other
            7 to Yellow
        )
        fun getTypeColour(typeId: Int): Color{
            return colourForTypeMap[typeId] ?: Color.Red
        }

    }
}