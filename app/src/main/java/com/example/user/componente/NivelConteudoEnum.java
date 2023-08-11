package com.example.user.componente;

import java.io.Serializable;

//talvez tirar o serializable
public enum NivelConteudoEnum implements Serializable {
    COBRE (1), BRONZE(2), PRATA(3), OURO(4), DIAMANTE(5);

    private final int valor;

    NivelConteudoEnum(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return this.valor;
    }

}
