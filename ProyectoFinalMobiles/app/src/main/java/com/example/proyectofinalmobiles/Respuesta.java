package com.example.proyectofinalmobiles;

public class Respuesta {
    private int id;
    private String respuesta;
    private String estado;

    public void setId(int id) {
        this.id = id;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Respuesta(int id, String respuesta, String estado) {
        this.id = id;
        this.respuesta = respuesta;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public String getEstado() {
        return estado;
    }


    public boolean esCorrecta() {
        return estado.equalsIgnoreCase("correcta");
    }
}