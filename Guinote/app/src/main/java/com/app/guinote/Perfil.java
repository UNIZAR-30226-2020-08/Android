package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Perfil extends Fragment{


    private CardView cartas;
    private CardView tapete;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    Customadapter AdapterTapetes;
    Customadapter AdapterCartas;
    Customadapter AdapterFPerfil;
    LottieAnimationView animacion;
    static String inicioCartas="";
    static String  antiguoCartas="";
    static String inicioTapetes="";
    static String inicioFotoperfil="";
    static String  antiguoTapetes="";
    static String  antiguoFotoperfil="";
    int[] sampleImages = {R.drawable.tapete2, R.drawable.tapete1, R.drawable.hierba, R.drawable.madera, R.drawable.football};
    private int mMenuId;
    private View view;
    ImageButton foto_gallery;
    private SQLiteDatabase db;
    CircleImageView mProfilePhoto;
    int cuantasPartidas;



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
        final View editar = view.findViewById(R.id.editperfil);

        //inicio=getCartas()
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_image);
        mProfilePhoto.setEnabled(true);
        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfilePhoto.setEnabled(false);
                Fperfil();
            }
        });

        final CardView reglas = view.findViewById(R.id.info);
        reglas.setEnabled(true);

        reglas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reglas.setEnabled(false);
                openReglas();
            }
        });
        animacion = (LottieAnimationView) view.findViewById(R.id.animation_carga_perf);


        ArrayList<Tipocarta> arrayTapete = new ArrayList<Tipocarta>();

        InputStream ims=null;
        InputStream ims1=null;
        InputStream ims2=null;
        InputStream ims3=null;




        editar.setEnabled(true);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar.setEnabled(false);
                openActivity2();
            }
        });

        MyOpenHelper dbHelper = new MyOpenHelper(getContext());
        db = dbHelper.getWritableDatabase();

        inicioCartas=getCartas();
        inicioTapetes=getTapete();
        inicioFotoperfil=getFoto();




        try {
            ims = getActivity().getAssets().open("tapete1.jpg");
            ims1 = getActivity().getAssets().open("tapete2.jpg");
            ims2 = getActivity().getAssets().open("tapete3.jpg");
            ims3 = getActivity().getAssets().open("tapete4.jpg");
        }catch (Exception e){
            e.printStackTrace();
        }

        Drawable d = Drawable.createFromStream(ims, null);
        Drawable d1 = Drawable.createFromStream(ims1, null);
        Drawable d2 = Drawable.createFromStream(ims2, null);
        Drawable d3 = Drawable.createFromStream(ims3, null);

        switch (Character.getNumericValue(inicioTapetes.charAt(6))-1){
            case 0:
                arrayTapete.add(new Tipocarta("Primero",d,true));
                arrayTapete.add(new Tipocarta("Segundo",d1,false));
                arrayTapete.add(new Tipocarta("Tercero",d2,false));
                arrayTapete.add(new Tipocarta("Cuarto",d3,false));
                break;
            case 1:
                arrayTapete.add(new Tipocarta("Primero",d,false));
                arrayTapete.add(new Tipocarta("Segundo",d1,true));
                arrayTapete.add(new Tipocarta("Tercero",d2,false));
                arrayTapete.add(new Tipocarta("Cuarto",d3,false));
                break;
            case 2:
                arrayTapete.add(new Tipocarta("Primero",d,false));
                arrayTapete.add(new Tipocarta("Segundo",d1,false));
                arrayTapete.add(new Tipocarta("Tercero",d2,true));
                arrayTapete.add(new Tipocarta("Cuarto",d3,false));
                break;
            case 3:
                arrayTapete.add(new Tipocarta("Primero",d,false));
                arrayTapete.add(new Tipocarta("Segundo",d1,false));
                arrayTapete.add(new Tipocarta("Tercero",d2,false));
                arrayTapete.add(new Tipocarta("Cuarto",d3,true));
                break;
            default:
                break;
        }

        AdapterTapetes = new Customadapter(getContext(), arrayTapete);

        try {
            ims = getActivity().getAssets().open("baraja1/reverso.png");
            ims1 = getActivity().getAssets().open("baraja2/reverso.png");
            ims2 = getActivity().getAssets().open("baraja3/reverso.png");
            ims3 = getActivity().getAssets().open("baraja4/reverso.png");
        }catch (Exception e){
            e.printStackTrace();
        }

        d = Drawable.createFromStream(ims, null);
        d1 = Drawable.createFromStream(ims1, null);
        d2 = Drawable.createFromStream(ims2, null);
        d3 = Drawable.createFromStream(ims3, null);
        ArrayList<Tipocarta> arrayCartas = new ArrayList<Tipocarta>();
        switch (Character.getNumericValue(inicioCartas.charAt(6))-1){
            case 0:
                arrayCartas.add(new Tipocarta("Primero",d,true));
                arrayCartas.add(new Tipocarta("Segundo",d1,false));
                arrayCartas.add(new Tipocarta("Tercero",d2,false));
                arrayCartas.add(new Tipocarta("Cuarto",d3,false));
                break;
            case 1:
                arrayCartas.add(new Tipocarta("Primero",d,false));
                arrayCartas.add(new Tipocarta("Segundo",d1,true));
                arrayCartas.add(new Tipocarta("Tercero",d2,false));
                arrayCartas.add(new Tipocarta("Cuarto",d3,false));
                break;
            case 2:
                arrayCartas.add(new Tipocarta("Primero",d,false));
                arrayCartas.add(new Tipocarta("Segundo",d1,false));
                arrayCartas.add(new Tipocarta("Tercero",d2,true));
                arrayCartas.add(new Tipocarta("Cuarto",d3,false));
                break;
            case 3:
                arrayCartas.add(new Tipocarta("Primero",d,false));
                arrayCartas.add(new Tipocarta("Segundo",d1,false));
                arrayCartas.add(new Tipocarta("Tercero",d2,false));
                arrayCartas.add(new Tipocarta("Cuarto",d3,true));
                break;
            default:
                break;
        }


        AdapterCartas = new Customadapter(getContext(), arrayCartas);

        try {
            ims = getActivity().getAssets().open("userlogo1.png");
            ims1 = getActivity().getAssets().open("userlogo2.png");
            ims2 = getActivity().getAssets().open("userlogo3.png");
            ims3 = getActivity().getAssets().open("userlogo4.png");
        }catch (Exception e){
            e.printStackTrace();
        }

        d = Drawable.createFromStream(ims, null);
        d1 = Drawable.createFromStream(ims1, null);
        d2 = Drawable.createFromStream(ims2, null);
        d3 = Drawable.createFromStream(ims3, null);
        ArrayList<Tipocarta> arrayFPerfil = new ArrayList<Tipocarta>();
        switch (Character.getNumericValue(inicioFotoperfil.charAt(8))-1){
            case 0:
                arrayFPerfil.add(new Tipocarta("Primero",d,true));
                arrayFPerfil.add(new Tipocarta("Segundo",d1,false));
                arrayFPerfil.add(new Tipocarta("Tercero",d2,false));
                arrayFPerfil.add(new Tipocarta("Cuarto",d3,false));
                break;
            case 1:
                arrayFPerfil.add(new Tipocarta("Primero",d,false));
                arrayFPerfil.add(new Tipocarta("Segundo",d1,true));
                arrayFPerfil.add(new Tipocarta("Tercero",d2,false));
                arrayFPerfil.add(new Tipocarta("Cuarto",d3,false));
                break;
            case 2:
                arrayFPerfil.add(new Tipocarta("Primero",d,false));
                arrayFPerfil.add(new Tipocarta("Segundo",d1,false));
                arrayFPerfil.add(new Tipocarta("Tercero",d2,true));
                arrayFPerfil.add(new Tipocarta("Cuarto",d3,false));
                break;
            case 3:
                arrayFPerfil.add(new Tipocarta("Primero",d,false));
                arrayFPerfil.add(new Tipocarta("Segundo",d1,false));
                arrayFPerfil.add(new Tipocarta("Tercero",d2,false));
                arrayFPerfil.add(new Tipocarta("Cuarto",d3,true));
                break;
            default:
                break;
        }


        AdapterFPerfil = new Customadapter(getContext(), arrayFPerfil);

        antiguoCartas=inicioCartas;
        antiguoTapetes=inicioTapetes;
        antiguoFotoperfil=inicioFotoperfil;
        Log.d("tapete",getTapete());
        Log.d("cartas",getCartas());


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


        String url = "https://las10ultimas-backend.herokuapp.com/api/partida/listarHistorial/"+getName();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray contenido = response;
                    cuantasPartidas = contenido.length();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        TextView name=view.findViewById(R.id.nameUserPerfil);
        name.setText(getName());
        TextView puntos=view.findViewById(R.id.puntosPerfil);
        puntos.setText(getPuntos());
        TextView partidasTotales=view.findViewById(R.id.partidasNumero);
        Integer cuantas = cuantasPartidas;
        Log.d("hola cuantas", cuantas.toString());
        partidasTotales.setText(cuantas.toString());
        assignProfilePicture(mProfilePhoto);
        return view;
    }

    private void assignProfilePicture(CircleImageView mProfilePhoto) {
        try {
            // get input stream
            InputStream ims = getActivity().getAssets().open(getFoto()+".png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            mProfilePhoto.setImageDrawable(d);
        }
        catch(Exception ex) {
            return;
        }
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("return-data");
                mProfilePhoto.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                builder.setTitle("Cartas");
                builder.setNeutralButton("Cancelar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("pruea",inicioCartas);
                        Log.d("prueaM",antiguoCartas);
                        AdapterCartas.arrayList.get(Character.getNumericValue(inicioCartas.charAt(6))-1).set_elegido(false);
                        inicioCartas=antiguoCartas;
                        Log.d("pruea2",inicioCartas);
                        AdapterCartas.arrayList.get(Character.getNumericValue(inicioCartas.charAt(6))-1).set_elegido(true);
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        antiguoCartas=inicioCartas;
                        String query="UPDATE auth SET f_carta='"+inicioCartas+"' WHERE user='"+getName()+"'";
                        Log.d("query",query);
                        db.execSQL(query);
                        updateCartas(inicioCartas);
                        Log.d("holaPO",getCartas());
                        dialog.dismiss();
                    }
                });

                builder.setSingleChoiceItems(AdapterCartas,Character.getNumericValue(inicioCartas.charAt(6))-1,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer m=which+1;
                        Log.d("num",inicioCartas);
                        AdapterCartas.arrayList.get(Character.getNumericValue(inicioCartas.charAt(6))-1).set_elegido(false);
                        AdapterCartas.arrayList.get(which).set_elegido(true);
                        inicioCartas="baraja"+m.toString();

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
                builder.setTitle("Tapetes");
                builder.setNeutralButton("Cancelar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AdapterTapetes.arrayList.get(Character.getNumericValue(inicioTapetes.charAt(6))-1).set_elegido(false);
                        inicioTapetes=antiguoTapetes;
                        AdapterTapetes.arrayList.get(Character.getNumericValue(inicioTapetes.charAt(6))-1).set_elegido(true);
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        antiguoTapetes=inicioTapetes;
                        String query="UPDATE auth SET f_tapete='"+inicioTapetes+"' WHERE user='"+getName()+"'";
                        Log.d("query",query);
                        db.execSQL(query);
                        updateTapete(inicioTapetes);
                        dialog.dismiss();
                    }
                });

                builder.setSingleChoiceItems(AdapterTapetes,Character.getNumericValue(inicioTapetes.charAt(6))-1,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer m=which+1;
                        AdapterTapetes.arrayList.get(Character.getNumericValue(inicioTapetes.charAt(6))-1).set_elegido(false);
                        AdapterTapetes.arrayList.get(which).set_elegido(true);

                        inicioTapetes="tapete"+m.toString();


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

    public void Fperfil(){
        final CharSequence[] charSequence = new CharSequence[] {"As Guest","I have account here"};
        final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Fotos de perfil");
        builder.setNeutralButton("Cancelar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("pruea",inicioFotoperfil);
                Log.d("prueaM",antiguoFotoperfil);
                AdapterFPerfil.arrayList.get(Character.getNumericValue(inicioFotoperfil.charAt(8))-1).set_elegido(false);
                inicioFotoperfil=antiguoFotoperfil;
                Log.d("pruea2",inicioFotoperfil);
                AdapterFPerfil.arrayList.get(Character.getNumericValue(inicioFotoperfil.charAt(8))-1).set_elegido(true);
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                antiguoFotoperfil=inicioFotoperfil;
                String query="UPDATE auth SET f_perfil='"+inicioFotoperfil+"' WHERE user='"+getName()+"'";
                Log.d("query",query);
                db.execSQL(query);
                updateFPerfil(inicioFotoperfil);
                Log.d("holaPO",getFoto());
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(AdapterFPerfil,Character.getNumericValue(inicioFotoperfil.charAt(8))-1,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer m=which+1;
                Log.d("num",inicioFotoperfil);
                AdapterFPerfil.arrayList.get(Character.getNumericValue(inicioFotoperfil.charAt(8))-1).set_elegido(false);
                AdapterFPerfil.arrayList.get(which).set_elegido(true);
                inicioFotoperfil="userlogo"+m.toString();

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


    public void updateTapete(String tapetico){

        String url = "https://las10ultimas-backend.herokuapp.com/api/usuario/updateUser/"+getName();

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

        requestQueue.add(jsonObjectRequest);
    }

    public void updateCartas(String cartas){

        String url = "https://las10ultimas-backend.herokuapp.com/api/usuario/updateUser/"+getName();

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

    public void updateFPerfil(String fotos){

        String url = "https://las10ultimas-backend.herokuapp.com/api/usuario/updateUser/"+getName();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject postData = new JSONObject();

        try {
            postData.put("f_perfil", fotos);
            Log.d("prueba",postData.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("holaaa",postData.toString());

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
        assignProfilePicture(mProfilePhoto);
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

    public String getFoto() {
        String query="SELECT f_perfil FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }
}