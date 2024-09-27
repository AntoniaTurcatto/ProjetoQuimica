package com.example.user.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.user.classesDominio.ClasseIntermediaria;
import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.DesempenhoConteudo;

import java.util.ArrayList;

public class DesempenhoConteudoDB {
    private SQLiteDatabase bancoDados;
    private Conexao conexao;

    public DesempenhoConteudoDB(Context context) {
        this.conexao = new Conexao(context);
    }
    public DesempenhoConteudoDB(Conexao conexao) {
        this.conexao = conexao;
    }

    //COLOCAR A MEDIA E AS NOVAS COLUNAS
    public String insereDesempenhosConteudos(ArrayList<DesempenhoConteudo> listaDesempenhoConteudos, long idDesempenhoQuestionario) {
        String retornoDesempenhoConteudo = "";
        long resultado;
        ContentValues valores;

        this.bancoDados = this.conexao.getWritableDatabase();

        for (int x = 0; x < listaDesempenhoConteudos.size(); x++) {
            DesempenhoConteudo meuDesempenhoConteudo = listaDesempenhoConteudos.get(x);

            valores = new ContentValues();

            valores.put(Conexao.getFkConteudoDesempenhoConteudo(), meuDesempenhoConteudo.getConteudo().getIdConteudo());
            valores.put(Conexao.getQuantidadePerguntas(), meuDesempenhoConteudo.getQuantidadePerguntas());
            valores.put(Conexao.getQuantidadeAcertos(), meuDesempenhoConteudo.getQuantidadeAcertos());
            valores.put(Conexao.getQuantidadeErros(), meuDesempenhoConteudo.getQuantidadeErros());
            valores.put(Conexao.getPontuacaoConteudo(), meuDesempenhoConteudo.getPontuacaoConteudo());
            valores.put(Conexao.getMediaAcertosUltimosQuestionarios(), meuDesempenhoConteudo.getMediaAcertosUltimosQuestionarios());
            valores.put(Conexao.getFkDesempenhoQuestionario(), idDesempenhoQuestionario);

            resultado = this.bancoDados.insert(Conexao.getTabelaDesempenhoConteudo(), null, valores);

            if (resultado == -1) {
                retornoDesempenhoConteudo = "Erro ao inserir os registros na tabela " + Conexao.getTabelaDesempenhoConteudo();
            } else {
                retornoDesempenhoConteudo = "Dados inseridos com sucesso na tabela " + Conexao.getTabelaDesempenhoConteudo();
            }
        }
        this.bancoDados.close();

        return retornoDesempenhoConteudo;
    }
    //COLOCAR A MEDIA E AS NOVAS COLUNAS
    public String insereDesempenhoConteudo(DesempenhoConteudo meuDesempenhoConteudo){
        String retornoDesempenhoConteudo = "";
        long resultado;
        ContentValues valores;

        this.bancoDados = this.conexao.getWritableDatabase();

        valores = new ContentValues();

        valores.put(Conexao.getFkDesempenhoQuestionario(), meuDesempenhoConteudo.getIdDesempenhoConteudo());
        valores.put(Conexao.getFkConteudoDesempenhoConteudo(), meuDesempenhoConteudo.getConteudo().getIdConteudo());
        valores.put(Conexao.getQuantidadePerguntas(), meuDesempenhoConteudo.getQuantidadePerguntas());
        valores.put(Conexao.getQuantidadeAcertos(), meuDesempenhoConteudo.getQuantidadeAcertos());
        valores.put(Conexao.getQuantidadeErros(), meuDesempenhoConteudo.getQuantidadeErros());
        valores.put(Conexao.getPontuacaoConteudo(), meuDesempenhoConteudo.getPontuacaoConteudo());
        valores.put(Conexao.getMediaAcertosUltimosQuestionarios(), meuDesempenhoConteudo.getMediaAcertosUltimosQuestionarios());

        resultado = this.bancoDados.insert(Conexao.getTabelaDesempenhoConteudo(),null,valores);

        this.bancoDados.close();

        if (resultado == -1) {
            retornoDesempenhoConteudo = "Erro ao inserir os registros na tabela " + Conexao.getTabelaDesempenhoConteudo();
        } else {
            retornoDesempenhoConteudo = "Dados inseridos com sucesso na tabela " + Conexao.getTabelaDesempenhoConteudo();
        }
        return retornoDesempenhoConteudo;
    }

    //COLOCAR A MEDIA E AS NOVAS COLUNAS
    public ArrayList<DesempenhoConteudo> buscaDesempenhoConteudo() {
        ArrayList<DesempenhoConteudo> listaDesempenhos = new ArrayList<>();
        String where = Conexao.getTabelaDesempenhoConteudo() +" INNER JOIN "+ Conexao.getTabelaConteudo()
                +" ON "+Conexao.getTabelaDesempenhoConteudo()+"."+Conexao.getFkConteudoDesempenhoConteudo()
                +" = "+Conexao.getTabelaConteudo()+"."+Conexao.getIdConteudo(); //INNER JOIN AQUI ULTRA NECESSÁRIO
        this.bancoDados = this.conexao.getWritableDatabase();
        Cursor cursor = this.bancoDados.query(where,null, null, null, null, null, null);

        while (cursor.moveToNext()) {

            int idDesempenhoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdDesempenhoConteudo()));
            int quantidadePerguntas = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadePerguntas()));
            int quantidadeAcertos = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeAcertos()));
            int quantidadeErros = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeErros()));
            float pontuacaoConteudo = cursor.getFloat(cursor.getColumnIndex(Conexao.getPontuacaoConteudo()));
            float mediaAcertosUltimosQuestionarios = cursor.getFloat(cursor.getColumnIndex(Conexao.getMediaAcertosUltimosQuestionarios()));

            //criando o objeto da classe conteúdo
            int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getFkConteudoDesempenhoConteudo()));
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            int tipoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoConteudo()));

            Conteudo meuConteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);

            DesempenhoConteudo meuDesempenhoConteudo = new DesempenhoConteudo(idDesempenhoConteudo, meuConteudo, quantidadePerguntas, quantidadeAcertos, quantidadeErros, pontuacaoConteudo, mediaAcertosUltimosQuestionarios);

            listaDesempenhos.add(meuDesempenhoConteudo);
        }
        this.bancoDados.close();
        return listaDesempenhos;
    }

    //CONCLUIDO
    public DesempenhoConteudo buscaDesempenhoConteudoComId(int id) {
        DesempenhoConteudo desempenhoConteudo = null;
        this.bancoDados = conexao.getWritableDatabase();
        Cursor cursor;
        //pega todas as colunas das tabelas DesempenhoConteudo e Conteudo
        // em que o fkConteudo(de desempenhoConteudo) = idConteudo(de conteudo)
        //onde o idDesempenhoConteudo = id(parametro)
        String sql = "SELECT * FROM " + Conexao.getTabelaDesempenhoConteudo() + " dc " +
                " INNER JOIN " + Conexao.getTabelaConteudo() + " c " +
                " ON dc." + Conexao.getFkConteudoDesempenhoConteudo() + " = c." + Conexao.getIdConteudo() +
                " WHERE " + Conexao.getIdDesempenhoConteudo() + " = " + id;

        cursor = this.bancoDados.rawQuery(sql, null, null);
        while (cursor.moveToNext()) {
            /*
            DESEMPENHO CONTEUDO
            private int idDesempenhoConteudo; // FEITO
            private Conteudo conteudo;
            private int quantidadePerguntas;
            private int quantidadeAcertos;
            private int quantidadeErros;
            private float pontuacaoConteudo;
            private float mediaAcertosUltimosQuestionarios;
            */

            //criando o conteudo associado
            int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdConteudo()));
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            int tipoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoConteudo()));


            //criando o desempenho conteudo
            int idDesempenhoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdDesempenhoConteudo()));
            Conteudo conteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);
            int quantidadePerguntas = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadePerguntas()));
            int quantidadeAcertos = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeAcertos()));
            int quantidadeErros = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeErros()));
            float pontuacaoConteudo = cursor.getFloat(cursor.getColumnIndex(Conexao.getPontuacaoConteudo()));
            float mediaAcertosUltimosQuestionarios = cursor.getFloat(cursor.getColumnIndex(Conexao.getMediaAcertosUltimosQuestionarios()));

            desempenhoConteudo = new DesempenhoConteudo(idDesempenhoConteudo,conteudo,quantidadePerguntas,quantidadeAcertos,quantidadeErros,pontuacaoConteudo,mediaAcertosUltimosQuestionarios);
        }
        cursor.close();
        this.bancoDados.close();
        return desempenhoConteudo;
    }

    public void deletaDesempenhoConteudo(int idDesempenhoConteudo){
        String where = Conexao.getIdDesempenhoConteudo() + "=" + idDesempenhoConteudo;
        this.bancoDados = this.conexao.getReadableDatabase();
        this.bancoDados.delete(Conexao.getTabelaDesempenhoConteudo(),where,null);
        this.bancoDados.close();
    }

    public ArrayList<DesempenhoConteudo> buscaUltimos3DesempenhosConteudosComConteudo(int idConteudo){
        this.bancoDados = this.conexao.getReadableDatabase();
        ArrayList<DesempenhoConteudo> listaDesempenhos = new ArrayList<>();
        String sql = "SELECT * FROM "+Conexao.getTabelaDesempenhoConteudo() +" INNER JOIN "+ Conexao.getTabelaConteudo()
                +" ON "+Conexao.getTabelaDesempenhoConteudo()+"."+Conexao.getFkConteudoDesempenhoConteudo()
                +" = "+Conexao.getTabelaConteudo()+"."+Conexao.getIdConteudo()
                +" WHERE "+Conexao.getIdConteudo()+"="+idConteudo
                +" ORDER BY "+Conexao.getIdDesempenhoConteudo()+" DESC"
                +" LIMIT 3";

        Cursor cursor = this.bancoDados.rawQuery(sql, null);

        while(cursor.moveToNext()){
            int idDesempenhoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdDesempenhoConteudo()));
            int quantidadePerguntas = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadePerguntas()));
            int quantidadeAcertos = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeAcertos()));
            int quantidadeErros = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeErros()));
            float pontuacaoConteudo = cursor.getFloat(cursor.getColumnIndex(Conexao.getPontuacaoConteudo()));
            float mediaAcertosUltimosQuestionarios = cursor.getFloat(cursor.getColumnIndex(Conexao.getMediaAcertosUltimosQuestionarios()));

            //criando o objeto da classe conteúdo
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            int tipoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoConteudo()));

            Conteudo meuConteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);

            DesempenhoConteudo meuDesempenhoConteudo = new DesempenhoConteudo(idDesempenhoConteudo, meuConteudo, quantidadePerguntas, quantidadeAcertos, quantidadeErros, pontuacaoConteudo, mediaAcertosUltimosQuestionarios);

            listaDesempenhos.add(meuDesempenhoConteudo);
        }

        this.bancoDados.close();
        cursor.close();
        return listaDesempenhos;
    }

    public DesempenhoConteudo buscaDesempenhoConteudoComConteudo(int idConteudo){
        DesempenhoConteudo desempenhoConteudo = null;
        this.bancoDados = this.conexao.getReadableDatabase();
        String sql = "SELECT * FROM "+Conexao.getTabelaDesempenhoConteudo() +" INNER JOIN "+ Conexao.getTabelaConteudo()
                +" ON "+Conexao.getTabelaDesempenhoConteudo()+"."+Conexao.getFkConteudoDesempenhoConteudo()
                +" = "+Conexao.getTabelaConteudo()+"."+Conexao.getIdConteudo()
                +" WHERE "+Conexao.getIdConteudo()+"="+idConteudo
                +" ORDER BY "+Conexao.getIdDesempenhoConteudo()+" DESC"
                +" LIMIT 1";

        Cursor cursor = this.bancoDados.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            int idDesempenhoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdDesempenhoConteudo()));
            int quantidadePerguntas = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadePerguntas()));
            int quantidadeAcertos = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeAcertos()));
            int quantidadeErros = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeErros()));
            float pontuacaoConteudo = cursor.getFloat(cursor.getColumnIndex(Conexao.getPontuacaoConteudo()));
            float mediaAcertosUltimosQuestionarios = cursor.getFloat(cursor.getColumnIndex(Conexao.getMediaAcertosUltimosQuestionarios()));

            //criando o objeto da classe conteúdo
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            int tipoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoConteudo()));

            Conteudo meuConteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);

            desempenhoConteudo = new DesempenhoConteudo(idDesempenhoConteudo, meuConteudo, quantidadePerguntas, quantidadeAcertos, quantidadeErros, pontuacaoConteudo, mediaAcertosUltimosQuestionarios);
        }
        cursor.close();
        this.bancoDados.close();
        return desempenhoConteudo;
    }
}
