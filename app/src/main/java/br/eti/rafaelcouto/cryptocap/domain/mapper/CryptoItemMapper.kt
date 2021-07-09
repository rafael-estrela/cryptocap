package br.eti.rafaelcouto.cryptocap.domain.mapper

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoItemMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI
import br.eti.rafaelcouto.cryptocap.ext.asMonetary
import br.eti.rafaelcouto.cryptocap.ext.asPercentage

class CryptoItemMapper : CryptoItemMapperAbs {

    override fun map(input: Result<List<CryptoItem>>) = Result(
        mapData(input.data),
        input.status,
        input.error
    )

    private fun mapData(data: List<CryptoItem>?) = data?.map {
        CryptoItemUI(
            id = it.id,
            name = it.name,
            symbol = it.symbol,
            dollarPrice = it.quote.usdQuote.price.asMonetary(),
            recentVariation = it.quote.usdQuote.dayVariation.asPercentage()
        )
    }
}
