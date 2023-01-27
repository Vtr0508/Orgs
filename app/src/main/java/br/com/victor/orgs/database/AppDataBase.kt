package br.com.victor.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.victor.orgs.database.converter.Converters
import br.com.victor.orgs.database.dao.ProdutoDao
import br.com.victor.orgs.database.dao.UsuarioDao
import br.com.victor.orgs.model.Produto
import br.com.victor.orgs.model.Usuario

@Database(entities = [Produto::class,Usuario::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun produtoDao(): ProdutoDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
       @Volatile private var db: AppDataBase? = null
        fun getInstance(context: Context): AppDataBase {
            return db?: Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "orgs.db"
            ).fallbackToDestructiveMigration()
                .build().also {
                    db = it
                }
        }
    }
}