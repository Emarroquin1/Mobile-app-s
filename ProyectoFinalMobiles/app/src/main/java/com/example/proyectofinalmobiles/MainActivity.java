package com.example.proyectofinalmobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonMundo1;
    private Button buttonPregunta1;
    private TextView textViewPregunta;
    private AdminSQLiteOpenHelper dbHelper;

    private List<Pregunta> listaPreguntas;
    private List<Respuesta> listaRespuesta;
    private List<Mundo> listaMundos;
    private static MainActivity instance;
    int puntosMundo=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMundo1 = findViewById(R.id.buttonMundo1);
        buttonPregunta1 = findViewById(R.id.buttonPregunta1);
        ListView listViewRespuestas = findViewById(R.id.listViewRespuestas);

        textViewPregunta = findViewById(R.id.textViewPregunta);
        dbHelper = new AdminSQLiteOpenHelper(this, "miBaseDeDatos", null, 1);


        dbHelper.borrarRegistros();
        insertarDatos();

        // Obtener los datos del Intent
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        String apodo = intent.getStringExtra("apodo");
        int puntos = intent.getIntExtra("puntos", 0);
        int id = intent.getIntExtra("id", 0);

        // Mostrar los datos en tus TextViews u otros elementos de la interfaz
        TextView textViewNombre = findViewById(R.id.textViewNombre);
        TextView textViewApodo = findViewById(R.id.textViewApodo);
        TextView textViewPuntos = findViewById(R.id.textViewPuntos);


        textViewNombre.setText(nombre);
        textViewApodo.setText(apodo);
        textViewPuntos.setText(String.valueOf(puntos));
        instance = this;
buttonMundo1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        int mundoId = 1; // Establece el ID del mundo deseado

        puntosMundo = dbHelper.obtenerPuntosMundo(mundoId);

        if (puntosMundo != -1) {
            Toast.makeText(MainActivity.this, "Puntos que puedes ganar en el mundo: " + puntosMundo, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "No se encontraron puntos para el mundo especificado", Toast.LENGTH_SHORT).show();
        }

    }
});
/*
        String registros = dbHelper.obtenerRegistros("Pregunta");
        listaPreguntas = new ArrayList<>();
        int mundoId = 1; // Aquí debes establecer el ID del mundo deseado
        listaPreguntas = dbHelper.obtenerPreguntasPorMundoId(mundoId); //lista de preguntas con su respectiva respuestas

        int nivelId = 1; // Aquí debes establecer el ID del mundo deseado
        listaMundos = dbHelper.obtenerMundosPorNivel(mundoId); //lista de preguntas con su respectiva respuestas
*/buttonPregunta1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        int mundoId = 1; // Establece el ID del mundo deseado

        listaPreguntas = dbHelper.obtenerPreguntasPorMundoId(mundoId);

        if (!listaPreguntas.isEmpty()) {
            Pregunta primeraPregunta = listaPreguntas.get(0);
            textViewPregunta.setText(primeraPregunta.getEnunciado());

            listaRespuesta = primeraPregunta.getRespuestas();
            RespuestaAdapter respuestaAdapter = new RespuestaAdapter(MainActivity.this, R.layout.item_respuesta, listaRespuesta);

            listViewRespuestas.setAdapter(respuestaAdapter);
        }
    }
});
        listViewRespuestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Respuesta respuesta = listaRespuesta.get(position);

                if (respuesta.esCorrecta()) {
                    Toast.makeText(MainActivity.this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
                    TextView textViewPuntos = findViewById(R.id.textViewPuntos);
                    Integer puntos = Integer.parseInt(textViewPuntos.getText().toString());

                    if(puntos<5){
                        actualizarExperiencia(true);
                    }else{
                        Toast.makeText(MainActivity.this, "Ya no puedes ganar mas puntos en este mundo", Toast.LENGTH_SHORT).show();
                    }



                } else {
                    Toast.makeText(MainActivity.this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
                    actualizarExperiencia(false);
                }
            }
        });

    }


    private void actualizarExperiencia(boolean respuestaCorrecta) {
        int usuarioId = getIntent().getIntExtra("id", 0);

        // Obtener los puntos y apodo actuales del usuario
        Experiencia experiencia = dbHelper.obtenerExperienciaPorUsuarioId(usuarioId);
        int puntos = experiencia.getPunto();
        String apodo = experiencia.getApodo();

        // Actualizar los puntos según la respuesta
        if (respuestaCorrecta) {
            puntos += 1; // Sumar 1 punto
        } else {
            puntos -= 1; // Restar 1 punto
        }

        // Actualizar la experiencia en la base de datos
        dbHelper.actualizarExperiencia(usuarioId, puntos, apodo);

        // Actualizar el TextView de los puntos en la interfaz
        TextView textViewPuntos = findViewById(R.id.textViewPuntos);
        textViewPuntos.setText(String.valueOf(puntos));
    }

    public void actualizarPuntos(int puntos) {
        TextView textViewPuntos = findViewById(R.id.textViewPuntos);
        textViewPuntos.setText(String.valueOf(puntos));
    }
    public static MainActivity getInstance() {
        return instance;
    }
    private void insertarDatos() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Obtener el ID del nivel con nombre 'Facil'
        Cursor cursor = db.rawQuery("SELECT id FROM Nivel WHERE nombre = 'Facil'", null);
        int nivelId = -1;
        if (cursor.moveToFirst()) {
            nivelId = cursor.getInt(0);
        }



        if (nivelId != -1) {
            // Insertar registros por defecto en la tabla Mundo
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (5, 'Suma entre 1 numero', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (5, 'Sumas entre 2 numeros', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (5, 'Resta entre 1 numero', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (5, 'Restas entre 2 numeros', " + nivelId + ")");
        }

        cursor = db.rawQuery("SELECT id FROM Nivel WHERE nombre = 'Intermedio'", null);
        nivelId = -1;
        if (cursor.moveToFirst()) {
            nivelId = cursor.getInt(0);
        }


        if (nivelId != -1) {
            // Insertar registros por defecto en la tabla Mundo relacionados con el nivel intermedio
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (10, 'Multiplicacion entre 1 numero', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (10, 'Multiplicaciones entre 2 numeros', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (10, 'Division entre 1 numero', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (10, 'Divisiones entre 2 numeros', " + nivelId + ")");
        }
        cursor = db.rawQuery("SELECT id FROM Nivel WHERE nombre = 'Dificil'", null);
        nivelId = -1;

        if (cursor.moveToFirst()) {
            nivelId = cursor.getInt(0);
        }
        cursor.close();

        if (nivelId != -1) {
            // Insertar registros por defecto en la tabla Mundo relacionados con el nivel Dificil
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (10, 'Multiplicaciones y Sumas', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (10, 'Multiplicaciones y Restas', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (10, 'Divisiones y sumas', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (10, 'Divisiones y Restas', " + nivelId + ")");
        }

        // Obtener el ID del mundo con nombre 'Suma entre 1 numero'
        Cursor cursorMundo = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Suma entre 1 numero'", null);
        int mundoId = -1;
        if (cursorMundo.moveToFirst()) {
            mundoId = cursorMundo.getInt(0);
        }
        cursorMundo.close();

        if (mundoId != -1) {
            // Insertar preguntas y respuestas en la tabla Pregunta y Respuesta
            // Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 7 + 4?', " + mundoId + ")");
            int pregunta1Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('11', 'CORRECTA', " + pregunta1Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'INCORRECTA', " + pregunta1Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'INCORRECTA', " + pregunta1Id + ")");

            // Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 3 + 9?', " + mundoId + ")");
            int pregunta2Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('12', 'CORRECTA', " + pregunta2Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'INCORRECTA', " + pregunta2Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('14', 'INCORRECTA', " + pregunta2Id + ")");

            // Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 5 + 2?', " + mundoId + ")");
            int pregunta3Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('7', 'CORRECTA', " + pregunta3Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta3Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('9', 'INCORRECTA', " + pregunta3Id + ")");

            // Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 1 + 6?', " + mundoId + ")");
            int pregunta4Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('7', 'CORRECTA', " + pregunta4Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'INCORRECTA', " + pregunta4Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'INCORRECTA', " + pregunta4Id + ")");

            // Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 9 + 1?', " + mundoId + ")");
            int pregunta5Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'CORRECTA', " + pregunta5Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'INCORRECTA', " + pregunta5Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('12', 'INCORRECTA', " + pregunta5Id + ")");

        } else {
            // No se encontró el mundo 'Sumas de 1 cifra'
        }


        // Obtener el ID del mundo con nombre 'Sumas entre 2 numeros'
        cursorMundo = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Sumas entre 2 numeros'", null);
        mundoId = -1;
        if (cursorMundo.moveToFirst()) {
            mundoId = cursorMundo.getInt(0);
        }
        cursorMundo.close();

        if (mundoId != -1) {
            // Insertar preguntas y respuestas en la tabla Pregunta y Respuesta
            // Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 25 + 13?', " + mundoId + ")");
            int pregunta1Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('38', 'CORRECTA', " + pregunta1Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('41', 'INCORRECTA', " + pregunta1Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('32', 'INCORRECTA', " + pregunta1Id + ")");

// Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 57 + 29?', " + mundoId + ")");
            int pregunta2Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('86', 'CORRECTA', " + pregunta2Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('75', 'INCORRECTA', " + pregunta2Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('91', 'INCORRECTA', " + pregunta2Id + ")");

// Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 42 + 19?', " + mundoId + ")");
            int pregunta3Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('61', 'CORRECTA', " + pregunta3Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('50', 'INCORRECTA', " + pregunta3Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('54', 'INCORRECTA', " + pregunta3Id + ")");

// Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 68 + 33?', " + mundoId + ")");
            int pregunta4Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('101', 'CORRECTA', " + pregunta4Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('92', 'INCORRECTA', " + pregunta4Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('115', 'INCORRECTA', " + pregunta4Id + ")");

// Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 39 + 27?', " + mundoId + ")");
            int pregunta5Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('66', 'CORRECTA', " + pregunta5Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('54', 'INCORRECTA', " + pregunta5Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('72', 'INCORRECTA', " + pregunta5Id + ")");
        } else {
            // No se encontró el mundo 'Sumas entre 2 numeros'
        }

        cursorMundo = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Resta entre 1 numero'", null);
        mundoId = -1;
        if (cursorMundo.moveToFirst()) {
            mundoId = cursorMundo.getInt(0);
        }
        cursorMundo.close();

        if (mundoId != -1) {

            // Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 9 - 4?', " + mundoId + ")");
            int pregunta1Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'CORRECTA', " + pregunta1Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta1Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'INCORRECTA', " + pregunta1Id + ")");

// Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 8 - 3?', " + mundoId + ")");
            int pregunta2Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'CORRECTA', " + pregunta2Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'INCORRECTA', " + pregunta2Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('3', 'INCORRECTA', " + pregunta2Id + ")");

// Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 7 - 2?', " + mundoId + ")");
            int pregunta3Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'CORRECTA', " + pregunta3Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'INCORRECTA', " + pregunta3Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('3', 'INCORRECTA', " + pregunta3Id + ")");

// Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 6 - 1?', " + mundoId + ")");
            int pregunta4Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'CORRECTA', " + pregunta4Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'INCORRECTA', " + pregunta4Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('2', 'INCORRECTA', " + pregunta4Id + ")");

// Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 5 - 1?', " + mundoId + ")");
            int pregunta5Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'CORRECTA', " + pregunta5Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'INCORRECTA', " + pregunta5Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('3', 'INCORRECTA', " + pregunta5Id + ")");


        } else {
            // No se encontró el mundo 'Restas de 1 número'
        }

        // Obtener el ID del mundo con nombre 'Restas entre 2 números'
        cursorMundo = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Restas entre 2 numeros'", null);
        mundoId = -1;
        if (cursorMundo.moveToFirst()) {
            mundoId = cursorMundo.getInt(0);
        }
        cursorMundo.close();

        if (mundoId != -1) {
            // Insertar preguntas y respuestas en la tabla Pregunta y Respuesta

            // Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 30 - 20?', " + mundoId + ")");
            int pregunta1Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'CORRECTA', " + pregunta1Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'INCORRECTA', " + pregunta1Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('20', 'INCORRECTA', " + pregunta1Id + ")");

            // Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 50 - 25?', " + mundoId + ")");
            int pregunta2Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('25', 'CORRECTA', " + pregunta2Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('15', 'INCORRECTA', " + pregunta2Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'INCORRECTA', " + pregunta2Id + ")");

            // Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 100 - 75?', " + mundoId + ")");
            int pregunta3Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('25', 'CORRECTA', " + pregunta3Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('50', 'INCORRECTA', " + pregunta3Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('15', 'INCORRECTA', " + pregunta3Id + ")");

            // Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 80 - 40?', " + mundoId + ")");
            int pregunta4Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('40', 'CORRECTA', " + pregunta4Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('30', 'INCORRECTA', " + pregunta4Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('20', 'INCORRECTA', " + pregunta4Id + ")");

            // Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 60 - 50?', " + mundoId + ")");
            int pregunta5Id = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'CORRECTA', " + pregunta5Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'INCORRECTA', " + pregunta5Id + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('20', 'INCORRECTA', " + pregunta5Id + ")");

        } else {
            // No se encontró el mundo 'Restas entre 2 números'
        }


        Cursor cursorMundoMultiplicacion1 = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Multiplicacion entre 1 numero'", null);
        int mundoMultiplicacion1Id = -1;
        if (cursorMundoMultiplicacion1.moveToFirst()) {
            mundoMultiplicacion1Id = cursorMundoMultiplicacion1.getInt(0);
        }


        if (mundoMultiplicacion1Id != -1) {
            // Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 7 * 3?', " + mundoMultiplicacion1Id + ")");
            int pregunta1IdMultiplicacion1 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('21', 'CORRECTA', " + pregunta1IdMultiplicacion1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('24', 'INCORRECTA', " + pregunta1IdMultiplicacion1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('18', 'INCORRECTA', " + pregunta1IdMultiplicacion1 + ")");

            // Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 5 * 4?', " + mundoMultiplicacion1Id + ")");
            int pregunta2IdMultiplicacion1 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('20', 'CORRECTA', " + pregunta2IdMultiplicacion1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('16', 'INCORRECTA', " + pregunta2IdMultiplicacion1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('25', 'INCORRECTA', " + pregunta2IdMultiplicacion1 + ")");

            // Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 6 * 2?', " + mundoMultiplicacion1Id + ")");
            int pregunta3IdMultiplicacion1 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('12', 'CORRECTA', " + pregunta3IdMultiplicacion1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'INCORRECTA', " + pregunta3IdMultiplicacion1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'INCORRECTA', " + pregunta3IdMultiplicacion1 + ")");

// Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 9 * 1?', " + mundoMultiplicacion1Id + ")");
            int pregunta4IdMultiplicacion1 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('9', 'CORRECTA', " + pregunta4IdMultiplicacion1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'INCORRECTA', " + pregunta4IdMultiplicacion1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('7', 'INCORRECTA', " + pregunta4IdMultiplicacion1 + ")");

// Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 3 * 5?', " + mundoMultiplicacion1Id + ")");
            int pregunta5IdMultiplicacion1 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('15', 'CORRECTA', " + pregunta5IdMultiplicacion1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('12', 'INCORRECTA', " + pregunta5IdMultiplicacion1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('18', 'INCORRECTA', " + pregunta5IdMultiplicacion1 + ")");

        } else {
            // No se encontró el mundo 'Multiplicación entre 1 número'
        }


        cursorMundoMultiplicacion1 = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Multiplicaciones entre 2 numeros'", null);
        int mundoMultiplicacion2Id = -1;
        if (cursorMundoMultiplicacion1.moveToFirst()) {
            mundoMultiplicacion2Id = cursorMundoMultiplicacion1.getInt(0);
        }
        cursorMundoMultiplicacion1.close();

        if (mundoMultiplicacion2Id != -1) {

            // Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 10 * 20?', " + mundoMultiplicacion2Id + ")");
            int pregunta1IdMultiplicacion2 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('200', 'CORRECTA', " + pregunta1IdMultiplicacion2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('100', 'INCORRECTA', " + pregunta1IdMultiplicacion2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('300', 'INCORRECTA', " + pregunta1IdMultiplicacion2 + ")");

// Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 15 * 25?', " + mundoMultiplicacion2Id + ")");
            int pregunta2IdMultiplicacion2 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('375', 'CORRECTA', " + pregunta2IdMultiplicacion2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('275', 'INCORRECTA', " + pregunta2IdMultiplicacion2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('425', 'INCORRECTA', " + pregunta2IdMultiplicacion2 + ")");

// Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 18 * 14?', " + mundoMultiplicacion2Id + ")");
            int pregunta3IdMultiplicacion2 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('252', 'CORRECTA', " + pregunta3IdMultiplicacion2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('162', 'INCORRECTA', " + pregunta3IdMultiplicacion2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('348', 'INCORRECTA', " + pregunta3IdMultiplicacion2 + ")");

// Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 12 * 18?', " + mundoMultiplicacion2Id + ")");
            int pregunta4IdMultiplicacion2 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('216', 'CORRECTA', " + pregunta4IdMultiplicacion2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('156', 'INCORRECTA', " + pregunta4IdMultiplicacion2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('282', 'INCORRECTA', " + pregunta4IdMultiplicacion2 + ")");

// Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 22 * 15?', " + mundoMultiplicacion2Id + ")");
            int pregunta5IdMultiplicacion2 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('330', 'CORRECTA', " + pregunta5IdMultiplicacion2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('240', 'INCORRECTA', " + pregunta5IdMultiplicacion2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('420', 'INCORRECTA', " + pregunta5IdMultiplicacion2 + ")");


        } else {

        }


        Cursor cursorMundoDivision1 = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Division entre 1 numero'", null);
        int mundoDivision1Id = -1;
        if (cursorMundoDivision1.moveToFirst()) {
            mundoDivision1Id = cursorMundoDivision1.getInt(0);
        }
        cursorMundoDivision1.close();
        if (mundoDivision1Id != -1) {
// Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 20 / 5?', " + mundoDivision1Id + ")");
            int pregunta1IdDivision1 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'CORRECTA', " + pregunta1IdDivision1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'INCORRECTA', " + pregunta1IdDivision1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('3', 'INCORRECTA', " + pregunta1IdDivision1 + ")");

// Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 15 / 3?', " + mundoDivision1Id + ")");
            int pregunta2IdDivision1 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'CORRECTA', " + pregunta2IdDivision1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'INCORRECTA', " + pregunta2IdDivision1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta2IdDivision1 + ")");

// Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 30 / 6?', " + mundoDivision1Id + ")");
            int pregunta3IdDivision1 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'CORRECTA', " + pregunta3IdDivision1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'INCORRECTA', " + pregunta3IdDivision1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta3IdDivision1 + ")");
// Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 50 / 10?', " + mundoDivision1Id + ")");
            int pregunta4IdDivision1 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'CORRECTA', " + pregunta4IdDivision1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'INCORRECTA', " + pregunta4IdDivision1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta4IdDivision1 + ")");

// Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 100 / 20?', " + mundoDivision1Id + ")");
            int pregunta5IdDivision1 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'CORRECTA', " + pregunta5IdDivision1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'INCORRECTA', " + pregunta5IdDivision1 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta5IdDivision1 + ")");


        } else {

        }

        Cursor cursorMundoDivision2 = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Divisiones entre 2 numeros'", null);
        int mundoDivision2Id = -1;
        if (cursorMundoDivision2.moveToFirst()) {
            mundoDivision2Id = cursorMundoDivision2.getInt(0);
        }
        cursorMundoDivision2.close();
        if (mundoDivision2Id != -1) {

            // Mundo: Divisiones entre 2 números
// Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 24 / 3?', " + mundoDivision2Id + ")");
            int pregunta1IdDivision2 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'CORRECTA', " + pregunta1IdDivision2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta1IdDivision2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'INCORRECTA', " + pregunta1IdDivision2 + ")");

// Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 36 / 4?', " + mundoDivision2Id + ")");
            int pregunta2IdDivision2 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('9', 'CORRECTA', " + pregunta2IdDivision2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'INCORRECTA', " + pregunta2IdDivision2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta2IdDivision2 + ")");

// Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 48 / 6?', " + mundoDivision2Id + ")");
            int pregunta3IdDivision2 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'CORRECTA', " + pregunta3IdDivision2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('7', 'INCORRECTA', " + pregunta3IdDivision2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('9', 'INCORRECTA', " + pregunta3IdDivision2 + ")");

// Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 60 / 5?', " + mundoDivision2Id + ")");
            int pregunta4IdDivision2 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('12', 'CORRECTA', " + pregunta4IdDivision2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'INCORRECTA', " + pregunta4IdDivision2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'INCORRECTA', " + pregunta4IdDivision2 + ")");

// Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 72 / 9?', " + mundoDivision2Id + ")");
            int pregunta5IdDivision2 = dbHelper.getLastInsertedId(db, "Pregunta");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'CORRECTA', " + pregunta5IdDivision2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta5IdDivision2 + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'INCORRECTA', " + pregunta5IdDivision2 + ")");


        } else {

        }

        Cursor cursorMundoDificil1 = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Multiplicaciones y Sumas'", null);
        int mundoMulSumasId = -1;
        if (cursorMundoDificil1.moveToFirst()) {
            mundoMulSumasId = cursorMundoDificil1.getInt(0);
        }
        cursorMundoDificil1.close();
        if (mundoMulSumasId != -1) {
// Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 3 + (4 * 2)?', " + mundoMulSumasId + ")");
            int pregunta1IdMulSumas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 1
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('11', 'CORRECTA', " + pregunta1IdMulSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('15', 'INCORRECTA', " + pregunta1IdMulSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('9', 'INCORRECTA', " + pregunta1IdMulSumas + ")");

// Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es (5 * 3) + 2?', " + mundoMulSumasId + ")");
            int pregunta2IdMulSumas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 2
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('17', 'CORRECTA', " + pregunta2IdMulSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('11', 'INCORRECTA', " + pregunta2IdMulSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('9', 'INCORRECTA', " + pregunta2IdMulSumas + ")");

// Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es (2 + 3) * 4?', " + mundoMulSumasId + ")");
            int pregunta3IdMulSumas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 3
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('20', 'CORRECTA', " + pregunta3IdMulSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('14', 'INCORRECTA', " + pregunta3IdMulSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('18', 'INCORRECTA', " + pregunta3IdMulSumas + ")");

// Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 7 * (4 + 1)?', " + mundoMulSumasId + ")");
            int pregunta4IdMulSumas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 4
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('35', 'CORRECTA', " + pregunta4IdMulSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('28', 'INCORRECTA', " + pregunta4IdMulSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('30', 'INCORRECTA', " + pregunta4IdMulSumas + ")");

// Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es (6 * 2) + 5?', " + mundoMulSumasId + ")");
            int pregunta5IdMulSumas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 5
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('17', 'CORRECTA', " + pregunta5IdMulSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('22', 'INCORRECTA', " + pregunta5IdMulSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('14', 'INCORRECTA', " + pregunta5IdMulSumas + ")");

        } else {

        }
        Cursor cursorMundoDificil2 = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Multiplicaciones y Restas'", null);
        int mundoMulRestasId = -1;
        if (cursorMundoDificil2.moveToFirst()) {
            mundoMulRestasId = cursorMundoDificil2.getInt(0);
        }
        cursorMundoDificil2.close();
        if (mundoMulRestasId != -1) {
// Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 5 - (3 * 2)?', " + mundoMulRestasId + ")");
            int pregunta1IdMulRestas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 1
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('-1', 'CORRECTA', " + pregunta1IdMulRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('-7', 'INCORRECTA', " + pregunta1IdMulRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('1', 'INCORRECTA', " + pregunta1IdMulRestas + ")");

// Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es (10 * 2) - 3?', " + mundoMulRestasId + ")");
            int pregunta2IdMulRestas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 2
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('17', 'CORRECTA', " + pregunta2IdMulRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('11', 'INCORRECTA', " + pregunta2IdMulRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('13', 'INCORRECTA', " + pregunta2IdMulRestas + ")");

// Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es (7 - 2) * 4?', " + mundoMulRestasId + ")");
            int pregunta3IdMulRestas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 3
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('20', 'CORRECTA', " + pregunta3IdMulRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('14', 'INCORRECTA', " + pregunta3IdMulRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('18', 'INCORRECTA', " + pregunta3IdMulRestas + ")");

// Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 12 - (4 * 2)?', " + mundoMulRestasId + ")");
            int pregunta4IdMulRestas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 4
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'CORRECTA', " + pregunta4IdMulRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'INCORRECTA', " + pregunta4IdMulRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta4IdMulRestas + ")");

// Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es (8 - 3) * 2?', " + mundoMulRestasId + ")");
            int pregunta5IdMulRestas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 5
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'CORRECTA', " + pregunta5IdMulRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'INCORRECTA', " + pregunta5IdMulRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('12', 'INCORRECTA', " + pregunta5IdMulRestas + ")");

        } else {

        }

        Cursor cursorMundoDificil3 = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Divisiones y sumas'", null);
        int mundoDivSumasId = -1;
        if (cursorMundoDificil3.moveToFirst()) {
            mundoDivSumasId = cursorMundoDificil3.getInt(0);
        }
        cursorMundoDificil3.close();
        if (mundoDivSumasId != -1) {

            // Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 10 / 2 + 3?', " + mundoDivSumasId + ")");
            int pregunta1IdDivSumas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 1
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'CORRECTA', " + pregunta1IdDivSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('7', 'INCORRECTA', " + pregunta1IdDivSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('9', 'INCORRECTA', " + pregunta1IdDivSumas + ")");

// Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 18 / 3 + 4?', " + mundoDivSumasId + ")");
            int pregunta2IdDivSumas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 2
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'CORRECTA', " + pregunta2IdDivSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('9', 'INCORRECTA', " + pregunta2IdDivSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('12', 'INCORRECTA', " + pregunta2IdDivSumas + ")");

// Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 14 / 2 + 5?', " + mundoDivSumasId + ")");
            int pregunta3IdDivSumas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 3
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('12', 'CORRECTA', " + pregunta3IdDivSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'INCORRECTA', " + pregunta3IdDivSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('14', 'INCORRECTA', " + pregunta3IdDivSumas + ")");

// Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 30 / 5 + 2?', " + mundoDivSumasId + ")");
            int pregunta4IdDivSumas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 4
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'CORRECTA', " + pregunta4IdDivSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('10', 'INCORRECTA', " + pregunta4IdDivSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta4IdDivSumas + ")");

// Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 22 / 2 + 7?', " + mundoDivSumasId + ")");
            int pregunta5IdDivSumas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 5
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('18', 'CORRECTA', " + pregunta5IdDivSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('15', 'INCORRECTA', " + pregunta5IdDivSumas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('20', 'INCORRECTA', " + pregunta5IdDivSumas + ")");


        } else {

        }
        Cursor cursorMundoDificil4 = db.rawQuery("SELECT id FROM Mundo WHERE nombre = 'Divisiones y Restas'", null);
        int mundoDivRestasId = -1;
        if (cursorMundoDificil4.moveToFirst()) {
            mundoDivRestasId = cursorMundoDificil4.getInt(0);
        }
        cursorMundoDificil4.close();
        if (mundoDivRestasId != -1) {

        } else {
// Pregunta 1
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 20 / 4 - 2?', " + mundoDivRestasId + ")");
            int pregunta1IdDivRestas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 1
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('3', 'CORRECTA', " + pregunta1IdDivRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'INCORRECTA', " + pregunta1IdDivRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('2', 'INCORRECTA', " + pregunta1IdDivRestas + ")");

// Pregunta 2
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 18 / 3 - 1?', " + mundoDivRestasId + ")");
            int pregunta2IdDivRestas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 2
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'CORRECTA', " + pregunta2IdDivRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'INCORRECTA', " + pregunta2IdDivRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta2IdDivRestas + ")");

// Pregunta 3
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 15 / 3 - 2?', " + mundoDivRestasId + ")");
            int pregunta3IdDivRestas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 3
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('2', 'CORRECTA', " + pregunta3IdDivRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('3', 'INCORRECTA', " + pregunta3IdDivRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('1', 'INCORRECTA', " + pregunta3IdDivRestas + ")");

// Pregunta 4
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 30 / 5 - 1?', " + mundoDivRestasId + ")");
            int pregunta4IdDivRestas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 4
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('5', 'CORRECTA', " + pregunta4IdDivRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('4', 'INCORRECTA', " + pregunta4IdDivRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('6', 'INCORRECTA', " + pregunta4IdDivRestas + ")");

// Pregunta 5
            db.execSQL("INSERT INTO Pregunta (enunciado, mundoId) VALUES ('¿Cuánto es 22 / 2 - 3?', " + mundoDivRestasId + ")");
            int pregunta5IdDivRestas = dbHelper.getLastInsertedId(db, "Pregunta");

// Respuestas para la Pregunta 5
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('8', 'CORRECTA', " + pregunta5IdDivRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('9', 'INCORRECTA', " + pregunta5IdDivRestas + ")");
            db.execSQL("INSERT INTO Respuesta (respuesta, estado, preguntaId) VALUES ('7', 'INCORRECTA', " + pregunta5IdDivRestas + ")");

        }


    }


}