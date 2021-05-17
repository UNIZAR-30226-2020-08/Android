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
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.app.guinote.ActivityTorneo.Torneo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private SQLiteDatabase db;
    static int gano=0;
    private String nameUser;
    LinearLayout juego1vs1;
    ImageView c1,c2,c3,c4,c5,c6,reverse,triumphe,j1image,chat,j2imagefront,j2imageback,estrella1,estrella2;
    EasyFlipView c1whole,c2whole,c3whole,c4whole,c5whole,c6whole,triumphewhole,j2image;
    TextView nombreOponente,cuentaatras, cuantascartas, copasadversario, ptmio, ptrival, cartasrestantes, ptmiotext, ptorivaltext;
    Button cantar;
    Integer cuantascartasint = 28;
    CircleImageView fperfiladversario;
    String resultado;

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
    String tapete = "1";
    String dibujo_carta;
    //Variables para el arrastre
    boolean arrastre;   //Si estamos en arrastre o no
    Integer paloArrastre;
    Integer RondaArrastre;
    Integer RankingArrastre;
    boolean deVueltas = false;

    long duration = TimeUnit.SECONDS.toMillis(20);
    CountDownTimer contador;
    Integer puntosmios, puntosrival = 0;

    public List<MensajeDeTexto> mensajeDeTextos;



    @Override
    public void onDestroy() {
        super.onDestroy();

        if (torneo==1 && gano==1){
            Torneo.terminoPartida();
        }
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
                    Log.d("jugador", username);
                    Log.d("cards", carta1);
                    Log.d("cards", carta2);
                    Log.d("cards", carta3);
                    Log.d("cards", carta4);
                    if (username.equals(nameUser)) {
                        arrastre = false;
                        paloArrastre = 0;
                        RondaArrastre = 0;
                        RankingArrastre = 11;
                        queEquipo = equipo;
                        queOrden = orden;
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
                        Log.d("reparto: ", cardsj1[0].getId());
                        Log.d("reparto: ", cardsj1[1].getId());
                        Log.d("reparto: ", cardsj1[2].getId());
                        Log.d("reparto: ", cardsj1[3].getId());
                        Log.d("reparto: ", cardsj1[4].getId());
                        Log.d("reparto: ", cardsj1[5].getId());
                    }else {
                        nombreOponente.setText(username);
                        //fperfiladversario.setImageResource(f_perfil);
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
                    Log.d("carta",carta);
                    Log.d("quien",quien);

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
                    }else{
                        Carta aux2 = new Carta("F");
                        cardsj1[QueCarta] = aux2;
                        estrella1.setVisibility(View.INVISIBLE);
                        estrella2.setVisibility(View.VISIBLE);
                        if(ultimo){
                            ultimo = false;
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
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
                    if(nronda != 19) {
                        if (!ganador.equals(nameUser)) {
                            estrella1.setVisibility(View.INVISIBLE);
                            estrella2.setVisibility(View.VISIBLE);
                            queOrden = 2;
                            ultimo = true;

                        } else {
                            estrella1.setVisibility(View.VISIBLE);
                            estrella2.setVisibility(View.INVISIBLE);
                            queOrden = 1;
                            contador.start();
                        }
                    }
                    disolverCartas();
                    nronda++;
                    cuantascartasint = cuantascartasint -2;
                    cuantascartas.setText(cuantascartasint.toString());
                    if(nronda == 14){
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
                    if(arrastre == true){
                        RondaArrastre = 0;
                        paloArrastre = 0;
                        RankingArrastre = 11;
                    }

                    if(nronda == 20){
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
                    Log.d("cambio7",jugador.toString());
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
                    Toast.makeText(getApplicationContext(),texto,Toast.LENGTH_LONG).show();
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
                        gano=1;
                    }else if(queEquipo == 0 && eq1 < eq2){
                        resultado = "¡Has perdido!\n";
                    }else if(queEquipo == 1 && eq1 > eq2){
                        resultado = "¡Has perdido!\n";
                    }else if(queEquipo == 1 && eq1 < eq2){
                        resultado = "¡Has ganado!\n";
                        gano=1;
                    }
                    openGanador();
<<<<<<< Updated upstream
                    if(torneo!=1 || gano==0) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
=======
>>>>>>> Stashed changes
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
                    Log.d(vueltas,"de vueltas");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pantalla_juego1vs1);

        FragmentManager fm = getSupportFragmentManager();
        Chat1vs1 fragmentoChat = (Chat1vs1) fm.findFragmentById(R.id.fragmento_chat1vs1);
        MyOpenHelper dbHelper = new MyOpenHelper(this);
        db = dbHelper.getWritableDatabase();
        mensajeDeTextos = new ArrayList<>();
        nameUser=getName();
        Bundle b = getIntent().getExtras();
        if(b != null) {
            room = b.getString("key");
<<<<<<< Updated upstream
            torneo= b.getInt("torneo");
        }

        Pantalla_app.enPartidaIndividual=room;
        mSocket= Pantalla_app.mSocket;
        //mSocket = IO.socket(URI.create("https://las10ultimas-backend-realtime.herokuapp.com"));
=======
            //tapete = b.getString("tapete");
            //dibujo_carta = b.getString("carta");
        mSocket = IO.socket(URI.create("https://las10ultimas-backend-realtime.herokuapp.com"));
>>>>>>> Stashed changes
        mSocket.on("message", onNewMessage);
        mSocket.on("RepartirCartas", onRepartirCartas);
        mSocket.on("RepartirTriunfo", onRepartirTriunfo);
        mSocket.on("cartaJugada", oncartaJugada);
        mSocket.on("cartaJugadaIA", oncartaJugada);
        mSocket.on("winner", onRecuento);
        mSocket.on("roba", onRobo);
        mSocket.on("cartaCambio", onCambio);
        mSocket.on("cante", onCante);
        mSocket.on("Resultado", onResultado);
        mSocket.on("Vueltas", onVueltas);
        mSocket.on("puntos",onPuntos);

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
        Log.d("jsonDePrueba",auxiliar.toString());
<<<<<<< Updated upstream

        if(torneo==2){
            mSocket.emit("joinPartidaIA", auxiliar, new Ack() {
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


=======
        mSocket.emit("join", auxiliar, new Ack() {
            @Override
            public void call(Object... args) {
                //JSONObject response = (JSONObject) args[0];
                //System.out.println(response); // "ok"
            }
        });
        juego1vs1 = (LinearLayout) findViewById(R.id.juego_layout1vs1);
>>>>>>> Stashed changes
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

        setTapete();

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

    private void setTapete() {
        if(tapete.equals("1")){
            juego1vs1.setBackgroundResource(R.drawable.tapete2);
        }else if(tapete.equals("2")){
            juego1vs1.setBackgroundResource(R.drawable.tapete2);
        }else if(tapete.equals("3")){
            juego1vs1.setBackgroundResource(R.drawable.tapete2);
        }else if(tapete.equals("4")){
            juego1vs1.setBackgroundResource(R.drawable.tapete2);
        }else if(tapete.equals("5")){
            juego1vs1.setBackgroundResource(R.drawable.tapete2);
        }
    }

    public void openGanador(){
        final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(this);
        builder.setTitle("Partida finalizada");
        builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        resultado = resultado + "Puntos: " + puntosmios + "\n" + "Puntos rival: " + puntosrival;
        builder.setMessage(resultado);
        builder.show();

        mSocket.emit("disconnect", new Ack() {
            @Override
            public void call(Object... args) {
                //JSONObject response = (JSONObject) args[0];
                //System.out.println(response); // "ok"
            }
        });

        Pantalla_app.enPartidaIndividual="";

    }

    private void actualizar_datos_arrastre(Integer palo, Integer ranking){
        if(RondaArrastre == 0){
            paloArrastre = palo;
            RondaArrastre = 1;
            RankingArrastre = ranking;
            Log.d("palo", palo.toString());
            Log.d("ranking", ranking.toString());
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
        Log.d("ronda", RondaArrastre.toString());
        Log.d("palo", paloArrastre.toString());
        Log.d("ranking", RankingArrastre.toString());
        Log.d("---------------------","hola");
        Log.d("hola", cardsj1[i].getId());
        if(arrastre == true) {
            if (cardsj1[i].getPalo() == 5) {
                return false;
            }
                Log.d("dpsarrastre", "");
                if (RondaArrastre == 0) {
                    paloArrastre = cardsj1[i].getPalo();
                    RondaArrastre = 1;
                    RankingArrastre = cardsj1[i].getRanking();
                    return true;
                } else {
                    Log.d("else obvio", "");
                    if (paloArrastre.equals(cardsj1[i].getPalo()) && (cardsj1[i].getRanking() > RankingArrastre)) {
                        Log.d("es igual el palo", "");
                        for (int j = 0; j < 6; j++) {
                            if (j != i) {
                                if (cardsj1[j].getRanking() < RankingArrastre && cardsj1[j].getPalo() == paloArrastre) {
                                    String texto = "Debes superar la carta que han echado";
                                    Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            }
                        }
                        if (cardsj1[i].getRanking() < RankingArrastre) {
                            RankingArrastre = cardsj1[i].getRanking();
                        }
                    } else if ((cardsj1[i].getPalo() == cartaTriunfo.getPalo()) && (!paloArrastre.equals(cartaTriunfo.getPalo()))) {
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
                    } else if (cardsj1[i].getPalo() != paloArrastre && paloArrastre == cartaTriunfo.getPalo()) {
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
                Log.d("jsonDePrueba", aux.toString());
                if(torneo==2){
                    mSocket.emit("lanzarCartaIA", aux, new Ack() {
                        @Override
                        public void call(Object... args) {
                            //JSONObject response = (JSONObject) args[0];
                            //System.out.println(response); // "ok"
                            }
                    });
                }else{
                    mSocket.emit("lanzarCarta", aux, new Ack() {
                        @Override
                        public void call(Object... args) {
                            //JSONObject response = (JSONObject) args[0];
                            //System.out.println(response); // "ok"
                        }
                    });
                }
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
                cuantascartas.setVisibility(View.VISIBLE);
                cartasrestantes.setVisibility(View.VISIBLE);
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