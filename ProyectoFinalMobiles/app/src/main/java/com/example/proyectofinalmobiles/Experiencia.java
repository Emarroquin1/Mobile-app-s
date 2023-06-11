package com.example.proyectofinalmobiles;

import java.io.Serializable;

public class Experiencia implements Serializable {
    private int punto;

    public void setPunto(int punto) {
        this.punto = punto;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

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
