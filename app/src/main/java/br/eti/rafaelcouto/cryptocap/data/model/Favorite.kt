package br.eti.rafaelcouto.cryptocap.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey val id: Long
)
