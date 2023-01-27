package br.com.victor.orgs.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.victor.orgs.database.AppDataBase
import br.com.victor.orgs.databinding.ActivityLoginBinding
import br.com.victor.orgs.extensions.vaiPara
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val usuarioDao by lazy {
        AppDataBase.getInstance(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
        configuraBotaoEntrar()
    }

    private fun configuraBotaoEntrar() {
        binding.activityLoginBotaoEntrar.setOnClickListener {
            val usuario = binding.activityLoginUsuario.text.toString()
            val senha = binding.activityLoginSenha.text.toString()

            lifecycleScope.launch {
                val usuarioAutenticado = usuarioDao.autentica(usuario, senha)
                usuarioAutenticado?.let {usuarioCarregado ->
                    vaiPara(ListaProdutosActivity::class.java){
                        putExtra(CHAVE_USUARIO, usuarioCarregado.id)
                    }
                } ?: Toast.makeText(
                    this@LoginActivity,
                    "Usuario inválido",
                    Toast.LENGTH_SHORT
                ).show()
            }
            

            
        }
    }

    private fun configuraBotaoCadastrar() {
        binding.activityLoginBotaoCadastrar.setOnClickListener {
            vaiPara(FormularioCadastroUsuarioActivity::class.java)
        }
    }

}