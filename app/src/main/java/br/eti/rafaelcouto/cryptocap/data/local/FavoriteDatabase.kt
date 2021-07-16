package br.eti.rafaelcouto.cryptocap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.eti.rafaelcouto.cryptocap.data.local.dao.FavoriteDao
import br.eti.rafaelcouto.cryptocap.data.model.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
}
