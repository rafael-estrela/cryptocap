package br.eti.rafaelcouto.cryptocap.domain.mapper

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoDetails
import br.eti.rafaelcouto.cryptocap.data.model.QuoteDetails
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoDetailsMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoDetailsUI
import br.eti.rafaelcouto.cryptocap.ext.asMonetary
import br.eti.rafaelcouto.cryptocap.ext.asPercentage

class CryptoDetailsMapper : CryptoDetailsMapperAbs {

    override fun map(
        details: Result<CryptoDetails>,
        quotes: Result<QuoteDetails>
    ) = details.data?.let { detailsData ->
        quotes.data?.let { quotesData ->
            Result.success(mapData(detailsData, quotesData))
        } ?: Result.error(quotes.error.orEmpty())
    } ?: Result.error(details.error.orEmpty())

    private fun mapData(details: CryptoDetails, quotes: QuoteDetails) = CryptoDetailsUI(
        details.id,
        "${details.name} (${details.symbol})",
        details.description,
        details.logo,
        "1 ${details.symbol} = ${quotes.quote.usdQuote.price.asMonetary()}",
        quotes.quote.usdQuote.dayVariation.asPercentage(),
        quotes.quote.usdQuote.weekVariation.asPercentage(),
        quotes.quote.usdQuote.monthVariation.asPercentage(),
    )
}
