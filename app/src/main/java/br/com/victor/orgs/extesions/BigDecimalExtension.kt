package br.com.victor.orgs.extesions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

fun BigDecimal.formataParaMoedaBrasileira(): String {

    val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
    return formatador.format(this)

}