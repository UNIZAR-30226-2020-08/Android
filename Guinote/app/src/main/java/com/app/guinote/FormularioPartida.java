package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class FormularioPartida extends Fragment {

    private View view;

    public FormularioPartida(){
        super(R.layout.activity_formulario_partida);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_formulario_partida,
                container, false);

        AutoCompleteTextView menumodalidad=  view.findViewById(R.id.menuModalidad);
        String lista[]={"Parejas","Individual"};
        ArrayAdapter adaptador= new ArrayAdapter(getContext(),R.layout.itemdropdown,lista);
        menumodalidad.setAdapter(adaptador);

        AutoCompleteTextView menuparticipantes=  view.findViewById(R.id.menuNumGente);
        String lista2[]={"16 equipos","8 equipos"};
        ArrayAdapter adaptadorGente= new ArrayAdapter(getContext(),R.layout.itemdropdown,lista2);
        menuparticipantes.setAdapter(adaptadorGente);
        return view;
    }

}