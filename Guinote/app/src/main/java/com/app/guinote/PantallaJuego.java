package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PantallaJuego extends AppCompatActivity {

    private Mensajeria mensajeria;
    private int mRemainingTime = 30;
    private String room="";
    private Socket mSocket;
    protected SQLiteDatabase db;
    private String nameUser;
    ImageView c1,c2,c3,c4,c5,c6,reverse,triumphe,j1image,j2image,j3image,j4image,chat;
    EasyFlipView c1whole,c2whole,c3whole,c4whole,c5whole,c6whole,triumphewhole;
    Button cantar;
    ArrayList<Carta> cards;
    Carta[] cardsj1 = new Carta[6];
    Carta[] cardsj2 = new Carta[6];
    Carta[] cardsj3 = new Carta[6];
    Carta[] cardsj4 = new Carta[6];
    Integer iterator;   //Cual es la siguiente carta a robar en el mazo
    Integer IDcomienzo; //Que carta estoy comenzando a arrastrar (cada jugador tiene su propio IDcomienzo)
    boolean arrastre;   //Si estamos en arrastre o no
    Integer triunfo;    //1 si el triunfo es oros, 2 espadas, 3 bastos y 4 copas
    boolean baza;       //Quien se ha llevado la ultima baza, tu equipo o el de los demas(uno para cada uno)
    Integer personaBaza; //Quien se ha llevado la ultima baza, 1 representa j1, 2 a j2, 3 a j3 y 4 a j4
    Integer puntosE1,puntosE2; //Puntos de cada equipo en general
    protected List<MensajeDeTexto> mensajeDeTextos;



    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("message", onNewMessage);
    }

    protected void attemptSend(String message) {

        mSocket.emit("sendMessage", message, new Ack() {
            @Override
            public void call(Object... args) {
                //JSONObject response = (JSONObject) args[0];
                //System.out.println(response); // "ok"
            }
        });
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("user");
                        message = data.getString("text");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    Log.d("username",username+" "+nameUser);
                    if (!username.equals(nameUser)) {
                        MensajeDeTexto mensajeDeTextoAuxiliar = new MensajeDeTexto("0",message,2,username);
                        mensajeDeTextos.add(mensajeDeTextoAuxiliar);
                    }
                }
            });
        }
    };

    private Emitter.Listener roomInfo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String room;
                    String participantes;
                    try {
                        room = data.getString("room");
                        participantes = data.getString("users");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    //CreateMensaje(room, participantes,2);
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_juego);


        MyOpenHelper dbHelper = new MyOpenHelper(this);
        db = dbHelper.getWritableDatabase();


        mensajeria = new Mensajeria();


        nameUser=getName();

        Bundle b = getIntent().getExtras();
        if(b != null)
            room = b.getString("key");

        mSocket = IO.socket(URI.create("http://192.168.1.33:5000"));

        mSocket.on("message", onNewMessage);
        mSocket.on("roomData", roomInfo);
        mSocket.connect();
        JSONObject auxiliar = new JSONObject();
        try {
            auxiliar.put("name", getName());
            auxiliar.put("room", room);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d("json",auxiliar.toString());
        mSocket.emit("join", auxiliar, new Ack() {
            @Override
            public void call(Object... args) {
                //JSONObject response = (JSONObject) args[0];
                //System.out.println(response); // "ok"
            }
        });


        chat = (ImageView) findViewById(R.id.icono_chat);
        j1image = (ImageView) findViewById(R.id.carta_jugador1);
        j2image = (ImageView) findViewById(R.id.carta_jugador2);
        j3image = (ImageView) findViewById(R.id.carta_jugador3);
        j4image = (ImageView) findViewById(R.id.carta_jugador4);
        c1 = (ImageView) findViewById(R.id.casilla_carta_1);
        c1whole = (EasyFlipView) findViewById(R.id.easyFlipView1);
        c2 = (ImageView) findViewById(R.id.casilla_carta_2);
        c2whole = (EasyFlipView) findViewById(R.id.easyFlipView2);
        c3 = (ImageView) findViewById(R.id.casilla_carta_3);
        c3whole = (EasyFlipView) findViewById(R.id.easyFlipView3);
        c4 = (ImageView) findViewById(R.id.casilla_carta_4);
        c4whole = (EasyFlipView) findViewById(R.id.easyFlipView4);
        c5 = (ImageView) findViewById(R.id.casilla_carta_5);
        c5whole = (EasyFlipView) findViewById(R.id.easyFlipView5);
        c6 = (ImageView) findViewById(R.id.casilla_carta_6);
        c6whole = (EasyFlipView) findViewById(R.id.easyFlipView6);
        reverse = (ImageView) findViewById(R.id.mazo_central);
        triumphe = (ImageView) findViewById(R.id.mazo_central_volteado);
        triumphewhole = (EasyFlipView) findViewById(R.id.easyFlipViewtriumphe);
        triumphewhole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCambio7();
            }
        });
        cantar = (Button) findViewById(R.id.button_cantar);
        cantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCantar();
            }
        });
        c1whole.setVisibility(View.INVISIBLE);
        c2whole.setVisibility(View.INVISIBLE);
        c3whole.setVisibility(View.INVISIBLE);
        c4whole.setVisibility(View.INVISIBLE);
        c5whole.setVisibility(View.INVISIBLE);
        c6whole.setVisibility(View.INVISIBLE);
        triumphewhole.setVisibility(View.INVISIBLE);
        reverse.setVisibility(View.INVISIBLE);
        arrastre = false;
        baza = false;
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
        System.out.println("Antes de barajar");
        for (int i = 0; i<40; i++){
            System.out.println(cards.get(i).getId());
        }
        Collections.shuffle(cards);
        System.out.println("Despues de barajar");
        for (int i = 0; i<40; i++){
            System.out.println(cards.get(i).getId());
        }

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

        iterator = 24;
        triunfo = cards.get(39).getPalo();

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Mensajeria.class);
                Bundle b = new Bundle();
                b.putString("room", room); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
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

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignImages(cardsj1[0],j1image);
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

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignImages(cardsj1[1],j1image);
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

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignImages(cardsj1[2],j1image);
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
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignImages(cardsj1[3],j1image);
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
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignImages(cardsj1[4],j1image);
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
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignImages(cardsj1[5],j1image);
            }
        });

        new Thread() {
            @Override
            public void run() {
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    setVisibility3firstcards();
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    updatefirst3cards();
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    setVisibility3secondcards();
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    updatelast3cards();
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    setVisibilitytriumphe();
                    try{
                        Thread.sleep(500);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    updatecenterCard();
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    setVisibilityreverse();
            }
        }.start();

    }

    private void setVisibility3firstcards() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                c1whole.setVisibility(View.VISIBLE);
                c2whole.setVisibility(View.VISIBLE);
                c3whole.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setVisibility3secondcards() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                c4whole.setVisibility(View.VISIBLE);
                c5whole.setVisibility(View.VISIBLE);
                c6whole.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setVisibilitytriumphe() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                triumphewhole.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setVisibilityreverse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                reverse.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updatecenterCard() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                triumphewhole.flipTheView();
            }
        });

    }

    private void updatelast3cards() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                c4whole.flipTheView();
                c5whole.flipTheView();
                c6whole.flipTheView();
            }
        });
    }

    private void updatefirst3cards() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                c1whole.flipTheView();
                c2whole.flipTheView();
                c3whole.flipTheView();
            }
        });
    }

    private void openActivityCantar() {
        Integer num = 0;
        for(int i = 1; i<5; i++){
            for(int k=0; k<6; k++){
                if(((cardsj1[k].getRanking() == 3) && (cardsj1[k].getPalo() == i))
                || ((cardsj1[k].getRanking() == 4) && (cardsj1[k].getPalo() == i))){
                    num++;
                }
            }
            if(num == 2){
                sumarPuntosCantar(i);
            }
            num = 0;
        }
    }

    private void sumarPuntosCantar(int i) {
        if(i == triunfo){
            puntosE1 = puntosE1 + 40;
        }else{
            puntosE1 = puntosE1 + 20;
        }
    }

    public void openActivityCambio7(){
        if(!arrastre){
            final Integer siete =tiene7();
            if( siete != 6){
                if(!baza){
                    new Thread(){
                        @Override
                        public void run() {
                            flipViews(queImagenFlip(siete),triumphewhole);
                            try{
                                Thread.sleep(1000);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            intercambiosiete(siete);
                            try{
                                Thread.sleep(1000);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            flipViews(queImagenFlip(siete),triumphewhole);
                        }
                    }.start();
                }
            }
        }
    }
    public Integer tiene7(){
        for (int i = 0; i<6; i++){
            if((cardsj1[i].getRanking() == 6) && (cardsj1[i].getPalo() == triunfo)){
                return i;
            }
        }
        return 6;
    }

    public void flipViews(final EasyFlipView a, final EasyFlipView b){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                a.flipTheView();
                b.flipTheView();
            }
        });
    }

    protected class MyDragEventListener implements View.OnDragListener {
        public boolean onDrag(final View v, DragEvent event) {
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
                    System.out.println(v.getId());
                    final Integer idEvent = v.getId();
                    new Thread(){
                        @Override
                        public void run() {
                            flipViews(queImagenFlip(queID(IDcomienzo)),queImagenFlip(queID(idEvent)));
                            try{
                                Thread.sleep(500);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            intercambiocartas(IDcomienzo,idEvent);
                            try{
                                Thread.sleep(500);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            flipViews(queImagenFlip(queID(IDcomienzo)),queImagenFlip(queID(idEvent)));
                        }
                    }.start();
                    v.setBackgroundColor(0);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(0);
                    v.invalidate();
                    return true;
                default:
                    break;


            }
            return false;
        }
    }

    private void intercambiocartas(final Integer iDcomienzo, final Integer iDfinal) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    private void intercambiosiete(Integer a) {
            Carta aux = cards.get(39);
            cards.remove(39);
            cards.add(cardsj1[a]);
            cardsj1[a] = aux;
            ImageView aux1 = queImagen(a);
            assignImages(cardsj1[a],aux1);
            assignImages(cards.get(39),triumphe);
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
    private EasyFlipView queImagenFlip(Integer a) {
        switch (a){
            case 0:
                return c1whole;
            case 1:
                return c2whole;
            case 2:
                return c3whole;
            case 3:
                return c4whole;
            case 4:
                return c5whole;
            case 5:
                return c6whole;
            default:
                return null;
        }
    }


    private Integer queID(Integer id){
        if (id ==c1.getId()) {
            return 0;
        }else if(id ==c2.getId()) {
            return 1;
        }else if(id ==c3.getId()) {
            return 2;
        }else if(id ==c4.getId()) {
            return 3;
        }else if(id ==c5.getId()) {
            return 4;
        }else if(id ==c6.getId()) {
            return 5;
        }else {
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
                break;
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


    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }

}