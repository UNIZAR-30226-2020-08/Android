package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Lista1vs1 extends Fragment {


    ListView listView;
    List<listItem> lista;

    public Lista1vs1(){
        super(R.layout.activity_lista1vs1);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista1vs1,
                container, false);
        listView = view.findViewById(R.id.lista1vs1);

        listAdapter adapter = new listAdapter(getActivity(),GetData());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItem ranking = lista.get(position);
                Intent intent = new Intent(getActivity(),PantallaJuego.class);
                startActivity(intent);

            }
        });
        return view;
    }

    private List<listItem> GetData() {
        lista = new ArrayList<>();
        lista.add(new listItem(1,"TABLERO SENCILLO",R.drawable.asoros));
        lista.add(new listItem(2,"TABLERO SENCILLO",R.drawable.dosoros));
        lista.add(new listItem(3,"TABLERO AMATEUR",R.drawable.tresoros));
        lista.add(new listItem(4,"TABLERO AVANZADO",R.drawable.cuatrooros));
        lista.add(new listItem(5,"TABLERO SENCILLO",R.drawable.asoros));
        lista.add(new listItem(6,"TABLERO SENCILLO",R.drawable.dosoros));
        lista.add(new listItem(7,"TABLERO AMATEUR",R.drawable.tresoros));
        lista.add(new listItem(8,"TABLERO AVANZADO",R.drawable.cuatrooros));
        lista.add(new listItem(9,"TABLERO SENCILLO",R.drawable.asoros));
        lista.add(new listItem(10,"TABLERO SENCILLO",R.drawable.dosoros));
        lista.add(new listItem(11,"TABLERO AMATEUR",R.drawable.tresoros));
        lista.add(new listItem(12,"TABLERO AVANZADO",R.drawable.cuatrooros));

        return lista;


    }
}