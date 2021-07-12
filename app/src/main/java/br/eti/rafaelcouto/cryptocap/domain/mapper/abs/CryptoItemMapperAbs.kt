package br.eti.rafaelcouto.cryptocap.domain.mapper.abs

import androidx.paging.PagingData
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI

interface CryptoItemMapperAbs {

    fun map(input: PagingData<CryptoItem>): PagingData<CryptoItemUI>
}
