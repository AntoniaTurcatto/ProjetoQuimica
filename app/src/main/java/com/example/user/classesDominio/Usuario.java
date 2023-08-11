package com.example.user.classesDominio;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int idUsuario;
    private String nomeCompleto;
    private String nomeUsuario;
    private String senha;
    private String email;
    private long cpf;

    public Usuario(String nomeUsuario, String senha) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
    }

    public Usuario(int idUsuario, String nomeCompleto, String nomeUsuario, String senha, String email, long cpf) {
        this.idUsuario = idUsuario;
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.email = email;
        this.cpf = cpf;
    }

    public Usuario(String nomeCompleto, String nomeUsuario, String senha, String email, long cpf) {
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.email = email;
        this.cpf = cpf;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public long getCpf() { return cpf; }

    public void setCpf(long cpf) { this.cpf = cpf; }
}
