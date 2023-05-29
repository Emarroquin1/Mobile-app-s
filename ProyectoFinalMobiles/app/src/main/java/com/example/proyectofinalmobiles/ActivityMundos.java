package com.example.proyectofinalmobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ActivityMundos extends AppCompatActivity {

    private List<Pregunta> listaPreguntas;
    private AdminSQLiteOpenHelper dbHelper;
    private List<Respuesta> listaRespuesta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mundos);
        TextView textViewDificultad = findViewById(R.id.textViewDificultad);

        Button  buttonMundo1 = findViewById(R.id.buttonMundo1);
        Intent intent = getIntent();

        Integer idDificultad = intent.getIntExtra("idDificultad", 0);

        buttonMundo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mundoId = 1; // Establece el ID del mundo deseado

                Intent intent = new Intent(ActivityMundos.this, ActivityPreguntas.class);


                intent.putExtra("id", mundoId);




                // Iniciar la MainActivity
                startActivity(intent);
                finish();

            }
        });
        if (idDificultad == 1) {

            textViewDificultad.setText("Fácil");




        } else if (idDificultad == 2) {

            textViewDificultad.setText("Intermedio");

        } else if (idDificultad == 3) {

            textViewDificultad.setText("Difícil");
        }



    }
}