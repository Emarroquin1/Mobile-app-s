package com.example.proyectofinalmobiles;


import java.util.ArrayList;
import java.util.List;

public class Pregunta {
    private int id;
    private String enunciado;

    private String estadoPregunta;

    public void setId(int id) {
        this.id = id;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public void setEstadoPregunta(String estadoPregunta) {
        this.estadoPregunta = estadoPregunta;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    private List<Respuesta> respuestas;

    public Pregunta(int id, String enunciado, String estadoPregunta) {
        this.id = id;
        this.enunciado = enunciado;
        this.respuestas = new ArrayList<>();
        this.estadoPregunta = estadoPregunta;
    }

    public int getId() {
        return id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getEstadoPregunta() {
        return estadoPregunta;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void agregarRespuesta(Respuesta respuesta) {
        respuestas.add(respuesta);
    }
}



