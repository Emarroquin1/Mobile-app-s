package com.example.proyectofinalmobiles;

import java.io.Serializable;

public class Usuario   implements Serializable {
    private int id;
    private String nombre;
    private String contraseña;

    public Usuario(int id, String nombre, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.contraseña = contraseña;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContraseña() {
        return contraseña;
    }
}
