package br.eti.rafaelcouto.cryptocap.application.network.model

data class Body<T>(
    val status: Status,
    val data: T
)
