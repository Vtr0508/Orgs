package br.com.victor.orgs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import br.com.victor.orgs.R
import br.com.victor.orgs.dao.ProdutosDao
import br.com.victor.orgs.databinding.ActivityFormularioProdutoBinding
import br.com.victor.orgs.model.Produto
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoSalvar()
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.botaoSalvar
        val dao = ProdutosDao()
        botaoSalvar.setOnClickListener {
            criaProdutoNovo()
            val produtoNovo = criaProdutoNovo()
            dao.adiciona(produtoNovo)
            finish()



        }
    }

    private fun criaProdutoNovo():Produto {
        val campoNome = binding.editTextNome
        val nome = campoNome.text.toString()

        val campoDescricao = binding.editTextDescricao
        val descricao = campoDescricao.text.toString()

        val campoValor = binding.editTextValor
        val valorEmTexto = campoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }


        return Produto(
            nome = nome,
            descricao = descricao,
            preco = valor
        )



    }
}