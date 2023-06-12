package com.example.proyectofinalmobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ActivityPreguntas extends AppCompatActivity {

    private AdminSQLiteOpenHelper dbHelper;
    private List<Pregunta> listaPreguntas;
    private int count=0;
    public  Usuario  user;
    public   Experiencia  experiencia;
    private List<Respuesta> listaRespuesta;

    private int limite;

    public Intent intentSendRegresar;
    Button btnRegresar;

    public Integer dificultad;
    public Intent intentGet;
    public void setUser(){

        intentGet = getIntent();
        //obtener el usuario
        user = (Usuario) intentGet.getSerializableExtra("objUser");
        //obtener la experiencia
        experiencia = (Experiencia) intentGet.getSerializableExtra("objExperiencia");
        // Mostrar los datos en tus TextViews u otros elementos de la interfaz
        TextView textViewNombre = findViewById(R.id.textViewNombre);
        TextView textViewApodo = findViewById(R.id.textViewApodo);
        TextView textViewPuntos = findViewById(R.id.textViewPuntos);

        String puntoView ="Puntos: "+experiencia.getPunto();
        textViewPuntos.setText(puntoView);
        textViewNombre.setText("Usuario: "+user.getNombre());
        String apodoView = "Apodo: "+experiencia.getApodo();

        textViewApodo.setText(apodoView);

    }

    private void actualizarExperiencia(boolean respuestaCorrecta) {
        int usuarioId = user.getId();

        // Obtener los puntos y apodo actuales del usuario
        experiencia = dbHelper.obtenerExperienciaPorUsuarioId(usuarioId);
        int puntos = experiencia.getPunto();
        String apodo = experiencia.getApodo();

        // Actualizar los puntos según la respuesta
        if (respuestaCorrecta) {
            puntos += 2; // Sumar 1 punto
        } else {
            puntos -= 2; // Restar 1 punto
        }
        if(puntos<20){
            apodo="Principiante";
        }
        else if(puntos>=20 && puntos<40){
            apodo="Intermedio";
        }else if(puntos>=40 && puntos<60){
            apodo="Avanzado";
        }else if(puntos>=60 && puntos<80){
            apodo="Experto";
        }else if(puntos>=80 && puntos<96){
            apodo="Genio";
        }else if(puntos>=96 && puntos==94){
            apodo="Ingeniero";
        }

        // Actualizar la experiencia en la base de datos
        dbHelper.actualizarExperiencia(usuarioId, puntos, apodo);
        // Obtener nueva experiencia
        experiencia = dbHelper.obtenerExperienciaPorUsuarioId(usuarioId);
        // Actualizar el TextView de los puntos en la interfaz
        TextView textViewPuntos = findViewById(R.id.textViewPuntos);
        TextView textViewApodo = findViewById(R.id.textViewApodo);

        String puntoView ="Puntos: "+experiencia.getPunto();
        textViewPuntos.setText(puntoView);

        String apodoView = "Apodo: "+experiencia.getApodo();

        textViewApodo.setText(apodoView);
    }
    public void sendUseRegresar(){

        intentSendRegresar = new Intent(ActivityPreguntas.this, ActivityMundos.class);
        intentSendRegresar.putExtra("objExperiencia", experiencia);
        intentSendRegresar.putExtra("objUser", user);
        intentSendRegresar.putExtra("idDificultad", dificultad);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        TextView textViewPregunta = findViewById(R.id.textViewPregunta);
        ListView listViewRespuestas = findViewById(R.id.listViewRespuestas);

        TextView textViewPregunta2 = findViewById(R.id.textViewPregunta2);
        ListView listViewRespuestas2 = findViewById(R.id.listViewRespuestas2);

        TextView textViewPregunta3 = findViewById(R.id.textViewPregunta3);
        ListView listViewRespuestas3 = findViewById(R.id.listViewRespuestas3);

        TextView textViewPregunta4 = findViewById(R.id.textViewPregunta4);
        ListView listViewRespuestas4 = findViewById(R.id.listViewRespuestas4);

        TextView textViewTitulo = findViewById(R.id.textViewTitulo);

        btnRegresar = findViewById(R.id.btnRegresar);
        dbHelper = new AdminSQLiteOpenHelper(this, "miBaseDeDatos", null, 1);

        setUser();

        int idMundo = intentGet.getIntExtra("id", 0);

        dificultad = intentGet.getIntExtra("idDificultad", 0);

        limite =  intentGet.getIntExtra("limiteMundo", 0);

        // Obtener el Intent
        listaPreguntas = dbHelper.obtenerPreguntasPorMundoId(idMundo);

        //obtener mundo
        Mundo mundo = dbHelper.obtenerMundoPorId(idMundo);

        //Setear en el textView
        textViewTitulo.setText(mundo.getNombre());


        if (!listaPreguntas.isEmpty()) {
            Pregunta primeraPregunta = listaPreguntas.get(0);
            textViewPregunta.setText(primeraPregunta.getEnunciado());

            listaRespuesta = primeraPregunta.getRespuestas();


            listaRespuesta.get(0).setRespuesta("A) " + listaRespuesta.get(0).getRespuesta());
            listaRespuesta.get(1).setRespuesta("B) " + listaRespuesta.get(1).getRespuesta());
            listaRespuesta.get(2).setRespuesta("C) " + listaRespuesta.get(2).getRespuesta());

            RespuestaAdapter respuestaAdapter = new RespuestaAdapter(ActivityPreguntas.this, R.layout.item_respuesta, listaRespuesta);
            listViewRespuestas.setAdapter(respuestaAdapter);
        }

        if (listaPreguntas.size() > 1) {
            Pregunta segundaPregunta = listaPreguntas.get(1);


            textViewPregunta2.setText(segundaPregunta.getEnunciado());

            listaRespuesta = segundaPregunta.getRespuestas();

            listaRespuesta.get(0).setRespuesta("A) " + listaRespuesta.get(0).getRespuesta());
            listaRespuesta.get(1).setRespuesta("B) " + listaRespuesta.get(1).getRespuesta());
            listaRespuesta.get(2).setRespuesta("C) " + listaRespuesta.get(2).getRespuesta());

            RespuestaAdapter respuestaAdapter2 = new RespuestaAdapter(ActivityPreguntas.this, R.layout.item_respuesta, listaRespuesta);

            listViewRespuestas2.setAdapter(respuestaAdapter2);
        }

        if (listaPreguntas.size() > 2) {
            Pregunta terceraPregunta = listaPreguntas.get(2);
            textViewPregunta3.setText(terceraPregunta.getEnunciado());

            listaRespuesta = terceraPregunta.getRespuestas();
            listaRespuesta.get(0).setRespuesta("A) " + listaRespuesta.get(0).getRespuesta());
            listaRespuesta.get(1).setRespuesta("B) " + listaRespuesta.get(1).getRespuesta());
            listaRespuesta.get(2).setRespuesta("C) " + listaRespuesta.get(2).getRespuesta());
            RespuestaAdapter respuestaAdapter3 = new RespuestaAdapter(ActivityPreguntas.this, R.layout.item_respuesta, listaRespuesta);

            listViewRespuestas3.setAdapter(respuestaAdapter3);
        }

        if (listaPreguntas.size() > 3) {
            Pregunta cuartaPregunta = listaPreguntas.get(3);
            textViewPregunta4.setText(cuartaPregunta.getEnunciado());

            listaRespuesta = cuartaPregunta.getRespuestas();
            listaRespuesta.get(0).setRespuesta("A) " + listaRespuesta.get(0).getRespuesta());
            listaRespuesta.get(1).setRespuesta("B) " + listaRespuesta.get(1).getRespuesta());
            listaRespuesta.get(2).setRespuesta("C) " + listaRespuesta.get(2).getRespuesta());
            RespuestaAdapter respuestaAdapter4 = new RespuestaAdapter(ActivityPreguntas.this, R.layout.item_respuesta, listaRespuesta);

            listViewRespuestas4.setAdapter(respuestaAdapter4);
        }


        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUseRegresar();
                startActivity(intentSendRegresar);
                finish();
            }
        });

        listViewRespuestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Respuesta respuesta = listaRespuesta.get(position);

                if (respuesta.esCorrecta()) {


                    Toast.makeText(ActivityPreguntas.this, "Respuesta correcta", Toast.LENGTH_SHORT).show();



                    MediaPlayer mediaPlayer = MediaPlayer.create(ActivityPreguntas.this, R.raw.correcto);
                    mediaPlayer.start();

                    if(listaPreguntas.get(0).getEstadoPregunta().equals("Pendiente")){

                        dbHelper.actualizarEstadoPregunta(listaPreguntas.get(0).getId(),"Respondido");

                        actualizarExperiencia(true);

                        listaPreguntas = dbHelper.obtenerPreguntasPorMundoId(idMundo);

                    }else{
                        Toast.makeText(ActivityPreguntas.this, "Ya no puedes ganar mas puntos en esta pregunta", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    MediaPlayer mediaPlayer = MediaPlayer.create(ActivityPreguntas.this, R.raw.incorrecto);
                    mediaPlayer.start();
                    dbHelper.actualizarEstadoPregunta(listaPreguntas.get(0).getId(),"Pendiente");
                    Toast.makeText(ActivityPreguntas.this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();

                    actualizarExperiencia(false);
                    listaPreguntas.get(0).setEstadoPregunta("Pendiente");
                }
            }
        });

        listViewRespuestas2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Respuesta respuesta = listaRespuesta.get(position);

                if (respuesta.esCorrecta()) {
                    Toast.makeText(ActivityPreguntas.this, "Respuesta correcta", Toast.LENGTH_SHORT).show();


                    MediaPlayer mediaPlayer = MediaPlayer.create(ActivityPreguntas.this, R.raw.correcto);
                    mediaPlayer.start();

                    if(listaPreguntas.get(1).getEstadoPregunta().equals("Pendiente")){

                        dbHelper.actualizarEstadoPregunta(listaPreguntas.get(1).getId(),"Respondido");
                        listaPreguntas = dbHelper.obtenerPreguntasPorMundoId(idMundo);
                        actualizarExperiencia(true);

                    }else{
                        Toast.makeText(ActivityPreguntas.this, "Ya no puedes ganar mas puntos en esta pregunta", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    MediaPlayer mediaPlayer = MediaPlayer.create(ActivityPreguntas.this, R.raw.incorrecto);
                    mediaPlayer.start();
                    dbHelper.actualizarEstadoPregunta(listaPreguntas.get(1).getId(),"Pendiente");
                    Toast.makeText(ActivityPreguntas.this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
                    actualizarExperiencia(false);
                    listaPreguntas.get(1).setEstadoPregunta("Pendiente");
                }
            }
        });

        listViewRespuestas3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Respuesta respuesta = listaRespuesta.get(position);

                if (respuesta.esCorrecta()) {
                    Toast.makeText(ActivityPreguntas.this, "Respuesta correcta", Toast.LENGTH_SHORT).show();

                    MediaPlayer mediaPlayer = MediaPlayer.create(ActivityPreguntas.this, R.raw.correcto);
                    mediaPlayer.start();

                    if(listaPreguntas.get(2).getEstadoPregunta().equals("Pendiente")){

                            dbHelper.actualizarEstadoPregunta(listaPreguntas.get(2).getId(),"Respondido");
                            listaPreguntas = dbHelper.obtenerPreguntasPorMundoId(idMundo);
                            actualizarExperiencia(true);

                    }else{

                        Toast.makeText(ActivityPreguntas.this, "Ya no puedes ganar mas puntos en esta pregunta", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    MediaPlayer mediaPlayer = MediaPlayer.create(ActivityPreguntas.this, R.raw.incorrecto);
                    mediaPlayer.start();
                    dbHelper.actualizarEstadoPregunta(listaPreguntas.get(2).getId(),"Pendiente");
                    Toast.makeText(ActivityPreguntas.this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
                    listaPreguntas.get(2).setEstadoPregunta("Pendiente");
                    actualizarExperiencia(false);
                }
            }
        });

        listViewRespuestas4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Respuesta respuesta = listaRespuesta.get(position);

                if (respuesta.esCorrecta()) {
                    Toast.makeText(ActivityPreguntas.this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
                    MediaPlayer mediaPlayer = MediaPlayer.create(ActivityPreguntas.this, R.raw.correcto);
                    mediaPlayer.start();
                    if(listaPreguntas.get(3).getEstadoPregunta().equals("Pendiente")){

                            dbHelper.actualizarEstadoPregunta(listaPreguntas.get(3).getId(),"Respondido");
                            listaPreguntas = dbHelper.obtenerPreguntasPorMundoId(idMundo);
                            actualizarExperiencia(true);

                    }else {

                        Toast.makeText(ActivityPreguntas.this, "Ya no puedes ganar mas puntos en esta pregunta", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    MediaPlayer mediaPlayer = MediaPlayer.create(ActivityPreguntas.this, R.raw.incorrecto);
                    mediaPlayer.start();
                    dbHelper.actualizarEstadoPregunta(listaPreguntas.get(3).getId(),"Pendiente");
                    Toast.makeText(ActivityPreguntas.this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
                    actualizarExperiencia(false);
                    listaPreguntas.get(3).setEstadoPregunta("Pendiente");
                }
            }
        });
    }

}