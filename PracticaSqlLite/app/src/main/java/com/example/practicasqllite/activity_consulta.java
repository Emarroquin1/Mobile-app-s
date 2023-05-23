package com.example.practicasqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class activity_consulta extends AppCompatActivity {

    private EditText et_codigo;
    private TextView tv_resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        et_codigo=findViewById(R.id.txt_codigoConsulta);
        tv_resultado = findViewById(R.id.txt_descripcion_consulta);
    }

    public void Consultar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();

        if (!codigo.isEmpty()){
            Cursor fila = BaseDeDatos.rawQuery("SELECT descripcion, precio FROM articulos WHERE codigo = ?", new String[]{codigo});

            if (fila.moveToFirst()){
                String descripcion = fila.getString(0);
                String precio = fila.getString(1);
                String resultado = " Descripcion: " + descripcion + "\n" + " Precio: $" + precio;
                tv_resultado.setText(resultado);

                BaseDeDatos.close();
            }else {
                Toast.makeText(this, "No existe el producto", Toast.LENGTH_SHORT).show();
                BaseDeDatos.close();
            }
        }else {
            Toast.makeText(this, "Debes introducir el codigo del producto", Toast.LENGTH_SHORT).show();
        }
    }
}