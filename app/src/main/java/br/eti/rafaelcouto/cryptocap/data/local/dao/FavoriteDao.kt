package br.eti.rafaelcouto.cryptocap.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.eti.rafaelcouto.cryptocap.data.model.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM Favorite")
    fun fetchAll(): Flow<List<Favorite>>

    @Query("SELECT * FROM Favorite WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): Favorite?

    @Insert
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)
}
