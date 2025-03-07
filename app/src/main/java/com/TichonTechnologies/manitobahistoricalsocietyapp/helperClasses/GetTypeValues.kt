package com.TichonTechnologies.manitobahistoricalsocietyapp.helperClasses

import androidx.compose.ui.graphics.Color
import com.TichonTechnologies.manitobahistoricalsocietyapp.ui.theme.Green
import com.TichonTechnologies.manitobahistoricalsocietyapp.ui.theme.Orange
import com.TichonTechnologies.manitobahistoricalsocietyapp.ui.theme.Purple
import com.TichonTechnologies.manitobahistoricalsocietyapp.ui.theme.Rose
import com.TichonTechnologies.manitobahistoricalsocietyapp.ui.theme.Teal200
import com.TichonTechnologies.manitobahistoricalsocietyapp.ui.theme.Yellow

//Holds the colours for each main site type
class GetTypeValues {
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

        private val nameForTypeMap = mapOf(
            //1 is featured site, and no site has that as a main type

            //2 is Museum or Archives
            2 to "Museum or Archives",
            //3 is Building
            3 to "Building",
            //4 is Monument
            4 to "Monument",
            //5 is Cemetery
            5 to "Cemetery",
            //6 is Location
            6 to "Location",
            //7 is Other
            7 to "Other"
        )
        fun getTypeColour(typeId: Int): Color{
            return colourForTypeMap[typeId] ?: Color.Red
        }

        fun getTypeName(typeId: Int): String {
            return nameForTypeMap[typeId] ?: "Error getting site type"
        }

    }
}