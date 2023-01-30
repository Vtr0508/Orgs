package br.com.victor.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.victor.orgs.model.Produto
import kotlinx.coroutines.flow.Flow


@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun buscaTodos(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId")
    fun buscaProdutoUsuario(usuarioId :String): Flow<List<Produto>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salva(produto: Produto)

    @Delete
    suspend fun remove(produto: Produto)


    @Query("SELECT * FROM Produto WHERE id = :id")
    fun buscaProdutoId(id: Long): Flow<Produto?>

    @Query("SELECT * FROM Produto ORDER BY nome ASC")
    suspend fun buscaProdutoAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY nome DESC")
    suspend fun buscaProdutoDesc(): List<Produto>

}