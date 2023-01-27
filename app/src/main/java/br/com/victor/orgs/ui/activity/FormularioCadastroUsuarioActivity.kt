package br.com.victor.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.victor.orgs.database.AppDataBase
import br.com.victor.orgs.databinding.ActivityFormularioCadastroUsuarioBinding
import br.com.victor.orgs.model.Usuario
import kotlinx.coroutines.launch

class FormularioCadastroUsuarioActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioCadastroUsuarioBinding.inflate(layoutInflater)
    }

    private val usuarioDao by lazy {
        AppDataBase.getInstance(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
    }

    private fun configuraBotaoCadastrar() {
        binding.activityFormularioCadastroBotaoCadastrar.setOnClickListener {
            val novoUsuario = criaUsuario()

            lifecycleScope.launch {
                try {
                    usuarioDao.salva(novoUsuario)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@FormularioCadastroUsuarioActivity,
                        "Usuario ja cadastrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }
    }

    private fun criaUsuario(): Usuario {
        val usuario = binding.activityFormularioCadastroUsuario.text.toString()
        val nome = binding.activityFormularioCadastroNome.text.toString()
        val senha = binding.activityFormularioCadastroSenha.text.toString()
        return Usuario(usuario, nome, senha)
    }
}