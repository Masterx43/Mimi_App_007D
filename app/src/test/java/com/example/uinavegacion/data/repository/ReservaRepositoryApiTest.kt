package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.reservas.ReservaServiceAPI
import com.example.uinavegacion.data.remote.reservas.dto.CrearReservaRequestDTO
import com.example.uinavegacion.data.remote.reservas.dto.ReservaDetalleDTO
import com.example.uinavegacion.data.remote.reservas.dto.ReservaResponseDTO
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import kotlin.Long
import kotlin.String

class ReservaRepositoryApiTest{
    @Test
    fun crearReserva_devuelve_exito()= runBlocking {
        val api = mockk<ReservaServiceAPI>()
        val repo = ReservaRepositoryAPI(api)

        val req = CrearReservaRequestDTO(
            idUsuario = 1,
            idTrabajador = 2,
            idServicio = 3,
            fecha = "2025-11-15",
            hora = "10:00",

        )
        val sample = ReservaResponseDTO(
            idReserva = 99,
            idUsuario = 1,
            idTrabajador = 2,
            idServicio = 3,
            fecha = "2025-11-15",
            hora = "10:00",
            estado = "Pendiente"
        )

        coEvery { api.crearReserva(req) } returns Response.success(sample)

        val result = repo.crearReserva(req)

        assertTrue(result.isSuccess)
        assertEquals(99, result.getOrNull()!!.idReserva)
    }

    @Test
    fun obtenerReservasUsuario_devuelve_lista()= runBlocking {
        val api= mockk<ReservaServiceAPI>()
        val repo = ReservaRepositoryAPI(api)

        val sample= listOf(
            ReservaResponseDTO(
            idReserva = 50,
            idUsuario = 1,
            idTrabajador = 2,
            idServicio = 3,
            fecha = "2025-11-20",
            hora = "15:00",
            estado = "CONFIRMADA"

            )
        )

        coEvery { api.getReservasUsuario(1) } returns Response.success(sample)

        val result = repo.obtenerReservasUsuario(1)
        assertTrue(result.isSuccess)
        assertEquals("CONFIRMADA", result.getOrNull()!![0].estado)
    }
    @Test
    fun obtenerReservaDetalleTrabajador_devuelve_lista()= runBlocking {
        val api = mockk<ReservaServiceAPI>()
        val repo = ReservaRepositoryAPI(api)

        val sample = listOf(
            ReservaDetalleDTO(
                idReserva= 1,
                fecha= "2025-11-21",
                hora="16:00",
                estado= "Pendiente",
                usuario="Loreto",
                trabajador="Bastian",
                servicio="Corte de pelo Hombre"

            )
        )
        coEvery { api.getReservasWorker(3) } returns Response.success(sample)

        val result= repo.obtenerReservasDetalleTrabajador(3)

        assertTrue(result.isSuccess)
        assertEquals("Bastian", result.getOrNull()!![0].trabajador)

    }
    @Test
    fun obtenerReservasDetalleUsuario_devuelve_lista() = runBlocking {
        val api = mockk<ReservaServiceAPI>()
        val repo = ReservaRepositoryAPI(api)

        val sample = listOf(
            ReservaDetalleDTO(
                idReserva= 9,
                fecha= "2025-11-22",
                hora="09:00",
                estado= "Pendiente",
                usuario="Ignacia",
                trabajador="Maria",
                servicio="Coloracion"
            )
        )

        coEvery { api.getReservasDetalleUsuario(1) } returns Response.success(sample)

        val result = repo.obtenerReservasDetalleUsuario(1)

        assertTrue(result.isSuccess)
        assertEquals("Ignacia", result.getOrNull()!![0].usuario)
    }

}