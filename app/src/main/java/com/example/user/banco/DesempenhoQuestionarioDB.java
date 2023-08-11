package com.example.user.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.user.classesDominio.DesempenhoQuestionario;
import com.example.user.classesDominio.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DesempenhoQuestionarioDB {
    private SQLiteDatabase bancoDados;
    private Conexao conexao;

    public DesempenhoQuestionarioDB(Context context) {
        this.conexao = new Conexao(context);
    }


    public String insereDesempenhoQuestionario(DesempenhoQuestionario meuDesempenhoQuestionario){
        String retornoDesempenhoQuestionario = "";
        long resultado;
        ContentValues valores;

        this.bancoDados = this.conexao.getWritableDatabase();

        valores = new ContentValues();

        valores.put(Conexao.getDataQuestionario(), meuDesempenhoQuestionario.getData().toString());
        valores.put(Conexao.getPontuacaoQuestionario(), meuDesempenhoQuestionario.getPontuacaoFinal());
        valores.put(Conexao.getFkUsuarioQuestionario(), meuDesempenhoQuestionario.getMeuUsuario().getIdUsuario());
        valores.put(Conexao.getTipoDesempenho(), meuDesempenhoQuestionario.getTipoDesempenho());
        resultado = this.bancoDados.insert(Conexao.getTabelaDesempenhoQuestionario(),null,valores);

        if (resultado == -1) {
            retornoDesempenhoQuestionario = "Erro ao inserir os registros na tabela " + Conexao.getTabelaDesempenhoQuestionario();
        } else {
            retornoDesempenhoQuestionario = "Dados inseridos com sucesso na tabela " + Conexao.getTabelaDesempenhoQuestionario();
            // inserindo os desempenhos conteudos desse questionário
            DesempenhoConteudoDB desempenhoConteudoDB = new DesempenhoConteudoDB(conexao);
            desempenhoConteudoDB.insereDesempenhosConteudos(meuDesempenhoQuestionario.getListaDesempenhoConteudos(), resultado);
        }

        this.bancoDados.close();

        return retornoDesempenhoQuestionario;
    }


    //METODO UTILIZADO PARA OBTER O DESEMPENHO DO QUESTIONARIO
    //PROVAVELMENTE IREMOS FILTRAR (OU NAO) A ORIGEM DO DESEMPENHO QUESTIONARIO (QUIZ OU DIAGNOSTICO)

    public ArrayList<DesempenhoQuestionario> buscaDesempenhoQuestionario(Usuario meuUsuario) {
        ArrayList<DesempenhoQuestionario> listaDesempenhos = new ArrayList<>();
        String consulta = Conexao.getTabelaDesempenhoQuestionario() + " INNER JOIN " + Conexao.getTabelaUsuario()
                + " ON " + Conexao.getTabelaDesempenhoQuestionario() + "." + Conexao.getFkUsuarioQuestionario()
                + " = " + Conexao.getTabelaUsuario() + "." + Conexao.getIdUsuario();
        String where = Conexao.getFkUsuarioQuestionario() + "=" + meuUsuario.getIdUsuario();
        this.bancoDados = this.conexao.getWritableDatabase();
        Cursor cursor = this.bancoDados.query(consulta,null,where, null, null, null, null, null);

        while (cursor.moveToNext()) {

            int idDesempenhoQuestionario = cursor.getInt(cursor.getColumnIndex(Conexao.getIdDesempenhoQuestionario()));
            int tipoQuiz = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoDesempenho()));
            String dataString = cursor.getString(cursor.getColumnIndex(Conexao.getDataQuestionario()));
            // pegando a string e passando para o formato Date
            Date data = null;
            try {
                data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataString);
            } catch(ParseException parse) {

            }
            float pontuacaoFinal = cursor.getFloat(cursor.getColumnIndex(Conexao.getPontuacaoQuestionario()));

            DesempenhoQuestionario meuDesempenhoQuestionario = new DesempenhoQuestionario(idDesempenhoQuestionario, data, pontuacaoFinal, tipoQuiz);
            this.bancoDados.close();

            listaDesempenhos.add(meuDesempenhoQuestionario);
        }
        return listaDesempenhos;
    }


    public void deletaDesempenhoQuestionario(int idDesempenhoQuestionario){
        String where = Conexao.getIdDesempenhoQuestionario() + "=" + idDesempenhoQuestionario;
        bancoDados = conexao.getReadableDatabase();
        bancoDados.delete(Conexao.getTabelaDesempenhoQuestionario(),where,null);
        bancoDados.close();
    }
}
