package com.example.user.classesDominio;

import com.example.user.classesDominio.Conteudo;

import java.io.Serializable;

public class Pergunta implements Serializable {
    private int idQuiz;
    private String enunciado;
    private String opcaoA;
    private String opcaoB;
    private String opcaoC;
    private String opcaoD;
    private String opcaoE;
    private byte[] imagem;
    private int testePrevio;
    private Conteudo conteudo;
    private char alternativaCorreta;
    private char opcaoEscolhida;
    private int nivelDificuldade; // 1- Fácil, 2- Média e 3- Difícil
    private int questaoVestibular;

    // Construtor 1 - sem id
    public Pergunta(String enunciado, String opcaoA, String opcaoB, String opcaoC, String opcaoD, String opcaoE, byte[] imagem, int testePrevio, Conteudo conteudo, char alternativaCorreta, char opcaoEscolhida, int nivelDificuldade, int questaoVestibular) {
        this.enunciado = enunciado;
        this.opcaoA = opcaoA;
        this.opcaoB = opcaoB;
        this.opcaoC = opcaoC;
        this.opcaoD = opcaoD;
        this.opcaoE = opcaoE;
        this.imagem = imagem;
        this.testePrevio = testePrevio; //1 = sim | 0 = não
        this.conteudo = conteudo;
        this.alternativaCorreta = alternativaCorreta;
        this.opcaoEscolhida = opcaoEscolhida;
        this.nivelDificuldade = nivelDificuldade;
        this.questaoVestibular = questaoVestibular;
    }

    // Construtor 2 - sem id e alternativa correta
    public Pergunta(String enunciado, String opcaoA, String opcaoB, String opcaoC, String opcaoD, String opcaoE, byte[] imagem, int testePrevio, Conteudo conteudo, char alternativaCorreta, int nivelDificuldade, int questaoVestibular) {
        this.enunciado = enunciado;
        this.opcaoA = opcaoA;
        this.opcaoB = opcaoB;
        this.opcaoC = opcaoC;
        this.opcaoD = opcaoD;
        this.opcaoE = opcaoE;
        this.imagem = imagem;
        this.testePrevio = testePrevio;
        this.conteudo = conteudo;
        this.alternativaCorreta = alternativaCorreta;
        this.nivelDificuldade = nivelDificuldade;
        this.questaoVestibular = questaoVestibular;
    }

    // Construtor 3 - com tudo
    public Pergunta(int idQuiz, String enunciado, String opcaoA, String opcaoB, String opcaoC, String opcaoD, String opcaoE, byte[] imagem, int testePrevio, Conteudo conteudo, char alternativaCorreta, char opcaoEscolhida, int nivelDificuldade, int questaoVestibular) {
        this.idQuiz = idQuiz;
        this.enunciado = enunciado;
        this.opcaoA = opcaoA;
        this.opcaoB = opcaoB;
        this.opcaoC = opcaoC;
        this.opcaoD = opcaoD;
        this.opcaoE = opcaoE;
        this.imagem = imagem;
        this.testePrevio = testePrevio;
        this.conteudo = conteudo;
        this.alternativaCorreta = alternativaCorreta;
        this.opcaoEscolhida = opcaoEscolhida;
        this.nivelDificuldade = nivelDificuldade;
        this.questaoVestibular = questaoVestibular;
    }

    // Construtor 4 - sem opcaoEscolhida
    public Pergunta(int idQuiz, String enunciado, String opcaoA, String opcaoB, String opcaoC, String opcaoD, String opcaoE, byte[] imagem, int testePrevio, Conteudo conteudo, char alternativaCorreta, int nivelDificuldade, int questaoVestibular) {
        this.idQuiz = idQuiz;
        this.enunciado = enunciado;
        this.opcaoA = opcaoA;
        this.opcaoB = opcaoB;
        this.opcaoC = opcaoC;
        this.opcaoD = opcaoD;
        this.opcaoE = opcaoE;
        this.imagem = imagem;
        this.testePrevio = testePrevio;
        this.conteudo = conteudo;
        this.alternativaCorreta = alternativaCorreta;
        this.nivelDificuldade = nivelDificuldade;
        this.questaoVestibular = questaoVestibular;
    }

    public int getNivelDificuldade() {
        return nivelDificuldade;
    }

    public void setNivelDificuldade(int nivelDificuldade) {
        this.nivelDificuldade = nivelDificuldade;
    }

    public char getAlternativaCorreta() {
        return alternativaCorreta;
    }

    public void setAlternativaCorreta(char alternativaCorreta) {
        this.alternativaCorreta = alternativaCorreta;
    }

    public char getOpcaoEscolhida() {
        return opcaoEscolhida;
    }

    public void setOpcaoEscolhida(char opcaoEscolhida) {
        this.opcaoEscolhida = opcaoEscolhida;
    }

    public Conteudo getConteudo() {
        return conteudo;
    }

    public void setConteudo(Conteudo conteudo) {
        this.conteudo = conteudo;
    }

    public int getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(int idQuiz) {
        this.idQuiz = idQuiz;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getOpcaoA() {
        return opcaoA;
    }

    public void setOpcaoA(String opcaoA) {
        this.opcaoA = opcaoA;
    }

    public String getOpcaoB() {
        return opcaoB;
    }

    public void setOpcaoB(String opcaoB) {
        this.opcaoB = opcaoB;
    }

    public String getOpcaoC() {
        return opcaoC;
    }

    public void setOpcaoC(String opcaoC) {
        this.opcaoC = opcaoC;
    }

    public String getOpcaoD() {
        return opcaoD;
    }

    public void setOpcaoD(String opcaoD) {
        this.opcaoD = opcaoD;
    }

    public String getOpcaoE() {
        return opcaoE;
    }

    public void setOpcaoE(String opcaoE) {
        this.opcaoE = opcaoE;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public int getTestePrevio() {
        return testePrevio;
    }

    public void setTestePrevio(int testePrevio) {
        this.testePrevio = testePrevio;
    }

    public int getQuestaoVestibular() { return questaoVestibular; }

    public void setQuestaoVestibular(int questaoVestibular) { this.questaoVestibular = questaoVestibular; }
}
