package com.app.guinote;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tapete} factory method to
 * create an instance of this fragment.
 */
public class Tapete extends Fragment {
    ImageView fizqtapete, fdertapete, choosetapete;
    Integer idtapete = 0;
    public Tapete() {
        super(R.layout.fragment_tapete);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tapete, container, false);
        fizqtapete = (ImageView) view.findViewById(R.id.tapeteflechaizda);
        fdertapete = (ImageView) view.findViewById(R.id.tapeteflechadcha);
        choosetapete = (ImageView) view.findViewById(R.id.choosetapete);

        fizqtapete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idtapete = idtapete-1;
                if(idtapete<0){
                    idtapete = 5;
                }
                openActivity1();
            }
        });
        fdertapete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idtapete = idtapete+1;
                if(idtapete == 6){
                    idtapete = 0;
                }
                openActivity1();
            }
        });

        return view;
    }

    public void openActivity1(){
        switch (idtapete){
            case 0:
                choosetapete.setImageResource(R.drawable.tapete2);
                break;
            case 1:
                choosetapete.setImageResource(R.drawable.tapete1);
                break;
            case 2:
                choosetapete.setImageResource(R.drawable.hierba);
                break;
            case 3:
                choosetapete.setImageResource(R.drawable.madera);
                break;
            case 4:
                choosetapete.setImageResource(R.drawable.football);
                break;
            case 5:
                choosetapete.setImageResource(R.drawable.tapete);
                break;
        }
    }
}