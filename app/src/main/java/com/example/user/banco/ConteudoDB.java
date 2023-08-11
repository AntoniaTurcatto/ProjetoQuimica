package com.example.user.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.Usuario;

import java.util.ArrayList;

public class ConteudoDB {
    private SQLiteDatabase bancoDados;
    private Conexao conexao;

    public ConteudoDB(Context context) {
        this.conexao = new Conexao(context);
    }

    public String insereConteudo(Conteudo umConteudo){
        String retornoConteudo = "";
        long resultado;
        ContentValues valores;

        this.bancoDados = this.conexao.getWritableDatabase();

        valores = new ContentValues();

        valores.put(Conexao.getNomeConteudo(), umConteudo.getNomeConteudo());
        valores.put(Conexao.getTipoConteudo(), umConteudo.getTipoConteudo());

        resultado = this.bancoDados.insert(Conexao.getTabelaConteudo(),null,valores);

        this.bancoDados.close();

        if (resultado == -1) {
            retornoConteudo = "Erro ao inserir os registros na tabela " + Conexao.getTabelaConteudo();
        } else {
            retornoConteudo = "Dados inseridos com sucesso na tabela " + Conexao.getTabelaConteudo();
        }
        return retornoConteudo;
    }


    public Conteudo buscaUMConteudo(String nomeConteudo){
        //QUANDO F0RMOS FAZER PRA VALER USAR UMA ACTIVITY EM QUE MOSTRE NA CAIXINHA AS OPÇOES DE CONTEUDO E AI JA ROLA
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        this.bancoDados = this.conexao.getWritableDatabase();

        String where = Conexao.getNomeConteudo()+"= '" + nomeConteudo + "'";
        Cursor cursor = this.bancoDados.query(Conexao.getTabelaConteudo(), null, where, null, null, null, null);

        cursor.moveToFirst();
        nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
        int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdConteudo()));
        int tipoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoConteudo()));

        this.bancoDados.close();

        Conteudo conteudoComId = new Conteudo(idConteudo,nomeConteudo,tipoConteudo);
        return conteudoComId;
    }


    public ArrayList<Conteudo> buscaConteudos(int tipoConteudo) {
        ArrayList<Conteudo> listaConteudos = new ArrayList<>();
        String where = Conexao.getTipoConteudo() + "=" + tipoConteudo;
        this.bancoDados = this.conexao.getWritableDatabase();
        Cursor cursor = this.bancoDados.query(Conexao.getTabelaConteudo(),null, where, null, null, null, null);

        while (cursor.moveToNext()) {
            int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdConteudo()));
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            Conteudo umConteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);
            Log.d("Teste", "DB-Nome" + nomeConteudo + "DB-TipoConteudo: " + cursor.getString(cursor.getColumnIndex(Conexao.getTipoConteudo())));
            this.bancoDados.close();

            listaConteudos.add(umConteudo);
        }
        return listaConteudos;
    }


    public ArrayList<Conteudo> buscaConteudosAleatorios(int quantidade, int tipoConteudo) {
        ArrayList<Conteudo> listaConteudos = new ArrayList<>();
        String where = Conexao.getTipoConteudo() + "=" + tipoConteudo;
        this.bancoDados = this.conexao.getWritableDatabase();
        Cursor cursor = this.bancoDados.query(Conexao.getTabelaConteudo(),null, where, null, null, null, "RANDOM() LIMIT " + quantidade);

        while (cursor.moveToNext()) {
            int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdConteudo()));
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            Conteudo umConteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);
            this.bancoDados.close();

            listaConteudos.add(umConteudo);
        }
        return listaConteudos;
    }


    public Conteudo carregaConteudoById(int id){
        Cursor cursor;
        Conteudo meuConteudo = null;
        String[] campos =  {Conexao.getIdConteudo(), Conexao.getNomeConteudo(), Conexao.getTipoConteudo()};
        String where = Conexao.getIdConteudo() + "=" + id;
        this.bancoDados = this.conexao.getReadableDatabase();
        cursor = this.bancoDados.query(Conexao.getTabelaConteudo(),campos,where, null, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
            // obtendo informação por informação
            int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdConteudo()));
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            int tipoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoConteudo()));

            // criando o objeto da classe
            meuConteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);
        }
        this.bancoDados.close();
        return meuConteudo;
    }


    public void alteraConteudo(Conteudo meuConteudo){
        ContentValues valores;
        String where;

        this.bancoDados = this.conexao.getWritableDatabase();

        where = Conexao.getIdConteudo() + "=" + meuConteudo.getIdConteudo();

        valores = new ContentValues();
        valores.put(Conexao.getNomeConteudo(), meuConteudo.getNomeConteudo());
        valores.put(Conexao.getTipoConteudo(), meuConteudo.getTipoConteudo());

        this.bancoDados.update(Conexao.getTabelaConteudo(),valores,where,null);
        this.bancoDados.close();
    }


    public void deletaConteudo(int idConteudo){
        String where = Conexao.getIdConteudo() + "=" + idConteudo;
        this.bancoDados = this.conexao.getReadableDatabase();
        this.bancoDados.delete(Conexao.getTabelaConteudo(),where,null);
        this.bancoDados.close();
    }
}
