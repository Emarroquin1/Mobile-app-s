package com.example.proyectofinalmobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ActivityDificultad extends AppCompatActivity {
    private AdminSQLiteOpenHelper dbHelper;
    public Intent intentSend;
    Button btnEasy,btnIntermediate,btnHard;

    public  Usuario  user;
    public   Experiencia  experiencia;

//setear los datos del usuario en los view
public void setUser(){

    // Obtener los datos del Intent
    Intent intent = getIntent();
    //obtener el usuario
   user = (Usuario) intent.getSerializableExtra("objUser");
    //obtener la experiencia
   experiencia= (Experiencia) intent.getSerializableExtra("objExperiencia");
    // Mostrar los datos en tus TextViews u otros elementos de la interfaz
    TextView textViewNombre = findViewById(R.id.textViewNombre);
    TextView textViewApodo = findViewById(R.id.textViewApodo);
    TextView textViewPuntos = findViewById(R.id.textViewPuntos);

    String puntoView ="Puntos: "+experiencia.getPunto();
    textViewPuntos.setText(puntoView);

    textViewNombre.setText("Usuario: "+user.getNombre());

    textViewApodo.setText("Apodo: "+experiencia.getApodo());

}

public void sendUser(){
    intentSend = new Intent(ActivityDificultad.this, ActivityMundos.class);
    intentSend.putExtra("objExperiencia", experiencia);
    intentSend.putExtra("objUser", user);
}
    private void actualizarExperiencia() {

        int usuarioId = user.getId();

        // Obtener los puntos y apodo actuales del usuario
        Experiencia experiencia = dbHelper.obtenerExperienciaPorUsuarioId(usuarioId);
        int puntos = experiencia.getPunto();
        String apodo = experiencia.getApodo();


        // Actualizar la experiencia en la base de datos
        dbHelper.actualizarExperiencia(usuarioId, puntos, apodo);

        // Actualizar el TextView de los puntos en la interfaz
        TextView textViewPuntos = findViewById(R.id.textViewPuntos);
        String puntoView ="Puntos: "+experiencia.getPunto();
        textViewPuntos.setText(puntoView);

        TextView textViewApodo = findViewById(R.id.textViewApodo);

        textViewApodo.setText("Apodo: "+experiencia.getApodo());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dificultad);

        dbHelper = new AdminSQLiteOpenHelper(this, "miBaseDeDatos", null, 1);
        setUser();
        sendUser();
        actualizarExperiencia();
        //obtener una instancia de los botones
        btnEasy = findViewById(R.id.btnEasy);
        btnIntermediate = findViewById(R.id.btnIntermediate);
        btnHard = findViewById(R.id.btnHard);

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enviar Id del mundo facil
                intentSend.putExtra("idDificultad", 1);
                // Iniciar la MainActivity
                startActivity(intentSend);
                finish();
            }
        });

        btnIntermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enviar Id del mundo intermedio
                intentSend.putExtra("idDificultad",2);
                //iniciar la ActivityMundos
                startActivity(intentSend);
                finish();
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enviar Id del mundo Dificil
                intentSend.putExtra("idDificultad",3);
                //iniciar la ActivityMundos
                startActivity(intentSend);
                finish();
            }
        });

    }
    public void onBackPressed(){
        Intent intent = new Intent(ActivityDificultad.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}