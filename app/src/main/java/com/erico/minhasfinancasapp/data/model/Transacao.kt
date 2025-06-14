package com.erico.minhasfinancasapp.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.Date

data class Transacao(
    val id: Int,
    val descricao: String,
    val valor: BigDecimal,
    val tipo: String,
    val data: Date,
    @SerializedName("usuario_id")
    val usuarioId: Int
)