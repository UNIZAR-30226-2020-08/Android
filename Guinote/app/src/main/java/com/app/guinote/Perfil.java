package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Perfil extends Fragment{


    private CardView cartas;
    private CardView tapete;
    Customadapter AdapterTapetes;
    LottieAnimationView animacion;
    static Integer inicioCartas=0;
    static Integer antiguoCartas;
    static Integer inicioTapetes=0;
    static Integer antiguoTapetes;
    int[] sampleImages = {R.drawable.tapete2, R.drawable.tapete1, R.drawable.hierba, R.drawable.madera, R.drawable.football};
    private int mMenuId;
    private View view;
    private SQLiteDatabase db;

    private String reglas="● Se juega con la baraja española de 40 cartas. \n\n" +
            "● Se puede jugar individualmente (1vs1) o por parejas (2vs2). \n\n" +
            "● Se hará un sorteo para determinar quién reparte y tras esto repartirá el jugador que se haya llevado las 10 últimas. Los jugadores están puestos de la siguiente manera: cada persona está en frente de su pareja, con el montón de cartas a robar en el centro de los cuatro jugadores. (en el caso de ir por parejas). \n\n" +
            "● Se reparten 6 cartas a cada jugador, de 3 en 3, y en el centro de la mesa se coloca una carta boca arriba, que corresponderá con el triunfo y encima se coloca el montón de cartas para robar hacia abajo, dejando ver claramente qué carta está boca arriba. Esta carta también se robará. \n\n" +
            "● El palo de la carta que quede boca arriba debajo del montón para robar, será el palo de triunfo, el cual es superior a todos los demás, es decir, cualquiera de sus cartas “gana” a cualquier carta de otro palo que no sea triunfo y si se juegan más cartas de triunfo gana la más alta. \n\n" +
            "● Sale el jugador siguiente al que ha repartido, lanzando una carta. Los siguientes jugadores, juegan sus cartas asistiendo (ir al palo de salida), montando (echar carta de mayor valor) o fallando (jugar triunfo), según corresponda. Las cartas se juegan en sentido antihorario. Gana la baza la carta más alta del palo de triunfo y si no se juega ninguna carta de triunfo, gana la carta más alta del palo de salida. \n\n" +
            "● El jugador que gane la baza, recoge las cartas y sale en la siguiente ronda. La baza es compartida entre los jugadores de la misma pareja, es decir, no importa quién gane de la pareja, las cartas van a la misma baza. \n\n" +
            "● Cuando se acaba una ronda, el jugador o jugadores que la hayan ganado podrán cantar. También puede intercambiar el 7 de triunfo con la carta del medio. ● Para cantar es necesario juntar la sota y el rey del mismo palo, en caso de que la pareja sea de triunfo se cantarán las 40, es decir, al final de la partida se sumarán 40 puntos extra a los que se hayan conseguido, y en caso contrario se cantarán las 20. Cuando se cantan las cartas se muestra el palo a todos los jugadores. \n\n" +
            "● El juego termina cuando todos los jugadores se han quedado sin cartas, tras esto, se procede al recuento de puntos. \n\n" +
            "● El jugador que se ha llevado la última baza se queda con “las 10 últimas” que son 10 puntos extra que se suman a la puntuación final. \n\n" +
            "● Para ganar, es necesario juntar un total de 101 puntos. \n\n" +
            "● Cuando se acaban las cartas del montón de robar, se juega el arrastre, en este momento el jugador está obligado a matar y si no puede debe tirar una carta del mismo palo del que haya salido el primer jugador. En el caso de que tu compañero tenga el mayor número, no es necesario matar si no tienes cartas del mismo palo que haya salido el primer jugador. \n\n" +
            "● Si ninguno de los jugadores consigue alcanzar los 101 puntos se juega una partida de vueltas. \n\n" +
            "● En la partida de vueltas se juega otra partida hasta que un jugador (o pareja) alcance los 101 puntos necesarios para ganar. En el momento que se alcance dicha puntuación se termina la partida. \n\n" +
            "● En todos los palos, el orden de las cartas, de mayor a menor , es el siguiente: as, tres, rey, sota, caballo, siete, seis, cinco, cuatro y dos. ";

    public Perfil(){
        super(R.layout.activity_perfil);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_perfil,
                container, false);
        View editar = view.findViewById(R.id.editperfil);

        //inicio=getCartas()
        antiguoCartas=inicioCartas;
        antiguoTapetes=inicioTapetes;

        CardView reglas = view.findViewById(R.id.info);

        reglas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReglas();
            }
        });
        animacion = (LottieAnimationView) view.findViewById(R.id.animation_carga_perf);
        ArrayList<Tipocarta> arrayTapete = new ArrayList<Tipocarta>();
        arrayTapete.add(new Tipocarta("Primero",R.drawable.tapete2,true));
        arrayTapete.add(new Tipocarta("Segundo",R.drawable.tapete1,false));
        arrayTapete.add(new Tipocarta("Tercero",R.drawable.hierba,false));
        arrayTapete.add(new Tipocarta("Cuarto",R.drawable.madera,false));
        arrayTapete.add(new Tipocarta("Quinto",R.drawable.football,false));

        AdapterTapetes = new Customadapter(getContext(), arrayTapete);




        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        cartas=view.findViewById(R.id.Cartascambio);
        tapete=view.findViewById(R.id.Tapetecambio);

        cartas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cartas();
            }
        });
        tapete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tapetes();
            }
        });

        MyOpenHelper dbHelper = new MyOpenHelper(getContext());
        db = dbHelper.getWritableDatabase();

        Button cerrar=view.findViewById(R.id.apagarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });


        Button eliminar=view.findViewById(R.id.eliminarSesion);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarCuenta();
            }
        });

        TextView name=view.findViewById(R.id.nameUserPerfil);
        name.setText(getName());
        TextView puntos=view.findViewById(R.id.puntosPerfil);
        puntos.setText(getPuntos());
        return view;
    }

    public void openReglas(){
        final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Reglas del Guiñote");
        builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage(reglas);
        builder.show();

    }

    public void openActivity2(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.side_in_left,
                        R.anim.slide_out_left
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app,EditProfile.class,null)
                .commit();
    }

    public void openActivity(){
        db.execSQL("DELETE FROM auth");
        Intent intent = new Intent(view.getContext(),navegacion_inicio.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXIT",true);
        startActivity(intent);
    }

    public void eliminarCuenta(){


        final String postUrl = "https://las10ultimas-backend.herokuapp.com/api/usuario/dropUser/"+getName();
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());


        final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("¿Desea eliminar su cuenta?");
        builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, postUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        db.execSQL("DELETE FROM auth");
                        Intent intent = new Intent(view.getContext(),navegacion_inicio.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("EXIT",true);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                requestQueue.add(jsonObjectRequest);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage("Si elimina su cuenta, perderá todos sus datos.");
        builder.show();




    }

    public void Cartas(){
                final CharSequence[] charSequence = new CharSequence[] {"As Guest","I have account here"};
                final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Tapetes");
                builder.setNeutralButton("Cancelar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inicioCartas=antiguoCartas;
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        antiguoCartas=inicioCartas;
                        ContentValues cv = new ContentValues();
                        cv.put("f_cartas", inicioCartas);
                        db.update("auth", cv,"where user="+getName(),null);
                        updateCartas(inicioCartas);
                        dialog.dismiss();
                    }
                });

                builder.setSingleChoiceItems(AdapterTapetes,inicioCartas,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer m=which;
                        AdapterTapetes.arrayList.get(inicioCartas).set_elegido(false);
                        AdapterTapetes.arrayList.get(which).set_elegido(true);
                        inicioCartas=which;

                        dialog.dismiss();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                animacion.setVisibility(View.VISIBLE);
                                animacion.playAnimation();
                            }
                        });

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                builder.show();
                                animacion.setVisibility(View.INVISIBLE);
                                animacion.pauseAnimation();
                            }
                        }, 1000);
                    }

                });



                builder.show();
    }
    public void Tapetes(){
                Log.d("hola","jejej");
                final CharSequence[] charSequence = new CharSequence[] {"As Guest","I have account here"};
                final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Cartas");
                builder.setNeutralButton("Cancelar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("holajeje",antiguoTapetes.toString());
                        Log.d("holajojo",inicioTapetes.toString());
                        inicioTapetes=antiguoTapetes;
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        antiguoTapetes=inicioTapetes;
                        ContentValues cv = new ContentValues();
                        cv.put("f_cartas", inicioTapetes);
                        db.update("auth", cv,"where user="+getName(),null);
                        updateCartas(inicioTapetes);
                        dialog.dismiss();
                    }
                });

                builder.setSingleChoiceItems(AdapterTapetes,inicioTapetes,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer m=which;
                        AdapterTapetes.arrayList.get(inicioTapetes).set_elegido(false);
                        AdapterTapetes.arrayList.get(which).set_elegido(true);
                        inicioTapetes=which;

                        dialog.dismiss();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                animacion.setVisibility(View.VISIBLE);
                                animacion.playAnimation();
                            }
                        });

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                builder.show();
                                animacion.setVisibility(View.INVISIBLE);
                                animacion.pauseAnimation();
                            }
                        }, 1000);
                    }
                });
                builder.show();
    }


    public void updateTapete(Integer tapetico){

        String url = "https://las10ultimas-backend.herokuapp.com/api/usuario/updateUser/"+getTapete();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject postData = new JSONObject();

        try {
            postData.put("f_tapete", tapetico);
            Log.d("prueba",postData.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("holaaa",postData.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        /*requestQueue.add(jsonObjectRequest);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.side_in_left,
                        R.anim.slide_out_left
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app, Perfil.class, null)
                .commit();*/
    }

    public void updateCartas(Integer cartas){

        String url = "https://las10ultimas-backend.herokuapp.com/api/usuario/updateUser/"+getCartas();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject postData = new JSONObject();

        try {
            postData.put("f_carta", cartas);
            Log.d("prueba",postData.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("holaaa",postData.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        /*requestQueue.add(jsonObjectRequest);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.side_in_left,
                        R.anim.slide_out_left
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app, Perfil.class, null)
                .commit();*/
    }

    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }

    public String getPuntos() {
        String query="SELECT copas FROM auth";
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






}