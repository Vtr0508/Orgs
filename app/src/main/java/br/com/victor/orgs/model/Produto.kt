package br.com.victor.orgs.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal


@Entity
@Parcelize
data class Produto(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    val nome: String,
    val descricao: String,
    val preco: BigDecimal,
    val imagem: String? = null
):Parcelable
