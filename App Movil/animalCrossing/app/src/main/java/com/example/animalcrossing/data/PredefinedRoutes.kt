package com.example.animalcrossing.data

import java.io.Serializable

object PredefinedRoutes {
    val shortRoute = PredefinedRoute(
        name = "Ruta Corta",
        time = "10 minutos",
        price = "$40 MXN",
        distance = "600 m",
        points = listOf(
            RoutePoint(19.396329, -99.095250),
            RoutePoint(19.394700, -99.095454),
            RoutePoint(19.394821, -99.096629),
            RoutePoint(19.396440, -99.096452),
            RoutePoint(19.396329, -99.095250)
        )
    )

    val longRoute = PredefinedRoute(
        name = "Ruta Larga",
        time = "20 minutos",
        price = "$75 MXN",
        distance = "1.2 km",
        points = listOf(
            RoutePoint(19.396329, -99.095250),
            RoutePoint(19.394700, -99.095454),
            RoutePoint(19.394993, -99.098694),
            RoutePoint(19.396577, -99.098335),
            RoutePoint(19.396329, -99.095250)
        ),
    )

    val allRoutes = listOf(shortRoute, longRoute)
}

data class RoutePoint(
    val latitude: Double,
    val longitude: Double
)

data class PredefinedRoute (
    val name: String,
    val time: String,
    val price: String,
    val distance: String,
    val points: List<RoutePoint>,
) : Serializable