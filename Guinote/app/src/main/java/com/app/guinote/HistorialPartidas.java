package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;


public class HistorialPartidas extends Fragment {
    ListView lista;

    public HistorialPartidas(){
        super(R.layout.activity_historial_partidas);
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_historial_partidas,
                container, false);
        super.onCreate(savedInstanceState);

        lista = (ListView) view.findViewById(R.id.lista_historial);

        lista.setAdapter(new HistAdapter(getContext(), datos, datosImg));
        return view;
    }
}