package com.app.guinote.ActivityTorneo;

import android.app.ActionBar;
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
import com.app.guinote.R;
import com.app.guinote.TorneoFragment.BracketsFragment;
import com.app.guinote.TorneoApp.App1;

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
    private String nombrePartida="";
    private SQLiteDatabase db;
    private int modalidad=0;
    private static int ronda=1;
    LottieAnimationView animacion;
    private int participantes=0;


    private Emitter.Listener onCompleto = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {

                @Override
                public void run(){
                    animacion.setVisibility(View.INVISIBLE);
                    animacion.pauseAnimation();
                    JSONObject auxiliar = new JSONObject();
                    Log.d("hola","holasdasd");
                    try {
                        auxiliar.put("ronda", ronda);
                        auxiliar.put("torneo", nombrePartida);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mSocket.emit("matchTournament", auxiliar, new Ack() {
                        @Override
                        public void call(Object... args) {
                            //JSONObject response = (JSONObject) args[0];
                            //System.out.println(response); // "ok"
                        }
                    });
                }});
        }
    };


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
                    Log.d("hola",args[0].toString());
                    List<String> lista=new ArrayList<>();
                    JSONArray data = (JSONArray) args[0];
                    for (int i=0;i<data.length();i++){
                        try {
                            lista.add(((JSONObject)data.get(i)).getString("jugador"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    bracketFragment.modifyData(ronda,lista,modalidad);

                    ronda++;
                }});
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MyOpenHelper dbHelper = new MyOpenHelper(this);
        db = dbHelper.getWritableDatabase();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_torneo);

        animacion = (LottieAnimationView) findViewById(R.id.animation_carga_perf_torneo);



        mSocket.on("completo",onCompleto);
        mSocket.on("matches",onMatches);
        mSocket.on("joinedT",onJoin);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            nombrePartida = b.getString("key");
            modalidad= b.getInt("modalidad");
            participantes= b.getInt("participantes");

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


    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }
}