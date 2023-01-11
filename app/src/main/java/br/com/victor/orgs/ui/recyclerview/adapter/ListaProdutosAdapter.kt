package br.com.victor.orgs.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.victor.orgs.R
import br.com.victor.orgs.databinding.ProdutoItemBinding
import br.com.victor.orgs.model.Produto

class ListaProdutosAdapter(
    produtos: List<Produto>,
    private val context: Context
)
    : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val produtos = produtos.toMutableList()

    class ViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val titulo = binding.textviewTopo
        private val descricao = binding.textviewDescricao
        private val valor = binding.textviewPreco
        fun vincula(produto: Produto) {
            titulo.text = produto.nome
            descricao.text = produto.descricao
            valor.text = produto.preco.toString()
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
        holder.vincula(produto)

    }

    override fun getItemCount(): Int = produtos.size

    fun atualiza(produtos: List<Produto>) {
        this.produtos.clear()
        this.produtos.addAll(produtos)
        notifyDataSetChanged()

    }

}
