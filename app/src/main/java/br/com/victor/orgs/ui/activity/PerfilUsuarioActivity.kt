package br.com.victor.orgs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import br.com.victor.orgs.R
import br.com.victor.orgs.databinding.ActivityPerfilUsuarioBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PerfilUsuarioActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityPerfilUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        preencheCampo()
        configuraBotaoDeslogar()


    }

    private fun configuraBotaoDeslogar() {
        binding.perfiUsuarioBotaoDeslogar.setOnClickListener {
            lifecycleScope.launch {
                launch {
                    deslogaUsuario()
                }

            }

        }
    }

    private fun preencheCampo() {
        lifecycleScope.launch {
            launch {
                usuario.filterNotNull().collect {
                    binding.perfiUsuarioTextview.text = it.nome
                }
            }
        }
    }
}