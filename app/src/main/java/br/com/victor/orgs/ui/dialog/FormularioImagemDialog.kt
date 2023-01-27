package br.com.victor.orgs.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.com.victor.orgs.databinding.FormularioDialogBinding
import br.com.victor.orgs.extesions.tentaCarregarImagem
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

class FormularioImagemDialog(private val context: Context) {

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    fun mostraDialog(urlPadrao: String? = null, quandoCarregarImagem: (String) -> Unit) {
        FormularioDialogBinding.inflate(LayoutInflater.from(context)).apply {
            urlPadrao?.let {
                formularioDialogImageview.tentaCarregarImagem(it, imageLoader)
                formularioDialogEdittext.setText(it)
            }

            formularioDialogBotao.setOnClickListener {
                val url = formularioDialogEdittext.text.toString()
                formularioDialogImageview.tentaCarregarImagem(url, imageLoader)
            }


            AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton("Confirmar") { _, _ ->
                    val url = formularioDialogEdittext.text.toString()
                    quandoCarregarImagem(url)

                }.setNegativeButton("Cancelar", DialogInterface.OnClickListener { _, _ ->

                })
                .show()


        }


    }
}