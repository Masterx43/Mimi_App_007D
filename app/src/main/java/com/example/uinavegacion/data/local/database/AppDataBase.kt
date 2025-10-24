package com.example.uinavegacion.data.local.database

import android.content.Context                                  // Contexto para construir DB
import androidx.room.Database                                   // Anotación @Database
import androidx.room.Room                                       // Builder de DB
import androidx.room.RoomDatabase                               // Clase base de DB
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase // Tipo del callback onCreate
import com.example.uinavegacion.data.local.converters.DateConverters
import com.example.uinavegacion.data.local.entities.categoria.*
import com.example.uinavegacion.data.local.entities.estado.*
import com.example.uinavegacion.data.local.entities.reservas.*
import com.example.uinavegacion.data.local.entities.rol.*
import com.example.uinavegacion.data.local.entities.servicio.*
import com.example.uinavegacion.data.local.entities.estado.*
import com.example.uinavegacion.data.local.entities.user.*// Import del DAO de usuario // Import de la entidad de usuario
import kotlinx.coroutines.CoroutineScope                        // Para corrutinas en callback
import kotlinx.coroutines.Dispatchers                           // Dispatcher IO
import kotlinx.coroutines.launch                                // Lanzar corrutina


// @Database registra entidades y versión del esquema.
// version = 1: como es primera inclusión con teléfono, partimos en 1.
@Database(
    entities = [
                UserEntity::class,
                EstadoEntity::class,
                ReservaEntity::class,
                RolEntity::class,
                ServicioEntity::class,
                CategoriaEntity::class
                ],
    version = 2,
    exportSchema = true // Mantener true para inspección de esquema (útil en educación)
)
abstract class AppDatabase : RoomDatabase() {

    // Exponemos el DAO de usuarios
    abstract fun userDao(): UserDao
    abstract fun rolDao(): RolDao
    abstract fun servicioDao(): ServicioDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun reservaDao() : ReservaDao
    abstract fun estadoDao() : EstadoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null              // Instancia singleton
        private const val DB_NAME = "ui_navegacion.db"         // Nombre del archivo .db

        // Obtiene la instancia única de la base
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Construimos la DB con callback de precarga
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    // Callback para ejecutar cuando la DB se crea por primera vez
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Lanzamos una corrutina en IO para insertar datos iniciales
                            CoroutineScope(Dispatchers.IO).launch {
                                val rolDao = getInstance(context).rolDao()
                                val servicioDao = getInstance(context).servicioDao()
                                val categoriaDao = getInstance(context).categoriaDao()
                                val userdao = getInstance(context).userDao()
                                val estadoDao = getInstance(context).estadoDao()

                                val estadosSeed = listOf(
                                    EstadoEntity(nombre = "Activo"),
                                    EstadoEntity(nombre = "Pendiente"),
                                    EstadoEntity(nombre = "Inactivo")
                                )
                                if (estadoDao.getTotalEstado() == 0) {
                                    estadosSeed.forEach { estadoDao.insertEstado(it) }
                                }


                                val rolesSeed = listOf(
                                    RolEntity( descripcion = "Cliente"),
                                    RolEntity(descripcion = "Administrador"),
                                    RolEntity(descripcion = "Trabajador")
                                )
                                if (rolDao.getTotalRols() == 0) rolesSeed.forEach { rolDao.insertRol(it) }

                                // Categorías
                                val categoriasSeed = listOf(CategoriaEntity(nombreCategoria = "Adulto"), CategoriaEntity(nombreCategoria = "Niño"))
                                if (categoriaDao.getTotalCategoria() == 0) categoriasSeed.forEach { categoriaDao.insert(it) }

                                // Precarga de usuarios (incluye teléfono)
                                // Reemplaza aquí por los mismitos datos que usas en Login/Register.
                                val seedUser = listOf(
                                    UserEntity(
                                        nombre = "Demo",
                                        apellido = "Usuario",
                                        correo = "demo@duoc.cl",
                                        pass = "Demo123!",          // en producción se debería encriptar
                                        phone = "987654321",
                                        rolId = 1L,                 // Cliente
                                        categoriaId = 2L,
                                        estadoId = 1L
                                    ),
                                    UserEntity(
                                        nombre = "Demo2",
                                        apellido = "Usuario",
                                        correo = "demo2@duoc.cl",
                                        pass = "Demo12!",          // en producción se debería encriptar
                                        phone = "987654321",
                                        rolId = 2L,                 // Administrador
                                        categoriaId = 1L,
                                        estadoId = 1L// Categoría por defecto
                                    ),
                                    UserEntity(
                                        nombre = "Demo3",
                                        apellido = "Trabajador",
                                        correo = "demo3@duoc.cl",
                                        pass = "Demo12!",          // en producción se debería encriptar
                                        phone = "9876543212",
                                        rolId = 3L,                 // Trabajador
                                        categoriaId = 1L,
                                        estadoId = 1L// Categoría por defecto
                                    )
                                )

                                // Inserta seed sólo si la tabla está vacía
                                if (userdao.count() == 0) {
                                    seedUser.forEach { userdao.insert(it) }
                                }


                                // Servicios
                                val serviciosSeed = listOf(
                                    ServicioEntity(
                                        nombre = "Corte de pelo",
                                        precio = 15000,
                                        descripcion = "Corte clásico de cabello para adultos",
                                        categoriaId = 1L   // Adulto
                                    ),
                                    ServicioEntity(
                                        nombre = "Peinado",
                                        precio = 10000,
                                        descripcion = "Peinado elegante para cualquier ocasión",
                                        categoriaId = 1L   // Adulto
                                    ),
                                    ServicioEntity(
                                        nombre = "Coloración",
                                        precio = 20000,
                                        descripcion = "Cambio de color o mechas",
                                        categoriaId = 1L   // Adulto
                                    ),
                                    ServicioEntity(
                                        nombre = "Corte niño",
                                        precio = 8000,
                                        descripcion = "Corte adaptado para niños",
                                        categoriaId = 2L   // Niño
                                    ),
                                    ServicioEntity(
                                        nombre = "Peinado niño",
                                        precio = 7000,
                                        descripcion = "Peinado sencillo para niños",
                                        categoriaId = 2L   // Niño
                                    )
                                )
                                if (servicioDao.getTotalServicios() == 0) serviciosSeed.forEach { servicioDao.insertServicio(it) }


                            }
                        }
                    })
                    // En entorno educativo, si cambias versión sin migraciones, destruye y recrea.
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance                             // Guarda la instancia
                instance                                        // Devuelve la instancia
            }
        }
    }
}