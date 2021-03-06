package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Activity;
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

import com.airbnb.lottie.L;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PantallaJuego1vs1 extends AppCompatActivity {


    private RecyclerView rv;
    public int TEXT_LINES=1;
    private Toolbar toolbar;

    private int mRemainingTime = 30;
    private String room="";
    private int torneo=0;
    private Socket mSocket;
    Boolean esta_sonando = true;

    private String miCarta="";
    private String miTapete="";
    private int puntosProv=0;
    private int puntosProv1=0;
    private Context ctx;

    private SQLiteDatabase db;
    private int gano=0;
    private String nameUser;
    ImageView c1,c2,c3,c4,c5,c6,reverse,triumphe,j1image,chat,j2imagefront,j2imageback,estrella1,estrella2;
    EasyFlipView c1whole,c2whole,c3whole,c4whole,c5whole,c6whole,triumphewhole,j2image;
    TextView nombreOponente,cuentaatras, cuantascartas, copasadversario, ptmio, ptrival, cartasrestantes, ptmiotext, ptorivaltext;
    Button cantar,pausar;
    Integer cuantascartasint = 28;
    CircleImageView fperfiladversario;
    private int pauso;
    String resultado;
    String quienWinner = "";
    Integer queEquipo;                 // En que equipo estoy, 1 o 0.
    Carta[] cardsj1 = new Carta[6]; //Las 6 cartas de nuestra mano
    Carta cartaTriunfo;             //La carta que esta en medio
    Integer nronda = 0;             //Rondas en las que nos encontramos
    Integer QueCarta;               //Que i de carta estoy lanzando a los adversarios (para robar)
    long numeroTimer = 0;

    Integer IDcomienzo; //Que carta estoy comenzando a arrastrar (solo para intercambio de cartas)
    Integer queOrden;
    Boolean ultimo = false;

    boolean aun_no = false;

    //Variables para el arrastre
    boolean arrastre;   //Si estamos en arrastre o no
    Integer paloArrastre;
    Integer RondaArrastre;
    Integer RankingArrastre;
    boolean deVueltas;

    long duration = TimeUnit.SECONDS.toMillis(30);
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
            Torneo.enPartida=0;
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
        Integer m=pauso;
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
        mSocket.off("cartaJugadaIA", oncartaJugadaIA);
        mSocket.off("winner", onRecuento);
        mSocket.off("pauseRequest", onPauseRequest);
        mSocket.off("roba", onRobo);
        mSocket.off("cartaCambio", onCambio);
        mSocket.off("cante", onCante);
        mSocket.off("Resultado", onResultado);
        mSocket.off("Vueltas", onVueltas);
        mSocket.off("puntos", onPuntos);
        mSocket.off("pause", onPause);
        mSocket.off("copasActualizadas",onCopasActualizadas);
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


                        Chat1vs1 fragment = (Chat1vs1) fm.findFragmentById(R.id.fragmento_chat1vs1);

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
                    String carta1;
                    String carta2;
                    String carta3;
                    String carta4;
                    String carta5;
                    String carta6;
                    Integer orden;
                    Integer equipo;
                    Integer copas;
                    String f_perfil;
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

                        Carta aux = new Carta(carta1);
                        cardsj1[0] = aux;
                        aux = new Carta(carta2);
                        cardsj1[1] = aux;
                        aux = new Carta(carta3);
                        cardsj1[2] = aux;
                        aux = new Carta(carta4);
                        cardsj1[3] = aux;
                        aux = new Carta(carta5);
                        cardsj1[4] = aux;
                        aux = new Carta(carta6);
                        cardsj1[5] = aux;
                        assignImages(cardsj1[0], c1);
                        assignImages(cardsj1[1], c2);
                        assignImages(cardsj1[2], c3);
                        assignImages(cardsj1[3], c4);
                        assignImages(cardsj1[4], c5);
                        assignImages(cardsj1[5], c6);
                    }else {
                        nombreOponente.setText(username);
                        assignFPerfil(f_perfil,fperfiladversario);
                        copasadversario.setText(copas.toString());

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
                    String carta1;
                    String carta2;
                    String carta3;
                    String carta4;
                    String carta5;
                    String carta6;
                    Integer orden;
                    Integer equipo;
                    Integer copas;
                    String f_perfil;
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
                        copas = datos.getInt("copas");
                        f_perfil = datos.getString("f_perfil");

                    } catch (JSONException e) {
                        return;
                    }
                    if (username.equals(nameUser)) {
                        //nronda>12
                        if(nronda>13) {
                            //nronda>18
                            if (nronda>19){
                                deVueltas=true;
                                arrastre = false;
                            }else{
                                arrastre = true;
                                triumphewhole.setVisibility(View.INVISIBLE);
                                reverse.setVisibility(View.INVISIBLE);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        updatecenterCard();
                                    }
                                }.start();
                                cuantascartas.setVisibility(View.INVISIBLE);
                                cartasrestantes.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            arrastre  = false;
                            deVueltas = false;
                        }
                        paloArrastre = 0;
                        RondaArrastre = 0;
                        RankingArrastre = 11;
                        queEquipo = equipo;

                        if(equipo==0){
                            puntosmios=puntosProv;
                            puntosrival=puntosProv1;
                        }else{
                            puntosmios=puntosProv1;
                            puntosrival=puntosProv;
                        }
                        if(queOrden == 2){
                            ultimo = true;
                            estrella2.setVisibility(View.VISIBLE);
                        }
                        if(queOrden == 1){
                            contador.start();
                            estrella1.setVisibility(View.VISIBLE);
                        }
                        Carta aux = new Carta(carta1);
                        cardsj1[0] = aux;
                        aux = new Carta(carta2);
                        cardsj1[1] = aux;
                        aux = new Carta(carta3);
                        cardsj1[2] = aux;
                        aux = new Carta(carta4);
                        cardsj1[3] = aux;
                        aux = new Carta(carta5);
                        cardsj1[4] = aux;
                        aux = new Carta(carta6);
                        cardsj1[5] = aux;
                        assignImages(cardsj1[0], c1);
                        assignImages(cardsj1[1], c2);
                        assignImages(cardsj1[2], c3);
                        assignImages(cardsj1[3], c4);
                        assignImages(cardsj1[4], c5);
                        assignImages(cardsj1[5], c6);
                    }else {
                        nombreOponente.setText(username);
                        assignFPerfil(f_perfil,fperfiladversario);
                        copasadversario.setText(copas.toString());

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
                    cuantascartasint=28;
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
                    if (!ganador.equals(nameUser)){
                        //if(nronda != 19) {
                            queOrden = 2;
                            ultimo = true;
                        //}
                    }else{
                        //if(nronda != 19) {
                            queOrden = 1;
                        //}
                    }
                    nronda++;
                    cuantascartasint=28-(nronda*2);
                    cartaTriunfo = new Carta(triunfo);
                    assignImages(cartaTriunfo, triumphe);
                    aun_no = true;
                    iniciarPartida();
                }
            });
        }
    };

    private Emitter.Listener oncartaJugadaIA = new Emitter.Listener(){
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
                        carta = data.getString("carta");
                        quien = data.getString("jugador");

                    } catch (JSONException e) {
                        return;
                    }
                        animacionCartaFront();
                        Carta aux = new Carta(carta);
                        assignImages(aux, j2imagefront);
                        aun_no = true;
                        if (arrastre == true) {
                            actualizar_datos_arrastre(aux.getPalo(), aux.getRanking());
                        }
                        queOrden--;
                        if(queOrden == 1){
                            contador.start();
                            estrella1.setVisibility(View.VISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                        }else{
                            ultimo = false;
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            final JSONObject aux2 = new JSONObject();
                            try {
                                aux2.put("partida", room);
                                aux2.put("nronda", nronda);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    mSocket.emit("contarPuntos", aux2, new Ack() {
                                        @Override
                                        public void call(Object... args) {
                                            //JSONObject response = (JSONObject) args[0];
                                            //System.out.println(response); // "ok"
                                        }
                                    });

                                    if(!arrastre) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mSocket.emit("robarCarta", aux2, new Ack() {
                                                    @Override
                                                    public void call(Object... args) {
                                                        //JSONObject response = (JSONObject) args[0];
                                                        //System.out.println(response); // "ok"
                                                    }
                                                });
                                            }
                                        },500);
                                    }else{
                                        robar_sigana_ia();
                                    }
                                }
                            }, 1000);
                        }
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
                        if(queOrden == 1){
                            contador.start();
                           estrella1.setVisibility(View.VISIBLE);
                           estrella2.setVisibility(View.INVISIBLE);
                        }
                        aun_no = true;
                        animacionCartaFront();
                        Carta aux = new Carta(carta);
                        assignImages(aux,j2imagefront);
                        if(arrastre == true){
                            actualizar_datos_arrastre(aux.getPalo(),aux.getRanking());
                        }
                    }else {
                        if (torneo == 2 && !ultimo) {
                            JSONObject aux = new JSONObject();
                            try {
                                aux.put("partida", room);
                                aux.put("nronda", nronda);
                                aux.put("carta", cardsj1[QueCarta].getId());

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            mSocket.emit("lanzarCartaIA", aux, new Ack() {
                                @Override
                                public void call(Object... args) {
                                    //JSONObject response = (JSONObject) args[0];
                                    //System.out.println(response); // "ok"
                                }
                            });
                            Carta aux3 = new Carta("NO");
                            cardsj1[QueCarta] = aux3;
                        }else{
                            Carta aux2 = new Carta("NO");
                            cardsj1[QueCarta] = aux2;
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.VISIBLE);
                            if (ultimo) {
                                ultimo = false;
                                estrella1.setVisibility(View.INVISIBLE);
                                estrella2.setVisibility(View.INVISIBLE);
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
                                        }else{
                                            robar_sigana_ia();
                                        }
                                    }
                                }, 3000);
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
                            if(nronda != 19) {
                                estrella1.setVisibility(View.INVISIBLE);
                                estrella2.setVisibility(View.VISIBLE);
                                queOrden = 2;
                                ultimo = true;
                            }

                    }else{
                        if(nronda != 19) {
                            estrella1.setVisibility(View.VISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            queOrden = 1;
                            contador.start();
                        }
                    }
                    quienWinner = ganador;
                    aun_no = true;
                    disolverCartas();
                    nronda++;
                    cuantascartasint = cuantascartasint -2;
                    cuantascartas.setText(cuantascartasint.toString());
                    if(nronda == 14){
                        arrastre = true;
                        triumphewhole.setVisibility(View.INVISIBLE);
                        reverse.setVisibility(View.INVISIBLE);
                        new Thread() {
                            @Override
                            public void run() {
                                updatecenterCard();
                            }
                        }.start();
                        cuantascartas.setVisibility(View.INVISIBLE);
                        cartasrestantes.setVisibility(View.INVISIBLE);

                    }
                    if(arrastre == true){
                        RondaArrastre = 0;
                        paloArrastre = 0;
                        RankingArrastre = 11;
                    }

                    if(nronda == 20){
                        quienWinner = ganador;
                        arrastre = false;
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
                    if(pauso==1){
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
                            assignImages(nueva, queImagen(QueCarta));
                            aun_no = true;
                            animacionRobarCarta(QueCarta);
                            robar_sigana_ia();
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
                        resultado = "¡Has ganado!\n+30 copas\n";
                        puntosmios=eq1;
                        puntosrival=eq2;
                        gano=1;
                    }else if(queEquipo == 0 && eq1 < eq2){
                        puntosmios=eq1;
                        puntosrival=eq2;
                        resultado = "¡Has perdido!\n-15 copas\n";
                    }else if(queEquipo == 1 && eq1 > eq2){
                        puntosmios=eq2;
                        puntosrival=eq1;
                        resultado = "¡Has perdido!\n-15 copas\n";
                    }else if(queEquipo == 1 && eq1 < eq2){
                        puntosmios=eq2;
                        puntosrival=eq1;
                        resultado = "¡Has ganado!\n+30 copas\n";
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
                    //cuantascartasint=28;

                    try {
                        if(queEquipo==0){
                            puntosmios= data.getInt("puntos_e0");
                            puntosrival= data.getInt("puntos_e1");
                        }else{
                            puntosrival= data.getInt("puntos_e0");
                            puntosmios= data.getInt("puntos_e1");
                        }
                        ptmio.setText(puntosmios.toString());
                        ptrival.setText(puntosrival.toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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

                    } else if (queEquipo == 1) {
                        puntosmios = p1;
                        puntosrival = p0;
                    }
                    if(puntosmios >= 50){
                        Integer puntosaux = puntosmios-50;
                        String aux = puntosaux.toString()+ " buenas";
                        ptmio.setText(aux);
                    }else{
                        String aux = puntosmios.toString()+ " malas";
                        ptmio.setText(aux);
                    }
                    if(puntosrival >= 50){
                        Integer puntosaux = puntosrival-50;
                        String aux = puntosaux.toString()+ " buenas";
                        ptrival.setText(aux);
                    }else{
                        String aux = puntosrival.toString()+ " malas";
                        ptrival.setText(aux);
                    }
                    if (deVueltas == true) {
                        if ((puntosmios >= 101) || (torneo == 2 && puntosrival >=101)) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pauso=0;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pantalla_juego1vs1);

        FragmentManager fm = getSupportFragmentManager();
        Chat1vs1 fragmentoChat = (Chat1vs1) fm.findFragmentById(R.id.fragmento_chat1vs1);
        MyOpenHelper dbHelper = new MyOpenHelper(this);
        db = dbHelper.getWritableDatabase();

        miCarta = getCartas();
        miTapete = getTapete();
        mensajeDeTextos = new ArrayList<>();
        nameUser = getName();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            room = b.getString("key");
            torneo = b.getInt("torneo");
        }

        Pantalla_app.enPartidaIndividual = room;
        mSocket = Pantalla_app.mSocket;
        //mSocket = IO.socket(URI.create("https://las10ultimas-backend-realtime.herokuapp.com"));
        mSocket.on("message", onNewMessage);
        mSocket.on("RepartirCartas", onRepartirCartas);
        mSocket.on("RepartirCartasRP", onRepartirCartasRP);
        mSocket.on("RepartirTriunfo", onRepartirTriunfo);
        mSocket.on("RepartirTriunfoRP", onRepartirTriunfoRP);
        mSocket.on("cartaJugada", oncartaJugada);
        mSocket.on("cartaJugadaIA", oncartaJugadaIA);
        mSocket.on("winner", onRecuento);
        mSocket.on("roba", onRobo);
        mSocket.on("cartaCambio", onCambio);
        mSocket.on("cante", onCante);
        mSocket.on("Resultado", onResultado);
        mSocket.on("Vueltas", onVueltas);
        mSocket.on("puntos", onPuntos);
        mSocket.on("pause", onPause);
        mSocket.on("pauseRequest", onPauseRequest);
        mSocket.on("copasActualizadas",onCopasActualizadas);

        ctx=this;
        //mSocket.connect();
        JSONObject auxiliar = new JSONObject();
        try {
            auxiliar.put("name", getName());
            auxiliar.put("room", room);
            auxiliar.put("tipo", 0);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LinearLayout juegotapete = (LinearLayout) findViewById(R.id.juego_layout1vs1);
        pausar = (Button) findViewById(R.id.button_pausar1vs1);
        cartasrestantes = (TextView) findViewById(R.id.cartasrestantes);
        ptorivaltext = (TextView) findViewById(R.id.puntosrivaltext);
        ptmiotext = (TextView) findViewById(R.id.puntosmiostext);
        ptmio = (TextView) findViewById(R.id.puntosmios);
        ptrival = (TextView) findViewById(R.id.puntosrival);
        fperfiladversario = (CircleImageView) findViewById(R.id.foto_perfil_j2);
        copasadversario = (TextView) findViewById(R.id.copasadversario);
        cuantascartas = (TextView) findViewById(R.id.cuantascartas);
        cuentaatras = (TextView) findViewById(R.id.cuentatras);
        estrella1 = (ImageView) findViewById(R.id.estrella_turnoj1);
        estrella2 = (ImageView) findViewById(R.id.estrella_turnoj2);
        nombreOponente = (TextView) findViewById(R.id.nombre_j21vs1);
        chat = (ImageView) findViewById(R.id.icono_chat1vs1);
        j1image = (ImageView) findViewById(R.id.carta_jugador11vs1);
        j2image = (EasyFlipView) findViewById(R.id.carta_jugador21vs1);
        j2imagefront = (ImageView) findViewById(R.id.frontcartaj21vs1);
        j2imageback = (ImageView) findViewById(R.id.backcartaj21vs1);
        c1 = (ImageView) findViewById(R.id.casilla_carta_11vs1);
        c1whole = (EasyFlipView) findViewById(R.id.easyFlipView11vs1);
        c2 = (ImageView) findViewById(R.id.casilla_carta_21vs1);
        c2whole = (EasyFlipView) findViewById(R.id.easyFlipView21vs1);
        c3 = (ImageView) findViewById(R.id.casilla_carta_31vs1);
        c3whole = (EasyFlipView) findViewById(R.id.easyFlipView31vs1);
        c4 = (ImageView) findViewById(R.id.casilla_carta_41vs1);
        c4whole = (EasyFlipView) findViewById(R.id.easyFlipView41vs1);
        c5 = (ImageView) findViewById(R.id.casilla_carta_51vs1);
        c5whole = (EasyFlipView) findViewById(R.id.easyFlipView51vs1);
        c6 = (ImageView) findViewById(R.id.casilla_carta_61vs1);
        c6whole = (EasyFlipView) findViewById(R.id.easyFlipView61vs1);
        reverse = (ImageView) findViewById(R.id.mazo_central1vs1);
        triumphe = (ImageView) findViewById(R.id.mazo_central_volteado1vs1);
        triumphewhole = (EasyFlipView) findViewById(R.id.easyFlipViewtriumphe1vs1);
        cantar = (Button) findViewById(R.id.button_cantar1vs1);

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
        estrella1.setVisibility(View.INVISIBLE);
        estrella2.setVisibility(View.INVISIBLE);
        ptmiotext.setVisibility(View.INVISIBLE);
        ptmio.setVisibility(View.INVISIBLE);
        ptorivaltext.setVisibility(View.INVISIBLE);
        ptrival.setVisibility(View.INVISIBLE);
        cuantascartas.setVisibility(View.INVISIBLE);
        cartasrestantes.setVisibility(View.INVISIBLE);
        cuentaatras.setVisibility(View.INVISIBLE);
        cantar.setVisibility(View.INVISIBLE);

        ImageView imagen1reverso = (ImageView) findViewById(R.id.casilla_carta_1_back1vs1);
        ImageView imagen2reverso = (ImageView) findViewById(R.id.casilla_carta_2_back1vs1);
        ImageView imagen3reverso = (ImageView) findViewById(R.id.casilla_carta_3_back1vs1);
        ImageView imagen4reverso = (ImageView) findViewById(R.id.casilla_carta_4_back1vs1);
        ImageView imagen5reverso = (ImageView) findViewById(R.id.casilla_carta_5_back1vs1);
        ImageView imagen6reverso = (ImageView) findViewById(R.id.casilla_carta_6_back1vs1);
        ImageView imagen7reverso = (ImageView) findViewById(R.id.backcartaj21vs1);
        ImageView imagen8reverso = (ImageView) findViewById(R.id.mazo_central_volteado_back1vs1);
        ImageView imagen9reverso = (ImageView) findViewById(R.id.frontcartaj21vs1);
        ImageView imagen10reverso = (ImageView) findViewById(R.id.mazo_central1vs1);

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
        }catch (Exception e){

        }
        if (torneo == 2) {
            pausar.setVisibility(View.INVISIBLE);
            mSocket.emit("joinPartidaIA", auxiliar, new Ack() {
                @Override
                public void call(Object... args) {
                    //JSONObject response = (JSONObject) args[0];
                    //System.out.println(response); // "ok"
                }
            });
            nombreOponente.setText("IA");

            InputStream ims = null;
            try {
                ims = getAssets().open("userlogoIA.png");
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                fperfiladversario.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (torneo == 3){
            JSONObject partidareanudar = new JSONObject();
            try {
                partidareanudar.put("usuario", nameUser);
                partidareanudar.put("partida", room);
                partidareanudar.put("tipo", 0);
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
            if(torneo==1){
                pausar.setVisibility(View.INVISIBLE);
            }
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
                cuentaatras.setVisibility(View.INVISIBLE);
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
                        .replace(R.id.fragmento_chat1vs1, Chat1vs1.class, null)
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
                //c4.setVisibility(View.INVISIBLE);
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
                if(quienWinner.equals(nameUser)){
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
                }else{
                    String texto = "Para cambiar el 7 debes hacer baza";
                    Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
                }
            }
        });
        cantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quienWinner.equals(nameUser)){
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
                }else{
                    String texto = "Para cantar debes hacer baza";
                    Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
                }
            }
        });

        pausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        String aux="", aux2="";
        if(puntosmios >= 50){
            Integer puntosaux = puntosmios-50;
            aux = puntosaux.toString()+ " buenas";
        }else{
            aux = puntosmios.toString()+ " malas";
        }
        if(puntosrival >= 50){
            Integer puntosaux = puntosrival-50;
            aux2 = puntosaux.toString()+ " buenas";
        }else{
            aux2 = puntosrival.toString()+ " malas";
        }
        resultado = resultado + "Puntos: " + aux + "\n" + "Puntos rival: " + aux2;
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

    private void actualizar_datos_arrastre(Integer palo, Integer ranking){
        if(RondaArrastre == 0){
            paloArrastre = palo;
            RondaArrastre = 1;
            RankingArrastre = ranking;
        }else{
            if(palo.equals(paloArrastre)){
                if( ranking < RankingArrastre){
                    RankingArrastre = ranking;
                }
            }else if((palo.equals(cartaTriunfo.getPalo())) && !(paloArrastre.equals(cartaTriunfo.getPalo()))){
                paloArrastre = palo;
                RankingArrastre = ranking;
            }
        }
    }

    private boolean arrastre_y_puede(Integer i){
        if(arrastre == true) {
            if (cardsj1[i].getPalo() == 5) {
                return false;
            }
                if (RondaArrastre == 0) {
                    paloArrastre = cardsj1[i].getPalo();
                    RondaArrastre = 1;
                    RankingArrastre = cardsj1[i].getRanking();
                    return true;
                } else {
                    if (paloArrastre.equals(cardsj1[i].getPalo()) && (cardsj1[i].getRanking() > RankingArrastre)) {
                        for (int j = 0; j < 6; j++) {
                            if (j != i) {
                                if ((cardsj1[j].getRanking() < RankingArrastre) && (cardsj1[j].getPalo() == paloArrastre)){
                                    String texto = "Debes superar la carta que han echado";
                                    Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            }
                        }
                        if (cardsj1[i].getRanking() < RankingArrastre) {
                            RankingArrastre = cardsj1[i].getRanking();
                        }
                    } else if ((cardsj1[i].getPalo() == cartaTriunfo.getPalo()) && !(paloArrastre.equals(cartaTriunfo.getPalo()))) {
                        for (int j = 0; j < 6; j++) {
                            if (j != i) {
                                if (cardsj1[j].getPalo() == paloArrastre) {
                                    String texto = "Debes echar del palo que se pide";
                                    Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            }
                        }
                        paloArrastre = cardsj1[i].getPalo();
                        RankingArrastre = cardsj1[i].getRanking();
                    } else if ((cardsj1[i].getPalo() != paloArrastre) && (paloArrastre != cartaTriunfo.getPalo())) {
                        for (int j = 0; j < 6; j++) {
                            if (j != i) {
                                if (cardsj1[j].getPalo() == paloArrastre) {
                                    String texto = "Tienes del palo al que vamos";
                                    Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                if (cardsj1[j].getPalo() == cartaTriunfo.getPalo()) {
                                    String texto = "Tienes triunfo por lo que tienes que echarlo";
                                    Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            }
                        }
                    } else if ((cardsj1[i].getPalo() != paloArrastre) && (paloArrastre == cartaTriunfo.getPalo())) {
                        for (int j = 0; j < 6; j++) {
                            if (j != i) {
                                if (cardsj1[j].getPalo() == cartaTriunfo.getPalo()) {
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
                cuentaatras.setVisibility(View.INVISIBLE);
                numeroTimer = 0;
                return true;
            }
        }
        return false;
    }


    private void vieneDevueltas(){
        if(deVueltas){
            if(quienWinner.equals(nameUser)){
                ultimo = true;
                queOrden=2;
                estrella2.setVisibility(View.VISIBLE);
                if(torneo ==2){
                    final JSONObject aux = new JSONObject();
                    try {
                        aux.put("partida", room);
                        aux.put("nronda", nronda);
                        aux.put("carta", "NO");

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSocket.emit("lanzarCartaIA", aux, new Ack() {
                                @Override
                                public void call(Object... args) {
                                    //JSONObject response = (JSONObject) args[0];
                                    //System.out.println(response); // "ok"
                                }
                            });
                        }
                    },2000);
                }
            }else{
                contador.start();
                estrella1.setVisibility(View.VISIBLE);
            }
        }else{
            if(queOrden == 2){
                ultimo = true;
                estrella2.setVisibility(View.VISIBLE);
            }
            if(queOrden == 1){
                contador.start();
                estrella1.setVisibility(View.VISIBLE);
            }
        }
    }

    //Animación de iniciar la partida;
    private void iniciarPartida(){

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                public void run() {
                    setVisibility3firstcards();

                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updatefirst3cards();
                                Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                                setVisibility3secondcards();
                                                Handler handler4 = new Handler();
                                                handler4.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        updatelast3cards();
                                                        Handler handler5 = new Handler();
                                                        handler5.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (torneo == 3) {
                                                                    if ((nronda < 13) || (nronda > 19)) {
                                                                        setVisibilitytriumphe();
                                                                    }
                                                                } else {
                                                                    setVisibilitytriumphe();
                                                                }
                                                                Handler handler6 = new Handler();
                                                                handler6.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        if (torneo == 3) {
                                                                            if (((nronda < 13) || (nronda > 19))) {
                                                                                updatecenterCard();
                                                                            }
                                                                        } else {
                                                                            updatecenterCard();
                                                                        }
                                                                        Handler handler7 = new Handler();
                                                                        handler7.postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                if (torneo == 3) {
                                                                                    if (((nronda < 13) || (nronda > 19))) {
                                                                                        setVisibilityreverse();
                                                                                    }
                                                                                } else {
                                                                                    setVisibilityreverse();
                                                                                }
                                                                                setlotsVisibilities();
                                                                                aun_no = false;
                                                                                vieneDevueltas();
                                                                            }},1000);
                                                                    }},1000);
                                                            }},1000);
                                                    }},1000);
                                            }},1000);

                            }},1000);

                }},1000);



    }

    private void disolverCartas(){
        setNotVisibilityLanzadasCard();
        aun_no = false;
    }

    private void robar_sigana_ia(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (torneo == 2 && ultimo) {
                    JSONObject aux = new JSONObject();
                    try {
                        aux.put("partida", room);
                        aux.put("nronda", nronda);
                        aux.put("carta", "NO");

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mSocket.emit("lanzarCartaIA", aux, new Ack() {
                        @Override
                        public void call(Object... args) {
                            //JSONObject response = (JSONObject) args[0];
                            //System.out.println(response); // "ok"
                        }
                    });
                }
                aun_no = false;
            }
        }, 3000);

    }

    private void animacionCartaFront(){
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

    private void updatej2Card() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                j2image.flipTheView();
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
                    if((nronda < 13) || (nronda > 19)){
                        cuantascartas.setVisibility(View.VISIBLE);
                        cartasrestantes.setVisibility(View.VISIBLE);
                    }
                }else{
                    cuantascartas.setVisibility(View.VISIBLE);
                    cartasrestantes.setVisibility(View.VISIBLE);
                    cuantascartas.setText(cuantascartasint.toString());
                }
                cuentaatras.setVisibility(View.VISIBLE);
                cantar.setVisibility(View.VISIBLE);
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