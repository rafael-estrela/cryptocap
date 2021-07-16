package br.eti.rafaelcouto.cryptocap.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.eti.rafaelcouto.cryptocap.data.local.dao.FavoriteDao
import br.eti.rafaelcouto.cryptocap.testhelper.factory.FavoriteFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteDatabaseTest {

    private lateinit var db: FavoriteDatabase
    private lateinit var dao: FavoriteDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(
            context,
            FavoriteDatabase::class.java
        ).build()

        dao = db.favoriteDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun saveFavoriteTest() = runBlocking {
        val favorite = FavoriteFactory.default

        assertThat(dao.findById(favorite.id)).isNull()

        dao.insert(favorite)

        assertThat(dao.findById(favorite.id)).isEqualTo(favorite)
    }

    @Test
    fun deleteFavoriteTest() = runBlocking {
        val favorite = FavoriteFactory.default

        dao.insert(favorite)

        assertThat(dao.findById(favorite.id)).isEqualTo(favorite)

        dao.delete(favorite)

        assertThat(dao.findById(favorite.id)).isNull()
    }

    @Test
    fun loadSingleFavoriteTest() = runBlocking {
        val expected = FavoriteFactory.default

        dao.insert(expected)

        val actual = dao.findById(expected.id)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun loadStreamOfFavoritesTest() = runBlocking {
        val entries = arrayOf(
            FavoriteFactory.default,
            FavoriteFactory.default,
            FavoriteFactory.default
        )

        entries.forEach { dao.insert(it) }

        val content = dao.fetchAll().take(1).toList()[0]

        assertThat(content).hasSize(3)
        assertThat(content).contains(entries[0])
        assertThat(content).contains(entries[1])
        assertThat(content).contains(entries[2])

        val newEntry = FavoriteFactory.default

        dao.insert(newEntry)

        val updatedContent = dao.fetchAll().take(1).toList()[0]

        assertThat(updatedContent).hasSize(4)
        assertThat(updatedContent).contains(entries[0])
        assertThat(updatedContent).contains(entries[1])
        assertThat(updatedContent).contains(entries[2])
        assertThat(updatedContent).contains(newEntry)
    }
}
