package com.example.user.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public String insereDesempenhoConteudo(DesempenhoConteudo meuDesempenhoConteudo){
        String retornoDesempenhoConteudo = "";
        long resultado;
        ContentValues valores;

        this.bancoDados = this.conexao.getWritableDatabase();

        valores = new ContentValues();

        valores.put(Conexao.getFkConteudoDesempenhoConteudo(), meuDesempenhoConteudo.getConteudo().getIdConteudo());
        valores.put(Conexao.getQuantidadePerguntas(), meuDesempenhoConteudo.getQuantidadePerguntas());
        valores.put(Conexao.getQuantidadeAcertos(), meuDesempenhoConteudo.getQuantidadeAcertos());
        valores.put(Conexao.getQuantidadeErros(), meuDesempenhoConteudo.getQuantidadeErros());
        valores.put(Conexao.getPontuacaoConteudo(), meuDesempenhoConteudo.getPontuacaoConteudo());

        resultado = this.bancoDados.insert(Conexao.getTabelaDesempenhoConteudo(),null,valores);

        this.bancoDados.close();

        if (resultado == -1) {
            retornoDesempenhoConteudo = "Erro ao inserir os registros na tabela " + Conexao.getTabelaDesempenhoConteudo();
        } else {
            retornoDesempenhoConteudo = "Dados inseridos com sucesso na tabela " + Conexao.getTabelaDesempenhoConteudo();
        }
        return retornoDesempenhoConteudo;
    }


    public ArrayList<DesempenhoConteudo> buscaDesempenhoConteudo() {
        ArrayList<DesempenhoConteudo> listaDesempenhos = new ArrayList<>();
        String where = Conexao.getTabelaDesempenhoConteudo() +" INNER JOIN "+ Conexao.getTabelaConteudo(); //INNER JOIN AQUI ULTRA NECESSÁRIO
        this.bancoDados = this.conexao.getWritableDatabase();
        Cursor cursor = this.bancoDados.query(where,null, null, null, null, null, null);

        while (cursor.moveToNext()) {

            int idDesempenhoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdDesempenhoConteudo()));
            int quantidadePerguntas = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadePerguntas()));
            int quantidadeAcertos = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeAcertos()));
            int quantidadeErros = cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeErros()));
            float pontuacaoConteudo = cursor.getFloat(cursor.getColumnIndex(Conexao.getPontuacaoConteudo()));

            //criando o objeto da classe conteúdo
            int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getFkConteudoDesempenhoConteudo()));
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            int tipoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoConteudo()));

            Conteudo meuConteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);

            DesempenhoConteudo meuDesempenhoConteudo = new DesempenhoConteudo(idDesempenhoConteudo, meuConteudo, quantidadePerguntas, quantidadeAcertos, quantidadeErros, pontuacaoConteudo);
            this.bancoDados.close();

            listaDesempenhos.add(meuDesempenhoConteudo);
        }
        return listaDesempenhos;
    }


    public void deletaDesempenhoConteudo(int idDesempenhoConteudo){
        String where = Conexao.getIdDesempenhoConteudo() + "=" + idDesempenhoConteudo;
        this.bancoDados = this.conexao.getReadableDatabase();
        this.bancoDados.delete(Conexao.getTabelaDesempenhoConteudo(),where,null);
        this.bancoDados.close();
    }
}
