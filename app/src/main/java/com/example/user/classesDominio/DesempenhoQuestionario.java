package com.example.user.classesDominio;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class DesempenhoQuestionario implements Serializable {
    private int idDesempenhoQuestionario;
    private Date data;
    private float pontuacaoFinal;
    private ArrayList<DesempenhoConteudo> listaDesempenhoConteudos;
    private Usuario meuUsuario;
    private int tipoDesempenho; //se igual 1 então é quiz, se igual a 2 é diagnóstico

    public DesempenhoQuestionario(int idDesempenhoQuestionario, Date data, float pontuacaoFinal, int tipoDesempenho) {
        this.idDesempenhoQuestionario = idDesempenhoQuestionario;
        this.data = data;
        this.pontuacaoFinal = pontuacaoFinal;
        this.tipoDesempenho = tipoDesempenho;
    }

    public DesempenhoQuestionario(int idDesempenhoQuestionario, Date data, float pontuacaoFinal, ArrayList<DesempenhoConteudo> listaDesempenhoConteudos, Usuario meuUsuario) {
        this.idDesempenhoQuestionario = idDesempenhoQuestionario;
        this.data = data;
        this.pontuacaoFinal = pontuacaoFinal;
        this.listaDesempenhoConteudos = listaDesempenhoConteudos;
        this.meuUsuario = meuUsuario;
    }

    public DesempenhoQuestionario(Date data, float pontuacaoFinal, ArrayList<DesempenhoConteudo> listaDesempenhoConteudos, Usuario meuUsuario) {
        this.data = data;
        this.pontuacaoFinal = pontuacaoFinal;
        this.listaDesempenhoConteudos = listaDesempenhoConteudos;
        this.meuUsuario = meuUsuario;
    }

    public DesempenhoQuestionario(Date data, int tipoDesempenho, Usuario meuUsuario) {
        this.data = data;
        this.tipoDesempenho = tipoDesempenho;
        this.meuUsuario = meuUsuario;
        this.listaDesempenhoConteudos = new ArrayList<>();
    }

    public void calculaPontuacaoFinal() {
        float somaDesempenho = 0;
        for (int x = 0; x < this.listaDesempenhoConteudos.size(); x++) {
            DesempenhoConteudo desempenhoConteudo = this.listaDesempenhoConteudos.get(x);
            somaDesempenho = somaDesempenho + desempenhoConteudo.getPontuacaoConteudo();
        }
        this.pontuacaoFinal = somaDesempenho / this.listaDesempenhoConteudos.size();
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public float getPontuacaoFinal() {
        return pontuacaoFinal;
    }

    public void setPontuacaoFinal(float pontuacaoFinal) {
        this.pontuacaoFinal = pontuacaoFinal;
    }

    public ArrayList<DesempenhoConteudo> getListaDesempenhoConteudos() { return listaDesempenhoConteudos; }

    public void setListaDesempenhoConteudos(ArrayList<DesempenhoConteudo> listaDesempenhoConteudos) {
        this.listaDesempenhoConteudos = listaDesempenhoConteudos;
    }

    public int getIdDesempenhoQuestionario() {
        return idDesempenhoQuestionario;
    }

    public void setIdDesempenhoQuestionario(int idDesempenhoQuestionario) {
        this.idDesempenhoQuestionario = idDesempenhoQuestionario;
    }

    public Usuario getMeuUsuario() { return meuUsuario; }

    public void setMeuUsuario(Usuario meuUsuario) { this.meuUsuario = meuUsuario; }

    public int getTipoDesempenho() {
        return tipoDesempenho;
    }

    public void setTipoDesempenho(int tipoDesempenho) {
        this.tipoDesempenho = tipoDesempenho;
    }
}
