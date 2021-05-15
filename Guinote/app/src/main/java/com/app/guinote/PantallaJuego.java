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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import org.json.JSONArray;
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
    ImageView c1,c2,c3,c4,c5,c6,reverse,triumphe,j1image,chat,
            j2imagefront,j2imageback,j3imagefront,
            j3imageback,j4imagefront,j4imageback,estrella1,estrella2,estrella3,estrella4;
    EasyFlipView c1whole,c2whole,c3whole,c4whole,c5whole,c6whole,triumphewhole,j2image,j3image,j4image;
    TextView nombreOponente2, nombreOponente3, nombreOponente4;
    Button cantar;

    Integer queEquipo;
    Carta[] cardsj1 = new Carta[6];
    Carta cartaTriunfo;
    Integer nronda = 0;
    Integer QueCarta;

    Integer IDcomienzo; //Que carta estoy comenzando a arrastrar (cada jugador tiene su propio IDcomienzo)
    Integer queOrden;
    Boolean ultimo = false;

    String name1,name2,name3;
    Integer orden1,orden2,orden3;
    Integer cuentaNombres = 0;
    Integer cuentaVeces = 0;

    //Variables para el arrastre
    boolean arrastre;   //Si estamos en arrastre o no
    Integer paloArrastre;
    Integer RondaArrastre;
    Integer RankingArrastre;
    Carta companyero;

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
                    try {
                        room = data.getString("room");
                    } catch (JSONException e) {
                        return;
                    }
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
                    Integer orden;
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
                        orden = datos.getInt("orden");

                    } catch (JSONException e) {
                        return;
                    }
                    if (username.equals(nameUser)) {
                        queEquipo = equipo;
                        queOrden = orden;
                        if(queOrden == 1){
                            ultimo = true;
                            estrella1.setVisibility(View.VISIBLE);
                        }
                        if(queOrden == 2){
                            estrella4.setVisibility(View.VISIBLE);
                        }
                        if(queOrden == 3){
                            estrella2.setVisibility(View.VISIBLE);
                        }
                        if(queOrden == 4){
                            estrella3.setVisibility(View.VISIBLE);
                        }
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
                        cuentaVeces++;
                    }else{
                        if(cuentaNombres == 0){
                            name1 = username;
                            orden1 = orden;
                        }else if(cuentaNombres == 1){
                            name2 = username;
                            orden2 = orden;
                        }else if(cuentaNombres == 2){
                            name3 = username;
                            orden3 = orden;
                        }
                        cuentaNombres++;
                        cuentaVeces++;
                        //////////////ACORDARSE DE LUEGO HACER EL SETTEXT CON MI NOMBRE Y MI ORDEN
                    }
                    if(cuentaVeces == 4){
                        switch (queOrden){
                            case 1:
                                switch (orden1){
                                    case 2:
                                        nombreOponente3.setText(name1);
                                        switch (orden2){
                                            case 3:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                break;
                                            case 4:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                break;
                                        }
                                        break;
                                    case 3:
                                        nombreOponente2.setText(name1);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente3.setText(name2);
                                                nombreOponente4.setText(name3);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name3);
                                                nombreOponente4.setText(name2);
                                                break;
                                        }
                                        break;
                                    case 4:
                                        nombreOponente4.setText(name1);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                break;
                                            case 3:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case 2:
                                switch (orden1){
                                    case 1:
                                        nombreOponente4.setText(name1);
                                        switch (orden2){
                                            case 3:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                break;
                                        }
                                        break;
                                    case 3:
                                        nombreOponente3.setText(name1);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                break;
                                            case 4:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                break;
                                        }
                                        break;
                                    case 4:
                                        nombreOponente4.setText(name1);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                break;
                                            case 3:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case 3:
                                switch (orden1){
                                    case 1:
                                        nombreOponente2.setText(name1);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente3.setText(name3);
                                                nombreOponente4.setText(name2);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name2);
                                                nombreOponente4.setText(name3);
                                                break;
                                        }
                                        break;
                                    case 2:
                                        nombreOponente4.setText(name1);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                break;
                                        }
                                        break;
                                    case 4:
                                        nombreOponente3.setText(name1);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                break;
                                            case 2:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case 4:
                                switch (orden1){
                                    case 1:
                                        nombreOponente3.setText(name1);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                break;
                                            case 3:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                break;
                                        }
                                        break;
                                    case 2:
                                        nombreOponente2.setText(name1);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name2);
                                                nombreOponente4.setText(name3);
                                                break;
                                            case 3:
                                                nombreOponente3.setText(name3);
                                                nombreOponente4.setText(name2);
                                                break;
                                        }
                                        break;
                                    case 3:
                                        nombreOponente4.setText(name1);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                break;
                                            case 2:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
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

    private Emitter.Listener oncartaJugada = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONObject todo;
                    String carta;
                    String quien;
                    try {
                        carta = data.getString("cartaJugada");
                        quien = data.getString("jugador");

                    } catch (JSONException e) {
                        return;
                    }
                    Log.d("carta",carta);
                    Log.d("quien",quien);

                    if (!quien.equals(nameUser)){
                        queOrden--;
                        Carta aux = new Carta(carta);
                        if(queOrden == 1){
                            animacionCartaj4Front();
                            aux = new Carta(carta);
                            assignImages(aux,j4imagefront);
                            estrella1.setVisibility(View.VISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                        }
                        if(queOrden == 2){
                            animacionCartaj2Front();
                            aux = new Carta(carta);
                            assignImages(aux,j2imagefront);
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.VISIBLE);
                        }
                        if(queOrden == 3){
                            animacionCartaj3Front();
                            aux = new Carta(carta);
                            assignImages(aux,j3imagefront);
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.VISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                        }
                        if(arrastre){
                            actualizar_datos_arrastre(aux);
                        }
                    }else{
                        estrella1.setVisibility(View.INVISIBLE);
                        estrella2.setVisibility(View.INVISIBLE);
                        estrella3.setVisibility(View.VISIBLE);
                        estrella4.setVisibility(View.INVISIBLE);
                        if(ultimo){
                            ultimo = false;
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                            JSONObject aux = new JSONObject();
                            try {
                                aux.put("partida", room);
                                aux.put("nronda", nronda);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            mSocket.emit("contarPuntos", aux, new Ack() {
                                @Override
                                public void call(Object... args) {
                                    //JSONObject response = (JSONObject) args[0];
                                    //System.out.println(response); // "ok"
                                }
                            });
                            if(!arrastre) {
                                mSocket.emit("robarCarta", aux, new Ack() {
                                    @Override
                                    public void call(Object... args) {
                                        //JSONObject response = (JSONObject) args[0];
                                        //System.out.println(response); // "ok"
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener onRecuento = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String ganador;
                    try {
                        ganador = data.getString("winner");

                    } catch (JSONException e) {
                        return;
                    }
                    if (!ganador.equals(nameUser)){
                        if(ganador.equals(nombreOponente2.getText().toString())){
                            queOrden=3;
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.VISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);

                        }else if(ganador.equals(nombreOponente3.getText().toString())){
                            ultimo = true;
                            queOrden=4;
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.VISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                        }else if(ganador.equals(nombreOponente4.getText().toString())){
                            queOrden=2;
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.VISIBLE);
                        }
                    }else{
                        estrella1.setVisibility(View.VISIBLE);
                        estrella2.setVisibility(View.INVISIBLE);
                        estrella3.setVisibility(View.INVISIBLE);
                        estrella4.setVisibility(View.INVISIBLE);
                        queOrden=1;
                    }
                    nronda++;
                    disolverCartas();
                    if(nronda == 4){
                        arrastre =true;
                        triumphewhole.setVisibility(View.GONE);
                        reverse.setVisibility(View.GONE);
                    }
                    if(arrastre){
                        RondaArrastre = 0;
                        paloArrastre = 0;
                        RankingArrastre = 11;
                    }
                }
            });
        }
    };

    private Emitter.Listener onRobo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String card;
                    String usuario;
                    try {
                        card = data.getString("carta");
                        usuario = data.getString("jugador");


                    } catch (JSONException e) {
                        return;
                    }
                    if (usuario.equals(nameUser)) {
                        Carta nueva = new Carta(card);
                        cardsj1[QueCarta] = nueva;
                        assignImages(nueva, queImagen(QueCarta));
                        animacionRobarCarta(QueCarta);
                    }
                }
            });
        }
    };

    private Emitter.Listener onCambio = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String card;
                    try {
                        card = data.getString("tuya");


                    } catch (JSONException e) {
                        return;
                    }
              //      if(username.equals(nameUser)){
                //        animacion7(sitio);
                  //  }
                    Log.d("cambio7",card.toString());
                    String texto = "El usuario "+card+ " ha cambiado el 7";
                    Toast.makeText(getApplicationContext(),texto,Toast.LENGTH_LONG).show();
                }
            });
        }
    };


    private Emitter.Listener onMedio = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String card;
                    try {
                        card = data.getString("medio");


                    } catch (JSONException e) {
                        return;
                    }
                    Log.d("medio7",card.toString());
                }
            });
        }
    };

    private Emitter.Listener onCante = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray) args[0];
                    JSONObject datos;
                    String username = "";
                    String o_20 = "";
                    String e_20 = "";
                    String b_20 = "";
                    String c_20 = "";
                    try {
                        for (int i=0;i<data.length();i++){
                            datos=data.getJSONObject(i);
                            username=datos.getString("nombre");
                            o_20 = datos.getString("o_20");
                            e_20 = datos.getString("e_20");
                            b_20 = datos.getString("b_20");
                            c_20 = datos.getString("c_20");
                        }

                    } catch (JSONException e) {
                        return;
                    }
                    boolean ha_entrado = false;
                    String texto = "";
                    if(!o_20.equals(null)){
                        texto = "El usuario ";
                        texto = texto+o_20;
                        texto = texto + " ha cantado";
                        ha_entrado = true;
                        if(cartaTriunfo.getPalo() == 1){
                            texto = texto + " las 40";
                        }else{
                            texto = texto + " las 20 en oros";
                        }
                        ha_entrado = true;
                    }
                    if(!e_20.equals(null)){
                        if(!ha_entrado){
                            texto = "El usuario ";
                            texto = texto+e_20;
                            ha_entrado = true;
                        }
                        if(cartaTriunfo.getPalo() == 2){
                            texto = texto + " las 40";
                        }else{
                            texto = texto + " las 20 en espadas";
                        }
                    }
                    if(!b_20.equals(null)){
                        if(!ha_entrado){
                            texto = "El usuario ";
                            texto = texto+b_20;
                            ha_entrado = true;
                        }
                        if(cartaTriunfo.getPalo() == 3){
                            texto = texto + " las 40";
                        }else{
                            texto = texto + " las 20 en bastos";
                        }
                    }
                    if(!c_20.equals(null)){
                        if(!ha_entrado){
                            texto = "El usuario ";
                            texto = texto+c_20;
                        }
                        if(cartaTriunfo.getPalo() == 4){
                            texto = texto + " las 40";
                        }else{
                            texto = texto + " las 20 en copas";
                        }
                    }
                    Toast.makeText(getApplicationContext(),texto,Toast.LENGTH_LONG).show();
                    Log.d("o_20",o_20);
                    Log.d("e_20",e_20);
                    Log.d("b_20",b_20);
                    Log.d("c_20",c_20);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pantalla_juego);

        FragmentManager fm = getSupportFragmentManager();
        Chat fragmentoChat = (Chat) fm.findFragmentById(R.id.fragmento_chat);
        MyOpenHelper dbHelper = new MyOpenHelper(this);
        db = dbHelper.getWritableDatabase();
        mensajeDeTextos = new ArrayList<>();
        nameUser=getName();
        Bundle b = getIntent().getExtras();
        if(b != null)
            room = b.getString("key");
        //mSocket = IO.socket(URI.create("http://148.3.47.50:5000"));
        mSocket=Pantalla_app.mSocket;
        mSocket.on("message", onNewMessage);
        mSocket.on("roomData", roomInfo);
        mSocket.on("RepartirCartas", onRepartirCartas);
        mSocket.on("RepartirTriunfo", onRepartirTriunfo);
        mSocket.on("cartaJugada", oncartaJugada);
        mSocket.on("winner", onRecuento);
        mSocket.on("roba", onRobo);
        mSocket.on("cartaMedio", onMedio);
        mSocket.on("cartaCambio", onCambio);
        mSocket.on("cante", onCante);
        //mSocket.connect();
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
        estrella1 = (ImageView) findViewById(R.id.estrella_turnj1);
        estrella2 = (ImageView) findViewById(R.id.estrella_turnj2);
        estrella3 = (ImageView) findViewById(R.id.estrella_turnj3);
        estrella4 = (ImageView) findViewById(R.id.estrella_turnj4);
        nombreOponente2 = (TextView) findViewById(R.id.nombre_j2);
        nombreOponente3 = (TextView) findViewById(R.id.nombre_j3);
        nombreOponente4 = (TextView) findViewById(R.id.nombre_j4);
        chat = (ImageView) findViewById(R.id.icono_chat);
        j1image = (ImageView) findViewById(R.id.carta_jugador1);
        j2image = (EasyFlipView) findViewById(R.id.carta_jugador2);
        j3image = (EasyFlipView) findViewById(R.id.carta_jugador3);
        j4image = (EasyFlipView) findViewById(R.id.carta_jugador4);
        j2imagefront = (ImageView) findViewById(R.id.frontcartaj2);
        j2imageback = (ImageView) findViewById(R.id.backcartaj2);
        j3imagefront = (ImageView) findViewById(R.id.frontcartaj3);
        j3imageback = (ImageView) findViewById(R.id.backcartaj3);
        j4imagefront = (ImageView) findViewById(R.id.frontcartaj4);
        j4imageback = (ImageView) findViewById(R.id.backcartaj4);
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
        j1image.setVisibility(View.INVISIBLE);
        j2image.setVisibility(View.INVISIBLE);
        j3image.setVisibility(View.INVISIBLE);
        j4image.setVisibility(View.INVISIBLE);
        estrella1.setVisibility(View.INVISIBLE);
        estrella2.setVisibility(View.INVISIBLE);
        estrella3.setVisibility(View.INVISIBLE);
        estrella4.setVisibility(View.INVISIBLE);

        arrastre = false;
        RondaArrastre = 0;
        paloArrastre = 0;
        RankingArrastre = 11;

        MyDragEventListener mDragListen = new MyDragEventListener();
        c1.setOnDragListener(mDragListen);
        c2.setOnDragListener(mDragListen);
        c3.setOnDragListener(mDragListen);
        c4.setOnDragListener(mDragListen);
        c5.setOnDragListener(mDragListen);
        c6.setOnDragListener(mDragListen);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                v.startDrag(dragData,myShadow,null,0);

                return true;
            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrastre_y_puede(0)){
                    puedeLanzar(0);
                }
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
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrastre_y_puede(1)){
                    puedeLanzar(1);
                }
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
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrastre_y_puede(2)){
                    puedeLanzar(2);
                }
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
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrastre_y_puede(3)){
                    puedeLanzar(3);
                }
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
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrastre_y_puede(4)){
                    puedeLanzar(4);
                }
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
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrastre_y_puede(5)){
                    puedeLanzar(5);
                }
            }
        });
        triumphewhole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject aux = new JSONObject();
                try {
                    aux.put("jugador", nameUser);
                    aux.put("nombre", room);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mSocket.emit("cambiar7", aux, new Ack() {
                    @Override
                    public void call(Object... args) {
                        //JSONObject response = (JSONObject) args[0];
                        //System.out.println(response); // "ok"
                    }
                });
            }
        });
        cantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject aux = new JSONObject();
                try {
                    aux.put("jugador", nameUser);
                    aux.put("nombre", room);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mSocket.emit("cantar", aux, new Ack() {
                    @Override
                    public void call(Object... args) {
                        //JSONObject response = (JSONObject) args[0];
                        //System.out.println(response); // "ok"
                    }
                });
            }
        });

    }

    private void actualizar_datos_arrastre(Carta hola){
        if(RondaArrastre == 0){
            paloArrastre = hola.getPalo();
            RondaArrastre = 1;
            RankingArrastre = hola.getRanking();
            if(queOrden == 2){
                companyero = new Carta(hola.getId());
            }
        }else{
            if(RondaArrastre == 1 && queOrden == 2){
                companyero = new Carta(hola.getId());
                RondaArrastre++;
            }
            if(hola.getPalo() == paloArrastre){
                if( hola.getRanking() < RankingArrastre){
                    RankingArrastre = hola.getRanking();
                }
            }else if((hola.getPalo() == cartaTriunfo.getPalo()) && (paloArrastre != cartaTriunfo.getPalo())){
                paloArrastre = hola.getPalo();
                RankingArrastre = hola.getRanking();
            }
        }
    }

    private boolean arrastre_y_puede(Integer i){
        if(arrastre){
            if(RondaArrastre == 0){
                paloArrastre = cardsj1[i].getPalo();
                RondaArrastre = 1;
                RankingArrastre = cardsj1[i].getRanking();
                return true;
            }else{
                if(RondaArrastre > 1 && companyero.getPalo() == paloArrastre && companyero.getRanking() == RankingArrastre){
                    return true;
                }
                if(cardsj1[i].getPalo() == paloArrastre){
                    for(int j = 0; j<6;j++){
                        if(j!=i){
                            if(cardsj1[j].getRanking() < RankingArrastre){
                                return false;
                            }
                        }
                    }
                    if(cardsj1[i].getRanking() < RankingArrastre){
                        RankingArrastre = cardsj1[i].getRanking();
                    }
                }else if((cardsj1[i].getPalo() == cartaTriunfo.getPalo()) && (paloArrastre != cartaTriunfo.getPalo())){
                    for(int j = 0; j<6;j++){
                        if(j!=i){
                            if(cardsj1[j].getPalo() == paloArrastre){
                                return false;
                            }
                        }
                    }
                    paloArrastre = cardsj1[i].getPalo();
                    RankingArrastre = cardsj1[i].getRanking();
                }else if(cardsj1[i].getPalo() != paloArrastre && paloArrastre != cartaTriunfo.getPalo()){
                    for(int j = 0; j<6;j++){
                        if(j!=i){
                            if(cardsj1[j].getPalo() == paloArrastre){
                                return false;
                            }
                            if(cardsj1[j].getPalo() == cartaTriunfo.getPalo()){
                                return false;
                            }
                        }
                    }
                }else if(cardsj1[i].getPalo() != paloArrastre && paloArrastre == cartaTriunfo.getPalo()){
                    for(int j = 0; j<6;j++){
                        if(j!=i){
                            if(cardsj1[j].getPalo() == cartaTriunfo.getPalo()){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean puedeLanzar(Integer i){
        if(queOrden == 1){
            QueCarta = i;
            assignImages(cardsj1[i],j1image);
            j1image.setVisibility(View.VISIBLE);
            JSONObject aux = new JSONObject();
            try {
                aux.put("jugador", getName());
                aux.put("partida", room);
                aux.put("nronda", nronda);
                aux.put("carta", cardsj1[i].getId());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.d("jsonDePrueba",aux.toString());
            mSocket.emit("lanzarCarta", aux, new Ack() {
                @Override
                public void call(Object... args) {
                    //JSONObject response = (JSONObject) args[0];
                    //System.out.println(response); // "ok"
                }
            });
            queOrden--;
            if(i == 0){
                c1whole.setVisibility(View.INVISIBLE);
                c1whole.flipTheView();
            }else if(i == 1){
                c2whole.setVisibility(View.INVISIBLE);
                c2whole.flipTheView();
            }else if(i == 2){
                c3whole.setVisibility(View.INVISIBLE);
                c3whole.flipTheView();
            }else if(i == 3){
                c4whole.setVisibility(View.INVISIBLE);
                c4whole.flipTheView();
            }else if(i == 4){
                c5whole.setVisibility(View.INVISIBLE);
                c5whole.flipTheView();
            }else if(i == 5){
                c6whole.setVisibility(View.INVISIBLE);
                c6whole.flipTheView();
            }
            estrella1.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
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
    private void disolverCartas(){
        new Thread() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                } catch (Exception e){
                    e.printStackTrace();
                }
                setNotVisibilityLanzadasCard();

            }
        }.start();
    }

    private void animacionCartaj2Front(){
        new Thread() {
            @Override
            public void run() {
                setVisibilityFrontCard();
                try{
                    Thread.sleep(400);
                } catch (Exception e){
                    e.printStackTrace();
                }
                updatej2Card();
            }
        }.start();
    }

    private void animacionCartaj3Front(){
        new Thread() {
            @Override
            public void run() {
                setVisibilityj3Card();
                try{
                    Thread.sleep(400);
                } catch (Exception e){
                    e.printStackTrace();
                }
                updatej3Card();
            }
        }.start();
    }

    private void animacionCartaj4Front(){
        new Thread() {
            @Override
            public void run() {
                setVisibilityj4Card();
                try{
                    Thread.sleep(400);
                } catch (Exception e){
                    e.printStackTrace();
                }
                updatej4Card();
            }
        }.start();
    }

    private void animacionRobarCarta(final Integer i){
        new Thread() {
            @Override
            public void run() {
                setVisibilityCard(i);
                try{
                    Thread.sleep(500);
                } catch (Exception e){
                    e.printStackTrace();
                }
                girarCarta(i);

            }
        }.start();
    }

    //Set visibilities

    private void setNotVisibilityLanzadasCard() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                j2image.setVisibility(View.INVISIBLE);
                updatej2Card();
                j3image.setVisibility(View.INVISIBLE);
                updatej3Card();
                j4image.setVisibility(View.INVISIBLE);
                updatej4Card();
                j1image.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setVisibilityFrontCard() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                j2image.setVisibility(View.VISIBLE);
            }
        });
    }
    private void setVisibilityj3Card() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                j3image.setVisibility(View.VISIBLE);
            }
        });
    }
    private void setVisibilityj4Card() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                j4image.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updatej2Card() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                j2image.flipTheView();
            }
        });

    }
    private void updatej3Card() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                j3image.flipTheView();
            }
        });

    }
    private void updatej4Card() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                j4image.flipTheView();
            }
        });

    }
    private void setVisibilityCard(final Integer i){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                queImagenFlip(i).setVisibility(View.VISIBLE);
            }
        });
    }
    private void girarCarta(final Integer i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                queImagenFlip(i).flipTheView();
            }
        });

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
        Carta aux = cartaTriunfo;
        cartaTriunfo = cardsj1[a];
        cardsj1[a] = aux;
        ImageView aux1 = queImagen(a);
        assignImages(cardsj1[a],aux1);
        assignImages(cartaTriunfo,triumphe);
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