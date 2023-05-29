package com.example.proyectofinalmobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ActivityPreguntas extends AppCompatActivity {

    private AdminSQLiteOpenHelper dbHelper;
    private List<Pregunta> listaPreguntas;

    private List<Respuesta> listaRespuesta;
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

        dbHelper = new AdminSQLiteOpenHelper(this, "miBaseDeDatos", null, 1);

        listaPreguntas = dbHelper.obtenerPreguntasPorMundoId(1);

        if (!listaPreguntas.isEmpty()) {
            Pregunta primeraPregunta = listaPreguntas.get(0);
            textViewPregunta.setText(primeraPregunta.getEnunciado());

            listaRespuesta = primeraPregunta.getRespuestas();
            RespuestaAdapter respuestaAdapter = new RespuestaAdapter(ActivityPreguntas.this, R.layout.item_respuesta, listaRespuesta);

            listViewRespuestas.setAdapter(respuestaAdapter);
        }

        if (listaPreguntas.size() > 1) {
            Pregunta segundaPregunta = listaPreguntas.get(1);
            textViewPregunta2.setText(segundaPregunta.getEnunciado());

            listaRespuesta = segundaPregunta.getRespuestas();
            RespuestaAdapter respuestaAdapter2 = new RespuestaAdapter(ActivityPreguntas.this, R.layout.item_respuesta, listaRespuesta);

            listViewRespuestas2.setAdapter(respuestaAdapter2);
        }

        if (listaPreguntas.size() > 2) {
            Pregunta terceraPregunta = listaPreguntas.get(2);
            textViewPregunta3.setText(terceraPregunta.getEnunciado());

            listaRespuesta = terceraPregunta.getRespuestas();
            RespuestaAdapter respuestaAdapter3 = new RespuestaAdapter(ActivityPreguntas.this, R.layout.item_respuesta, listaRespuesta);

            listViewRespuestas3.setAdapter(respuestaAdapter3);
        }

        if (listaPreguntas.size() > 3) {
            Pregunta cuartaPregunta = listaPreguntas.get(3);
            textViewPregunta4.setText(cuartaPregunta.getEnunciado());

            listaRespuesta = cuartaPregunta.getRespuestas();
            RespuestaAdapter respuestaAdapter4 = new RespuestaAdapter(ActivityPreguntas.this, R.layout.item_respuesta, listaRespuesta);

            listViewRespuestas4.setAdapter(respuestaAdapter4);
        }

    }
}