package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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


    private RecyclerView rv;
    public int TEXT_LINES=1;
    private Toolbar toolbar;

    private int mRemainingTime = 30;
    private String room="";
    private Socket mSocket;
    private SQLiteDatabase db;
    private String nameUser;
    ImageView c1,c2,c3,c4,c5,c6,reverse,triumphe,j1image,j2image,j3image,j4image,chat;
    EasyFlipView c1whole,c2whole,c3whole,c4whole,c5whole,c6whole,triumphewhole;
    Button cantar;
    ArrayList<Carta> cards;
    Carta[] cardsj1 = new Carta[6];
    Integer iterator;   //Cual es la siguiente carta a robar en el mazo
    Integer IDcomienzo; //Que carta estoy comenzando a arrastrar (cada jugador tiene su propio IDcomienzo)
    boolean arrastre;   //Si estamos en arrastre o no
    Carta cartaTriunfo;
    Integer triunfo;    //1 si el triunfo es oros, 2 espadas, 3 bastos y 4 copas
    boolean baza;       //Quien se ha llevado la ultima baza, tu equipo o el de los demas(uno para cada uno)
    Integer personaBaza; //Quien se ha llevado la ultima baza, 1 representa j1, 2 a j2, 3 a j3 y 4 a j4
    Integer puntosE1,puntosE2; //Puntos de cada equipo en general
    public List<MensajeDeTexto> mensajeDeTextos;



    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("message", onNewMessage);
    }

    public void attemptSend(String message) {

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
                        FragmentManager fm = getSupportFragmentManager();


                        Chat fragment = (Chat) fm.findFragmentById(R.id.fragmento_chat);

                        if (fragment != null) {
                            fragment.CreateMensaje(username, message, 2);
                        }
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

    private Emitter.Listener onRepartirCartas = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String todo;
                    String username;
                    String partida;
                    Integer equipo;
                    String carta1;
                    String carta2;
                    String carta3;
                    String carta4;
                    String carta5;
                    String carta6;
                    try {
                        JSONObject datos = data.getJSONObject("repartidas");
                        username = datos.getString("jugador");
                        partida = datos.getString("partida");
                        equipo = datos.getInt("equipo");
                        carta1 = datos.getString("c1");
                        carta2 = datos.getString("c2");
                        carta3 = datos.getString("c3");
                        carta4 = datos.getString("c4");
                        carta5 = datos.getString("c5");
                        carta6 = datos.getString("c6");

                    } catch (JSONException e) {
                        return;
                    }
                    if (username.equals(nameUser)) {
                        Log.d("username", username);
                        Log.d("nameuser", nameUser);
                        cardsj1[0] = new Carta(carta1);
                        cardsj1[1] = new Carta(carta2);
                        cardsj1[2] = new Carta(carta3);
                        cardsj1[3] = new Carta(carta4);
                        cardsj1[4] = new Carta(carta5);
                        cardsj1[5] = new Carta(carta6);
                        assignImages(cardsj1[0], c1);
                        assignImages(cardsj1[1], c2);
                        assignImages(cardsj1[2], c3);
                        assignImages(cardsj1[3], c4);
                        assignImages(cardsj1[4], c5);
                        assignImages(cardsj1[5], c6);
                    }

                }
            });
        }
    };

    private Emitter.Listener onRepartirTriunfo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String triunfo;
                    try {
                        triunfo = data.getString("triunfoRepartido");

                    } catch (JSONException e) {
                        return;
                    }
                    cartaTriunfo = new Carta(triunfo);
                    assignImages(cartaTriunfo, triumphe);
                    iniciarPartida();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_juego);

        FragmentManager fm = getSupportFragmentManager();
        Chat fragmentoChat = (Chat) fm.findFragmentById(R.id.fragmento_chat);
        MyOpenHelper dbHelper = new MyOpenHelper(this);
        db = dbHelper.getWritableDatabase();
        mensajeDeTextos = new ArrayList<>();
        nameUser=getName();
        Log.d("holaaaaaaaa ",nameUser);
        Bundle b = getIntent().getExtras();
        if(b != null)
            room = b.getString("key");
        mSocket = IO.socket(URI.create("http://148.3.47.50:5000"));
        mSocket.on("message", onNewMessage);
        mSocket.on("roomData", roomInfo);
        mSocket.on("RepartirCartas", onRepartirCartas);
        mSocket.on("RepartirTriunfo", onRepartirTriunfo);
        mSocket.connect();
        JSONObject auxiliar = new JSONObject();
        try {
            auxiliar.put("name", getName());
            auxiliar.put("room", room);
            auxiliar.put("tipo", 1);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("jsonDePrueba",auxiliar.toString());
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
        cantar = (Button) findViewById(R.id.button_cantar);

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
        iterator = 0;
        IDcomienzo = 0;

        cards = new ArrayList<>();
        MyDragEventListener mDragListen = new MyDragEventListener();
        c1.setOnDragListener(mDragListen);
        c2.setOnDragListener(mDragListen);
        c3.setOnDragListener(mDragListen);
        c4.setOnDragListener(mDragListen);
        c5.setOnDragListener(mDragListen);
        c6.setOnDragListener(mDragListen);



        /*
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
        */
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("holapulsa","hoas");
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmento_chat, Chat.class, null)
                        .commit();
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
        triumphewhole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCambio7();
            }
        });
        cantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCantar();
            }
        });

    }
    //Animación de iniciar la partida;
    private void iniciarPartida(){
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

    //Set visibilities
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
        String cual = card.getId();
        switch (cual){
            case "0O":
                image.setImageResource(R.drawable.asoros);
                break;
            case "0E":
                image.setImageResource(R.drawable.asespadas);
                break;
            case "0B":
                image.setImageResource(R.drawable.asbastos);
                break;
            case "0C":
                image.setImageResource(R.drawable.ascopas);
                break;
            case "2O":
                image.setImageResource(R.drawable.tresoros);
                break;
            case "2E":
                image.setImageResource(R.drawable.tresespadas);
                break;
            case "2B":
                image.setImageResource(R.drawable.tresbastos);
                break;
            case "2C":
                image.setImageResource(R.drawable.trescopas);
                break;
            case "9O":
                image.setImageResource(R.drawable.reyoros);
                break;
            case "9E":
                image.setImageResource(R.drawable.reyespadas);
                break;
            case "9B":
                image.setImageResource(R.drawable.reybastos);
                break;
            case "9C":
                image.setImageResource(R.drawable.reycopas);
                break;
            case "7O":
                image.setImageResource(R.drawable.sotaoros);
                break;
            case "7E":
                image.setImageResource(R.drawable.sotaespadas);
                break;
            case "7B":
                image.setImageResource(R.drawable.sotabastos);
                break;
            case "7C":
                image.setImageResource(R.drawable.sotacopas);
                break;
            case "8O":
                image.setImageResource(R.drawable.caballooros);
                break;
            case "8E":
                image.setImageResource(R.drawable.caballoespadas);
                break;
            case "8B":
                image.setImageResource(R.drawable.caballobastos);
                break;
            case "8C":
                image.setImageResource(R.drawable.caballocopas);
                break;
            case "6O":
                image.setImageResource(R.drawable.sieteoros);
                break;
            case "6E":
                image.setImageResource(R.drawable.sieteespadas);
                break;
            case "6B":
                image.setImageResource(R.drawable.sietebastos);
                break;
            case "6C":
                image.setImageResource(R.drawable.sietecopas);
                break;
            case "5O":
                image.setImageResource(R.drawable.seisoros);
                break;
            case "5E":
                image.setImageResource(R.drawable.seisespadas);
                break;
            case "5B":
                image.setImageResource(R.drawable.seisbastos);
                break;
            case "5C":
                image.setImageResource(R.drawable.seiscopas);
                break;
            case "4O":
                image.setImageResource(R.drawable.cincooros);
                break;
            case "4E":
                image.setImageResource(R.drawable.cincoespadas);
                break;
            case "4B":
                image.setImageResource(R.drawable.cincobastos);
                break;
            case "4C":
                image.setImageResource(R.drawable.cincocopas);
                break;
            case "3O":
                image.setImageResource(R.drawable.cuatrooros);
                break;
            case "3E":
                image.setImageResource(R.drawable.cuatroespadas);
                break;
            case "3B":
                image.setImageResource(R.drawable.cuatrobastos);
                break;
            case "3C":
                image.setImageResource(R.drawable.cuatrocopas);
                break;
            case "1O":
                image.setImageResource(R.drawable.dosoros);
                break;
            case "1E":
                image.setImageResource(R.drawable.dosespadas);
                break;
            case "1B":
                image.setImageResource(R.drawable.dosbastos);
                break;
            case "1C":
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