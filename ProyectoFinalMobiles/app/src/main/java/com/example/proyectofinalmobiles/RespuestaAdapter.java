package com.example.proyectofinalmobiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RespuestaAdapter extends BaseAdapter {
    private Context context;
    private List<Respuesta> respuestas;

    public RespuestaAdapter(Context context, int item_respuesta, List<Respuesta> respuestas) {
        this.context = context;
        this.respuestas = respuestas;
    }

    @Override
    public int getCount() {
        return respuestas.size();
    }

    @Override
    public Object getItem(int position) {
        return respuestas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_respuesta, parent, false);
        }

        Respuesta respuesta = respuestas.get(position);

        // Configurar los datos de la respuesta en el diseño del elemento

        TextView textViewRespuesta = convertView.findViewById(R.id.textViewRespuesta);
        textViewRespuesta.setText(respuesta.getRespuesta());

        // Otros elementos de diseño y configuraciones

        return convertView;
    }
}
