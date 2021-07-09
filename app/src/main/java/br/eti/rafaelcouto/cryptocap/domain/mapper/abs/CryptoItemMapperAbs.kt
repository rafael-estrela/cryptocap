package br.eti.rafaelcouto.cryptocap.domain.mapper.abs

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI

interface CryptoItemMapperAbs {

    fun map(input: Result<List<CryptoItem>>): Result<List<CryptoItemUI>>
}
