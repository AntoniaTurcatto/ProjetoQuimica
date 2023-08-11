package com.example.user.classesDominio;

import java.io.Serializable;

public class Conteudo implements Serializable {
    private int idConteudo;
    private String nomeConteudo;
    private int tipoConteudo;
    private int quantidade;
    private float precisao;
    private int porcentagemNivel;
    private int caiuNivel;

    //método construtor 1 (nome e tipo)
    public Conteudo(String nomeConteudo, int tipoConteudo) {
        this.nomeConteudo = nomeConteudo;
        this.tipoConteudo = tipoConteudo;
    }

    //método construtor 2 (id, nome e tipo)
    public Conteudo(int idConteudo, String nomeConteudo, int tipoConteudo) {
        this.idConteudo = idConteudo;
        this.nomeConteudo = nomeConteudo;
        this.tipoConteudo = tipoConteudo;
    }

    //método construtor 3 (nome, tipo, quantidade, precisao e porcentagem nivel)
    public Conteudo(String nomeConteudo, int tipoConteudo, int quantidade, float precisao, int porcentagemNivel) {
        this.nomeConteudo = nomeConteudo;
        this.tipoConteudo = tipoConteudo;
        this.quantidade = quantidade;
        this.precisao = precisao;
        this.porcentagemNivel = porcentagemNivel;
    }

    public String getTipoConteudoLiteral() {
        String retorno = "";
        if (this.tipoConteudo == 1){
            retorno = "Orgânica";
        } else if (this.tipoConteudo == 2){
            retorno = "Inorgânica";
        }
        return retorno;
    }

    public void calculaPrecisao(Conteudo meuConteudo, float pontuacaoConteudo){
        float media = 0;
        if(meuConteudo.getQuantidade() == 0){
            this.precisao = 20;
        } else {
            media = meuConteudo.getPrecisao()*(pontuacaoConteudo/100);
            if(media >= 10 || media <=20){
                this.precisao = media;
            } else if (media < 10){
                this.precisao = 10;
            } else if (media > 20){
                this.precisao = 20;
            }
        }
    }

    public int getIdConteudo() {
        return idConteudo;
    }

    public void setIdConteudo(int idConteudos) {
        this.idConteudo = idConteudos;
    }

    public String getNomeConteudo() {
        return nomeConteudo;
    }

    public void setNomeConteudo(String nomeConteudo) {
        this.nomeConteudo = nomeConteudo;
    }

    public int getTipoConteudo() {
        return tipoConteudo;
    }

    public void setTipoConteudo(int tipoConteudo) {
        this.tipoConteudo = tipoConteudo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getPrecisao() {
        return precisao;
    }

    public void setPrecisao(float precisao) {
        this.precisao = precisao;
    }

    public int getPorcentagemNivel() {
        return porcentagemNivel;
    }

    public void setPorcentagemNivel(int porcentagemNivel) {
        this.porcentagemNivel = porcentagemNivel;
    }

    public int getCaiuNivel() {
        return caiuNivel;
    }

    public void setCaiuNivel(int caiuNivel) {
        this.caiuNivel = caiuNivel;
    }

}
