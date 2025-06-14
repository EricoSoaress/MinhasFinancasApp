package com.erico.minhasfinancasapp.data.model

import java.math.BigDecimal

data class NovaTransacao(
    val descricao: String,
    val valor: BigDecimal,
    val tipo: String // "receita" ou "despesa"
)