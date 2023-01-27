package br.com.victor.orgs.ui.recyclerview.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.victor.orgs.R
import br.com.victor.orgs.databinding.ProdutoItemBinding
import br.com.victor.orgs.extesions.carregaImageLoader
import br.com.victor.orgs.extesions.formataParaMoedaBrasileira
import br.com.victor.orgs.extesions.tentaCarregarImagem
import br.com.victor.orgs.model.Produto
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import java.text.NumberFormat
import java.util.*


class ListaProdutosAdapter(
    produtos: List<Produto> = emptyList(),
    private val context: Context,
    var quandoClicaNoItem: (produto: Produto) -> Unit = {},
    var quandoClicaEmRemover: (produtos: Produto) -> Unit = {},
    var quandoClicaEmEditar: (produto: Produto) -> Unit = {}
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {


    private val produtos = produtos.toMutableList()

    inner class ViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root),
        PopupMenu.OnMenuItemClickListener {

        private val titulo = binding.textviewTopo
        private val descricao = binding.textviewDescricao
        private val valor = binding.textviewPreco
        private val imagem = binding.produtoItemImageview
        private lateinit var produto: Produto

        init {
            itemView.setOnClickListener {

                if (::produto.isInitialized) {
                    quandoClicaNoItem(produto)
                }
            }

            itemView.setOnLongClickListener {
                PopupMenu(context, itemView).apply {
                    menuInflater.inflate(
                        R.menu.menu_detalhe_produto,
                        menu
                    )
                    setOnMenuItemClickListener(this@ViewHolder)
                }.show()
                true
            }


        }


        fun vincula(produto: Produto, context: Context) {

            this.produto = produto

            val imageLoader: ImageLoader = carregaImageLoader(context)
            titulo.text = produto.nome
            descricao.text = produto.descricao

            val valorFormatado = produto.preco.formataParaMoedaBrasileira()
            valor.text = valorFormatado

            val visibilidade = if (produto.imagem != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
            imagem.visibility = visibilidade




            imagem.tentaCarregarImagem(produto.imagem, imageLoader)


        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            item?.let {
                when (item.itemId) {
                    R.id.menu_detalhe_produto_editar -> {
                        quandoClicaEmEditar(produto)
                    }
                    R.id.menu_detalhe_produto_remover -> {
                        quandoClicaEmRemover(produto)
                        atualiza(produtos)

                    }
                }
            }
            return true

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val biding = ProdutoItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(biding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]
        holder.vincula(produto, context)

    }

    override fun getItemCount(): Int = produtos.size

    fun atualiza(produtos: List<Produto>) {
        this.produtos.clear()
        this.produtos.addAll(produtos)
        notifyDataSetChanged()

    }

}
