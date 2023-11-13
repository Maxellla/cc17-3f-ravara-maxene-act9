import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.entropia.flightsearch.data.AirportDao
import com.entropia.flightsearch.data.FlightSearchDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AirportDaoTest {
    private lateinit var airportDao: AirportDao
    private lateinit var database: FlightSearchDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.databaseBuilder(
            context = context, FlightSearchDatabase::class.java, "test_database"
        ).createFromAsset("database/flight_search.db")
            .fallbackToDestructiveMigration()
            .build()
        airportDao = database.airportDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAll_getAllByPassengerCount() = runBlocking {
        val allItems = airportDao.getAllByPassengers(0).first()
        allItems.forEach {
            if (it.id < allItems.count()) assertTrue(allItems[it.id - 1].passengers >= allItems[it.id].passengers)
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoGetByName_getByIataCodeFull() = runBlocking {
        val allItems=airportDao.getAirportByName("WAW").first()
        assertEquals(allItems[0].name, "Warsaw Chopin Airport")
    }

    @Test
    @Throws(Exception::class)
    fun daoGetByName_getByIataCodePartial() = runBlocking {
        val allItems=airportDao.getAirportByName("WA%").first()
        assertEquals(allItems[0].name, "Warsaw Chopin Airport")
    }

    @Test
    @Throws(Exception::class)
    fun daoGetByName_getByNameFull() = runBlocking {
        val allItems=airportDao.getAirportByName("Warsaw Chopin Airport").first()
        assertEquals(allItems[0].iataCode, "WAW")
    }

    @Test
    @Throws(Exception::class)
    fun daoGetByName_getByNamePartial() = runBlocking {
        val allItems=airportDao.getAirportByName("%saw Chopin%").first()
        assertEquals(allItems[0].iataCode, "WAW")
    }
}