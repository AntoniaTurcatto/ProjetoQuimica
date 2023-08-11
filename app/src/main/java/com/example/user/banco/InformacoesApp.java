package com.example.user.banco;

import android.app.Application;

import com.example.user.classesDominio.NivelConteudo;
import com.example.user.classesDominio.Usuario;

import java.util.ArrayList;

public class InformacoesApp extends Application {
    Usuario meuUsuario;
    int tipoConteudo;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Usuario getMeuUsuario() {
        return meuUsuario;
    }

    public void setMeuUsuario(Usuario meuUsuario) {
        this.meuUsuario = meuUsuario;
    }

    public int getTipoConteudo() {
        return tipoConteudo;
    }

    public void setTipoConteudo(int tipoConteudo) {
        this.tipoConteudo = tipoConteudo;
    }
}
