package br.eti.rafaelcouto.cryptocap.domain.mapper

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoDetails
import br.eti.rafaelcouto.cryptocap.data.model.QuoteDetails
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoCompareMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoCompareUI

class CryptoCompareMapper : CryptoCompareMapperAbs {

    override fun mapElement(
        details: Result<CryptoDetails>,
        quotes: Result<QuoteDetails>
    ) = details.data?.let { detailsData ->
        quotes.data?.let { quotesData ->
            Result.success(mapElement(detailsData, quotesData))
        } ?: Result.error(quotes.error.orEmpty())
    } ?: Result.error(details.error.orEmpty())

    override fun map(
        from: Result<CryptoCompareUI.Element>,
        to: Result<CryptoCompareUI.Element>
    ) = from.data?.let { fromData ->
        to.data?.let { toData ->
            Result.success(map(fromData, toData))
        } ?: Result.error(to.error.orEmpty())
    } ?: Result.error(from.error.orEmpty())

    override fun map(from: CryptoCompareUI.Element, to: CryptoCompareUI.Element) = CryptoCompareUI(
        from, to, fromToRatio = from.usdValue / to.usdValue
    )

    private fun mapElement(details: CryptoDetails, quotes: QuoteDetails) = CryptoCompareUI.Element(
        details.symbol,
        details.logo,
        quotes.quote.usdQuote.price
    )
}
