package br.com.victor.orgs.extesions

import android.widget.ImageView
import br.com.victor.orgs.R
import coil.ImageLoader
import coil.load

fun ImageView.tentaCarregarImagem(url:String? = null,imageLoader: ImageLoader){
    load(url ,imageLoader){
        fallback(R.drawable.erro)
        error(R.drawable.erro)
        placeholder(R.drawable.placeholder)

    }

}