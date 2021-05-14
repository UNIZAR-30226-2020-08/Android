package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        return view;
    }
}