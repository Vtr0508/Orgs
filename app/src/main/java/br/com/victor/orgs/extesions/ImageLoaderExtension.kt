package br.com.victor.orgs.extesions

import android.content.Context
import android.os.Build
import br.com.victor.orgs.databinding.ActivityDetalheProdutoBinding
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

fun carregaImageLoader(context: Context): ImageLoader{
    val imageLoader: ImageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    return imageLoader
}

