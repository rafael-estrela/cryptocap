package br.eti.rafaelcouto.cryptocap.domain.mapper.abs

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoDetails
import br.eti.rafaelcouto.cryptocap.data.model.QuoteDetails
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoCompareUI

interface CryptoCompareMapperAbs {

    fun mapElement(
        details: Result<CryptoDetails>,
        quotes: Result<QuoteDetails>
    ): Result<CryptoCompareUI.Element>

    fun map(
        from: Result<CryptoCompareUI.Element>,
        to: Result<CryptoCompareUI.Element>
    ): Result<CryptoCompareUI>

    fun map(from: CryptoCompareUI.Element, to: CryptoCompareUI.Element): CryptoCompareUI
}
