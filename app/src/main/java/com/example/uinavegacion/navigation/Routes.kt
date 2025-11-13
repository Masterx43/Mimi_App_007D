package com.example.uinavegacion.navigation

// Clase sellada para rutas: evita "strings mágicos" y facilita refactors
sealed class Route(val path: String) { // Cada objeto representa una pantalla
    data object Home     : Route("home")     // Ruta Home
    data object Login    : Route("login")    // Ruta Login
    data object Register : Route("register") // Ruta Registro

    data object Booking : Route("booking")   //ruta hacia el agendar

    data object UserInfo: Route ("userInfo")  // ruta UserInfo

    data object AdminInfo: Route("adminInfo")   //ruta admin

    data object WorkerInfo: Route("workerInfo") //ruta trabajador


    data object Historial : Route("historiaLReserva")
}

