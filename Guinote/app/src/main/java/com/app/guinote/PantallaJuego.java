package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PantallaJuego extends AppCompatActivity {

    ImageView c1,c2,c3,c4,c5,c6,reverse,triumphe;
    ArrayList<Carta> cards;
    Carta[] cardsj1 = new Carta[6];
    Carta[] cardsj2 = new Carta[6];
    Carta[] cardsj3 = new Carta[6];
    Carta[] cardsj4 = new Carta[6];
    Integer iterator;
    Integer IDcomienzo;

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
        iterator = 0;
        IDcomienzo = 0;
        MyDragEventListener mDragListen = new MyDragEventListener();
        c1.setOnDragListener(mDragListen);
        c2.setOnDragListener(mDragListen);
        c3.setOnDragListener(mDragListen);
        c4.setOnDragListener(mDragListen);
        c5.setOnDragListener(mDragListen);
        c6.setOnDragListener(mDragListen);


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
                assignImages(cards.get(1), c2);
                assignImages(cards.get(2), c3);
                cardsj1[0] = cards.get(0);
                cardsj1[1] = cards.get(1);
                cardsj1[2] = cards.get(2);
                cardsj3[0] = cards.get(3);
                cardsj3[1] = cards.get(4);
                cardsj3[2] = cards.get(5);
                cardsj2[0] = cards.get(6);
                cardsj2[1] = cards.get(7);
                cardsj2[2] = cards.get(8);
                cardsj4[0] = cards.get(9);
                cardsj4[1] = cards.get(10);
                cardsj4[2] = cards.get(11);
                assignImages(cards.get(12), c4);
                assignImages(cards.get(13), c5);
                assignImages(cards.get(14), c6);
                cardsj1[3] = cards.get(12);
                cardsj1[4] = cards.get(13);
                cardsj1[5] = cards.get(14);
                cardsj3[3] = cards.get(15);
                cardsj3[4] = cards.get(16);
                cardsj3[5] = cards.get(17);
                cardsj2[3] = cards.get(18);
                cardsj2[4] = cards.get(19);
                cardsj2[5] = cards.get(20);
                cardsj4[3] = cards.get(21);
                cardsj4[4] = cards.get(22);
                cardsj4[5] = cards.get(23);
                assignImages(cards.get(39), triumphe);

                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.VISIBLE);
                c4.setVisibility(View.VISIBLE);
                c5.setVisibility(View.VISIBLE);
                c6.setVisibility(View.VISIBLE);
                triumphe.setVisibility(View.VISIBLE);

                iterator = 24;
            }
        });

        c1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item("Cambio de cartas!");

                ClipData dragData = new ClipData(
                        (CharSequence) v.getTag(), new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );

                IDcomienzo = v.getId();

                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(c1);
                //c1.setVisibility(View.INVISIBLE);
                //c1.invalidate();
                v.startDrag(dragData,myShadow,null,0);

                return true;
            }
        });

        c2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item("Cambio de cartas!");

                ClipData dragData = new ClipData(
                        (CharSequence) v.getTag(), new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );
                IDcomienzo = v.getId();

                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(c2);
                //c2.setVisibility(View.INVISIBLE);
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });

        c3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item("Cambio de cartas!");

                ClipData dragData = new ClipData(
                        (CharSequence) v.getTag(), new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );
                IDcomienzo = v.getId();

                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(c3);
                //c3.setVisibility(View.INVISIBLE);
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });

        c4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item("Cambio de cartas!");

                ClipData dragData = new ClipData(
                        (CharSequence) v.getTag(), new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );
                IDcomienzo = v.getId();

                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(c4);
                //c4.setVisibility(View.INVISIBLE);
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });

        c5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item("Cambio de cartas!");

                ClipData dragData = new ClipData(
                        (CharSequence) v.getTag(), new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );
                IDcomienzo = v.getId();

                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(c5);
                //c5.setVisibility(View.INVISIBLE);
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });

        c6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item("Cambio de cartas!");

                ClipData dragData = new ClipData(
                        (CharSequence) v.getTag(), new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );
                IDcomienzo = v.getId();

                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(c6);
                //c6.setVisibility(View.INVISIBLE);
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });

    }

    protected class MyDragEventListener implements View.OnDragListener {
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            switch (action){
                case DragEvent.ACTION_DRAG_STARTED:
                    if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        v.setBackgroundColor(0);
                        v.invalidate();
                        return true;
                    }

                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(Color.YELLOW);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(0);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    CharSequence dragData = item.getText();

                    Toast.makeText(PantallaJuego.this, dragData, Toast.LENGTH_LONG).show();

                    v.setBackgroundColor(0);
                    v.invalidate();
                    intercambioCartas(IDcomienzo,v.getId());
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(0);
                    v.invalidate();
                    //queImagen(queID(IDcomienzo)).setVisibility(View.VISIBLE);
                    return true;
                default:
                    break;


            }
            return false;
        }
    }

    private void intercambioCartas(Integer iDcomienzo, Integer iDfinal) {
        Integer a = queID(iDcomienzo);
        Integer b = queID(iDfinal);
        if(a!=-1 && b!=-1){
            Carta aux = cardsj1[a];
            cardsj1[a] = cardsj1[b];
            cardsj1[b] = aux;
            ImageView aux1 = queImagen(a);
            ImageView aux2 = queImagen(b);
            assignImages(cardsj1[a],aux1);
            assignImages(cardsj1[b],aux2);
        }
    }

    private ImageView queImagen(Integer a) {
        switch (a){
            case 0:
                return c1;
            case 1:
                return c2;
            case 2:
                return c3;
            case 3:
                return c4;
            case 4:
                return c5;
            case 5:
                return c6;
            default:
                return null;
        }
    }

    private Integer queID(Integer id){
        switch (id){
            case 2131230827:
                return 0;
            case 2131230828:
                return 1;
            case 2131230829:
                return 2;
            case 2131230830:
                return 3;
            case 2131230831:
                return 4;
            case 2131230832:
                return 5;
            default:
                return -1;
        }
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