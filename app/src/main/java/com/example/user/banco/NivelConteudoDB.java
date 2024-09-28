package com.example.user.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.util.Log;

import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.DesempenhoConteudo;
import com.example.user.classesDominio.DesempenhoQuestionario;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.classesDominio.Usuario;
import com.example.user.componente.NivelConteudoEnum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NivelConteudoDB {
    private SQLiteDatabase bancoDados;
    private Conexao conexao;
    //NivelConteudo meuNivelConteudo;

    public NivelConteudoDB(Context context) {
        this.conexao = new Conexao(context);
    }

    public NivelConteudoDB(Conexao conexao){
        this.conexao = conexao;
    }

    //A lista que é carregada ficaria nula  e geraria o problema
    public ArrayList<NivelConteudo> carregaListaCompleta(Usuario meuUsuario, int tipoConteudo) {
        ArrayList<NivelConteudo> listaNiveisConteudos = new ArrayList<>();

        this.bancoDados = this.conexao.getWritableDatabase();

        String sql = "SELECT * FROM "+Conexao.getTabelaNivelConteudo() + " INNER JOIN " + Conexao.getTabelaConteudo()
                + " ON " + Conexao.getTabelaNivelConteudo() + "." + Conexao.getFkConteudoNivel()
                + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo()
                +" INNER JOIN "+Conexao.getTabelaDesempenhoConteudo()
                +" ON "+Conexao.getTabelaNivelConteudo()+"."+Conexao.getFkUltimoDesempenhoConteudo()
                +" = "+Conexao.getTabelaDesempenhoConteudo()+"."+Conexao.getIdDesempenhoConteudo()
                +" WHERE "+Conexao.getTipoConteudo() + "=" + tipoConteudo
                + " AND " + Conexao.getFkUsuarioNivel() + "=" + meuUsuario.getIdUsuario();

        //String where = Conexao.getTipoConteudo() + "=" + tipoConteudo + " and " + Conexao.getFkUsuarioNivel() + "=" + meuUsuario.getIdUsuario();

        Cursor cursor = this.bancoDados.rawQuery(sql,null,null);

        while (cursor.moveToNext()) {
            //CONTEUDO
            //Conteudo(int idConteudo, String nomeConteudo, int tipoConteudo)
            Conteudo meuConteudo = new Conteudo(
                    cursor.getInt(cursor.getColumnIndex(Conexao.getIdConteudo()))
                    ,cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()))
                    ,tipoConteudo
            );

            //DESENMPENHO CONTEUDO

            DesempenhoConteudo desempenhoConteudo = new DesempenhoConteudo(
                    cursor.getInt(cursor.getColumnIndex(Conexao.getIdDesempenhoConteudo()))//idDesempenhoConteudo
                    ,meuConteudo
                    ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadePerguntas()))
                    ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeAcertos()))
                    ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeErros()))
                    ,cursor.getFloat(cursor.getColumnIndex(Conexao.getPontuacaoConteudo()))
                    ,cursor.getFloat(cursor.getColumnIndex(Conexao.getMediaAcertosUltimosQuestionarios()))
            );

            int idNivelConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdNivelConteudo()));
            int nivelBanco = cursor.getInt(cursor.getColumnIndex(Conexao.getNIVEL()));
            int tentativas = cursor.getInt(cursor.getColumnIndex(Conexao.getTENTATIVAS()));
            int vidas = cursor.getInt(cursor.getColumnIndex(Conexao.getVIDAS()));
            //int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getFkConteudoNivel()));
            //String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));
            int subiuOuDesceuNivel = cursor.getInt(cursor.getColumnIndex(Conexao.getSubiuOuDesceuNivel()));
            String dataStringUltimoTeste = cursor.getString(cursor.getColumnIndex(Conexao.getDataUltimoTeste()));
            String dataStringAtualizacaoNivel = cursor.getString(cursor.getColumnIndex(Conexao.getDataAtualizacaoNivel()));

            Date dataAtualizacaoNivel = null;
            Date dataUltimoTeste = null;
            try {
                if (!dataAtualizacaoNivel.equals("")){
                    dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse(dataStringAtualizacaoNivel);
                } else {
                    dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
                }

                if (!dataUltimoTeste.equals("")){
                    dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse(dataStringUltimoTeste);
                } else {
                    dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
                }

            } catch(ParseException parse) {
                parse.printStackTrace();
            }

            if (vidas == 0){
                vidas = 5;
            }
            NivelConteudoEnum nivel = null;

            if (nivelBanco == 1) {
                nivel = NivelConteudoEnum.COBRE;
            } else if (nivelBanco == 2) {
                nivel = NivelConteudoEnum.BRONZE;
            } else if (nivelBanco == 3) {
                nivel = NivelConteudoEnum.PRATA;
            } else if (nivelBanco == 4) {
                nivel = NivelConteudoEnum.OURO;
            } else if (nivelBanco == 5) {
                nivel = NivelConteudoEnum.DIAMANTE;
            }


            //Conteudo meuConteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);
            NivelConteudo meuNivelConteudo = new NivelConteudo(idNivelConteudo, nivel, subiuOuDesceuNivel, dataUltimoTeste, dataAtualizacaoNivel, desempenhoConteudo,meuUsuario, meuConteudo, tentativas, vidas);


            listaNiveisConteudos.add(meuNivelConteudo);
        }
        this.bancoDados.close();
        return listaNiveisConteudos;
    } //ATUALIZADO COM DATAS E SUBIUDESCEU e id do ultimo DesempenhoConteudo

    public ArrayList<NivelConteudo> buscaConteudosComNivel(ArrayList<Conteudo> listaConteudos, Usuario meuUsuario) {
        ArrayList<NivelConteudo> listaNiveisConteudos = new ArrayList<>();
        this.bancoDados = this.conexao.getWritableDatabase();

        for (int x = 0; x < listaConteudos.size(); x++) {
            NivelConteudo nivelConteudo;
            Conteudo meuConteudo = listaConteudos.get(x);
            Cursor cursor = null;

            try {
                String sql = "SELECT * FROM "+Conexao.getTabelaNivelConteudo()+" nc left JOIN "+Conexao.getTabelaDesempenhoConteudo()+" dc"
                        +" ON nc."+Conexao.getFkUltimoDesempenhoConteudo()+" = dc."+Conexao.getIdDesempenhoConteudo()
                        +" WHERE nc."+Conexao.getFkUsuarioNivel()+" = ?"
                        +" AND nc."+Conexao.getFkConteudoNivel()+" = ?";
                Log.d("SQL", "ID Usuário: " + meuUsuario.getIdUsuario() + ", ID Conteúdo: " + meuConteudo.getIdConteudo());
                cursor = this.bancoDados.rawQuery(sql, new String[]{String.valueOf(meuUsuario.getIdUsuario()), String.valueOf(meuConteudo.getIdConteudo())});

                if (cursor != null && cursor.moveToFirst()) {
                                        //nivel conteudo

                    int idNivelConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdNivelConteudo()));
                    int nivelBanco = cursor.getInt(cursor.getColumnIndex(Conexao.getNIVEL()));
                    int tentativas = cursor.getInt(cursor.getColumnIndex(Conexao.getTENTATIVAS()));
                    int vidas = cursor.getInt(cursor.getColumnIndex(Conexao.getVIDAS()));
                    int subiuOuDesceuNivel = cursor.getInt(cursor.getColumnIndex(Conexao.getSubiuOuDesceuNivel()));
                    String dataStringUltimoTeste = cursor.getString(cursor.getColumnIndex(Conexao.getDataUltimoTeste()));
                    String dataStringAtualizacaoNivel = cursor.getString(cursor.getColumnIndex(Conexao.getDataAtualizacaoNivel()));


                    Date dataAtualizacaoNivel = null;
                    Date dataUltimoTeste = null;
                    try {
                        if (!dataStringAtualizacaoNivel.equals("")) {
                            // Corrigido para reconhecer "GMT-03:00"
                            dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse(dataStringAtualizacaoNivel);
                        } else {
                            dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
                        }

                        if (!dataStringUltimoTeste.equals("")) {
                            dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse(dataStringUltimoTeste);
                        } else {
                            dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
                        }

                    } catch (ParseException parse) {
                        parse.printStackTrace();
                    }

                    NivelConteudoEnum nivel = null; //PEDRO - provisório

                    if (nivelBanco == 1) {
                        nivel = NivelConteudoEnum.COBRE;
                    } else if (nivelBanco == 2) {
                        nivel = NivelConteudoEnum.BRONZE;
                    } else if (nivelBanco == 3) {
                        nivel = NivelConteudoEnum.PRATA;
                    } else if (nivelBanco == 4) {
                        nivel = NivelConteudoEnum.OURO;
                    } else if (nivelBanco == 5) {
                        nivel = NivelConteudoEnum.DIAMANTE;
                    }

                    //NivelConteudo(int idNivelConteudo, NivelConteudoEnum nivel, int subiuOuDesceuNivel,
                    // Date dataUltimoTeste, Date dataAtualizacaoNivel, DesempenhoConteudo ultimoDesempenhoConteudo,
                    // Usuario usuario, Conteudo conteudo, int tentativas, int vidas)


                    //DesempenhoConteudo(int idDesempenhoConteudo, Conteudo conteudo, int quantidadePerguntas,
                    // int quantidadeAcertos, int quantidadeErros, float pontuacaoConteudo, float mediaAcertosUltimosQuestionarios)

                    DesempenhoConteudo desempenhoConteudo;

                    int fkUltimoDesempenhoConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getFkUltimoDesempenhoConteudo()));
                    if (fkUltimoDesempenhoConteudo != 0){

                        desempenhoConteudo = new DesempenhoConteudo(
                                fkUltimoDesempenhoConteudo//idDesempenhoConteudo
                                ,meuConteudo
                                ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadePerguntas()))
                                ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeAcertos()))
                                ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeErros()))
                                ,cursor.getFloat(cursor.getColumnIndex(Conexao.getPontuacaoConteudo()))
                                ,cursor.getFloat(cursor.getColumnIndex(Conexao.getMediaAcertosUltimosQuestionarios()))
                        );



                    } else {
                        desempenhoConteudo = new DesempenhoConteudo(
                                meuConteudo
                                ,0
                                ,0
                                ,0
                                ,0f
                                ,0f);
                        new DesempenhoConteudoDB(conexao).insereDesempenhoConteudo(desempenhoConteudo);
                        //nivelConteudo.setUltimoDesempenhoConteudo(desempenhoConteudo);
                        desempenhoConteudo = new DesempenhoConteudoDB(conexao).buscaDesempenhoConteudoComConteudo(meuConteudo.getIdConteudo());
                    }


                    nivelConteudo = new NivelConteudo(idNivelConteudo, nivel, subiuOuDesceuNivel, dataUltimoTeste, dataAtualizacaoNivel, desempenhoConteudo, meuUsuario, meuConteudo, tentativas, vidas);
                    listaNiveisConteudos.add(nivelConteudo);
                } else {
                    // senão encontrar o nível no banco, é pq o usuário está no nível inicial, nesse caso Cobre

                    Date dataAtualizacaoNivel = null;
                    Date dataUltimoTeste = null;
                    try {
                        dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
                        dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
                    } catch(ParseException parse) {
                        parse.printStackTrace();
                    }

                    DesempenhoConteudo desempenhoConteudo = new DesempenhoConteudo(
                            meuConteudo
                            ,0
                            ,0
                            ,0
                            ,0.0f
                            ,0.0f
                    );

                    nivelConteudo = new NivelConteudo(NivelConteudoEnum.COBRE, -1, dataUltimoTeste, dataAtualizacaoNivel, desempenhoConteudo, meuUsuario, meuConteudo, 0, 5);
                    listaNiveisConteudos.add(nivelConteudo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        this.bancoDados.close();
        return listaNiveisConteudos;
    }

    /*


    public ArrayList<NivelConteudo> buscaConteudosComNivel(ArrayList<Conteudo> listaConteudos, Usuario meuUsuario) {
        ArrayList<NivelConteudo> listaNiveisConteudos = new ArrayList<>();

        this.bancoDados = this.conexao.getWritableDatabase();

        for (int x = 0; x < listaConteudos.size(); x++) {
            Conteudo meuConteudo = listaConteudos.get(x);
            Cursor cursor;


            String sql = "SELECT * FROM "+Conexao.getTabelaNivelConteudo()+" nc INNER JOIN "+Conexao.getTabelaDesempenhoConteudo()+" dc"
                    +" ON nc."+Conexao.getFkUltimoDesempenhoConteudo()+" = dc."+Conexao.getIdDesempenhoConteudo()
                    +" WHERE nc."+Conexao.getFkUsuarioNivel()+" = "+ meuUsuario.getIdUsuario()
                    +" AND nc."+Conexao.getFkConteudoNivel()+" = "+meuConteudo.getIdConteudo();
            //String where = Conexao.getFkConteudoNivel() + "='" + meuConteudo.getIdConteudo() + "' and " + Conexao.getFkUsuarioNivel() + "=" + meuUsuario.getIdUsuario();

            cursor = this.bancoDados.rawQuery(sql,null, null);

            if (cursor.moveToFirst()) {
                Log.d("Teste", "Obtive: " + cursor.toString());
                //desempenhoConteudo

                //construtor utilizado:
                //DesempenhoConteudo(int idDesempenhoConteudo, Conteudo conteudo, int quantidadePerguntas,
                // int quantidadeAcertos, int quantidadeErros, float pontuacaoConteudo, float mediaAcertosUltimosQuestionarios)

                DesempenhoConteudo desempenhoConteudo = new DesempenhoConteudo(
                                cursor.getInt(cursor.getColumnIndex(Conexao.getIdDesempenhoConteudo()))//idDesempenhoConteudo
                                ,meuConteudo
                                ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadePerguntas()))
                        ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeAcertos()))
                        ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeErros()))
                        ,cursor.getFloat(cursor.getColumnIndex(Conexao.getPontuacaoConteudo()))
                        ,cursor.getFloat(cursor.getColumnIndex(Conexao.getMediaAcertosUltimosQuestionarios()))
                );

                //nivel conteudo

                int idNivelConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdNivelConteudo()));
                int nivelBanco = cursor.getInt(cursor.getColumnIndex(Conexao.getNIVEL()));
                int tentativas = cursor.getInt(cursor.getColumnIndex(Conexao.getTENTATIVAS()));
                int vidas = cursor.getInt(cursor.getColumnIndex(Conexao.getVIDAS()));
                int subiuOuDesceuNivel = cursor.getInt(cursor.getColumnIndex(Conexao.getSubiuOuDesceuNivel()));
                String dataStringUltimoTeste = cursor.getString(cursor.getColumnIndex(Conexao.getDataUltimoTeste()));
                String dataStringAtualizacaoNivel = cursor.getString(cursor.getColumnIndex(Conexao.getDataAtualizacaoNivel()));

                Date dataAtualizacaoNivel = null;
                Date dataUltimoTeste = null;
                try {
                    if (!dataStringAtualizacaoNivel.equals("")){
                        dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataStringAtualizacaoNivel);
                    } else {
                        dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/0001 00:00:00");
                    }

                    if (!dataStringUltimoTeste.equals("")){
                        dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataStringUltimoTeste);
                    } else {
                        dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/0001 00:00:00");
                    }

                } catch(ParseException parse) {
                    parse.printStackTrace();
                }

                NivelConteudoEnum nivel = null; //PEDRO - provisório

                if (nivelBanco == 1) {
                    nivel = NivelConteudoEnum.COBRE;
                } else if (nivelBanco == 2) {
                    nivel = NivelConteudoEnum.BRONZE;
                } else if (nivelBanco == 3) {
                    nivel = NivelConteudoEnum.PRATA;
                } else if (nivelBanco == 4) {
                    nivel = NivelConteudoEnum.OURO;
                } else if (nivelBanco == 5) {
                    nivel = NivelConteudoEnum.DIAMANTE;
                }

                //NivelConteudo(int idNivelConteudo, NivelConteudoEnum nivel, int subiuOuDesceuNivel,
                // Date dataUltimoTeste, Date dataAtualizacaoNivel, DesempenhoConteudo ultimoDesempenhoConteudo,
                // Usuario usuario, Conteudo conteudo, int tentativas, int vidas)
                meuNivelConteudo = new NivelConteudo(idNivelConteudo, nivel, subiuOuDesceuNivel, dataUltimoTeste, dataAtualizacaoNivel, desempenhoConteudo, meuUsuario, meuConteudo, tentativas, vidas);

            } else {
                // senão encontrar o nível no banco, é pq o usuário está no nível inicial, nesse caso Cobre

                Date dataAtualizacaoNivel = null;
                Date dataUltimoTeste = null;
                try {
                    dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/0001 00:00:00");
                    dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/0001 00:00:00");
                } catch(ParseException parse) {
                    parse.printStackTrace();
                }

                DesempenhoConteudo desempenhoConteudo = new DesempenhoConteudo(
                        meuConteudo
                        ,0
                        ,0
                        ,0
                        ,0.0f
                        ,0.0f
                );

                meuNivelConteudo = new NivelConteudo(NivelConteudoEnum.COBRE, -1, dataUltimoTeste, dataAtualizacaoNivel, desempenhoConteudo, meuUsuario, meuConteudo, 0, 5);
            }
            listaNiveisConteudos.add(meuNivelConteudo);
        }
        this.bancoDados.close();
        return listaNiveisConteudos;
    }

     */

    public NivelConteudo buscaConteudoComNivel(Conteudo meuConteudo, Usuario meuUsuario) {
        NivelConteudo nivelConteudo;
        this.bancoDados = this.conexao.getWritableDatabase();
        String sql = "SELECT * FROM "+Conexao.getTabelaNivelConteudo() + " Left JOIN " + Conexao.getTabelaConteudo()
                + " ON " + Conexao.getTabelaNivelConteudo() + "." + Conexao.getFkConteudoNivel()
                + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo()
                +" INNER JOIN "+Conexao.getTabelaDesempenhoConteudo()
                +" ON "+Conexao.getTabelaNivelConteudo()+"."+Conexao.getFkUltimoDesempenhoConteudo()
                +" = "+Conexao.getTabelaDesempenhoConteudo()+"."+Conexao.getIdDesempenhoConteudo()
                +" WHERE "+Conexao.getFkConteudoNivel() + "='" + meuConteudo.getIdConteudo()
                +"' and " + Conexao.getFkUsuarioNivel() + "=" + meuUsuario.getIdUsuario()
                +" AND " + Conexao.getIdNivelConteudo()+"="+Conexao.getFkUltimoDesempenhoConteudo();

        //String where = Conexao.getFkConteudoNivel() + "='" + meuConteudo.getIdConteudo() + "' and " + Conexao.getFkUsuarioNivel() + "=" + meuUsuario.getIdUsuario();
        //Cursor cursor = this.bancoDados.query(Conexao.getTabelaNivelConteudo(), null, where, null, null, null, null);

        Cursor cursor = this.bancoDados.rawQuery(sql, null, null);
        if (cursor.moveToNext()) {
            DesempenhoConteudo desempenhoConteudo = new DesempenhoConteudo(
                    cursor.getInt(cursor.getColumnIndex(Conexao.getIdDesempenhoConteudo()))//idDesempenhoConteudo
                    ,meuConteudo
                    ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadePerguntas()))
                    ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeAcertos()))
                    ,cursor.getInt(cursor.getColumnIndex(Conexao.getQuantidadeErros()))
                    ,cursor.getFloat(cursor.getColumnIndex(Conexao.getPontuacaoConteudo()))
                    ,cursor.getFloat(cursor.getColumnIndex(Conexao.getMediaAcertosUltimosQuestionarios()))
            );

            Log.d("Teste", "Obtive: " + cursor.toString());
            Log.d("Teste", "Nivel: " + cursor.getColumnIndex(Conexao.getNIVEL()));
            int idNivelConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdNivelConteudo()));
            int nivelBanco = cursor.getInt(cursor.getColumnIndex(Conexao.getNIVEL()));
            int tentativas = cursor.getInt(cursor.getColumnIndex(Conexao.getTENTATIVAS()));
            int vidas = cursor.getInt(cursor.getColumnIndex(Conexao.getVIDAS()));
            int subiuOuDesceuNivel = cursor.getInt(cursor.getColumnIndex(Conexao.getSubiuOuDesceuNivel()));
            String dataStringUltimoTeste = cursor.getString(cursor.getColumnIndex(Conexao.getDataUltimoTeste()));
            String dataStringAtualizacaoNivel = cursor.getString(cursor.getColumnIndex(Conexao.getDataAtualizacaoNivel()));

            Date dataAtualizacaoNivel = null;
            Date dataUltimoTeste = null;
            try {
                if (!dataAtualizacaoNivel.equals("")){
                    dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse(dataStringAtualizacaoNivel);
                } else {
                    dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
                }

                if (!dataUltimoTeste.equals("")){
                    dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse(dataStringUltimoTeste);
                } else {
                    dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
                }

            } catch(ParseException parse) {
                parse.printStackTrace();
            }

            NivelConteudoEnum nivel = null;

            if (nivelBanco == 1) {
                nivel = NivelConteudoEnum.COBRE;
            } else if (nivelBanco == 2) {
                nivel = NivelConteudoEnum.BRONZE;
            } else if (nivelBanco == 3) {
                nivel = NivelConteudoEnum.PRATA;
            } else if (nivelBanco == 4) {
                nivel = NivelConteudoEnum.OURO;
            } else if (nivelBanco == 5) {
                nivel = NivelConteudoEnum.DIAMANTE;
            }

            nivelConteudo = new NivelConteudo(idNivelConteudo, nivel, subiuOuDesceuNivel, dataUltimoTeste, dataAtualizacaoNivel, desempenhoConteudo, meuUsuario, meuConteudo, tentativas, vidas);

        } else {
            // se não encontrar o nível no banco é pq o usuário está no nível inicial, nesse caso Cobre

            DesempenhoConteudo desempenhoConteudo = new DesempenhoConteudo(
                    meuConteudo
                    ,0
                    ,0
                    ,0
                    ,0.0f
                    ,0.0f
            );

            Date dataAtualizacaoNivel = null;
            Date dataUltimoTeste = null;
            try {
                dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
                dataUltimoTeste = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
            } catch(ParseException parse) {
                parse.printStackTrace();
            }

            nivelConteudo = new NivelConteudo(NivelConteudoEnum.COBRE, 0, dataUltimoTeste, dataAtualizacaoNivel, desempenhoConteudo, meuUsuario, meuConteudo, 0, 5);
        }
        this.bancoDados.close();
        return nivelConteudo;
    }

    public void incrementaNivel(NivelConteudo meuNivelConteudo, Usuario meuUsuario){
        ContentValues valores;
        valores = new ContentValues();
        String where;

        this.bancoDados = this.conexao.getWritableDatabase();

            if (meuNivelConteudo.getIdNivelConteudo() != -1) {
            where = Conexao.getIdNivelConteudo() + "=" + meuNivelConteudo.getIdNivelConteudo();
            Log.d("Teste", "Entrei no if de incrementaNivel em NivelConteudoDB!");
            valores.put(Conexao.getNIVEL(), meuNivelConteudo.getNivel().getValor());
            valores.put(Conexao.getSubiuOuDesceuNivel(), 1);
            valores.put(Conexao.getDataAtualizacaoNivel(), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").format(new Date(System.currentTimeMillis())));
            valores.put(Conexao.getDataUltimoTeste(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").format(new Date(System.currentTimeMillis())));
            //update em NivelConteudo
            long retorno = this.bancoDados.update(Conexao.getTabelaNivelConteudo(), valores, where, null);
        } else {
            Log.d("Teste", "Entrei no else de incrementaNivel em NivelConteudoDB!");
            valores.put(Conexao.getFkConteudoNivel(), meuNivelConteudo.getConteudo().getIdConteudo());
            valores.put(Conexao.getNIVEL(), meuNivelConteudo.getNivel().getValor());
            valores.put(Conexao.getFkUsuarioNivel(), meuUsuario.getIdUsuario());
            valores.put(Conexao.getSubiuOuDesceuNivel(), -1);
            Date dataAtualizacaoNivel = null;
            try {
                dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
            } catch(ParseException parse) {
                parse.printStackTrace();
            }
            valores.put(Conexao.getDataAtualizacaoNivel(), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").format(dataAtualizacaoNivel));
            valores.put(Conexao.getDataUltimoTeste(), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").format(new Date(System.currentTimeMillis())));
            //insert em NivelConteudo, não update
            long retorno = this.bancoDados.insert(Conexao.getTabelaNivelConteudo(), null, valores);
            if (retorno >= 0) {
                int aux = (int) retorno;
                meuNivelConteudo.setIdNivelConteudo(aux);
            }
            Log.d("Teste", "Inseri no banco!" + valores.toString());
            Log.d("Teste", "Nivel novo banco: " + retorno);
        }

        this.bancoDados.close();
    }

    public void incrementaNivel(ArrayList<NivelConteudo> listaNiveisConteudos, Usuario meuUsuario) {
        Log.d("Teste", "Entrei no incrementaNivel em NivelConteudoDB!");
        for (int x = 0; x < listaNiveisConteudos.size(); x++) {
            NivelConteudo meuNivelConteudo = listaNiveisConteudos.get(x);
            Log.d("Teste", "Entrei no for de incrementaNivel em NivelConteudoDB!");
            incrementaNivel(meuNivelConteudo, meuUsuario);
        }
    }

    public void decaiUmNivel (NivelConteudo meuNivelConteudo, Usuario meuUsuario){
        ContentValues valores = new ContentValues();
        String where;
        this.bancoDados = this.conexao.getWritableDatabase();
        if (meuNivelConteudo.getIdNivelConteudo() != -1 && meuNivelConteudo.getNivel().getValor()>1) {
            where = Conexao.getIdNivelConteudo() + "=" + meuNivelConteudo.getIdNivelConteudo() + " AND "+ Conexao.getFkUsuarioNivel()+"="+ meuUsuario.getIdUsuario(); //Pedro - Peguntar se isso não deveria estar em todos os lugares
            Log.d("Teste", "Entrei no if de decaiNivel em NivelConteudoDB!");
            valores.put(Conexao.getNIVEL(), meuNivelConteudo.getNivel().getValor());
            valores.put(Conexao.getSubiuOuDesceuNivel(), -1);
            valores.put(Conexao.getDataAtualizacaoNivel(), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").format(new Date(System.currentTimeMillis())));
            valores.put(Conexao.getDataUltimoTeste(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").format(new Date(System.currentTimeMillis())));
            //update em NivelConteudo
            long retorno = this.bancoDados.update(Conexao.getTabelaNivelConteudo(), valores, where, null);
        } else {
            Log.d("Teste", "Entrei no else de decaiNivel em NivelConteudoDB!");
            valores.put(Conexao.getFkConteudoNivel(), meuNivelConteudo.getConteudo().getIdConteudo());
            valores.put(Conexao.getNIVEL(), meuNivelConteudo.getNivel().getValor());
            valores.put(Conexao.getFkUsuarioNivel(), meuUsuario.getIdUsuario());
            valores.put(Conexao.getSubiuOuDesceuNivel(), -1);
            Date dataAtualizacaoNivel = null;
            try {
                dataAtualizacaoNivel = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").parse("01/01/1971 00:00:000 GMT-03:00");
            } catch(ParseException parse) {
                parse.printStackTrace();
            }
            valores.put(Conexao.getDataAtualizacaoNivel(), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").format(dataAtualizacaoNivel));
            valores.put(Conexao.getDataUltimoTeste(), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").format(new Date(System.currentTimeMillis())));
            //insert em NivelConteudo, não update
            long retorno = this.bancoDados.insert(Conexao.getTabelaNivelConteudo(), null, valores);
            if (retorno >= 0) {
                int aux = (int) retorno;
                meuNivelConteudo.setIdNivelConteudo(aux);
            }
            Log.d("Teste", "Inseri no banco!" + valores.toString());
            Log.d("Teste", "Nivel novo banco: " + retorno);
        }

        this.bancoDados.close();
    }

    public void decaiNivel(ArrayList<NivelConteudo> listaNiveisConteudos, Usuario meuUsuario){
        Log.d("Teste", "Entrei no decaiNivel em NivelConteudoDB!");
        for (int x = 0; x< listaNiveisConteudos.size(); x++){
            NivelConteudo meuNivelConteudo = listaNiveisConteudos.get(x);
            Log.d("Teste", "Entrei no for de decaiNivel em NivelConteudoDB!");
            decaiUmNivel(meuNivelConteudo, meuUsuario);
        }
    }

    public String alteraNivel(NivelConteudo meuNivelConteudo) {
        Log.d("Teste","Entrei no alteraNivel em NivelConteudoDB!");
        ContentValues valores;
        valores = new ContentValues();
        String where;
        String retorno;
        Log.d("Teste","Estou indo realizar a operacao em alteraNivel em NivelConteudoDB!");
        this.bancoDados = this.conexao.getWritableDatabase();

        where = Conexao.getIdNivelConteudo() + "=" + meuNivelConteudo.getIdNivelConteudo();
        valores.put(Conexao.getNIVEL(), meuNivelConteudo.getNivel().getValor());
        //para caso o atributo Nivel seja setado sem alterar o NivelAnterior, caso seja setado vai funcionar igual
        if (meuNivelConteudo.getNivel().getValor() > meuNivelConteudo.getNivelAnterior().getValor()){
            valores.put(Conexao.getSubiuOuDesceuNivel(), 1);
        } else if (meuNivelConteudo.getNivel().getValor() < meuNivelConteudo.getNivelAnterior().getValor()){
            valores.put(Conexao.getSubiuOuDesceuNivel(), -1);
        } else {
            valores.put(Conexao.getSubiuOuDesceuNivel(), 0);
        }
        valores.put(Conexao.getDataAtualizacaoNivel(), meuNivelConteudo.getDataAtualizacaoNivel().toString());
        valores.put(Conexao.getDataUltimoTeste(), meuNivelConteudo.getDataUltimoTeste().toString());

        //update em NivelConteudo
        long resultado = this.bancoDados.update(Conexao.getTabelaNivelConteudo(), valores, where, null);
        Log.d("Teste","Ralizei a operacao em alteraNivel em NivelConteudoDB!");

        if (resultado == -1){
            retorno = "Erro ao atualizar nível";
        } else {
            retorno = "Parabéns!!! Você pulou no conteúdo: " + meuNivelConteudo.getConteudo().getNomeConteudo() + " para o nível: " + meuNivelConteudo.getNivel();
        }

        this.bancoDados.close();
        return retorno;
    }

    public String insereNivel(NivelConteudo meuNivelConteudo) {
        Log.d("Teste","Entrei no insereNivel em NivelConteudoDB!");
        String retorno = "";
        long resultado;
        ContentValues valores;

        this.bancoDados = this.conexao.getWritableDatabase();

        valores = new ContentValues();
        meuNivelConteudo.setTentativas(0);
        meuNivelConteudo.setVidas(5);

        Log.d("Teste","Estou indo realizar a operacao em insereNivel em NivelConteudoDB!");
        valores.put(Conexao.getNIVEL(), meuNivelConteudo.getNivel().getValor());
        valores.put(Conexao.getTENTATIVAS(), meuNivelConteudo.getTentativas());
        valores.put(Conexao.getVIDAS(), meuNivelConteudo.getVidas());
        valores.put(Conexao.getFkConteudoNivel(), meuNivelConteudo.getConteudo().getIdConteudo());
        valores.put(Conexao.getFkUsuarioNivel(), meuNivelConteudo.getUsuario().getIdUsuario());
        valores.put(Conexao.getSubiuOuDesceuNivel(), -1);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz");
        valores.put(Conexao.getDataUltimoTeste(), df.format(meuNivelConteudo.getDataUltimoTeste()));
        valores.put(Conexao.getDataAtualizacaoNivel(), df.format(meuNivelConteudo.getDataAtualizacaoNivel()));

        resultado = this.bancoDados.insert(Conexao.getTabelaNivelConteudo(), null, valores);
        this.bancoDados.close();
        Log.d("Teste","Realizei a operacao em insereNivel em NivelConteudoDB!");
        if (resultado == -1) {
            retorno = "Erro ao inserir os registros na tabela " + Conexao.getTabelaNivelConteudo();
            Log.d("Teste","Entrei no if em insereNivel em NivelConteudoDB!");
        } else {
            retorno = "Dados inseridos com sucesso na tabela " + Conexao.getTabelaNivelConteudo();
            Log.d("Teste","Entrei no else em insereNivel em NivelConteudoDB!");
        }
        return retorno;
    }

    public String deletaNivelConteudo(int id){
        String retorno = "";
        String where = Conexao.getIdNivelConteudo() + "=" + id;
        this.bancoDados = this.conexao.getReadableDatabase();
        long resultado = this.bancoDados.delete(Conexao.getTabelaNivelConteudo(), where,null);
        if (resultado == -1){
            retorno = "Erro ao deletar";
        } else {
            retorno = "Nivel conteudo deletado com sucesso";
        }
        this.bancoDados.close();
        return retorno;
    }

    public void atualizaTentativas(NivelConteudo meuNivelConteudo, Usuario meuUsuario){
        ContentValues valores = new ContentValues();
        String where;
        this.bancoDados = this.conexao.getWritableDatabase();

        if (meuNivelConteudo.getIdNivelConteudo() != -1){
            where = Conexao.getIdNivelConteudo() + " = " + meuNivelConteudo.getIdNivelConteudo();
            Log.d("Teste", "Entrei no if de atualizaTentativas em NivelConteudoDB!");
            valores.put(Conexao.getTENTATIVAS(), meuNivelConteudo.getTentativas());
            //update em NivelConteudoDB
            long retorno = this.bancoDados.update(Conexao.getTabelaNivelConteudo(), valores, where, null);
            if (retorno == -1){
                Log.d("Teste", "Erro ao atualizar TENTATIVAS no banco");
            } else {
                Log.d("Teste", "TENTATIVAS atualizadas com sucesso");
            }
        } else {
            Log.d("Teste", "Entrei no else de incrementaNivel em NivelConteudoDB!");
            valores.put(Conexao.getFkConteudoNivel(), meuNivelConteudo.getConteudo().getIdConteudo());
            valores.put(Conexao.getTENTATIVAS(), meuNivelConteudo.getTentativas());
            valores.put(Conexao.getFkUsuarioNivel(), meuUsuario.getIdUsuario());
            valores.put(Conexao.getSubiuOuDesceuNivel(), -1);
            valores.put(Conexao.getDataUltimoTeste(), new Date(System.currentTimeMillis()).toString());
            valores.put(Conexao.getDataAtualizacaoNivel(), "01/01/1971 00:00:000 GMT-03:00");
            //EEE MMM dd HH:mm:ss zzz yyyy
            //insert em NivelConteudo, não update
            long retorno = this.bancoDados.insert(Conexao.getTabelaNivelConteudo(), null, valores);
            if (retorno >= 0) {
                int aux = (int) retorno;
                meuNivelConteudo.setIdNivelConteudo(aux);
            }
            Log.d("Teste", "Inseri no banco!" + valores.toString());
            Log.d("Teste", "Tentativa no banco: " + retorno);
        }
        this.bancoDados.close();
    }

    public void atualizaVidas(NivelConteudo meuNivelConteudo, Usuario meuUsuario){
        ContentValues valores = new ContentValues();
        String where;
        this.bancoDados = this.conexao.getWritableDatabase();
        if (meuNivelConteudo.getIdNivelConteudo() != -1){
            where = Conexao.getIdNivelConteudo()+"="+meuNivelConteudo.getIdNivelConteudo()+" AND "+Conexao.getFkUsuarioNivel()+"="+meuUsuario.getIdUsuario();
            Log.d("Teste","Entrei no IF de atualizaVidas  em NivelConteudoDB!");
            valores.put(Conexao.getVIDAS(),meuNivelConteudo.getVidas());
            //Update em NivelConteudoDB
            long retorno = bancoDados.update(Conexao.getTabelaNivelConteudo(), valores, where, null);
            if (retorno == -1){
                Log.d("Teste","Erro ao atulziar as vidas em atualizaVidas NivelConteudoDB");
            } else {
                Log.d("Teste","Vidas atualizadas com sucesso em atualizaVidas NivelConteudoDB");
            }
        } else {
            Log.d("Teste", "Entrei no else de incrementaNivel em NivelConteudoDB!");
            valores.put(Conexao.getFkConteudoNivel(), meuNivelConteudo.getConteudo().getIdConteudo());
            valores.put(Conexao.getVIDAS(), meuNivelConteudo.getVidas());
            valores.put(Conexao.getFkUsuarioNivel(), meuUsuario.getIdUsuario());
            //insert em NivelConteudo, não update
            long retorno = this.bancoDados.insert(Conexao.getTabelaNivelConteudo(), null, valores);
            if (retorno >= 0) {
                int aux = (int) retorno;
                meuNivelConteudo.setIdNivelConteudo(aux);
            }
            Log.d("Teste", "Inseri no banco!" + valores.toString());
            Log.d("Teste", "Vidas no banco: " + retorno);
        }
    }

    public void atualizaDatas(ArrayList<NivelConteudo> listaNivelConteudo, Usuario usuario){
        for (NivelConteudo nivelConteudo: listaNivelConteudo){
            atualizaDatas(nivelConteudo, usuario);
        }
    }

    public void atualizaDatas(NivelConteudo nivelConteudo, Usuario usuario){
        this.bancoDados = this.conexao.getWritableDatabase();
        ContentValues valores = new ContentValues();
        String where;

        if (nivelConteudo.getIdNivelConteudo() != -1){
            where = Conexao.getIdNivelConteudo()+" = "+nivelConteudo.getIdNivelConteudo()+" AND "+Conexao.getFkUsuarioNivel()+" = "+usuario.getIdUsuario();
            //  dd/MM/yyyy HH:mm:ss
            valores.put(Conexao.getDataUltimoTeste(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz").format(new Date(System.currentTimeMillis())));
            //Update em NivelConteudoDB
            long retorno = bancoDados.update(Conexao.getTabelaNivelConteudo(), valores, where, null);
            if (retorno == -1){
                Log.d("Teste","Erro ao atulziar as datas em atualizaVidas NivelConteudoDB");
            } else {
                Log.d("Teste","Datas atualizadas com sucesso em atualizaVidas NivelConteudoDB");
            }
        }
        this.bancoDados.close();
        valores.clear();
    }

    /*public Image carregaImagemNivel(){
        Image imagem;
        String where = Conexao.getImagemNivel();
        this.bancoDados = this.conexao.getWritableDatabase();
        buscaConteudoComNivel();

        where = Conexao.getFkConteudoNivel() + "='" + meuConteudo.getIdConteudo() + "' and " + Conexao.getFkUsuarioNivel() + "=" + meuUsuario.getIdUsuario();
        Cursor cursor = this.bancoDados.query(Conexao.getTabelaNivelConteudo(), null, where, null, null, null, null);

        if (cursor.moveToNext()) {
            Log.d("Teste", "Obtive: " + cursor.toString());
            int idNivelConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdNivelConteudo()));
            Image imagemBanco = cursor.get(cursor.getColumnIndex(Conexao.getNIVEL()));
            NivelConteudoEnum nivel = null;

            if (nivelBanco == 1) {
                nivel = NivelConteudoEnum.COBRE;
            } else if (nivelBanco == 2) {
                nivel = NivelConteudoEnum.BRONZE;
            } else if (nivelBanco == 3) {
                nivel = NivelConteudoEnum.PRATA;
            } else if (nivelBanco == 4) {
                nivel = NivelConteudoEnum.OURO;
            } else if (nivelBanco == 5) {
                nivel = NivelConteudoEnum.DIAMANTE;
            }

            meuNivelConteudo = new NivelConteudo(idNivelConteudo, nivel, meuUsuario, meuConteudo);

        }

        return imagem;
    }*/

    public void atualizaDesempenhoConteudo(ArrayList<NivelConteudo> listaNivelConteudo, DesempenhoQuestionario desempenhoQuestionario){

        ContentValues valores = new ContentValues();
        String where;
        DesempenhoConteudoDB desempenhoConteudoDB = new DesempenhoConteudoDB(conexao);

        ArrayList<DesempenhoConteudo> desempenhoConteudoArrayListAtualizada = new ArrayList<>();

        for (int i = 0; i < desempenhoQuestionario.getListaDesempenhoConteudos().size(); i++){
            desempenhoConteudoDB.insereDesempenhoConteudo(desempenhoQuestionario.getListaDesempenhoConteudos().get(i));
            DesempenhoConteudo desempenhoConteudoAtual = desempenhoConteudoDB.buscaUltimos3DesempenhosConteudosComConteudo(listaNivelConteudo.get(i).getConteudo().getIdConteudo()).get(0);
            desempenhoConteudoArrayListAtualizada.add(desempenhoConteudoAtual);
        }

        desempenhoQuestionario.setListaDesempenhoConteudos(desempenhoConteudoArrayListAtualizada);

        for (int i = 0; i < listaNivelConteudo.size(); i++){
            if (listaNivelConteudo.get(i).getIdNivelConteudo() != -1 ){
                where = Conexao.getIdNivelConteudo()+" = "+listaNivelConteudo.get(i).getIdNivelConteudo()+" AND "+Conexao.getFkUsuarioNivel()+" = "+desempenhoQuestionario.getMeuUsuario().getIdUsuario();
                valores.put(Conexao.getFkUltimoDesempenhoConteudo(), desempenhoQuestionario.getListaDesempenhoConteudos().get(i).getIdDesempenhoConteudo());
                valores.put(Conexao.getNIVEL(), listaNivelConteudo.get(i).getNivel().getValor());
                //Update em NivelConteudoDB
                this.bancoDados = this.conexao.getWritableDatabase();
                long retorno = bancoDados.update(Conexao.getTabelaNivelConteudo(), valores, where, null);
                if (retorno == -1){
                    Log.d("Teste","Erro ao atulziar UltimoDesempenhoConteudo em NivelConteudoDB");
                } else {
                    Log.d("Teste","UltimoDesempenhoConteudo atualizado com sucesso em NivelConteudoDB");
                }
            }

        }


        this.bancoDados.close();
        valores.clear();
    }
}