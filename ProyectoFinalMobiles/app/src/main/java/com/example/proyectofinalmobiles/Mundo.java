package com.example.proyectofinalmobiles;

public class Mundo {
    private int id;
    private int punto;
    private String nombre;

    public Mundo(int id, int punto, String nombre) {
        this.id = id;
        this.punto = punto;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public int getPunto() {
        return punto;
    }

    public String getNombre() {
        return nombre;
    }
}