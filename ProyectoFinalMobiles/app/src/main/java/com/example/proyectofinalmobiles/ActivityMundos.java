package com.example.proyectofinalmobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class ActivityMundos extends AppCompatActivity {

    private List<Pregunta> listaPreguntas;
    private AdminSQLiteOpenHelper dbHelper;
    private List<Respuesta> listaRespuesta;
    ImageButton buttonMundo1,buttonMundo2,buttonMundo3,buttonMundo4;

    Button btnRegresar;

     ;
    int mundoId;
    public  Usuario  user;
    public   Experiencia  experiencia;

    public Intent intentSend, intentSendRegresar;
    public Intent intentGet;
    public Integer dificultad;

    public Integer limite, limiteMax;
    TextView textViewPuntos;
    public void setUser(){

        // Obtener los datos del Intent
        intentGet = getIntent();
        //obtener el usuario
        user = (Usuario) intentGet.getSerializableExtra("objUser");
        //obtener la experiencia
        experiencia= (Experiencia) intentGet.getSerializableExtra("objExperiencia");
        // Mostrar los datos en tus TextViews u otros elementos de la interfaz
        TextView textViewNombre = findViewById(R.id.textViewNombre);
        TextView textViewApodo = findViewById(R.id.textViewApodo);

        textViewPuntos = findViewById(R.id.textViewPuntos);
        String puntoView ="Puntos: "+experiencia.getPunto();
        textViewPuntos.setText(puntoView);

        textViewNombre.setText(user.getNombre());
        textViewApodo.setText("Apodo: "+experiencia.getApodo());

    }

    public void sendUser(){
        intentSend = new Intent(ActivityMundos.this, ActivityPreguntas.class);
        intentSend.putExtra("objExperiencia", experiencia);
        intentSend.putExtra("objUser", user);
        intentSend.putExtra("idDificultad", dificultad);

    }

    public void sendUseRegresar(){
        intentSendRegresar = new Intent(ActivityMundos.this, ActivityDificultad.class);


        intentSendRegresar.putExtra("objExperiencia", experiencia);
        intentSendRegresar.putExtra("objUser", user);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mundos);
        TextView textViewDificultad = findViewById(R.id.textViewDificultad);

        buttonMundo1 = findViewById(R.id.buttonMundo1);
        buttonMundo2 = findViewById(R.id.buttonMundo2);
        buttonMundo3 = findViewById(R.id.buttonMundo3);
        buttonMundo4 = findViewById(R.id.buttonMundo4);
        btnRegresar = findViewById(R.id.btnRegresar);

        setUser();
        dificultad = intentGet.getIntExtra("idDificultad", 0);

        sendUser();

        if (dificultad == 1) {

            textViewDificultad.setText("FACIL");
            limite=20;
            mundoId=1;



        } else if (dificultad == 2) {
            mundoId=4;
            limite=60;


            textViewDificultad.setText("INTERMEDIO");

        } else if (dificultad == 3) {
            mundoId=8;
            limite=100;


            textViewDificultad.setText("DIFICIL");
        }

        //Funcionalidad Boton Mundo 1
        buttonMundo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(limite>20 ){

                    limite=limite-30;

                }else if(limite<=20){

                    limite=limite-15;
                }
                int mundo1Id = mundoId; // Establece el ID del mundo deseado
                intentSend.putExtra("id", mundo1Id);
                intentSend.putExtra("limiteMundo", limite);
                // Iniciar la MainActivity
                startActivity(intentSend);
                finish();
            }
        });
        //Funcionalidad Boton Mundo 2
        buttonMundo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Evaluar limite y tambien el usuario solo podra ganar esa cantidad de puntos
                if(limite>20){

                    limite=limite-20;

                }else if(limite<=20){

                    limite=limite-10;
                }

                int mundoI2d = mundoId+2; // Establece el ID del mundo deseado
                intentSend.putExtra("id", mundoI2d);
                intentSend.putExtra("limiteMundo", limite);
                //iniciar la ActivityMundos
                startActivity(intentSend);
                finish();
            }
        });
        //Funcionalidad Boton Mundo 3
        buttonMundo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(limite>20){

                    limite=limite-10;

                }else if(limite<=20){

                    limite=limite-5;
                }

                int mundo3Id = mundoId+3; // Establece el ID del mundo deseado
                intentSend.putExtra("id",mundo3Id);
                intentSend.putExtra("limiteMundo", limite);
                //intentSend.putExtra("limiteMaximo");
                //iniciar la activityMundos
                startActivity(intentSend);
                finish();
            }
        });
        //Funcionalidad Boton Mundo 4
        buttonMundo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mundo4Id = mundoId+4; // Establece el ID del mundo deseado
                intentSend.putExtra("id",mundo4Id);
                intentSend.putExtra("limiteMundo", limite);
                //iniciar la activityMundos
                startActivity(intentSend);
                finish();
            }
        });

            btnRegresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendUseRegresar();
                    startActivity(intentSendRegresar);
                    finish();
                }
            });

    }

}