package com.example.proyectofinalmobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registro extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextContraseña;
    private Button buttonGuardar;

    private AdminSQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextContraseña = findViewById(R.id.editTextContraseña);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        dbHelper = new AdminSQLiteOpenHelper(this, "miBaseDeDatos", null, 1);

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombre.getText().toString();
                String contraseña = editTextContraseña.getText().toString();

                if (nombre.isEmpty() || contraseña.isEmpty()) {
                    Toast.makeText(registro.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    guardarUsuario(nombre, contraseña);
                }
            }
        });

        Button buttonVolver = findViewById(R.id.buttonVolver);
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registro.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Opcional: si deseas finalizar la actividad actual después de iniciar la LoginActivity
            }
        });

    }

    private void guardarUsuario(String nombre, String contraseña) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("contraseña", contraseña);

        long resultado = db.insert("Usuario", null, values);

        if (resultado != -1) {
            Toast.makeText(registro.this, "Usuario guardado correctamente", Toast.LENGTH_SHORT).show();

            // Obtener el ID del usuario recién insertado
            int usuarioId = (int) resultado;

            // Insertar registro en la tabla Experiencia
            ContentValues experienciaValues = new ContentValues();
            experienciaValues.put("punto", 0);
            experienciaValues.put("apodo", "principiante");
            experienciaValues.put("usuario_id", usuarioId);
            db.insert("Experiencia", null, experienciaValues);

            finish();
        } else {
            Toast.makeText(registro.this, "Error al guardar el usuario", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

}
