package com.TichonTechnologies.manitobahistoricalsocietyapp.storage_classes

enum class SiteDisplayState(val mapBottomPaddingPercent: Float) {
    FullMap(0F),
    MapWithLegend(0.40F),
    HalfSite(0.60F),
    FullSite(1.00F),
}