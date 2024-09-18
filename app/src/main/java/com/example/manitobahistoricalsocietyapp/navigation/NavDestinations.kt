package com.example.manitobahistoricalsocietyapp.navigation

interface NavDestinations {
    val route: String
    val title: String
}

object MapDestination: NavDestinations{
    override val route = "map"
    override val title = "Map Page"
}
object AboutDestination: NavDestinations{
    override val route = "about"
    override val title = "About Page"
}