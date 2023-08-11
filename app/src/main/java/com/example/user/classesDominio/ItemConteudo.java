package com.example.user.classesDominio;

import com.example.user.classesDominio.Conteudo;

public class ItemConteudo {
    private Conteudo conteudo;
    private Boolean value; //validar se marcou ou n o conteudo desejado

    public ItemConteudo(Conteudo conteudo) {
        this.conteudo = conteudo;
        this.value = false;
    }

    public Conteudo getConteudo() {
        return conteudo;
    }

    public void setConteudo(Conteudo conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

}