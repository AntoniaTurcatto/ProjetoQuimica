package com.example.user.classesDominio;

import com.example.user.componente.NivelConteudoEnum;

import java.io.Serializable;

public class Feedback implements Serializable {
    private Conteudo conteudo;
    private NivelConteudoEnum nivelAnterior;
    private NivelConteudoEnum nivelAtual;
    private int niveisAvancados;

    public Feedback(Conteudo conteudo, NivelConteudoEnum nivelAnterior, NivelConteudoEnum nivelAtual, int niveisAvancados) {
        this.conteudo = conteudo;
        this.nivelAnterior = nivelAnterior;
        this.nivelAtual = nivelAtual;
        this.niveisAvancados = niveisAvancados;
    }

    public Feedback(Conteudo conteudo, NivelConteudoEnum nivelAnterior) {
        this.conteudo = conteudo;
        this.nivelAnterior = nivelAnterior;
    }

    public Conteudo getConteudo() {
        return conteudo;
    }

    public void setConteudo(Conteudo conteudo) {
        this.conteudo = conteudo;
    }

    public NivelConteudoEnum getNivelAnterior() {
        return nivelAnterior;
    }

    public void setNivelAnterior(NivelConteudoEnum nivelAnterior) {
        this.nivelAnterior = nivelAnterior;
    }

    public NivelConteudoEnum getNivelAtual() {
        return nivelAtual;
    }

    public void setNivelAtual(NivelConteudoEnum nivelAtual) {
        this.nivelAtual = nivelAtual;
    }

    public int getNiveisAvancados() {
        return niveisAvancados;
    }

    public void setNiveisAvancados(int niveisAvancados) {
        this.niveisAvancados = niveisAvancados;
    }
}
