package br.com.victor.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.victor.orgs.R
import br.com.victor.orgs.database.AppDataBase
import br.com.victor.orgs.databinding.ActivityDetalheProdutoBinding
import br.com.victor.orgs.extesions.carregaImageLoader
import br.com.victor.orgs.extesions.formataParaMoedaBrasileira
import br.com.victor.orgs.extesions.tentaCarregarImagem
import br.com.victor.orgs.model.Produto
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DetalheProdutoActivity : AppCompatActivity() {


    private var produto: Produto? = null
    private var produtoId: Long = 0L

    private val produtoDao by lazy {
        AppDataBase.getInstance(this).produtoDao()
    }
    private val binding by lazy {
        ActivityDetalheProdutoBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tentaCarregarProduto()
        buscaProdutoPorId()


    }



    private fun buscaProdutoPorId() {

        lifecycleScope.launch {
            produtoDao.buscaProdutoId(produtoId).collect{produtoEncontrado ->
                produto = produtoEncontrado
                produtoEncontrado?.let {
                    preencheCampos(it)
                    title = it.nome
                } ?: finish()

            }

        }




    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhe_produto, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_detalhe_produto_editar -> {
                Intent(this, FormularioProdutoActivity::class.java).apply {
                    putExtra(CHAVE_PRODUTO_ID, produtoId)
                    startActivity(this)
                }

            }
            R.id.menu_detalhe_produto_remover -> {
              produto?.let { produto ->
                  lifecycleScope.launch {
                      produtoDao.remove(produto)
                      finish()
                  }
              }


            }
        }


        return super.onOptionsItemSelected(item)
    }


    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)

    }

    private fun preencheCampos(produtoCarregado: Produto) {

        val gif = carregaImageLoader(this)

        with(binding) {


            detalheProdutoImageview.tentaCarregarImagem(produtoCarregado.imagem, gif)
            detalheProdutoTextviewTitulo.text = produtoCarregado.nome
            detalheProdutoTextviewDesc.text = produtoCarregado.descricao
            detalheProdutoTextviewValor.text = produtoCarregado.preco.formataParaMoedaBrasileira()
        }
    }
}