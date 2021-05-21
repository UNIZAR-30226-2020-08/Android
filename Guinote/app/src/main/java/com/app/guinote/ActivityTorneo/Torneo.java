package com.app.guinote.ActivityTorneo;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.app.guinote.MyOpenHelper;
import com.app.guinote.PantallaJuego;
import com.app.guinote.PantallaJuego1vs1;
import com.app.guinote.Pantalla_app;
import com.app.guinote.R;
import com.app.guinote.TorneoFragment.BracketsFragment;
import com.app.guinote.TorneoApp.App1;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Ack;
import io.socket.emitter.Emitter;

import static com.app.guinote.Pantalla_app.mSocket;



public class Torneo extends AppCompatActivity {


    private BracketsFragment bracketFragment;
    private static String nombrePartida="";
    private int enPartida=0;
    public static SQLiteDatabase db;
    public static int modalidad=0;
    private static int ronda=1;
    private static int perdido=0;
    private static Context mContext;
    static LottieAnimationView animacion;
    private int participantes=0;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(perdido==0){
            if (enPartida == 0) {
                JSONObject auxiliar = new JSONObject();
                Log.d("hola", "holasdasd");
                try {
                    auxiliar.put("jugador", getName());
                    auxiliar.put("torneo", nombrePartida);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mSocket.emit("leaveTorneo", new Ack() {
                    @Override
                    public void call(Object... args) {
                        //JSONObject response = (JSONObject) args[0];
                        //System.out.println(response); // "ok"
                    }
                });
            } else {
                mSocket.emit("leaveTorneoEmpezado", new Ack() {
                    @Override
                    public void call(Object... args) {
                        //JSONObject response = (JSONObject) args[0];
                        //System.out.println(response); // "ok"
                    }
                });
            }
        }
    }




    private Emitter.Listener onJoin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {

                @Override
                public void run(){
                    Log.d("hola",args[0].toString());
                }});
        }
    };
    private Emitter.Listener onMatches = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {

                @Override
                public void run(){
                    animacion.setVisibility(View.INVISIBLE);
                    animacion.pauseAnimation();
                    enPartida=1;
                    Log.d("hola",args[0].toString());
                    List<String> lista=new ArrayList<>();
                    List<String> listaPartidas=new ArrayList<>();
                    JSONArray data = (JSONArray) args[0];
                    for (int i=0;i<data.length();i++){
                        try {
                            lista.add(((JSONObject)data.get(i)).getString("jugador"));
                            listaPartidas.add(((JSONObject)data.get(i)).getString("partida"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    Log.d("holatete",lista.toString());
                    if(participantes==16){
                        bracketFragment.modifyData(ronda,lista,modalidad,listaPartidas);
                    }else{
                        bracketFragment.modifyData(ronda-1,lista,modalidad,listaPartidas);
                    }
                }});
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mContext=this;
        MyOpenHelper dbHelper = new MyOpenHelper(this);
        db = dbHelper.getWritableDatabase();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_torneo);

        animacion = (LottieAnimationView) findViewById(R.id.animation_carga_perf_torneo);


        mSocket.on("matches",onMatches);
        mSocket.on("joinedT",onJoin);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            nombrePartida = b.getString("key");
            modalidad= b.getInt("modalidad");
            participantes= b.getInt("participantes");

        }

        if (participantes==16){
            ronda=0;
        }else{
            ronda=1;
        }
        initialiseBracketsFragment();

        JSONObject auxiliar = new JSONObject();
        try {
            auxiliar.put("name", getName());
            auxiliar.put("tournament", nombrePartida);
            auxiliar.put("tipo", modalidad);
            auxiliar.put("nTeams", participantes);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("jsonDePrueba",auxiliar.toString());
        animacion.setVisibility(View.VISIBLE);
        animacion.playAnimation();

        mSocket.emit("joinTournament", auxiliar, new Ack() {
            @Override
            public void call(Object... args) {
                //JSONObject response = (JSONObject) args[0];
                //System.out.println(response); // "ok"
            }
        });
    }

    private void initialiseBracketsFragment() {


        bracketFragment = new BracketsFragment(participantes);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.containerTorneo, bracketFragment, "brackets_home_fragment");
        transaction.commit();
        manager.executePendingTransactions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setScreenSize();

    }

    private void setScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        App1.getInstance().setScreenHeight(height);
    }


    public static String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }

    public void actividad(String nameMatch){

        if(modalidad==0){
            Intent intent = new Intent(this, PantallaJuego1vs1.class);

            Bundle b = new Bundle();
            b.putString("key", nameMatch); //Your id
            intent.putExtras(b); //Put your id to your next Intent
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, PantallaJuego.class);

            Bundle b = new Bundle();
            b.putString("key", nameMatch); //Your id
            intent.putExtras(b); //Put your id to your next Intent
            startActivity(intent);
        }
    }

    public static void terminoPartida(){
        if(ronda!=3) {
            animacion.setVisibility(View.VISIBLE);
            animacion.playAnimation();
            JSONObject auxiliar = new JSONObject();
            Integer rondaString = ronda;
            Log.d("fase", rondaString.toString());
            try {
                auxiliar.put("fase", ronda);
                auxiliar.put("torneo", nombrePartida);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mSocket.emit("partidaTorneoFin", auxiliar, new Ack() {
                @Override
                public void call(Object... args) {
                    //JSONObject response = (JSONObject) args[0];
                    //System.out.println(response); // "ok"
                }
            });

            ronda++;
        }else{
            final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(mContext);
            builder.setTitle("¡Felicitaciones has ganado!");
            builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(mContext, Pantalla_app.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("EXIT", true);
                    mContext.startActivity(intent);
                    dialog.dismiss();
                }
            });
            builder.setMessage(getName()+" se proclama ganador del torneo "+nombrePartida);
            builder.show();
        }
    }




}