package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;


public class HistorialPartidas extends AppCompatActivity {
    ListView lista;

    String[][] datos = {
            {"VICTORIA", "TAPETE ORO", "4 Jugadores", "59 BUENAS", "10:30"},
            {"DERROTA", "TAPETE PLATA", "2 Jugadores", "7 BUENAS", "15:00"},
            {"VICTORIA", "TAPETE ORO", "2 Jugadores", "51 BUENAS", "10:20"},
            {"VICTORIA", "TAPETE MAESTRO", "4 Jugadores", "55 BUENAS", "10:30"},
            {"DERROTA", "TAPETE MAESTRO", "2 Jugadores", "6 BUENAS", "10:55"},
            {"DERROTA", "TAPETE ORO", "4 Jugadores", "60 BUENAS", "9:05"}
    };
    int[] datosImg = {R.drawable.asoros, R.drawable.asoros, R.drawable.asoros, R.drawable.asoros, R.drawable.asoros, R.drawable.asoros};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_partidas);

        lista = (ListView) findViewById(R.id.lista_historial);

        lista.setAdapter(new HistAdapter(this, datos, datosImg));
    }
}