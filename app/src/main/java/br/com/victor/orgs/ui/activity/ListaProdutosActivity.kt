package br.com.victor.orgs.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.victor.orgs.R
import br.com.victor.orgs.database.AppDataBase
import br.com.victor.orgs.databinding.ActivityListaProdutosBinding
import br.com.victor.orgs.model.Produto
import br.com.victor.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "Clicou"

class ListaProdutosActivity : AppCompatActivity() {

    private val produtoDao by lazy {
        AppDataBase.getInstance(this).produtoDao()
    }
    private val usuarioDao by lazy {
        AppDataBase.getInstance(this).usuarioDao()
    }

    private val binding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }


    private val adapter = ListaProdutosAdapter(
        context = this

    )


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()

        lifecycleScope.launch {
            launch {
                produtoDao.buscaTodos().collect { produto ->
                    adapter.atualiza(produto)
                }
            }


            intent.getStringExtra(CHAVE_USUARIO)?.let {usuarioId ->
                usuarioDao.buscaPorId(usuarioId).collect{
                    Log.i("pegou!", "onCreate: $it")
                }

            }



        }




    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ordenar, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        lifecycleScope.launch {

            val produtoOrdenado: List<Produto>? = when (item.itemId) {
                R.id.menu_ordenar_asc ->
                    produtoDao.buscaProdutoAsc()

                R.id.menu_ordenar_decr ->
                    produtoDao.buscaProdutoDesc()
                else -> null
            }
            produtoOrdenado?.let {
                adapter.atualiza(it)
            }
        }



        return super.onOptionsItemSelected(item)
    }

    private fun configuraFab() {
        val fab = binding.floatingActionButton
        fab.setOnClickListener {
            vaiParaFormulario()
        }
    }

    private fun vaiParaFormulario() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun configuraRecyclerView() {
        val db = AppDataBase.getInstance(this)
        val produtoDao = db.produtoDao()
        val recyclerView = binding.activityListaProdutosRecyclerView
        recyclerView.adapter = adapter
        adapter.quandoClicaNoItem = {
            val intent = Intent(
                this,
                DetalheProdutoActivity::class.java
            ).apply {
                putExtra(CHAVE_PRODUTO_ID, it.id)
            }
            startActivity(intent)

        }
        adapter.quandoClicaEmEditar = {
            val intent = Intent(this, DetalheProdutoActivity::class.java).apply {
                putExtra(CHAVE_PRODUTO_ID, it.id)
            }
            startActivity(intent)
        }

        adapter.quandoClicaEmRemover = {

            lifecycleScope.launch {
                produtoDao.remove(it)
            }


        }
    }
}