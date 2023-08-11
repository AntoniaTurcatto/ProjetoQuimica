package com.example.user.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.Usuario;

public class UsuarioDB {
    private SQLiteDatabase bancoDados;
    private Conexao conexao;

    public UsuarioDB(Context context) {
        this.conexao = new Conexao(context);
    }


    public String insereUsuario(Usuario meuUsuario) {
        String retornoUsuario = "";
        ContentValues valores;
        long resultado;

        this.bancoDados = this.conexao.getWritableDatabase();

        valores = new ContentValues();

        valores.put(Conexao.getNomeCompletoUsuario(), meuUsuario.getNomeCompleto());
        valores.put(Conexao.getNomeUsuario(), meuUsuario.getNomeUsuario());
        valores.put(Conexao.getSenhaUsuario(), meuUsuario.getSenha());
        valores.put(Conexao.getEMAIL(), meuUsuario.getEmail());
        valores.put(Conexao.getCpfUsuario(), meuUsuario.getCpf());

        resultado = this.bancoDados.insert(Conexao.getTabelaUsuario(), null, valores);

        this.bancoDados.close();

        if (resultado == -1) {
            retornoUsuario = "Erro ao inserir os registros na tabela " + Conexao.getTabelaUsuario();
        } else {
            retornoUsuario = "Dados inseridos com sucesso na tabela " + Conexao.getTabelaUsuario();
            }
        return retornoUsuario;
    }


    public Usuario logaUsuario(Usuario usuario){
        Cursor cursor;
        Usuario meuUsuario = null;
        String[] campos =  {Conexao.getIdUsuario(), Conexao.getNomeCompletoUsuario(), Conexao.getNomeUsuario(), Conexao.getSenhaUsuario(), Conexao.getEMAIL(), Conexao.getCpfUsuario()};
        String where = Conexao.getNomeUsuario() + "='" + usuario.getNomeUsuario() + "' and " + Conexao.getSenhaUsuario() + "='" + usuario.getSenha() + "'";
        this.bancoDados = this.conexao.getReadableDatabase();
        cursor = this.bancoDados.query(Conexao.getTabelaUsuario(),campos,where, null, null, null, null, null);

        if(cursor != null){
            if (cursor.moveToFirst()){
                // obtendo informação por informação
                int idUsuario = cursor.getInt(cursor.getColumnIndex(Conexao.getIdUsuario()));
                String nomeCompleto = cursor.getString(cursor.getColumnIndex(Conexao.getNomeCompletoUsuario()));
                String nomeUsuario = cursor.getString(cursor.getColumnIndex(Conexao.getNomeUsuario()));
                String senha = cursor.getString(cursor.getColumnIndex(Conexao.getSenhaUsuario()));
                String email = cursor.getString(cursor.getColumnIndex(Conexao.getEMAIL()));
                long cpf = cursor.getLong(cursor.getColumnIndex(Conexao.getCpfUsuario()));

                // criando o objeto da classe
                meuUsuario = new Usuario(idUsuario, nomeCompleto, nomeUsuario, senha, email, cpf);
            }
        }
        this.bancoDados.close();
        return meuUsuario;
    }


    public void alteraUsuario(Usuario meuUsuario){
        ContentValues valores;
        String where;

        this.bancoDados = this.conexao.getWritableDatabase();

        where = Conexao.getIdUsuario() + "=" + meuUsuario.getIdUsuario();

        valores = new ContentValues();

        valores.put(Conexao.getNomeCompletoUsuario(), meuUsuario.getNomeCompleto());
        valores.put(Conexao.getNomeUsuario(), meuUsuario.getNomeUsuario());
        valores.put(Conexao.getSenhaUsuario(), meuUsuario.getSenha());
        valores.put(Conexao.getEMAIL(), meuUsuario.getEmail());
        valores.put(Conexao.getCpfUsuario(), meuUsuario.getCpf());

        this.bancoDados.update(Conexao.getTabelaUsuario(),valores,where,null);
        this.bancoDados.close();
    }


    public void deletaUsuario(int idUsuario){
        String where = Conexao.getIdUsuario() + "=" + idUsuario;
        this.bancoDados = this.conexao.getReadableDatabase();
        this.bancoDados.delete(Conexao.getTabelaUsuario(),where,null);
        this.bancoDados.close();
    }
}
