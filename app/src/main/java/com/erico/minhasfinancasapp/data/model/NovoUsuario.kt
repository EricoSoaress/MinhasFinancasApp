// Local: app/src/main/java/com/erico/minhasfinancasapp/data/model/NovoUsuario.kt

package com.erico.minhasfinancasapp.data.model

// Adicionamos o campo 'nome' para corresponder exatamente ao que a API espera.
// Não precisamos mais do @SerializedName se os nomes forem idênticos.
data class NovoUsuario(
    val nome: String,
    val email: String,
    val senha: String
)
