package br.eti.rafaelcouto.cryptocap.domain.mapper.abs

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoDetails
import br.eti.rafaelcouto.cryptocap.data.model.Favorite
import br.eti.rafaelcouto.cryptocap.data.model.QuoteDetails
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoDetailsUI

interface CryptoDetailsMapperAbs {

    fun map(details: Result<CryptoDetails>, quotes: Result<QuoteDetails>): Result<CryptoDetailsUI>
    fun map(id: Long): Favorite
}
