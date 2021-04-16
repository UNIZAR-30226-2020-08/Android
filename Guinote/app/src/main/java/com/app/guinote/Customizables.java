package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Customizables extends AppCompatActivity {

    ImageView fizqtapete, fdertapete, fizqreverso, fderreverso, fizqcartas,fdercartas, choosetapete, choosereverso, choosecartas;
    Integer idtapete = 0, idreverso=0, idcartas=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customizables);

        fizqtapete = (ImageView) findViewById(R.id.tapeteflechaizda);
        fdertapete = (ImageView) findViewById(R.id.tapeteflechadcha);
        fizqreverso = (ImageView) findViewById(R.id.reversoflechaizda);
        fderreverso = (ImageView) findViewById(R.id.reversoflechadcha);
        fizqcartas = (ImageView) findViewById(R.id.cartasflechaizda);
        fdercartas = (ImageView) findViewById(R.id.cartasflechadcha);
        choosetapete = (ImageView) findViewById(R.id.choosetapete);
        choosereverso = (ImageView) findViewById(R.id.choosereverso);
        choosecartas = (ImageView) findViewById(R.id.choosecartas);

        fizqtapete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idtapete = (idtapete-1)%6;
                openActivity1();
            }
        });
        fdertapete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idtapete = (idtapete+1)%6;
                openActivity1();
            }
        });
        /*fizqreverso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        fderreverso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        fizqcartas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });
        fdercartas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });*/

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