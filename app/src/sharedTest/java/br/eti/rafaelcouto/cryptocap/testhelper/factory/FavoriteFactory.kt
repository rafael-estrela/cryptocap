package br.eti.rafaelcouto.cryptocap.testhelper.factory

import br.eti.rafaelcouto.cryptocap.data.model.Favorite
import kotlin.random.Random

object FavoriteFactory {

    val default get() = fromId(Random.nextLong())

    fun fromId(id: Long) = Favorite(id)
}
