package com.example.proyectoweb;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {


        // Crear tabla Usuario
        String createTableUsuario = "CREATE TABLE Usuario (id INTEGER PRIMARY KEY, nombre TEXT, contraseña TEXT)";
        db.execSQL(createTableUsuario);

        // Crear tabla Experiencia
        String createTableExperiencia = "CREATE TABLE Experiencia (id INTEGER PRIMARY KEY, punto INTEGER, apodo TEXT, usuario_id INTEGER, " +
                "FOREIGN KEY(usuario_id) REFERENCES Usuario(id))";
        db.execSQL(createTableExperiencia);

        // Crear tabla Nivel
        String createTableNivel = "CREATE TABLE Nivel (id INTEGER PRIMARY KEY, nombre TEXT)";
        db.execSQL(createTableNivel);

        // Insertar registros por defecto en la tabla Nivel
        db.execSQL("INSERT INTO Nivel (nombre) VALUES ('Facil')");
        db.execSQL("INSERT INTO Nivel (nombre) VALUES ('Intermedio')");
        db.execSQL("INSERT INTO Nivel (nombre) VALUES ('Dificil')");

        // Crear tabla Mundo
        String createTableMundo = "CREATE TABLE Mundo (id INTEGER PRIMARY KEY, punto INTEGER, nombre TEXT, nivelId INTEGER, " +
                "FOREIGN KEY(nivelId) REFERENCES Nivel(id))";
        db.execSQL(createTableMundo);

        // Obtener el ID del nivel con nombre 'Facil'
        Cursor cursor = db.rawQuery("SELECT id FROM Nivel WHERE nombre = 'Facil'", null);
        int nivelId = -1;
        if (cursor.moveToFirst()) {
            nivelId = cursor.getInt(0);
        }



        if (nivelId != -1) {
            // Insertar registros por defecto en la tabla Mundo
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (0, 'Suma entre 1 numero', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (0, 'Sumas entre 2 numeros', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (0, 'resta entre 1 numero', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (0, 'restas entre 2 numeros', " + nivelId + ")");
        }

        cursor = db.rawQuery("SELECT id FROM Nivel WHERE nombre = 'Intermedio'", null);
         nivelId = -1;
        if (cursor.moveToFirst()) {
            nivelId = cursor.getInt(0);
        }
        cursor.close();

        if (nivelId != -1) {
            // Insertar registros por defecto en la tabla Mundo relacionados con el nivel intermedio
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (0, 'Suma entre 1 numero', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (0, 'Sumas entre 2 numeros', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (0, 'resta entre 1 numero', " + nivelId + ")");
            db.execSQL("INSERT INTO Mundo (punto, nombre, nivelId) VALUES (0, 'restas entre 2 numeros', " + nivelId + ")");
        }

        // Crear tabla Pregunta
        String createTablePregunta = "CREATE TABLE Pregunta (id INTEGER PRIMARY KEY, enunciado TEXT, mundoId INTEGER, " +
                "FOREIGN KEY(mundoId) REFERENCES Mundo(id))";
        db.execSQL(createTablePregunta);

        // Crear tabla Respuesta
        String createTableRespuesta = "CREATE TABLE Respuesta (id INTEGER PRIMARY KEY, respuesta TEXT, estado TEXT, preguntaId INTEGER, " +
                "FOREIGN KEY(preguntaId) REFERENCES Pregunta(id))";
        db.execSQL(createTableRespuesta);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}