package br.com.victor.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.victor.orgs.database.AppDataBase
import br.com.victor.orgs.databinding.ActivityFormularioProdutoBinding
import br.com.victor.orgs.extesions.carregaImageLoader
import br.com.victor.orgs.extesions.tentaCarregarImagem
import br.com.victor.orgs.model.Produto
import br.com.victor.orgs.preferences.dataStore
import br.com.victor.orgs.preferences.usuarioLogadoPreferences
import br.com.victor.orgs.ui.dialog.FormularioImagemDialog
import coil.ImageLoader
import coil.imageLoader
import coil.load
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FormularioProdutoActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }

    private var url: String? = null
    private var produtoId = 0L

    private val produtoDao by lazy {
        AppDataBase.getInstance(this).produtoDao()
    }

    private val usuarioDao by lazy {
        AppDataBase.getInstance(this).usuarioDao()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Cadastrar Produtos"
        configuraBotaoSalvar()
        val imageLoader = carregaImageLoader(this)

        binding.formularioProdutoImageview.setOnClickListener {
            FormularioImagemDialog(this).mostraDialog(url) { imagem ->
                url = imagem
                binding.formularioProdutoImageview.tentaCarregarImagem(url, imageLoader)

            }
        }

        tentaCarregarProduto()
        lifecycleScope.launch {
            launch {
                produtoDao.buscaProdutoId(produtoId).collect { produto ->
                    produto?.let {
                        setTitle("Editar Produto ${it.nome}")
                        preencheCampos(it, imageLoader)
                    }

                }
            }


        }


    }


    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }

    private fun preencheCampos(it: Produto, imageLoader: ImageLoader) {

        url = it.imagem
        binding.formularioProdutoImageview.tentaCarregarImagem(it.imagem, imageLoader)
        binding.formularioProdutoEdittextNome.setText(it.nome)
        binding.formularioProdutoEdittextDesc.setText(it.descricao)
        binding.formularioProdutoEdittextValor.setText(it.preco.toPlainString())
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.botaoSalvar
        botaoSalvar.setOnClickListener {
            lifecycleScope.launch {

                usuario.value?.let {usuario ->
                    val produtoNovo = criaProdutoNovo(usuario.id)
                    produtoDao.salva(produtoNovo)
                    finish()
                }


            }


        }
    }

    private fun criaProdutoNovo(usuarioId: String): Produto {
        val campoNome = binding.formularioProdutoEdittextNome
        val nome = campoNome.text.toString()


        val campoDescricao = binding.formularioProdutoEdittextDesc
        val descricao = campoDescricao.text.toString()

        val campoValor = binding.formularioProdutoEdittextValor
        val valorEmTexto = campoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }




        return Produto(
            id = produtoId,
            nome = nome,
            descricao = descricao,
            preco = valor,
            imagem = url,
            usuarioId = usuarioId
        )


    }
}