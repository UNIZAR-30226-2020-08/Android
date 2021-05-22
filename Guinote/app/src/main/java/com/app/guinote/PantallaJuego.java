package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.guinote.ActivityTorneo.Torneo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
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
    private int torneo=0;
    private Context ctx;

    private String miCarta="";
    private String miTapete="";
    private int puntosProv=0;
    private int puntosProv1=0;
    String quienWinner ="";
    private SQLiteDatabase db;
    private int gano=0;
    private String nameUser;
    ImageView c1,c2,c3,c4,c5,c6,reverse,triumphe,j1image,chat,
            j2imagefront,j2imageback,j3imagefront,
            j3imageback,j4imagefront,j4imageback,estrella1,estrella2,estrella3,estrella4;
    EasyFlipView c1whole,c2whole,c3whole,c4whole,c5whole,c6whole,triumphewhole,j2image,j3image,j4image;
    TextView nombreOponente2, nombreOponente3, nombreOponente4, cuentaatras, cuantascartas, copasadversarioj2,copasadversarioj3,copasadversarioj4, ptmio, ptrival, cartasrestantes, ptmiotext, ptorivaltext;
    Button cantar,pausar;
    Integer cuantascartasint = 16;
    CircleImageView fperfiladversarioj2,fperfiladversarioj3,fperfiladversarioj4;
    private int pauso;
    String resultado;

    Integer queEquipo;
    Carta[] cardsj1 = new Carta[6];
    Carta cartaTriunfo;
    Integer nronda = 0;
    Integer QueCarta;
    long numeroTimer = 0;

    Integer IDcomienzo; //Que carta estoy comenzando a arrastrar (cada jugador tiene su propio IDcomienzo)
    Integer queOrden;
    Boolean ultimo = false;

    Boolean aun_no = false;

    String name1,name2,name3;
    Integer orden1,orden2,orden3;
    Integer copas1,copas2,copas3;
    String foto1,foto2,foto3;
    Integer cuentaNombres = 0;
    Integer cuentaVeces = 0;

    //Variables para el arrastre
    boolean arrastre;   //Si estamos en arrastre o no
    Integer paloArrastre;
    Integer RondaArrastre;
    Integer RankingArrastre;
    Carta companyero;
    boolean deVueltas;
    String winner = "";

    long duration = TimeUnit.SECONDS.toMillis(20);
    CountDownTimer contador;
    Integer puntosmios, puntosrival = 0;

    public List<MensajeDeTexto> mensajeDeTextos;



    @Override
    public void onDestroy() {
        super.onDestroy();
        contador.cancel();
        if (torneo==1 && gano==1){
            Torneo.terminoPartida(room);
        }else if(torneo==1 && gano==0){
            Intent intent = new Intent(this, Pantalla_app.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }


        JSONObject aux = new JSONObject();
        try {
            aux.put("partida", room);
            aux.put("jugador", nameUser);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        if(pauso!=1) {
            contador.cancel();
            mSocket.emit("leavePartida", aux, new Ack() {
                @Override
                public void call(Object... args) {
                    //JSONObject response = (JSONObject) args[0];
                    //System.out.println(response); // "ok"
                }
            });
        }else{
            contador.cancel();
            mSocket.emit("leavePartidaRP", new Ack() {
                @Override
                public void call(Object... args) {
                    //JSONObject response = (JSONObject) args[0];
                    //System.out.println(response); // "ok"
                }
            });
        }

        mSocket.off("message", onNewMessage);
        mSocket.off("RepartirCartas", onRepartirCartas);
        mSocket.off("RepartirCartasRP", onRepartirCartasRP);
        mSocket.off("RepartirTriunfo", onRepartirTriunfo);
        mSocket.off("RepartirTriunfoRP", onRepartirTriunfoRP);
        mSocket.off("cartaJugada", oncartaJugada);
        mSocket.off("winner", onRecuento);
        mSocket.off("roba", onRobo);
        mSocket.off("cartaCambio", onCambio);
        mSocket.off("cante", onCante);
        mSocket.off("Resultado", onResultado);
        mSocket.off("Vueltas", onVueltas);
        mSocket.off("puntos", onPuntos);
        mSocket.off("pause", onPause);
        mSocket.off("copasActualizadas",onCopasActualizadas);
        mSocket.off("pauseRequest", onPauseRequest);
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

    private Emitter.Listener onPause = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pauso=1;
                }
            });
        }
    };

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

                    if (!username.equals(nameUser)) {
                        chat.setImageResource(R.drawable.baseline_announcement_black_48);
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
                    String f_perfil;
                    Integer copas;
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
                        copas = datos.getInt("copas");
                        f_perfil = datos.getString("f_perfil");

                    } catch (JSONException e) {
                        return;
                    }
                    if (username.equals(nameUser)) {
                        arrastre = false;
                        paloArrastre = 0;
                        RondaArrastre = 0;
                        RankingArrastre = 11;
                        queEquipo = equipo;
                        queOrden = orden;
                        if(queOrden == 1){
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
                            ultimo = true;
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
                            copas1 = copas;
                            foto1 = f_perfil;
                        }else if(cuentaNombres == 1){
                            name2 = username;
                            orden2 = orden;
                            copas2 = copas;
                            foto2 = f_perfil;
                        }else if(cuentaNombres == 2){
                            name3 = username;
                            orden3 = orden;
                            copas3 = copas;
                            foto3 = f_perfil;
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
                                        copasadversarioj3.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj3);
                                        switch (orden2){
                                            case 3:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj2.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj2);
                                                assignFPerfil(foto3,fperfiladversarioj4);
                                                break;
                                            case 4:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj2.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                assignFPerfil(foto2,fperfiladversarioj4);
                                                break;
                                        }
                                        break;
                                    case 3:
                                        nombreOponente2.setText(name1);
                                        copasadversarioj2.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj2);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente3.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj4);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj3);
                                                assignFPerfil(foto2,fperfiladversarioj4);
                                                break;
                                        }
                                        break;
                                    case 4:
                                        nombreOponente4.setText(name1);
                                        copasadversarioj4.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj4);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj2.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj2);

                                                break;
                                            case 3:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj2.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj3);
                                                assignFPerfil(foto2,fperfiladversarioj2);
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case 2:
                                switch (orden1){
                                    case 1:
                                        nombreOponente4.setText(name1);
                                        copasadversarioj4.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj4);
                                        switch (orden2){
                                            case 3:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj2.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj2.setText(copas2.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                break;
                                        }
                                        break;
                                    case 3:
                                        nombreOponente3.setText(name1);
                                        copasadversarioj3.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj3);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj2.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                assignFPerfil(foto2,fperfiladversarioj4);
                                                break;
                                            case 4:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj2.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                assignFPerfil(foto2,fperfiladversarioj2);
                                                assignFPerfil(foto3,fperfiladversarioj4);
                                                break;
                                        }
                                        break;
                                    case 4:
                                        nombreOponente4.setText(name1);
                                        copasadversarioj4.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj4);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj2.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                break;
                                            case 3:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj2.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj3);
                                                assignFPerfil(foto2,fperfiladversarioj2);
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case 3:
                                switch (orden1){
                                    case 1:
                                        nombreOponente2.setText(name1);
                                        copasadversarioj2.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj2);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente3.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj3);
                                                assignFPerfil(foto2,fperfiladversarioj4);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj4);
                                                break;
                                        }
                                        break;
                                    case 2:
                                        nombreOponente4.setText(name1);
                                        copasadversarioj4.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj4);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj2.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj3);
                                                assignFPerfil(foto2,fperfiladversarioj2);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj2.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                break;
                                        }
                                        break;
                                    case 4:
                                        nombreOponente3.setText(name1);
                                        copasadversarioj3.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj3);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj2.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());
                                                break;
                                            case 2:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj2.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case 4:
                                switch (orden1){
                                    case 1:
                                        nombreOponente3.setText(name1);
                                        copasadversarioj3.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj3);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj2.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());
                                                break;
                                            case 3:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj2.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                break;
                                        }
                                        break;
                                    case 2:
                                        nombreOponente2.setText(name1);
                                        copasadversarioj2.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj2);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());

                                                break;
                                            case 3:
                                                nombreOponente3.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                break;
                                        }
                                        break;
                                    case 3:
                                        nombreOponente4.setText(name1);
                                        copasadversarioj4.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj4);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj2.setText(copas3.toString());
                                                break;
                                            case 2:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj2.setText(copas2.toString());
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        CharSequence n3 = nombreOponente3.getText();
                        CharSequence n2 = nombreOponente2.getText();
                        CharSequence n4 = nombreOponente4.getText();

                        if(quienWinner.equals(nameUser)){
                            contador.start();
                            queOrden= 1;
                            estrella1.setVisibility(View.VISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                        }else if(quienWinner.equals(n3.toString())){
                            queOrden = 4;
                            ultimo = true;
                            estrella3.setVisibility(View.VISIBLE);
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                        }else if(quienWinner.equals(n2.toString())){
                            queOrden = 3;
                            estrella2.setVisibility(View.VISIBLE);
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                        }else if(quienWinner.equals(n4.toString())){
                            queOrden = 2;
                            estrella4.setVisibility(View.VISIBLE);
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener onRepartirCartasRP = new Emitter.Listener() {
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
                    String f_perfil;
                    Integer copas;
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
                        copas = datos.getInt("copas");
                        f_perfil = datos.getString("f_perfil");

                    } catch (JSONException e) {
                        return;
                    }
                    if (username.equals(nameUser)) {
                        queOrden = orden;
                        if(nronda>3) {
                            if (nronda>8){
                                deVueltas=true;
                                arrastre = false;
                            }else{
                                arrastre = true;
                                triumphewhole.setVisibility(View.GONE);
                                reverse.setVisibility(View.GONE);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        updatecenterCard();
                                    }
                                }.start();
                                cuantascartas.setVisibility(View.GONE);
                            }
                        }else{
                            arrastre  = false;
                            deVueltas = false;
                        }
                        paloArrastre = 0;
                        RondaArrastre = 0;
                        RankingArrastre = 11;
                        queEquipo = equipo;
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
                            copas1 = copas;
                            foto1 = f_perfil;
                        }else if(cuentaNombres == 1){
                            name2 = username;
                            orden2 = orden;
                            copas2 = copas;
                            foto2 = f_perfil;
                        }else if(cuentaNombres == 2){
                            name3 = username;
                            orden3 = orden;
                            copas3 = copas;
                            foto3 = f_perfil;
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
                                        copasadversarioj3.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj3);
                                        switch (orden2){
                                            case 3:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj2.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj2);
                                                assignFPerfil(foto3,fperfiladversarioj4);
                                                break;
                                            case 4:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj2.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                assignFPerfil(foto2,fperfiladversarioj4);
                                                break;
                                        }
                                        break;
                                    case 3:
                                        nombreOponente2.setText(name1);
                                        copasadversarioj2.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj2);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente3.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj4);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj3);
                                                assignFPerfil(foto2,fperfiladversarioj4);
                                                break;
                                        }
                                        break;
                                    case 4:
                                        nombreOponente4.setText(name1);
                                        copasadversarioj4.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj4);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj2.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj2);

                                                break;
                                            case 3:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj2.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj3);
                                                assignFPerfil(foto2,fperfiladversarioj2);
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case 2:
                                switch (orden1){
                                    case 1:
                                        nombreOponente4.setText(name1);
                                        copasadversarioj4.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj4);
                                        switch (orden2){
                                            case 3:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj2.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj2.setText(copas2.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                break;
                                        }
                                        break;
                                    case 3:
                                        nombreOponente3.setText(name1);
                                        copasadversarioj3.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj3);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj2.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                assignFPerfil(foto2,fperfiladversarioj4);
                                                break;
                                            case 4:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj2.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                assignFPerfil(foto2,fperfiladversarioj2);
                                                assignFPerfil(foto3,fperfiladversarioj4);
                                                break;
                                        }
                                        break;
                                    case 4:
                                        nombreOponente4.setText(name1);
                                        copasadversarioj4.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj4);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj2.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                break;
                                            case 3:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj2.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj3);
                                                assignFPerfil(foto2,fperfiladversarioj2);
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case 3:
                                switch (orden1){
                                    case 1:
                                        nombreOponente2.setText(name1);
                                        copasadversarioj2.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj2);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente3.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj3);
                                                assignFPerfil(foto2,fperfiladversarioj4);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj4);
                                                break;
                                        }
                                        break;
                                    case 2:
                                        nombreOponente4.setText(name1);
                                        copasadversarioj4.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj4);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj2.setText(copas2.toString());
                                                assignFPerfil(foto3,fperfiladversarioj3);
                                                assignFPerfil(foto2,fperfiladversarioj2);
                                                break;
                                            case 4:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj2.setText(copas3.toString());
                                                assignFPerfil(foto2,fperfiladversarioj3);
                                                assignFPerfil(foto3,fperfiladversarioj2);
                                                break;
                                        }
                                        break;
                                    case 4:
                                        nombreOponente3.setText(name1);
                                        copasadversarioj3.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj3);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj2.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());
                                                break;
                                            case 2:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj2.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case 4:
                                switch (orden1){
                                    case 1:
                                        nombreOponente3.setText(name1);
                                        copasadversarioj3.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj3);
                                        switch (orden2){
                                            case 2:
                                                nombreOponente2.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj2.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());
                                                break;
                                            case 3:
                                                nombreOponente2.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj2.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                break;
                                        }
                                        break;
                                    case 2:
                                        nombreOponente2.setText(name1);
                                        copasadversarioj2.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj2);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name2);
                                                nombreOponente4.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj4.setText(copas3.toString());

                                                break;
                                            case 3:
                                                nombreOponente3.setText(name3);
                                                nombreOponente4.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj4.setText(copas2.toString());
                                                break;
                                        }
                                        break;
                                    case 3:
                                        nombreOponente4.setText(name1);
                                        copasadversarioj4.setText(copas1.toString());
                                        assignFPerfil(foto1,fperfiladversarioj4);
                                        switch (orden2){
                                            case 1:
                                                nombreOponente3.setText(name2);
                                                nombreOponente2.setText(name3);
                                                copasadversarioj3.setText(copas2.toString());
                                                copasadversarioj2.setText(copas3.toString());
                                                break;
                                            case 2:
                                                nombreOponente3.setText(name3);
                                                nombreOponente2.setText(name2);
                                                copasadversarioj3.setText(copas3.toString());
                                                copasadversarioj2.setText(copas2.toString());
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        CharSequence n3 = nombreOponente3.getText();
                        CharSequence n2 = nombreOponente2.getText();
                        CharSequence n4 = nombreOponente4.getText();

                        if(winner.equals(nameUser)){
                            contador.start();
                            queOrden= 1;
                            estrella1.setVisibility(View.VISIBLE);
                        }else if(winner.equals(n3.toString())){
                            queOrden = 4;
                            ultimo = true;
                            estrella3.setVisibility(View.VISIBLE);
                        }else if(winner.equals(n2.toString())){
                            queOrden = 3;
                            estrella2.setVisibility(View.VISIBLE);
                        }else if(winner.equals(n4.toString())){
                            queOrden = 2;
                            estrella4.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener onCopasActualizadas = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String hola;
                    String jugador;
                    try {
                        hola = data.getString("copas");
                        jugador = data.getString("jugador");
                    } catch (JSONException e) {
                        return;
                    }
                    if(jugador.equals(nameUser)){
                        String query="UPDATE auth SET copas='"+hola+"' WHERE user='"+getName()+"'";
                        db.execSQL(query);
                        updateCopas(hola);
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
                    aun_no = true;
                    iniciarPartida();
                }
            });
        }
    };

    private Emitter.Listener onRepartirTriunfoRP = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String triunfo;
                    String ganador;
                    try {
                        triunfo = data.getString("triunfoRepartido");
                        nronda = data.getInt("nronda");
                        ganador = data.getString("winner");
                        puntosProv= data.getInt("puntos_e0");
                        puntosProv1= data.getInt("puntos_e1");

                    } catch (JSONException e) {
                        return;
                    }
                    winner = ganador;
                    nronda++;
                    cuantascartasint=26-(nronda*4);
                    cartaTriunfo = new Carta(triunfo);
                    assignImages(cartaTriunfo, triumphe);
                    aun_no = true;
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

                    if (!quien.equals(nameUser)){
                        queOrden--;
                        Carta aux = new Carta(carta);
                        if(queOrden == 1){
                            contador.start();
                            aux = new Carta(carta);
                            assignImages(aux,j4imagefront);
                            animacionCartaj4Front();
                            estrella1.setVisibility(View.VISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                        }
                        else if(queOrden == 2){
                            aux = new Carta(carta);
                            assignImages(aux,j2imagefront);
                            animacionCartaj2Front();
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.VISIBLE);
                        }
                        else if(queOrden == 3){
                            aux = new Carta(carta);
                            assignImages(aux,j3imagefront);
                            animacionCartaj3Front();
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.VISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                        }
                        else if(queOrden == -1){
                            aux = new Carta(carta);
                            assignImages(aux,j3imagefront);
                            animacionCartaj3Front();
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.VISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                        }
                        else if(queOrden == -2){
                            aux = new Carta(carta);
                            assignImages(aux,j2imagefront);
                            animacionCartaj2Front();
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.VISIBLE);
                        }
                        else if(queOrden == -3){
                            aux = new Carta(carta);
                            assignImages(aux,j4imagefront);
                            animacionCartaj4Front();
                            estrella1.setVisibility(View.VISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                        }
                        if(arrastre){
                            actualizar_datos_arrastre(aux);
                        }
                    }else{
                        Carta aux2 = new Carta("F");
                        cardsj1[QueCarta] = aux2;
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
                            final JSONObject aux = new JSONObject();
                            try {
                                aux.put("partida", room);
                                aux.put("nronda", nronda);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //builder.show();
                                    mSocket.emit("contarPuntos", aux, new Ack() {
                                        @Override
                                        public void call(Object... args) {
                                            //JSONObject response = (JSONObject) args[0];
                                            //System.out.println(response); // "ok"
                                        }
                                    });


                                    if (!arrastre) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mSocket.emit("robarCarta", aux, new Ack() {
                                                    @Override
                                                    public void call(Object... args) {
                                                        //JSONObject response = (JSONObject) args[0];
                                                        //System.out.println(response); // "ok"
                                                    }
                                                });
                                            }
                                        },2000);
                                    }
                                }
                            }, 3000);
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
                    if(nronda != 9) {
                        if (!ganador.equals(nameUser)) {
                            if (ganador.equals(nombreOponente2.getText().toString())) {
                                queOrden = 3;
                                estrella1.setVisibility(View.INVISIBLE);
                                estrella2.setVisibility(View.VISIBLE);
                                estrella3.setVisibility(View.INVISIBLE);
                                estrella4.setVisibility(View.INVISIBLE);

                            } else if (ganador.equals(nombreOponente3.getText().toString())) {
                                ultimo = true;
                                queOrden = 4;
                                estrella1.setVisibility(View.INVISIBLE);
                                estrella2.setVisibility(View.INVISIBLE);
                                estrella3.setVisibility(View.VISIBLE);
                                estrella4.setVisibility(View.INVISIBLE);
                            } else if (ganador.equals(nombreOponente4.getText().toString())) {
                                queOrden = 2;
                                estrella1.setVisibility(View.INVISIBLE);
                                estrella2.setVisibility(View.INVISIBLE);
                                estrella3.setVisibility(View.INVISIBLE);
                                estrella4.setVisibility(View.VISIBLE);
                            }
                        } else {
                            estrella1.setVisibility(View.VISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            estrella3.setVisibility(View.INVISIBLE);
                            estrella4.setVisibility(View.INVISIBLE);
                            queOrden = 1;
                            contador.start();
                        }
                    }
                    aun_no = true;
                    nronda++;
                    disolverCartas();
                    cuantascartasint = cuantascartasint -4;
                    cuantascartas.setText(cuantascartasint.toString());
                    if(nronda == 4){
                        arrastre =true;
                        triumphewhole.setVisibility(View.GONE);
                        reverse.setVisibility(View.GONE);
                        new Thread() {
                            @Override
                            public void run() {
                                updatecenterCard();
                            }
                        }.start();
                        cuantascartas.setVisibility(View.GONE);
                    }
                    if(arrastre){
                        RondaArrastre = 0;
                        paloArrastre = 0;
                        RankingArrastre = 11;
                    }
                    if(nronda == 10){
                        quienWinner = ganador;
                        arrastre = false;
                        new Thread() {
                            @Override
                            public void run() {
                                try{
                                    Thread.sleep(5000);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                                JSONObject aux = new JSONObject();
                                try {
                                    aux.put("partida", room);
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                mSocket.emit("finalizarPartida", aux, new Ack() {
                                    @Override
                                    public void call(Object... args) {
                                        //JSONObject response = (JSONObject) args[0];
                                        //System.out.println(response); // "ok"
                                    }
                                });
                            }
                        }.start();
                    }
                    if(pauso==1) {
                        contador.cancel();
                        Intent intent = new Intent(getApplicationContext(), Pantalla_app.class);
                        startActivity(intent);
                        finish();
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
                        aun_no = true;
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
                    JSONObject hola;
                    String jugador;
                    String carta;
                    try {
                        hola = data.getJSONObject("tuya");
                        jugador = hola.getString("jugador");


                    } catch (JSONException e) {
                        return;
                    }
                    if(jugador.equals(nameUser)){
                        aun_no = true;
                        animacion7(find7());
                    }else{
                        Carta aux = null;
                        if(cartaTriunfo.getPalo() == 1){
                            aux = new Carta("6O");
                        }else if(cartaTriunfo.getPalo() == 2){
                            aux = new Carta("6E");
                        }else if(cartaTriunfo.getPalo() == 3){
                            aux = new Carta("6B");
                        }else if(cartaTriunfo.getPalo() == 4){
                            aux = new Carta("6C");
                        }
                        assignImages(aux,triumphe);
                    }
                    String texto = "El usuario "+jugador+ " ha cambiado el 7";
                    Toast.makeText(getApplicationContext(),texto,Toast.LENGTH_LONG).show();
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
                    String[] palo = new String[4];
                    String partida = "";
                    try {
                        for (int i=0;i<data.length();i++){
                            datos=data.getJSONObject( i);
                            username=datos.getString("usuario");
                            palo[i] = datos.getString("palo");
                            partida = datos.getString("partida");
                        }

                    } catch (JSONException e) {
                        return;
                    }
                    boolean ha_entrado = false;
                    String texto = "";
                    for (int i = 0; i<palo.length;i++){
                        if(palo[i] != null) {
                            if (palo[i].equals("o_20")) {
                                texto = "El usuario ";
                                texto = texto + username;
                                texto = texto + " ha cantado";
                                ha_entrado = true;
                                if (cartaTriunfo.getPalo() == 1) {
                                    texto = texto + " las 40";

                                } else {
                                    texto = texto + " las 20 en oros";
                                }
                                ha_entrado = true;
                            }
                            if (palo[i].equals("e_20")) {
                                if (!ha_entrado) {
                                    texto = "El usuario ";
                                    texto = texto + username;
                                    texto = texto + " ha cantado";
                                    ha_entrado = true;
                                }
                                if (cartaTriunfo.getPalo() == 2) {
                                    texto = texto + " las 40";
                                } else {
                                    texto = texto + " las 20 en espadas";
                                }
                            }
                            if (palo[i].equals("b_20")) {
                                if (!ha_entrado) {
                                    texto = "El usuario ";
                                    texto = texto + username;
                                    texto = texto + " ha cantado";
                                    ha_entrado = true;
                                }
                                if (cartaTriunfo.getPalo() == 3) {
                                    texto = texto + " las 40";
                                } else {
                                    texto = texto + " las 20 en bastos";
                                }
                            }
                            if (palo[i].equals("c_20")) {
                                if (!ha_entrado) {
                                    texto = "El usuario ";
                                    texto = texto + username;
                                    texto = texto + " ha cantado";
                                }
                                if (cartaTriunfo.getPalo() == 4) {
                                    texto = texto + " las 40";
                                } else {
                                    texto = texto + " las 20 en copas";
                                }
                            }
                        }
                    }
                    if(!texto.equals("")){
                        Toast.makeText(getApplicationContext(),texto,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    private Emitter.Listener onResultado = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Integer eq1;
                    Integer eq2;
                    try {
                        eq1 = data.getInt("puntos_e0");
                        eq2 = data.getInt("puntos_e1");

                    } catch (JSONException e) {
                        return;
                    }
                    if(queEquipo == 0 && eq1 > eq2){
                        resultado = "¡Has ganado!\n";
                        puntosmios=eq1;
                        puntosrival=eq2;
                        gano=1;
                    }else if(queEquipo == 0 && eq1 < eq2){
                        puntosmios=eq1;
                        puntosrival=eq2;
                        resultado = "¡Has perdido!\n";
                    }else if(queEquipo == 1 && eq1 > eq2){
                        puntosmios=eq2;
                        puntosrival=eq1;
                        resultado = "¡Has perdido!\n";
                    }else if(queEquipo == 1 && eq1 < eq2){
                        puntosmios=eq2;
                        puntosrival=eq1;
                        resultado = "¡Has ganado!\n";
                        gano=1;
                    }
                    openGanador();
                }
            });
        }
    };

    private Emitter.Listener onVueltas = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String vueltas;
                    try {
                        vueltas = data.getString("mensaje");

                    } catch (JSONException e) {
                        return;
                    }
                    deVueltas = true;
                    cuantascartasint=16;
                }
            });
        }
    };

    private Emitter.Listener onPuntos = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Integer p0;
                    Integer p1;
                    try {
                        p0 = data.getInt("puntos_e0");
                        p1 = data.getInt("puntos_e1");

                    } catch (JSONException e) {
                        return;
                    }
                    if (queEquipo == 0) {
                        puntosmios = p0;
                        puntosrival = p1;
                        ptmio.setText(puntosmios.toString());
                        ptrival.setText(puntosrival.toString());

                    } else if (queEquipo == 1) {
                        puntosmios = p1;
                        puntosrival = p0;
                        ptmio.setText(puntosmios.toString());
                        ptrival.setText(puntosrival.toString());
                    }
                    if (deVueltas == true) {
                        if (puntosmios >= 101) {
                            JSONObject aux = new JSONObject();
                            try {
                                aux.put("partida", room);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            mSocket.emit("finalizarPartida", aux, new Ack() {
                                @Override
                                public void call(Object... args) {
                                    //JSONObject response = (JSONObject) args[0];
                                    //System.out.println(response); // "ok"
                                }
                            });
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener onPauseRequest = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    contador.cancel();
                    JSONObject data = (JSONObject) args[0];
                    try {
                        final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(ctx);
                        builder.setTitle(data.getString("pauseMessage"));
                        builder.setMessage("Solicitud de pausa de partida. La partida puede" +
                                "ser reanudada posteriormente");
                        builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { ;
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { ;
                                JSONObject aux = new JSONObject();
                                try {
                                    aux.put("partida", room);
                                    aux.put("usuario", getName());
                                    aux.put("tipo", 0);
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                mSocket.emit("pausar", aux, new Ack() {
                                    @Override
                                    public void call(Object... args) {
                                        //JSONObject response = (JSONObject) args[0];
                                        //System.out.println(response); // "ok"
                                    }
                                });
                            }
                        });
                        builder.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pauso=0;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pantalla_juego);

        ctx=this;

        FragmentManager fm = getSupportFragmentManager();
        Chat fragmentoChat = (Chat) fm.findFragmentById(R.id.fragmento_chat);
        MyOpenHelper dbHelper = new MyOpenHelper(this);
        db = dbHelper.getWritableDatabase();

        miCarta=getCartas();
        miTapete=getTapete();
        mensajeDeTextos = new ArrayList<>();
        nameUser=getName();
        Bundle b = getIntent().getExtras();
        if(b != null){
            room = b.getString("key");
            torneo= b.getInt("torneo");
        }
        //mSocket = IO.socket(URI.create("http://148.3.47.50:5000"));
        mSocket=Pantalla_app.mSocket;
        mSocket.on("message", onNewMessage);
        mSocket.on("RepartirCartas", onRepartirCartas);
        mSocket.on("RepartirCartasRP", onRepartirCartasRP);
        mSocket.on("RepartirTriunfoRP", onRepartirTriunfoRP);
        mSocket.on("RepartirTriunfo", onRepartirTriunfo);
        mSocket.on("cartaJugada", oncartaJugada);
        mSocket.on("winner", onRecuento);
        mSocket.on("roba", onRobo);
        mSocket.on("cartaCambio", onCambio);
        mSocket.on("cante", onCante);
        mSocket.on("Resultado", onResultado);
        mSocket.on("Vueltas", onVueltas);
        mSocket.on("puntos",onPuntos);
        mSocket.on("pause",onPause);
        mSocket.on("copasActualizadas",onCopasActualizadas);
        mSocket.on("pauseRequest", onPauseRequest);
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

        LinearLayout juegotapete = (LinearLayout) findViewById(R.id.juego_layout);
        pausar = (Button) findViewById(R.id.button_pausar);
        cartasrestantes = (TextView) findViewById(R.id.cartasrestantes2vs2);
        ptorivaltext = (TextView) findViewById(R.id.puntosrivaltext2vs2);
        ptmiotext = (TextView) findViewById(R.id.puntosmiostext2vs2);
        ptmio = (TextView) findViewById(R.id.puntosmios2vs2);
        ptrival = (TextView) findViewById(R.id.puntosrival2vs2);
        fperfiladversarioj2 = (CircleImageView) findViewById(R.id.foto_perfil_j22vs2);
        copasadversarioj2 = (TextView) findViewById(R.id.copasadversarioj2);
        fperfiladversarioj3 = (CircleImageView) findViewById(R.id.foto_perfil_j32vs2);
        copasadversarioj3 = (TextView) findViewById(R.id.copasadversarioj3);
        fperfiladversarioj4 = (CircleImageView) findViewById(R.id.foto_perfil_j42vs2);
        copasadversarioj4 = (TextView) findViewById(R.id.copasadversarioj4);
        cuantascartas = (TextView) findViewById(R.id.cuantascartas2vs2);
        cuentaatras = (TextView) findViewById(R.id.cuentatras2vs2);

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
        ptmiotext.setVisibility(View.INVISIBLE);
        ptmio.setVisibility(View.INVISIBLE);
        ptorivaltext.setVisibility(View.INVISIBLE);
        ptrival.setVisibility(View.INVISIBLE);
        cuantascartas.setVisibility(View.INVISIBLE);
        cartasrestantes.setVisibility(View.INVISIBLE);
        cuentaatras.setVisibility(View.INVISIBLE);

        ImageView imagen1reverso = (ImageView) findViewById(R.id.casilla_carta_1_back);
        ImageView imagen2reverso = (ImageView) findViewById(R.id.casilla_carta_2_back);
        ImageView imagen3reverso = (ImageView) findViewById(R.id.casilla_carta_3_back);
        ImageView imagen4reverso = (ImageView) findViewById(R.id.casilla_carta_4_back);
        ImageView imagen5reverso = (ImageView) findViewById(R.id.casilla_carta_5_back);
        ImageView imagen6reverso = (ImageView) findViewById(R.id.casilla_carta_6_back);
        ImageView imagen7reverso = (ImageView) findViewById(R.id.backcartaj2);
        ImageView imagen8reverso = (ImageView) findViewById(R.id.mazo_central_volteado_back);
        ImageView imagen9reverso = (ImageView) findViewById(R.id.frontcartaj2);
        ImageView imagen10reverso = (ImageView) findViewById(R.id.mazo_central);
        ImageView imagen11reverso = (ImageView) findViewById(R.id.frontcartaj3);
        ImageView imagen12reverso = (ImageView) findViewById(R.id.frontcartaj4);
        ImageView imagen13reverso = (ImageView) findViewById(R.id.backcartaj3);
        ImageView imagen14reverso = (ImageView) findViewById(R.id.backcartaj4);


        try {
            InputStream ims = getAssets().open(miCarta+"/reverso.png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            imagen1reverso.setImageDrawable(d);
            imagen2reverso.setImageDrawable(d);
            imagen3reverso.setImageDrawable(d);
            imagen4reverso.setImageDrawable(d);
            imagen5reverso.setImageDrawable(d);
            imagen6reverso.setImageDrawable(d);
            imagen7reverso.setImageDrawable(d);
            imagen8reverso.setImageDrawable(d);
            imagen9reverso.setImageDrawable(d);
            imagen10reverso.setImageDrawable(d);
            imagen11reverso.setImageDrawable(d);
            imagen12reverso.setImageDrawable(d);
            imagen13reverso.setImageDrawable(d);
            imagen14reverso.setImageDrawable(d);
        }catch (Exception e){

        }
        if (torneo == 3){
            JSONObject partidareanudar = new JSONObject();
            try {
                partidareanudar.put("usuario", nameUser);
                partidareanudar.put("partida", room);
                partidareanudar.put("tipo", 1);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mSocket.emit("reanudarPartida", partidareanudar, new Ack() {
                @Override
                public void call(Object... args) {
                    //JSONObject response = (JSONObject) args[0];
                    //System.out.println(response); // "ok"
                }
            });
        }else{
            mSocket.emit("join", auxiliar, new Ack() {
                @Override
                public void call(Object... args) {
                    //JSONObject response = (JSONObject) args[0];
                    //System.out.println(response); // "ok"
                }
            });
        }
        assignTapete(juegotapete);
        deVueltas = false;

        contador = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long sDuration = (TimeUnit.MILLISECONDS.toSeconds(duration)-numeroTimer);
                cuentaatras.setText(String.valueOf(sDuration));
                cuentaatras.setVisibility(View.VISIBLE);
                numeroTimer++;
            }

            @Override
            public void onFinish() {
                cuentaatras.setVisibility(View.GONE);
                numeroTimer = 0;
                if(arrastre_y_puede(0)){
                    puedeLanzar(0);
                }else if(arrastre_y_puede(1)){
                    puedeLanzar(1);
                }else if(arrastre_y_puede(2)){
                    puedeLanzar(2);
                }else if(arrastre_y_puede(3)){
                    puedeLanzar(3);
                }else if(arrastre_y_puede(4)){
                    puedeLanzar(4);
                }else if(arrastre_y_puede(5)){
                    puedeLanzar(5);
                }
            }
        };

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
                chat.setImageResource(R.drawable.baseline_chat_black_48);
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

        pausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject aux = new JSONObject();
                try {
                    aux.put("partida", room);
                    aux.put("usuario", getName());
                    aux.put("tipo", 1);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                contador.cancel();
                mSocket.emit("pausar", aux, new Ack() {
                    @Override
                    public void call(Object... args) {
                        //JSONObject response = (JSONObject) args[0];
                        //System.out.println(response); // "ok"
                    }
                });
            }
        });

    }

    public void openGanador(){
        final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(this);
        builder.setTitle("Partida finalizada");
        builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { ;
                if(torneo!=1 || gano==0) {
                    Intent intent = new Intent(getApplicationContext(), Pantalla_app.class);
                    startActivity(intent);
                }
                dialog.dismiss();
                finish();
            }
        });
        resultado = resultado + "Puntos: " + puntosmios + "\n" + "Puntos rival: " + puntosrival;
        builder.setMessage(resultado);
        builder.show();

        /*mSocket.emit("disconnect", new Ack() {
            @Override
            public void call(Object... args) {
                //JSONObject response = (JSONObject) args[0];
                //System.out.println(response); // "ok"
            }
        });*/


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
                if((cardsj1[i].getPalo() == paloArrastre) && (cardsj1[i].getRanking() > RankingArrastre)){
                    for(int j = 0; j<6;j++){
                        if(j!=i){
                            if((cardsj1[j].getRanking() < RankingArrastre) && (cardsj1[j].getPalo() == paloArrastre)){
                                String texto = "Debes superar la carta que han echado";
                                Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
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
                                String texto = "Debes echar del palo que se pide";
                                Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }
                    }
                    paloArrastre = cardsj1[i].getPalo();
                    RankingArrastre = cardsj1[i].getRanking();
                }else if((cardsj1[i].getPalo() != paloArrastre) && (paloArrastre != cartaTriunfo.getPalo())){
                    for(int j = 0; j<6;j++){
                        if(j!=i){
                            if(cardsj1[j].getPalo() == paloArrastre){
                                String texto = "Tienes del palo al que vamos";
                                Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                            if(cardsj1[j].getPalo() == cartaTriunfo.getPalo()){
                                String texto = "Tienes triunfo por lo que tienes que echarlo";
                                Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }
                    }
                }else if((cardsj1[i].getPalo() != paloArrastre) && (paloArrastre == cartaTriunfo.getPalo())){
                    for(int j = 0; j<6;j++){
                        if(j!=i){
                            if(cardsj1[j].getPalo() == cartaTriunfo.getPalo()){
                                String texto = "Tienes triunfo, debes echarlo";
                                Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
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
        if(aun_no == false) {
            if (queOrden == 1) {
                QueCarta = i;
                assignImages(cardsj1[i], j1image);
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
                mSocket.emit("lanzarCarta", aux, new Ack() {
                    @Override
                    public void call(Object... args) {
                        //JSONObject response = (JSONObject) args[0];
                        //System.out.println(response); // "ok"
                    }
                });
                queOrden--;
                if (i == 0) {
                    c1whole.setVisibility(View.INVISIBLE);
                    c1whole.flipTheView();
                } else if (i == 1) {
                    c2whole.setVisibility(View.INVISIBLE);
                    c2whole.flipTheView();
                } else if (i == 2) {
                    c3whole.setVisibility(View.INVISIBLE);
                    c3whole.flipTheView();
                } else if (i == 3) {
                    c4whole.setVisibility(View.INVISIBLE);
                    c4whole.flipTheView();
                } else if (i == 4) {
                    c5whole.setVisibility(View.INVISIBLE);
                    c5whole.flipTheView();
                } else if (i == 5) {
                    c6whole.setVisibility(View.INVISIBLE);
                    c6whole.flipTheView();
                }
                estrella1.setVisibility(View.INVISIBLE);
                contador.cancel();
                cuentaatras.setVisibility(View.GONE);
                numeroTimer = 0;
                return true;
            }
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
                if(torneo ==3) {
                    if((nronda < 3) || (nronda > 9))
                        setVisibilitytriumphe();
                }else{
                    setVisibilitytriumphe();
                }
                try{
                    Thread.sleep(500);
                } catch (Exception e){
                    e.printStackTrace();
                }
                if(torneo ==3) {
                    if((nronda < 3) || (nronda > 9))
                        updatecenterCard();
                }else{
                    updatecenterCard();
                }
                try{
                    Thread.sleep(1000);
                } catch (Exception e){
                    e.printStackTrace();
                }
                if(torneo ==3) {
                    if((nronda < 3) || (nronda > 9))
                        setVisibilityreverse();
                }else{
                    setVisibilityreverse();
                }
                setlotsVisibilities();
                try{
                    Thread.sleep(100);
                } catch (Exception e){
                    e.printStackTrace();
                }
                aun_no = false;

            }
        }.start();
    }

    //Set visibilities
    private void disolverCartas(){
        setNotVisibilityLanzadasCard();
        aun_no = false;
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
                try{
                    Thread.sleep(200);
                } catch (Exception e){
                    e.printStackTrace();
                }
                aun_no = false;
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
                try{
                    Thread.sleep(200);
                } catch (Exception e){
                    e.printStackTrace();
                }
                aun_no = false;
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
                try{
                    Thread.sleep(200);
                } catch (Exception e){
                    e.printStackTrace();
                }
                aun_no = false;
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
                try{
                    Thread.sleep(200);
                } catch (Exception e){
                    e.printStackTrace();
                }
                aun_no = false;
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
    private void setlotsVisibilities() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ptmiotext.setVisibility(View.VISIBLE);
                ptmio.setVisibility(View.VISIBLE);
                ptorivaltext.setVisibility(View.VISIBLE);
                ptrival.setVisibility(View.VISIBLE);
                if(torneo ==3 ){
                    if((nronda < 3) || (nronda > 9)){
                        cuantascartas.setVisibility(View.VISIBLE);
                        cartasrestantes.setVisibility(View.VISIBLE);
                    }
                }else{
                    cuantascartas.setVisibility(View.VISIBLE);
                    cartasrestantes.setVisibility(View.VISIBLE);
                }
                cuentaatras.setVisibility(View.VISIBLE);
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
    public void animacion7(final Integer i){
        new Thread(){
            @Override
            public void run() {
                flipViews(queImagenFlip(i),triumphewhole);
                try{
                    Thread.sleep(1000);
                } catch (Exception e){
                    e.printStackTrace();
                }
                intercambiosiete(i);
                try{
                    Thread.sleep(1000);
                } catch (Exception e){
                    e.printStackTrace();
                }
                flipViews(queImagenFlip(i),triumphewhole);
                try{
                    Thread.sleep(200);
                } catch (Exception e){
                    e.printStackTrace();
                }
                aun_no = false;
            }
        }.start();
    }

    public int find7(){
        int cual = 0;
        for(int i = 0; i<6;i++ ){
            if(cardsj1[i].getPalo() == cartaTriunfo.getPalo() && cardsj1[i].getRanking() == 6){
                cual = i;
            }
        }
        return cual;
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

    private void intercambiosiete(final Integer a) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Carta aux = cartaTriunfo;
                cartaTriunfo = cardsj1[a];
                cardsj1[a] = aux;
                ImageView aux1 = queImagen(a);
                assignImages(cardsj1[a],aux1);
                assignImages(cartaTriunfo,triumphe);
            }
        });
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
    public void assignFPerfil(String queFoto, CircleImageView image){

        try {
            // get input stream
            InputStream ims = getAssets().open(queFoto+".png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            image.setImageDrawable(d);
        }
        catch(Exception ex) {
            return;
        }

    }

    public void assignTapete(LinearLayout juegotapete){

        try {
            // get input stream
            InputStream ims = getAssets().open(miTapete+".jpg");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            juegotapete.setBackground(d);
        }
        catch(Exception ex) {
            return;
        }

    }

    public void assignImages(Carta card, ImageView image){

        String cual = card.getId();
        try {
            // get input stream
            InputStream ims = getAssets().open(miCarta+"/"+cual+".png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            image.setImageDrawable(d);
        }
        catch(Exception ex) {
            return;
        }

        /*switch (cual){
            case "0O":
                int resID = getResources().getIdentifier(cual , "drawable", getPackageName());
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

        }*/

    }

    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }

    public String getCartas() {
        String query="SELECT f_carta FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }

    public String getTapete() {
        String query="SELECT f_tapete FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }
    public void updateCopas(String copas){

        String url = "https://las10ultimas-backend.herokuapp.com/api/usuario/updateUser/"+getName();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();

        try {
            postData.put("copas", copas);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
}