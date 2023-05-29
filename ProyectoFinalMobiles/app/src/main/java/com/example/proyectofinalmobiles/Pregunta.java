package com.example.proyectofinalmobiles;


import java.util.ArrayList;
import java.util.List;

public class Pregunta {
    private int id;
    private String enunciado;
    private List<Respuesta> respuestas;

    public Pregunta(int id, String enunciado) {
        this.id = id;
        this.enunciado = enunciado;
        this.respuestas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void agregarRespuesta(Respuesta respuesta) {
        respuestas.add(respuesta);
    }
}



