package com.app.guinote;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class HistAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    String [][] datos;
    int cantidad;
    int [] imagenes;

    public HistAdapter(Context context, String[][] datos, int[] imagenes,int _cantidad) {
        this.context = context;
        this.datos = datos;
        this.imagenes = imagenes;
        cantidad=_cantidad;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return cantidad;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View vista = inflater.inflate(R.layout.historial, null);

        TextView titulo = (TextView) vista.findViewById(R.id.Victoria);
        TextView duracion = (TextView) vista.findViewById(R.id.Duracion);
        TextView tapete = (TextView) vista.findViewById(R.id.Tapete);
        TextView puntuacion = (TextView) vista.findViewById(R.id.Puntuación);
        TextView jugadores = (TextView) vista.findViewById(R.id.Jugadores);

        ImageView imagen = (ImageView) vista.findViewById(R.id.ImagenUsuario);

        titulo.setText(datos[position][0]);
        tapete.setText(datos[position][1]);
        duracion.setText(datos[position][4]);
        puntuacion.setText(datos[position][3]);
        jugadores.setText(datos[position][2]);


        if (datos[position][0]=="VICTORIA"){
            int myColor = ContextCompat.getColor(context, R.color.ganada);
            vista.setBackgroundColor(myColor);
            imagen.setImageResource(imagenes[1]);
        }else{
            int myColor2 = ContextCompat.getColor(context, R.color.perdida);
            vista.setBackgroundColor(myColor2);
            imagen.setImageResource(imagenes[0]);
        }
        return vista;

    }
}
