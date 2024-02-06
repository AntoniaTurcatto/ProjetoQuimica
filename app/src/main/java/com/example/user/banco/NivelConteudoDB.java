package com.example.user.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.util.Log;

import com.example.user.classesDominio.Conteudo;
import com.example.user.classesDominio.NivelConteudo;
import com.example.user.classesDominio.Usuario;
import com.example.user.componente.NivelConteudoEnum;

import java.util.ArrayList;

public class NivelConteudoDB {
    private SQLiteDatabase bancoDados;
    private Conexao conexao;
    NivelConteudo meuNivelConteudo;

    public NivelConteudoDB(Context context) {
        this.conexao = new Conexao(context);
    }
//A lista que é carregada ficaria nula  e geraria o problema
    public ArrayList<NivelConteudo> carregaListaCompleta(Usuario meuUsuario, int tipoConteudo) {
        ArrayList<NivelConteudo> listaNiveisConteudos = new ArrayList<>();

        this.bancoDados = this.conexao.getWritableDatabase();

        String mensagem = Conexao.getTabelaNivelConteudo() + " INNER JOIN " + Conexao.getTabelaConteudo()
                + " ON " + Conexao.getTabelaNivelConteudo() + "." + Conexao.getFkConteudoNivel()
                + " = " + Conexao.getTabelaConteudo() + "." + Conexao.getIdConteudo();

        String where = Conexao.getTipoConteudo() + "=" + tipoConteudo + " and " + Conexao.getFkUsuarioNivel() + "=" + meuUsuario.getIdUsuario();

        Cursor cursor = this.bancoDados.query(mensagem, null, where, null, null, null, null);

        while (cursor.moveToNext()) {
            int idNivelConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdNivelConteudo()));
            int nivelBanco = cursor.getInt(cursor.getColumnIndex(Conexao.getNIVEL()));
            int tentativas = cursor.getInt(cursor.getColumnIndex(Conexao.getTENTATIVAS()));
            int vidas = cursor.getInt(cursor.getColumnIndex(Conexao.getVIDAS()));
            int idConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getFkConteudoNivel()));
            String nomeConteudo = cursor.getString(cursor.getColumnIndex(Conexao.getNomeConteudo()));


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

            Conteudo meuConteudo = new Conteudo(idConteudo, nomeConteudo, tipoConteudo);
            NivelConteudo meuNivelConteudo = new NivelConteudo(idNivelConteudo, nivel, meuUsuario, meuConteudo, tentativas, vidas);

            this.bancoDados.close();

            listaNiveisConteudos.add(meuNivelConteudo);
        }
        return listaNiveisConteudos;
    }

    public ArrayList<NivelConteudo> buscaConteudosComNivel(ArrayList<Conteudo> listaConteudos, Usuario meuUsuario) {
        ArrayList<NivelConteudo> listaNiveisConteudos = new ArrayList<>();

        this.bancoDados = this.conexao.getWritableDatabase();

        for (int x = 0; x < listaConteudos.size(); x++) {
            Conteudo meuConteudo = listaConteudos.get(x);

            String where = Conexao.getFkConteudoNivel() + "='" + meuConteudo.getIdConteudo() + "' and " + Conexao.getFkUsuarioNivel() + "=" + meuUsuario.getIdUsuario();
            Cursor cursor = this.bancoDados.query(Conexao.getTabelaNivelConteudo(), null, where, null, null, null, null);

            if (cursor.moveToNext()) {
                Log.d("Teste", "Obtive: " + cursor.toString());
                int idNivelConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdNivelConteudo()));
                int nivelBanco = cursor.getInt(cursor.getColumnIndex(Conexao.getNIVEL()));
                int tentativas = cursor.getInt(cursor.getColumnIndex(Conexao.getTENTATIVAS()));
                int vidas = cursor.getInt(cursor.getColumnIndex(Conexao.getVIDAS()));
                NivelConteudoEnum nivel = null; //PEDRO - provisório

                if (nivelBanco == 0) {
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

                meuNivelConteudo = new NivelConteudo(idNivelConteudo, nivel, meuUsuario, meuConteudo, tentativas, vidas);

            } else {
                // senão encontrar o nível no banco, é pq o usuário está no nível inicial, nesse caso Cobre
                meuNivelConteudo = new NivelConteudo(NivelConteudoEnum.COBRE, meuUsuario, meuConteudo, 0, 5);
            }
            listaNiveisConteudos.add(meuNivelConteudo);
        }
        this.bancoDados.close();
        return listaNiveisConteudos;
    }

    public NivelConteudo buscaConteudoComNivel(Conteudo meuConteudo, Usuario meuUsuario) {

        this.bancoDados = this.conexao.getWritableDatabase();

        String where = Conexao.getFkConteudoNivel() + "='" + meuConteudo.getIdConteudo() + "' and " + Conexao.getFkUsuarioNivel() + "=" + meuUsuario.getIdUsuario();
        Cursor cursor = this.bancoDados.query(Conexao.getTabelaNivelConteudo(), null, where, null, null, null, null);

        if (cursor.moveToNext()) {
            Log.d("Teste", "Obtive: " + cursor.toString());
            Log.d("Teste", "Nivel: " + cursor.getColumnIndex(Conexao.getNIVEL()));
            int idNivelConteudo = cursor.getInt(cursor.getColumnIndex(Conexao.getIdNivelConteudo()));
            int nivelBanco = cursor.getInt(cursor.getColumnIndex(Conexao.getNIVEL()));
            int tentativas = cursor.getInt(cursor.getColumnIndex(Conexao.getTENTATIVAS()));
            int vidas = cursor.getInt(cursor.getColumnIndex(Conexao.getVIDAS()));
            NivelConteudoEnum nivel = null;

            if (nivelBanco == 0) {
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

            meuNivelConteudo = new NivelConteudo(idNivelConteudo, nivel, meuUsuario, meuConteudo, tentativas, vidas);

        } else {
            // senão encontrar o nível no banco, é pq o usuário está no nível inicial, nesse caso Cobre
            meuNivelConteudo = new NivelConteudo(NivelConteudoEnum.COBRE, meuUsuario, meuConteudo, 0, 5);
        }
        this.bancoDados.close();
        return meuNivelConteudo;
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
            //update em NivelConteudo
            long retorno = this.bancoDados.update(Conexao.getTabelaNivelConteudo(), valores, where, null);
        } else {
            Log.d("Teste", "Entrei no else de incrementaNivel em NivelConteudoDB!");
            valores.put(Conexao.getFkConteudoNivel(), meuNivelConteudo.getConteudo().getIdConteudo());
            valores.put(Conexao.getNIVEL(), meuNivelConteudo.getNivel().getValor());
            valores.put(Conexao.getFkUsuarioNivel(), meuUsuario.getIdUsuario());
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
        if (meuNivelConteudo.getIdNivelConteudo() != -1) {
            where = Conexao.getIdNivelConteudo() + "=" + meuNivelConteudo.getIdNivelConteudo() + " AND "+ Conexao.getFkUsuarioNivel()+"="+ meuUsuario.getIdUsuario(); //Pedro - Peguntar se isso não deveria estar em todos os lugares
            Log.d("Teste", "Entrei no if de decaiNivel em NivelConteudoDB!");
            valores.put(Conexao.getNIVEL(), meuNivelConteudo.getNivel().getValor());
            //update em NivelConteudo
            long retorno = this.bancoDados.update(Conexao.getTabelaNivelConteudo(), valores, where, null);
        } else {
            Log.d("Teste", "Entrei no else de incrementaNivel em NivelConteudoDB!");
            valores.put(Conexao.getFkConteudoNivel(), meuNivelConteudo.getConteudo().getIdConteudo());
            valores.put(Conexao.getNIVEL(), meuNivelConteudo.getNivel().getValor());
            valores.put(Conexao.getFkUsuarioNivel(), meuUsuario.getIdUsuario());
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
        String retorno = "";
        Log.d("Teste","Estou indo realizar a operacao em alteraNivel em NivelConteudoDB!");
        this.bancoDados = this.conexao.getWritableDatabase();

        where = Conexao.getIdNivelConteudo() + "=" + meuNivelConteudo.getIdNivelConteudo();
        valores.put(Conexao.getNIVEL(), meuNivelConteudo.getNivel().getValor());
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
}