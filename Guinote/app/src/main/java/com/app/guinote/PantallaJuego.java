package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PantallaJuego extends AppCompatActivity {

    ImageView c1,c2,c3,c4,c5,c6,reverse,triumphe;
    ArrayList<Carta> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_juego);

        c1 = (ImageView) findViewById(R.id.casilla_carta_1);
        c2 = (ImageView) findViewById(R.id.casilla_carta_2);
        c3 = (ImageView) findViewById(R.id.casilla_carta_3);
        c4 = (ImageView) findViewById(R.id.casilla_carta_4);
        c5 = (ImageView) findViewById(R.id.casilla_carta_5);
        c6 = (ImageView) findViewById(R.id.casilla_carta_6);
        reverse = (ImageView) findViewById(R.id.mazo_central);
        triumphe = (ImageView) findViewById(R.id.mazo_central_volteado);
        c1.setVisibility(View.INVISIBLE);
        c2.setVisibility(View.INVISIBLE);
        c3.setVisibility(View.INVISIBLE);
        c4.setVisibility(View.INVISIBLE);
        c5.setVisibility(View.INVISIBLE);
        c6.setVisibility(View.INVISIBLE);
        triumphe.setVisibility(View.INVISIBLE);

        cards = new ArrayList<>();

        Carta asoros = new Carta(1,11,1,1);
        cards.add(asoros);
        Carta asespadas = new Carta(2,11,2,1);
        cards.add(asespadas);
        Carta asbastos = new Carta(3,11,3,1);
        cards.add(asbastos);
        Carta ascopas = new Carta(4,11,4,1);
        cards.add(ascopas);

        Carta tresoros = new Carta(5,10,1,2);
        cards.add(tresoros);
        Carta tresespadas = new Carta(6,10,2,2);
        cards.add(tresespadas);
        Carta tresbastos = new Carta(7,10,3,2);
        cards.add(tresbastos);
        Carta trescopas = new Carta(8,10,4,2);
        cards.add(trescopas);

        Carta reyoros = new Carta(9,4,1,3);
        cards.add(reyoros);
        Carta reyespadas = new Carta(10,4,2,3);
        cards.add(reyespadas);
        Carta reybastos = new Carta(11,4,3,3);
        cards.add(reybastos);
        Carta reycopas = new Carta(12,4,4,3);
        cards.add(reycopas);

        Carta sotaoros = new Carta(13,3,1,4);
        cards.add(sotaoros);
        Carta sotaespadas = new Carta(14,3,2,4);
        cards.add(sotaespadas);
        Carta sotabastos = new Carta(15,3,3,4);
        cards.add(sotabastos);
        Carta sotacopas = new Carta(16,3,4,4);
        cards.add(sotacopas);

        Carta caballooros = new Carta(17,2,1,5);
        cards.add(caballooros);
        Carta caballoespadas = new Carta(18,2,2,5);
        cards.add(caballoespadas);
        Carta caballobastos = new Carta(19,2,3,5);
        cards.add(caballobastos);
        Carta caballocopas = new Carta(20,2,4,5);
        cards.add(caballocopas);

        Carta sieteoros = new Carta(21,0,1,6);
        cards.add(sieteoros);
        Carta sieteespadas = new Carta(22,0,2,6);
        cards.add(sieteespadas);
        Carta sietebastos = new Carta(23,0,3,6);
        cards.add(sietebastos);
        Carta sietecopas = new Carta(24,0,4,6);
        cards.add(sietecopas);

        Carta seisoros = new Carta(25,0,1,7);
        cards.add(seisoros);
        Carta seisespadas = new Carta(26,0,2,7);
        cards.add(seisespadas);
        Carta seisbastos = new Carta(27,0,3,7);
        cards.add(seisbastos);
        Carta seiscopas = new Carta(28,0,4,7);
        cards.add(seiscopas);

        Carta cincooros = new Carta(29,0,1,8);
        cards.add(cincooros);
        Carta cincoespadas = new Carta(30,0,2,8);
        cards.add(cincoespadas);
        Carta cincobastos = new Carta(31,0,3,8);
        cards.add(cincobastos);
        Carta cincocopas = new Carta(32,0,4,8);
        cards.add(cincocopas);

        Carta cuatrooros = new Carta(33,0,1,9);
        cards.add(cuatrooros);
        Carta cuatroespadas = new Carta(34,0,2,9);
        cards.add(cuatroespadas);
        Carta cuatrobastos = new Carta(35,0,3,9);
        cards.add(cuatrobastos);
        Carta cuatrocopas = new Carta(36,0,4,9);
        cards.add(cuatrocopas);

        Carta dosoros = new Carta(37,0,1,10);
        cards.add(dosoros);
        Carta dosespadas = new Carta(38,0,2,10);
        cards.add(dosespadas);
        Carta dosbastos = new Carta(39,0,3,10);
        cards.add(dosbastos);
        Carta doscopas = new Carta(40,0,4,10);
        cards.add(doscopas);

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.shuffle(cards);

                assignImages(cards.get(0), c1);
                System.out.println(cards.get(0).getId());
                assignImages(cards.get(1), c2);
                System.out.println(cards.get(1).getId());
                assignImages(cards.get(2), c3);
                System.out.println(cards.get(2).getId());
                assignImages(cards.get(3), c4);
                assignImages(cards.get(4), c5);
                assignImages(cards.get(5), c6);
                assignImages(cards.get(39), triumphe);


                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.VISIBLE);
                c4.setVisibility(View.VISIBLE);
                c5.setVisibility(View.VISIBLE);
                c6.setVisibility(View.VISIBLE);
                triumphe.setVisibility(View.VISIBLE);
            }
        });

    }

    public void assignImages(Carta card, ImageView image){
        int cual = card.getId();
        switch (cual){
            case 1:
                image.setImageResource(R.drawable.asoros);
                break;
            case 2:
                image.setImageResource(R.drawable.asespadas);
                break;
            case 3:
                image.setImageResource(R.drawable.asbastos);
                break;
            case 4:
                image.setImageResource(R.drawable.ascopas);
                break;
            case 5:
                image.setImageResource(R.drawable.tresoros);
                break;
            case 6:
                image.setImageResource(R.drawable.tresespadas);
                break;
            case 7:
                image.setImageResource(R.drawable.tresbastos);
                break;
            case 8:
                image.setImageResource(R.drawable.trescopas);
                break;
            case 9:
                image.setImageResource(R.drawable.reyoros);
                break;
            case 10:
                image.setImageResource(R.drawable.reyespadas);
                break;
            case 11:
                image.setImageResource(R.drawable.reybastos);
                break;
            case 12:
                image.setImageResource(R.drawable.reycopas);
                break;
            case 13:
                image.setImageResource(R.drawable.sotaoros);
                break;
            case 14:
                image.setImageResource(R.drawable.sotaespadas);
                break;
            case 15:
                image.setImageResource(R.drawable.sotabastos);
                break;
            case 16:
                image.setImageResource(R.drawable.sotacopas);
                break;
            case 17:
                image.setImageResource(R.drawable.caballooros);

            case 18:
                image.setImageResource(R.drawable.caballoespadas);
                break;
            case 19:
                image.setImageResource(R.drawable.caballobastos);
                break;
            case 20:
                image.setImageResource(R.drawable.caballocopas);
                break;
            case 21:
                image.setImageResource(R.drawable.sieteoros);
                break;
            case 22:
                image.setImageResource(R.drawable.sieteespadas);
                break;
            case 23:
                image.setImageResource(R.drawable.sietebastos);
                break;
            case 24:
                image.setImageResource(R.drawable.sietecopas);
                break;
            case 25:
                image.setImageResource(R.drawable.seisoros);
                break;
            case 26:
                image.setImageResource(R.drawable.seisespadas);
                break;
            case 27:
                image.setImageResource(R.drawable.seisbastos);
                break;
            case 28:
                image.setImageResource(R.drawable.seiscopas);
                break;
            case 29:
                image.setImageResource(R.drawable.cincooros);
                break;
            case 30:
                image.setImageResource(R.drawable.cincoespadas);
                break;
            case 31:
                image.setImageResource(R.drawable.cincobastos);
                break;
            case 32:
                image.setImageResource(R.drawable.cincocopas);
                break;
            case 33:
                image.setImageResource(R.drawable.cuatrooros);
                break;
            case 34:
                image.setImageResource(R.drawable.cuatroespadas);
                break;
            case 35:
                image.setImageResource(R.drawable.cuatrobastos);
                break;
            case 36:
                image.setImageResource(R.drawable.cuatrocopas);
                break;
            case 37:
                image.setImageResource(R.drawable.dosoros);
                break;
            case 38:
                image.setImageResource(R.drawable.dosespadas);
                break;
            case 39:
                image.setImageResource(R.drawable.dosbastos);
                break;
            case 40:
                image.setImageResource(R.drawable.doscopas);
                break;
            default:
                image.setImageResource(R.drawable.reverso);
                break;

        }

    }
}