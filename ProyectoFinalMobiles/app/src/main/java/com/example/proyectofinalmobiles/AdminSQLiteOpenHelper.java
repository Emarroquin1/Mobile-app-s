package com.example.proyectofinalmobiles;


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

        // Crear tabla Mundo
        String createTableMundo = "CREATE TABLE Mundo (id INTEGER PRIMARY KEY, punto INTEGER, nombre TEXT, nivelId INTEGER, " +
                "FOREIGN KEY(nivelId) REFERENCES Nivel(id))";
        db.execSQL(createTableMundo);

        // Crear tabla Pregunta
        String createTablePregunta = "CREATE TABLE Pregunta (id INTEGER PRIMARY KEY, enunciado TEXT, mundoId INTEGER, " +
                "FOREIGN KEY(mundoId) REFERENCES Mundo(id))";
        db.execSQL(createTablePregunta);

        // Crear tabla Respuesta
        String createTableRespuesta = "CREATE TABLE Respuesta (id INTEGER PRIMARY KEY, respuesta TEXT, estado TEXT, preguntaId INTEGER, " +
                "FOREIGN KEY(preguntaId) REFERENCES Pregunta(id))";
        db.execSQL(createTableRespuesta);


        // Insertar registros por defecto en la tabla Nivel
        db.execSQL("INSERT INTO Nivel (nombre) VALUES ('Facil')");
        db.execSQL("INSERT INTO Nivel (nombre) VALUES ('Intermedio')");
        db.execSQL("INSERT INTO Nivel (nombre) VALUES ('Dificil')");

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