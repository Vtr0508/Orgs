package br.com.victor.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.com.victor.orgs.database.AppDataBase
import br.com.victor.orgs.extensions.vaiPara
import br.com.victor.orgs.model.Usuario
import br.com.victor.orgs.preferences.dataStore
import br.com.victor.orgs.preferences.usuarioLogadoPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class UsuarioBaseActivity : AppCompatActivity() {

    private val usuarioDao by lazy {
        AppDataBase.getInstance(this).usuarioDao()
    }

    private var _usuario: MutableStateFlow<Usuario?> = MutableStateFlow(null)
    protected var usuario: StateFlow<Usuario?> = _usuario


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {

            verificaUsuarioLogado()


        }
    }






    private suspend fun verificaUsuarioLogado() {
        dataStore.data.collect { preferences ->
            preferences[usuarioLogadoPreferences]?.let { usuarioId ->
                buscaUsuario(usuarioId)

            } ?: vaiParaLogin()

        }
    }

    private suspend fun buscaUsuario(usuarioId: String) {

        _usuario.value = usuarioDao
            .buscaPorId(usuarioId)
            .firstOrNull()

    }

    protected suspend fun deslogaUsuario() {
        dataStore.edit { preferences ->
            preferences.remove(usuarioLogadoPreferences)

        }
    }

    protected fun vaiParaLogin() {
        vaiPara(LoginActivity::class.java){
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }
}