package com.example.proyectofinalmobiles;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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

        // Crear tabla Mundo
        String createTableMundo = "CREATE TABLE Mundo (id INTEGER PRIMARY KEY, punto INTEGER, nombre TEXT, nivelId INTEGER, " +
                "FOREIGN KEY(nivelId) REFERENCES Nivel(id))";
        db.execSQL(createTableMundo);

        // Crear tabla Pregunta
        String createTablePregunta = "CREATE TABLE Pregunta (id INTEGER PRIMARY KEY, enunciado TEXT, mundoId INTEGER,estado TEXT, " +
                "FOREIGN KEY(mundoId) REFERENCES Mundo(id))";
        db.execSQL(createTablePregunta);


       // alterar tabla Pregunta
       String alterTablePregunta = "ALTER TABLE Pregunta ADD COLUMN estado TEXT";
        db.execSQL(alterTablePregunta);

        // Crear tabla Respuesta
        String createTableRespuesta = "CREATE TABLE Respuesta (id INTEGER PRIMARY KEY, respuesta TEXT, estado TEXT, preguntaId INTEGER, " +
                "FOREIGN KEY(preguntaId) REFERENCES Pregunta(id))";
        db.execSQL(createTableRespuesta);


        // Insertar registros por defecto en la tabla Nivel
        db.execSQL("INSERT INTO Nivel (nombre) VALUES ('Facil')");
        db.execSQL("INSERT INTO Nivel (nombre) VALUES ('Intermedio')");
        db.execSQL("INSERT INTO Nivel (nombre) VALUES ('Dificil')");

    }
    public int obtenerPuntosMundo(int mundoId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { "punto" };
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(mundoId) };

        Cursor cursor = db.query("Mundo", projection, selection, selectionArgs, null, null, null);

        int puntosMundo = -1;
        if (cursor.moveToFirst()) {
            puntosMundo = cursor.getInt(cursor.getColumnIndexOrThrow("punto"));
        }

        cursor.close();
        db.close();

        return puntosMundo;
    }

    public void actualizarExperiencia(int usuarioId, int puntos, String apodo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("punto", puntos);
        values.put("apodo", apodo);

        String selection = "usuario_id = ?";
        String[] selectionArgs = { String.valueOf(usuarioId) };

        db.update("Experiencia", values, selection, selectionArgs);
        db.close();

        // Actualizar el TextView textViewPuntos en MainActivity
        MainActivity activity = MainActivity.getInstance();
        if (activity != null) {
            activity.actualizarPuntos(puntos);
        }
    }

    public void actualizarEstadoPregunta(int preguntaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("estado", "respondido");

        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(preguntaId) };

        db.update("Pregunta", values, selection, selectionArgs);
        db.close();
    }


    public Experiencia obtenerExperienciaPorUsuarioId(int usuarioId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"punto", "apodo"};
        String selection = "usuario_id = ?";
        String[] selectionArgs = {String.valueOf(usuarioId)};

        Cursor cursor = db.query("Experiencia", projection, selection, selectionArgs, null, null, null);

        Experiencia experiencia = null;
        if (cursor.moveToFirst()) {
            int punto = cursor.getInt(cursor.getColumnIndexOrThrow("punto"));
            String apodo = cursor.getString(cursor.getColumnIndexOrThrow("apodo"));
            experiencia = new Experiencia(punto, apodo);
        }

        cursor.close();
        db.close();

        return experiencia;
    }

    public List<Mundo> obtenerMundosPorNivel(int nivelId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorMundos = db.rawQuery("SELECT * FROM Mundo WHERE nivelId = " + nivelId, null);

        List<Mundo> listaMundos = new ArrayList<>();

        if (cursorMundos.moveToFirst()) {
            do {
                int mundoId = cursorMundos.getInt(0);
                int punto = cursorMundos.getInt(1);
                String nombre = cursorMundos.getString(2);
                Mundo mundo = new Mundo(mundoId, punto, nombre);
                listaMundos.add(mundo);
            } while (cursorMundos.moveToNext());
        }
        cursorMundos.close();
        db.close();

        return listaMundos;
    }

    public void borrarRegistros() {
        SQLiteDatabase db = getWritableDatabase();

        // Borrar todos los registros de la tabla Respuesta
        db.execSQL("DELETE FROM Mundo");

        db.execSQL("DELETE FROM Respuesta");

        // Borrar todos los registros de la tabla Pregunta
        db.execSQL("DELETE FROM Pregunta");
    }
    public String obtenerRegistros(String nombreTabla) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        StringBuilder registros = new StringBuilder();

        try {
            // Ejecutar la consulta para obtener todos los registros
            String consulta = "SELECT * FROM " + nombreTabla;
            cursor = db.rawQuery(consulta, null);

            if (cursor != null && cursor.moveToFirst()) {
                int columnCount = cursor.getColumnCount();

                do {
                    // Recorrer las columnas de cada registro
                    for (int i = 0; i < columnCount; i++) {
                        String valor = cursor.getString(i);
                        registros.append(valor).append(", ");
                    }
                    registros.append("\n");
                } while (cursor.moveToNext());
            }
        } finally {
            // Cerrar el cursor
            if (cursor != null) {
                cursor.close();
            }
        }

        return registros.toString();
    }

    public List<Pregunta> listarTodasLasPreguntas() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorPreguntas = db.rawQuery("SELECT id, enunciado, estado FROM Pregunta", null);

        List<Pregunta> listaPreguntas = new ArrayList<>();

        if (cursorPreguntas.moveToFirst()) {
            do {
                int preguntaId = cursorPreguntas.getInt(0);
                String enunciado = cursorPreguntas.getString(1);
                String estadoPregunta = cursorPreguntas.getString(2);
                Pregunta pregunta = new Pregunta(preguntaId, enunciado, estadoPregunta);
                listaPreguntas.add(pregunta);

                Cursor cursorRespuestas = db.rawQuery("SELECT * FROM Respuesta WHERE preguntaId = ?", new String[]{String.valueOf(preguntaId)});
                if (cursorRespuestas.moveToFirst()) {
                    do {
                        int respuestaId = cursorRespuestas.getInt(0);
                        String respuesta = cursorRespuestas.getString(1);
                        String estado = cursorRespuestas.getString(2);

                        Respuesta respuestaObj = new Respuesta(respuestaId, respuesta, estado);
                        pregunta.agregarRespuesta(respuestaObj);
                    } while (cursorRespuestas.moveToNext());
                }
                cursorRespuestas.close();
            } while (cursorPreguntas.moveToNext());
        }
        cursorPreguntas.close();
        db.close();

        return listaPreguntas;
    }

    public Mundo obtenerMundoPorId(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorMundos = db.rawQuery("SELECT * FROM Mundo WHERE id = " + id, null);
        Mundo mundo = null;
if (cursorMundos.moveToFirst()){

    do {
        int puntos = cursorMundos.getInt(1);
        String nombre = cursorMundos.getString(2);
         mundo = new Mundo(id,puntos,nombre);


    }while (cursorMundos.moveToNext());

}
        return  mundo;}


    public List<Pregunta> obtenerPreguntasPorMundoId(int mundoId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorPreguntas = db.rawQuery("SELECT id, enunciado, estado FROM Pregunta WHERE mundoId = ?", new String[]{String.valueOf(mundoId)});

        List<Pregunta> listaPreguntas = new ArrayList<>();

        if (cursorPreguntas.moveToFirst()) {
            do {
                int preguntaId = cursorPreguntas.getInt(0);
                String enunciado = cursorPreguntas.getString(1);
                String estadoPregunta = cursorPreguntas.getString(2);
                Pregunta pregunta = new Pregunta(preguntaId, enunciado, estadoPregunta);
                listaPreguntas.add(pregunta);

                Cursor cursorRespuestas = db.rawQuery("SELECT * FROM Respuesta WHERE preguntaId = ?", new String[]{String.valueOf(preguntaId)});
                if (cursorRespuestas.moveToFirst()) {
                    do {
                        int respuestaId = cursorRespuestas.getInt(0);
                        String respuesta = cursorRespuestas.getString(1);
                        String estado = cursorRespuestas.getString(2);

                        Respuesta respuestaObj = new Respuesta(respuestaId, respuesta, estado);
                        pregunta.agregarRespuesta(respuestaObj);
                    } while (cursorRespuestas.moveToNext());
                }
                cursorRespuestas.close();
            } while (cursorPreguntas.moveToNext());
        }
        cursorPreguntas.close();
        db.close();

        return listaPreguntas;
    }


    // Método para obtener el último ID insertado en una tabla
    public int getLastInsertedId(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid() FROM " + tableName, null);
        int lastInsertedId = -1;
        if (cursor.moveToFirst()) {
            lastInsertedId = cursor.getInt(0);
        }
        cursor.close();
        return lastInsertedId;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}