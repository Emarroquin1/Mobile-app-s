package com.example.proyectofinalmobiles;

import java.io.Serializable;

public class Experiencia implements Serializable {
    private int punto;
    private String apodo;

    public Experiencia(int punto, String apodo) {
        this.punto = punto;
        this.apodo = apodo;
    }

    public int getPunto() {
        return punto;
    }

    public String getApodo() {
        return apodo;
    }
}
