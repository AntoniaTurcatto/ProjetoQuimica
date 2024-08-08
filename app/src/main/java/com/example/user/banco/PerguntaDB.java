package com.example.user.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.classesDominio.Pergunta;

import java.util.ArrayList;

public class PerguntaDB {
    private SQLiteDatabase bancoDados;
    private Conexao conexao;

    public PerguntaDB(Context context) {
        this.conexao = new Conexao(context);
    }


    public String inserePergunta(Pergunta umaPergunta){
        String retornoPergunta = "";
        long resultado;
        ContentValues valores;

        this.bancoDados = this.conexao.getWritableDatabase();

        valores = new ContentValues();

        valores.put(Conexao.getEnunciadoPergunta(), umaPergunta.getEnunciado());
        valores.put(Conexao.getOpcaoA(),umaPergunta.getOpcaoA());
        valores.put(Conexao.getOpcaoB(),umaPergunta.getOpcaoB());
        valores.put(Conexao.getOpcaoC(),umaPergunta.getOpcaoC());
        valores.put(Conexao.getOpcaoD(),umaPergunta.getOpcaoD());
        valores.put(Conexao.getOpcaoE(),umaPergunta.getOpcaoE());
        valores.put(Conexao.getImagemPergunta(), umaPergunta.getImagem());
        valores.put(Conexao.getFkConteudoPergunta(),umaPergunta.getConteudo().getIdConteudo());
        valores.put(Conexao.getTestePrevio(),umaPergunta.getTestePrevio());
        valores.put(Conexao.getAlternativaCorreta(),String.valueOf(umaPergunta.getAlternativaCorreta()));
        valores.put(Conexao.getNivelDificuldade(), umaPergunta.getNivelDificuldade());
        valores.put(Conexao.getQuestaoVestibular(), umaPergunta.getQuestaoVestibular());

        resultado = this.bancoDados.insert(Conexao.getTabelaPergunta(),null,valores);
        this.bancoDados.close();

        if (resultado == -1) {
            retornoPergunta = "Erro ao inserir os registros na tabela " + Conexao.getTabelaPergunta();
        } else {
            retornoPergunta = "Dados inseridos com sucesso na tabela " + Conexao.getTabelaPergunta();
        }
        return retornoPergunta;
    }


    public ArrayList<Pergunta> buscaPerguntasPorConteudos(ArrayList<Conteudo> listaConteudos, int quantidade) {
        ArrayList<Pergunta> listaPerguntas = new ArrayList<>();

        this.bancoDados = this.conexao.getWritableDatabase();

        for (int x = 0; x < listaConteudos.size(); x++) {
            Conteudo meuConteudo = listaConteudos.get(x);

            //QUANDO CONSEGUIR O HAVING TEREMOS QUE ALTERAR AQUI A CONSULTA SQL
            String mensagem = Conexao.getTabelaPergunta() + " INNER JOIN " + Conexao.getTabelaConteudo()
                    + " ON " + Conexao.getTabelaPergunta() + "." + Conexao.getFkConteudoPergunta()
                    + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo();

            String selecao = Conexao.getNomeConteudo() + "= '" + meuConteudo.getNomeConteudo() + "'";
            Cursor cursor = this.bancoDados.query(mensagem, null, selecao, null, null, null, "RANDOM() LIMIT " + quantidade);

            while (cursor.moveToNext()) {

                int idQuiz = cursor.getInt(cursor.getColumnIndex(Conexao.getIdPergunta()));
                String enunciado = cursor.getString(cursor.getColumnIndex(Conexao.getEnunciadoPergunta()));
                String opcaoA = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoA()));
                String opcaoB = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoB()));
                String opcaoC = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoC()));
                String opcaoD = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoD()));
                String opcaoE = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoE()));
                int tipoTestePrevio = cursor.getInt(cursor.getColumnIndex(Conexao.getTestePrevio()));
                char alternativaCorreta = cursor.getString(cursor.getColumnIndex(Conexao.getAlternativaCorreta())).charAt(0);
                byte[] imagem = cursor.getBlob(cursor.getColumnIndex(Conexao.getImagemPergunta()));
                int nivelDificuldade = cursor.getInt(cursor.getColumnIndex(Conexao.getNivelDificuldade()));
                int questaoVestibular = cursor.getInt(cursor.getColumnIndex(Conexao.getQuestaoVestibular()));


                Pergunta umaPergunta = new Pergunta(idQuiz, enunciado, opcaoA, opcaoB, opcaoC, opcaoD, opcaoE, imagem, tipoTestePrevio, meuConteudo, alternativaCorreta, nivelDificuldade, questaoVestibular);

                listaPerguntas.add(umaPergunta);
            }
        }
        this.bancoDados.close();
        return listaPerguntas;
    }


    public ArrayList<Pergunta> buscaPerguntasPorConteudosUnionAll(ArrayList<NivelConteudo> listaNivelConteudos, int[][] quantidadesPerguntas) {
        ArrayList<Pergunta> listaPerguntas = new ArrayList<>();

        this.bancoDados = this.conexao.getWritableDatabase();

        // criando as variáveis auxiliares
        String sqlWith = "WITH ";
        String sqlUnion = "";

        // montando a query sql
        for (int x = 0; x < listaNivelConteudos.size(); x++) {
            String sqlWithIntermediaria = Conexao.getTabelaPergunta() + x + "1 AS (SELECT * FROM " + Conexao.getTabelaPergunta()
                    + " INNER JOIN " + Conexao.getTabelaConteudo() + " ON " + Conexao.getTabelaPergunta() + "." + Conexao.getFkConteudoPergunta() + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo()
                    + " WHERE " + Conexao.getFkConteudoPergunta() + " = " + listaNivelConteudos.get(x).getConteudo().getIdConteudo()
                    + " AND " + Conexao.getTestePrevio() + " = 0"
                    + " AND " + Conexao.getNivelDificuldade()
                    + " = 1 ORDER BY RANDOM() LIMIT " + quantidadesPerguntas[x][1] + "),"

                    + Conexao.getTabelaPergunta() + x + "2 AS (SELECT * FROM " + Conexao.getTabelaPergunta()
                    + " INNER JOIN " + Conexao.getTabelaConteudo() + " ON " + Conexao.getTabelaPergunta() + "." + Conexao.getFkConteudoPergunta() + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo()
                    + " WHERE " + Conexao.getFkConteudoPergunta() + " = " + listaNivelConteudos.get(x).getConteudo().getIdConteudo()
                    + " AND " + Conexao.getTestePrevio() + " = 0"
                    + " AND " + Conexao.getNivelDificuldade()
                    + " = 2 ORDER BY RANDOM() LIMIT " + quantidadesPerguntas[x][2] + "),"

                    + Conexao.getTabelaPergunta() + x + "3 AS (SELECT * FROM " + Conexao.getTabelaPergunta()
                    + " INNER JOIN " + Conexao.getTabelaConteudo() + " ON " + Conexao.getTabelaPergunta() + "." + Conexao.getFkConteudoPergunta() + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo()
                    + " WHERE " + Conexao.getFkConteudoPergunta() + " = " + listaNivelConteudos.get(x).getConteudo().getIdConteudo()
                    + " AND " + Conexao.getTestePrevio() + " = 0"
                    + " AND " + Conexao.getNivelDificuldade()
                    + " = 3 ORDER BY RANDOM() LIMIT " + quantidadesPerguntas[x][3] + ") ";

            sqlWith = sqlWith + sqlWithIntermediaria;

            String sqlUnionIntermediaria = "SELECT * FROM " + Conexao.getTabelaPergunta() + x + "1 UNION ALL "
                    + "SELECT * FROM " + Conexao.getTabelaPergunta() + x + "2 UNION ALL "
                    + "SELECT * FROM " + Conexao.getTabelaPergunta() + x + "3 ";
            sqlUnion = sqlUnion + sqlUnionIntermediaria;

            // para todos, tem que adicionar essas questões. Exceção é no ultimo
            if (x < (listaNivelConteudos.size() - 1)) {
                sqlWith = sqlWith + ", ";
                sqlUnion = sqlUnion + "UNION ALL ";
            }
        }

        // concatenando
        String sql = sqlWith + sqlUnion;

        Log.d("Teste", "Query SQL: " + sql);

        Cursor cursor = this.bancoDados.rawQuery(sql, null, null);

        while (cursor.moveToNext()) {

            int idQuiz = cursor.getInt(cursor.getColumnIndex(Conexao.getIdPergunta()));
            String enunciado = cursor.getString(cursor.getColumnIndex(Conexao.getEnunciadoPergunta()));
            String opcaoA = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoA()));
            String opcaoB = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoB()));
            String opcaoC = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoC()));
            String opcaoD = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoD()));
            String opcaoE = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoE()));
            int tipoTestePrevio = cursor.getInt(cursor.getColumnIndex(Conexao.getTestePrevio()));
            char alternativaCorreta = cursor.getString(cursor.getColumnIndex(Conexao.getAlternativaCorreta())).charAt(0);
            byte[] imagem = cursor.getBlob(cursor.getColumnIndex(Conexao.getImagemPergunta()));
            int nivelDificuldade = cursor.getInt(cursor.getColumnIndex(Conexao.getNivelDificuldade()));
            int questaoVestibular = cursor.getInt(cursor.getColumnIndex(Conexao.getQuestaoVestibular()));

            //criando o objeto da classe conteúdo
            int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getFkConteudoPergunta()));
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            int tipoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoConteudo()));

            Conteudo meuConteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);

            Pergunta umaPergunta = new Pergunta(idQuiz, enunciado, opcaoA, opcaoB, opcaoC, opcaoD, opcaoE, imagem, tipoTestePrevio, meuConteudo, alternativaCorreta, nivelDificuldade, questaoVestibular);

            listaPerguntas.add(umaPergunta);
        }

        this.bancoDados.close();
        return listaPerguntas;

    }

    public ArrayList<Pergunta> buscaPerguntasPorConteudosUnionAllComTestePrevio(ArrayList<NivelConteudo> listaNivelConteudos, int[][] quantidadesPerguntas) {
        ArrayList<Pergunta> listaPerguntas = new ArrayList<>();

        this.bancoDados = this.conexao.getWritableDatabase();

        // criando as variáveis auxiliares
        String sqlWith = "WITH ";
        String sqlUnion = "";

        // montando a query sql
        for (int x = 0; x < listaNivelConteudos.size(); x++) {
            String sqlWithIntermediaria = Conexao.getTabelaPergunta() + x + "1 AS (SELECT * FROM " + Conexao.getTabelaPergunta()
                    + " INNER JOIN " + Conexao.getTabelaConteudo() + " ON " + Conexao.getTabelaPergunta() + "." + Conexao.getFkConteudoPergunta() + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo()
                    + " WHERE " + Conexao.getFkConteudoPergunta() + " = " + listaNivelConteudos.get(x).getConteudo().getIdConteudo() + " AND " + Conexao.getNivelDificuldade()
                    + " = 1 ORDER BY RANDOM() LIMIT " + quantidadesPerguntas[x][1] + "),"
                    + Conexao.getTabelaPergunta() + x + "2 AS (SELECT * FROM " + Conexao.getTabelaPergunta()
                    + " INNER JOIN " + Conexao.getTabelaConteudo() + " ON " + Conexao.getTabelaPergunta() + "." + Conexao.getFkConteudoPergunta() + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo()
                    + " WHERE " + Conexao.getFkConteudoPergunta() + " = " + listaNivelConteudos.get(x).getConteudo().getIdConteudo() + " AND " + Conexao.getNivelDificuldade()
                    + " = 2 ORDER BY RANDOM() LIMIT " + quantidadesPerguntas[x][2] + "),"
                    + Conexao.getTabelaPergunta() + x + "3 AS (SELECT * FROM " + Conexao.getTabelaPergunta()
                    + " INNER JOIN " + Conexao.getTabelaConteudo() + " ON " + Conexao.getTabelaPergunta() + "." + Conexao.getFkConteudoPergunta() + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo()
                    + " WHERE " + Conexao.getFkConteudoPergunta() + " = " + listaNivelConteudos.get(x).getConteudo().getIdConteudo() + " AND " + Conexao.getNivelDificuldade()
                    + " = 3 ORDER BY RANDOM() LIMIT " + quantidadesPerguntas[x][3] + ") ";

            sqlWith = sqlWith + sqlWithIntermediaria;

            String sqlUnionIntermediaria = "SELECT * FROM " + Conexao.getTabelaPergunta() + x + "1 UNION ALL "
                    + "SELECT * FROM " + Conexao.getTabelaPergunta() + x + "2 UNION ALL "
                    + "SELECT * FROM " + Conexao.getTabelaPergunta() + x + "3 ";
            sqlUnion = sqlUnion + sqlUnionIntermediaria;

            // para todos, tem que adicionar essas questões. Exceção é no ultimo
            if (x < (listaNivelConteudos.size() - 1)) {
                sqlWith = sqlWith + ", ";
                sqlUnion = sqlUnion + "UNION ALL ";
            }
        }

        // concatenando
        String sql = sqlWith + sqlUnion;

        Log.d("Teste", "Query SQL: " + sql);

        Cursor cursor = this.bancoDados.rawQuery(sql, null, null);

        while (cursor.moveToNext()) {

            int idQuiz = cursor.getInt(cursor.getColumnIndex(Conexao.getIdPergunta()));
            String enunciado = cursor.getString(cursor.getColumnIndex(Conexao.getEnunciadoPergunta()));
            String opcaoA = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoA()));
            String opcaoB = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoB()));
            String opcaoC = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoC()));
            String opcaoD = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoD()));
            String opcaoE = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoE()));
            int tipoTestePrevio = cursor.getInt(cursor.getColumnIndex(Conexao.getTestePrevio()));
            char alternativaCorreta = cursor.getString(cursor.getColumnIndex(Conexao.getAlternativaCorreta())).charAt(0);
            byte[] imagem = cursor.getBlob(cursor.getColumnIndex(Conexao.getImagemPergunta()));
            int nivelDificuldade = cursor.getInt(cursor.getColumnIndex(Conexao.getNivelDificuldade()));
            int questaoVestibular = cursor.getInt(cursor.getColumnIndex(Conexao.getQuestaoVestibular()));

            //criando o objeto da classe conteúdo
            int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getFkConteudoPergunta()));
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            int tipoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoConteudo()));

            Conteudo meuConteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);

            Pergunta umaPergunta = new Pergunta(idQuiz, enunciado, opcaoA, opcaoB, opcaoC, opcaoD, opcaoE, imagem, tipoTestePrevio, meuConteudo, alternativaCorreta, nivelDificuldade, questaoVestibular);

            listaPerguntas.add(umaPergunta);
        }

        this.bancoDados.close();
        return listaPerguntas;

    }


    //apenas para teste - depois devera receber o objeto da classe conteudo
    public ArrayList<Pergunta> buscaPerguntasPorConteudo(Conteudo meuConteudo, int quantidade) {
        ArrayList<Pergunta> listaPerguntas = new ArrayList<>();

        this.bancoDados = this.conexao.getWritableDatabase();
        String mensagem = Conexao.getTabelaPergunta() + " INNER JOIN " + Conexao.getTabelaConteudo()
                + " ON " + Conexao.getTabelaPergunta() + "." + Conexao.getFkConteudoPergunta()
                + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo();

        String where = Conexao.getNomeConteudo() + "= '" + meuConteudo.getNomeConteudo() + "'";
        Cursor cursor = this.bancoDados.query(mensagem, null, where, null, null, null, "RANDOM() LIMIT " + quantidade);

        while (cursor.moveToNext()) {

            int idQuiz = cursor.getInt(cursor.getColumnIndex(Conexao.getIdPergunta()));
            String enunciado = cursor.getString(cursor.getColumnIndex(Conexao.getEnunciadoPergunta()));
            String opcaoA = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoA()));
            String opcaoB = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoB()));
            String opcaoC = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoC()));
            String opcaoD = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoD()));
            String opcaoE = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoE()));
            int tipoTestePrevio = cursor.getInt(cursor.getColumnIndex(Conexao.getTestePrevio()));
            char alternativaCorreta = cursor.getString(cursor.getColumnIndex(Conexao.getAlternativaCorreta())).charAt(0);
            byte[] imagem = cursor.getBlob(cursor.getColumnIndex(Conexao.getImagemPergunta()));
            int nivelDificuldade = cursor.getInt(cursor.getColumnIndex(Conexao.getNivelDificuldade()));
            int questaoVestibular = cursor.getInt(cursor.getColumnIndex(Conexao.getQuestaoVestibular()));

            Pergunta umaPergunta = new Pergunta(idQuiz, enunciado, opcaoA, opcaoB, opcaoC, opcaoD, opcaoE, imagem, tipoTestePrevio, meuConteudo, alternativaCorreta, nivelDificuldade, questaoVestibular);
            this.bancoDados.close();

            listaPerguntas.add(umaPergunta);
        }
        return listaPerguntas;
    }


    public ArrayList<Pergunta> buscaPergunta(Context context) {

        ArrayList<Pergunta> listaPerguntas = new ArrayList<>();
        this.bancoDados = this.conexao.getWritableDatabase();
        Cursor cursor = this.bancoDados.query(Conexao.getTabelaPergunta(), null, null, null, null, null, null);

        while (cursor.moveToNext()) {

            int idQuiz = cursor.getInt(cursor.getColumnIndex(Conexao.getIdPergunta()));
            String enunciado = cursor.getString(cursor.getColumnIndex(Conexao.getEnunciadoPergunta()));
            String opcaoA = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoA()));
            String opcaoB = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoB()));
            String opcaoC = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoC()));
            String opcaoD = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoD()));
            String opcaoE = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoE()));
            byte[] imagem = cursor.getBlob(cursor.getColumnIndex(Conexao.getImagemPergunta()));
            int testePrevio = cursor.getInt(cursor.getColumnIndex(Conexao.getTestePrevio()));
            char alternativaCorreta = cursor.getString(cursor.getColumnIndex(Conexao.getAlternativaCorreta())).charAt(0);
            int nivelDificuldade = cursor.getInt(cursor.getColumnIndex(Conexao.getNivelDificuldade()));
            int questaoVestibular = cursor.getInt(cursor.getColumnIndex(Conexao.getQuestaoVestibular()));

            //criando o objeto da classe conteúdo
            int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getFkConteudoPergunta()));

            //foi comentado pois esse a tabela não possui essa coluna
            // String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            //int tipoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoConteudo()));

            //é necessário instanciar o conteudo do id encontrado
            ConteudoDB conteudoDB = new ConteudoDB(context);
            Conteudo conteudo = conteudoDB.carregaConteudoById(idConteudo);

            //Conteudo meuConteudo = new Conteudo(idConteudo, conteudo.getNomeConteudo(), conteudo.getTipoConteudo());

            Pergunta umaPergunta = new Pergunta(idQuiz, enunciado, opcaoA, opcaoB, opcaoC, opcaoD, opcaoE, imagem, testePrevio, conteudo, alternativaCorreta, nivelDificuldade, questaoVestibular);
            this.bancoDados.close();

            listaPerguntas.add(umaPergunta);

        }
        return listaPerguntas;
    }


    public Pergunta carregaPerguntaById(int id){
        Cursor cursor;
        Pergunta minhaPergunta = null;
        String consulta = Conexao.getTabelaPergunta() + " INNER JOIN " + Conexao.getTabelaConteudo()
                + " ON " + Conexao.getTabelaPergunta() + "." + Conexao.getFkConteudoPergunta()
                + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo();
        String[] campos =  {Conexao.getIdPergunta(), Conexao.getEnunciadoPergunta(), Conexao.getOpcaoA(), Conexao.getOpcaoB(), Conexao.getOpcaoC(), Conexao.getOpcaoD(), Conexao.getOpcaoE(), Conexao.getImagemPergunta(), Conexao.getTestePrevio(), Conexao.getFkConteudoPergunta(), conexao.getAlternativaCorreta(), conexao.getNivelDificuldade()};
        String where = Conexao.getIdPergunta() + "=" + id;
        this.bancoDados = this.conexao.getReadableDatabase();
        cursor = this.bancoDados.query(consulta,campos,where, null, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
            // obtendo informação por informação
            int idQuiz = cursor.getInt(cursor.getColumnIndex(Conexao.getIdPergunta()));
            String enunciado = cursor.getString(cursor.getColumnIndex(Conexao.getEnunciadoPergunta()));
            String opcaoA = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoA()));
            String opcaoB = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoB()));
            String opcaoC = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoC()));
            String opcaoD = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoD()));
            String opcaoE = cursor.getString(cursor.getColumnIndex(Conexao.getOpcaoE()));
            byte[] imagem = cursor.getBlob(cursor.getColumnIndex(Conexao.getImagemPergunta()));
            int testePrevio = cursor.getInt(cursor.getColumnIndex(Conexao.getTestePrevio()));
            char alternativaCorreta = cursor.getString(cursor.getColumnIndex(Conexao.getAlternativaCorreta())).charAt(0);
            int nivelDificuldade = cursor.getInt(cursor.getColumnIndex(Conexao.getNivelDificuldade()));
            int questaoVestibular = cursor.getInt(cursor.getColumnIndex(Conexao.getQuestaoVestibular()));

            //criando o objeto da classe conteúdo
            int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getFkConteudoPergunta()));
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            int tipoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getTipoConteudo()));

            Conteudo conteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);

            // criando o objeto da classe
            minhaPergunta = new Pergunta(idQuiz, enunciado, opcaoA, opcaoB, opcaoC, opcaoD, opcaoE, imagem, testePrevio, conteudo, alternativaCorreta, nivelDificuldade, questaoVestibular);
        }
        this.bancoDados.close();
        return minhaPergunta;
    }


    public void alteraPergunta(Pergunta minhaPergunta){
        ContentValues valores;
        String where;

        this.bancoDados = this.conexao.getWritableDatabase();

        where = Conexao.getIdPergunta()+ "=" + minhaPergunta.getIdQuiz();

        valores = new ContentValues();
        valores.put(Conexao.getEnunciadoPergunta(),minhaPergunta.getEnunciado());
        valores.put(Conexao.getOpcaoA(), minhaPergunta.getOpcaoA());
        valores.put(Conexao.getOpcaoB(), minhaPergunta.getOpcaoB());
        valores.put(Conexao.getOpcaoC(), minhaPergunta.getOpcaoC());
        valores.put(Conexao.getOpcaoD(), minhaPergunta.getOpcaoD());
        valores.put(Conexao.getOpcaoE(), minhaPergunta.getOpcaoE());
        valores.put(Conexao.getImagemPergunta(), minhaPergunta.getImagem());
        valores.put(Conexao.getTestePrevio(), minhaPergunta.getTestePrevio());
        valores.put(Conexao.getAlternativaCorreta(), String.valueOf(minhaPergunta.getAlternativaCorreta()));
        valores.put(Conexao.getFkConteudoPergunta(), minhaPergunta.getConteudo().getIdConteudo());
        valores.put(Conexao.getNivelDificuldade(), minhaPergunta.getNivelDificuldade());
        valores.put(Conexao.getQuestaoVestibular(), minhaPergunta.getQuestaoVestibular());

        this.bancoDados.update(Conexao.getTabelaPergunta(),valores,where,null);
        this.bancoDados.close();
    }


    public void deletaPergunta(int idPergunta){
        String where = Conexao.getIdPergunta() + "=" + idPergunta;
        this.bancoDados = this.conexao.getReadableDatabase();
        this.bancoDados.delete(Conexao.getTabelaPergunta(),where,null);
        this.bancoDados.close();
    }
}
