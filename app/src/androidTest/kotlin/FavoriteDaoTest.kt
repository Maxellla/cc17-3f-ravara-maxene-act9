import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.entropia.flightsearch.data.Favorite
import com.entropia.flightsearch.data.FavoriteDao
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
class FavoriteDaoTest {
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var database: FlightSearchDatabase

    private val favorite1 = Favorite(1, "WAW", "OPO")
    private val favorite2 = Favorite(2, "AMS", "EVN")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context = context, FlightSearchDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        favoriteDao = database.favoriteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsFavoriteIntoDb() = runBlocking {
        addOneItemToDb()
        val allItems = favoriteDao.getAll().first()
        assertEquals(allItems[0], favorite1)
    }

    @Test
    @Throws(Exception::class)
    fun daoRemove_removesFavoriteFromDb() = runBlocking {
        addOneItemToDb()
        favoriteDao.removeFavorite(favorite1)
        val allItems = favoriteDao.getAll().first()
        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAll_returnsAllItemsOrderByDepartureCode() = runBlocking {
        addTwoItemsToDb()
        val allItems = favoriteDao.getAll().first()
        assertEquals(allItems[0], favorite2)
        assertEquals(allItems[1], favorite1)
    }

    private suspend fun addOneItemToDb() {
        favoriteDao.addFavorite(favorite1)
    }

    private suspend fun addTwoItemsToDb() {
        favoriteDao.addFavorite(favorite1)
        favoriteDao.addFavorite(favorite2)
    }


}