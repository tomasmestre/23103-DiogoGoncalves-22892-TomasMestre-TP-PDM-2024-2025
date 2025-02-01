package com.example.pdm_projeto.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Vehicle::class, Utilizador::class, Reserva::class, Review::class], version = 18)
@TypeConverters(Converters::class, EnumConverters::class) // Adicionado type converters
abstract class CarDatabase : RoomDatabase() {

    abstract fun CarDao(): CarDAO
    abstract fun UtilizadorDAO(): UtilizadorDAO
    abstract fun ReservaDao(): ReservaDAO
    abstract fun ReviewDao(): ReviewForDao

    companion object {
        @Volatile
        private var INSTANCE: CarDatabase? = null

        fun getInstance(context: Context): CarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarDatabase::class.java,
                    "car_database"
                )
                    //.addMigrations(MIGRATION_1_2)
                    .addCallback(CarDatabaseCallback()) // Register the callback
                    .fallbackToDestructiveMigration()
                    .build()


                INSTANCE = instance
                instance
            }
        }
    }
    /**
     * Callback to populate the database during creation.
     */
/*    private class CarDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.CarDao(), database.UtilizadorDAO())
                }
            }
        }*/
    private class CarDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("CarDatabaseCallback", "Database created")
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    // Verificar se os dados estão vazios e, se estiverem, populá-los
                    if (database.CarDao().getAllCars().isEmpty()) {
                        populateDatabase(database.CarDao(), database.UtilizadorDAO())
                    }
                }
            }
        }

        /**
         * Populate the database with initial data.
         */
        private suspend fun populateDatabase(carDao: CarDAO, utilizadorDAO: UtilizadorDAO) {
            // Initial vehicles
            val cars = listOf(
                Vehicle(
                    id = 0,
                    name = "Toyota Corolla",
                    details = "Econômico, 5 lugares, Automático",
                    price = 150f,
                    state = true,
                    coordinates = "40.7128,-74.0060",
                    imageUrl = "https://example.com/toyota.jpg"
                ),
                Vehicle(
                    id = 0,
                    name = "Honda Civic",
                    details = "Econômico, 5 lugares, Automático",
                    price = 100f,
                    state = true,
                    coordinates = "34.0522,-118.2437",
                    imageUrl = "https://example.com/civic.jpg"
                ),
                Vehicle(
                    id = 0,
                    name = "Chevrolet Camaro",
                    details = "Desportivo, 4 lugares, Manual",
                    price = 300f,
                    state = true,
                    coordinates = "37.7749,-122.4194",
                    imageUrl = "https://example.com/onix.jpg"
                ),
                Vehicle(
                    id = 0,
                    name = "Ford Ranger",
                    details = "Carrinha, 5 lugares, 4x4",
                    price = 250f,
                    state = true,
                    coordinates = "48.8566,2.3522",
                    imageUrl = "https://example.com/ranger.jpg"
                ),
                Vehicle(
                    id = 0,
                    name = "BMW X5",
                    details = "SUV, 5 lugares, Luxo",
                    price = 400f,
                    state = true,
                    coordinates = "51.5074,-0.1278",
                    imageUrl = "https://example.com/bmw.jpg"
                )
            )
            carDao.insertAll(cars)

            // Initial users
            val users = listOf(
                Utilizador(
                    id = 0,
                    nome = "admin",
                    password = "admin",
                    role = Role.ADMIN,
                    email = "admin@admin.com"
                ),
                Utilizador(
                    id = 0,
                    nome = "user",
                    password = "user",
                    role = Role.USER,
                    email = "user@user.com"
                )
            )
            utilizadorDAO.insertAll(users)
        }
    }
}

//migração parar criar a tabela review
/*val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `review` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`reservationId` INTEGER NOT NULL, " +
                    "`reviewText` TEXT NOT NULL, " +
                    "`rating` REAL NOT NULL, " +
                    "`imagePath` TEXT)"
        )
    }
}*/


