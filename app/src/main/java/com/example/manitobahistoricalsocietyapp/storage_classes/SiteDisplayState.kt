package com.example.manitobahistoricalsocietyapp.storage_classes

enum class SiteDisplayState(val mapBottomPaddingPercent: Int) {
    FullMap(0),
    MapWithLegend(40),
    HalfSite(60),
    FullSite(99),
}