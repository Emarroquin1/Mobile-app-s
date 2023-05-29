package com.example.proyectofinalmobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextContraseña;
    private Button buttonIniciarSesion;

    private TextView textViewRegistro;


    private AdminSQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextContraseña = findViewById(R.id.editTextContraseña);
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion);

        textViewRegistro = findViewById(R.id.textViewRegistro);

        dbHelper = new AdminSQLiteOpenHelper(this, "miBaseDeDatos", null, 1);

        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombre.getText().toString();
                String contraseña = editTextContraseña.getText().toString();

                if (nombre.isEmpty() || contraseña.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (verificarCredenciales(nombre, contraseña)) {
                        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                        // Crear el Intent para iniciar la MainActivity
                        // Obtener el ID del usuario
                        int usuarioId = obtenerUsuarioId(nombre);

                         // Obtener los datos del usuario de la base de datos
                        Usuario usuario = obtenerUsuario(usuarioId);

                        // Crear el Intent para iniciar la MainActivity
                        Intent intent = new Intent(LoginActivity.this, ActivityDificultad.class);

                        // Pasar los datos del usuario al Intent
                        intent.putExtra("nombre", usuario.getNombre());
                        intent.putExtra("id", usuario.getId());

                        Experiencia experiencia = dbHelper.obtenerExperienciaPorUsuarioId(usuarioId);

                        if (experiencia != null) {
                            intent.putExtra("apodo", experiencia.getApodo());
                            intent.putExtra("puntos", experiencia.getPunto());
                        }


                        // Iniciar la MainActivity
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        textViewRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, registro.class));
            }
        });
    }

    private Usuario obtenerUsuario(int usuarioId) {
        Usuario usuario = null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"id", "nombre", "contraseña"};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(usuarioId)};

        Cursor cursor = db.query("Usuario", projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            String contraseña = cursor.getString(cursor.getColumnIndexOrThrow("contraseña"));

            usuario = new Usuario(id, nombre, contraseña);
        }

        cursor.close();
        db.close();

        return usuario;
    }

    private int obtenerUsuarioId(String nombre) {
        int usuarioId = -1; // Valor predeterminado si no se encuentra el usuario

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"id"};
        String selection = "nombre = ?";
        String[] selectionArgs = {nombre};

        Cursor cursor = db.query("Usuario", projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }

        cursor.close();
        db.close();

        return usuarioId;
    }

    private boolean verificarCredenciales(String nombre, String contraseña) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"nombre", "contraseña"};
        String selection = "nombre = ? AND contraseña = ?";
        String[] selectionArgs = {nombre, contraseña};

        Cursor cursor = db.query("Usuario", projection, selection, selectionArgs, null, null, null);

        boolean credencialesCorrectas = cursor.moveToFirst();
        cursor.close();
        db.close();

        return credencialesCorrectas;
    }
}
