package com.app.guinote;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.guinote.ActivityTorneo.Torneo;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Fragment {


    String[] ListElements = new String[] {
            "Android",
            "PHP"
    };
    CardView createroom;

    ListView lista1vs1;
    CardView cardvs1;
    CardView play2;
    CardView iapartida;
    CardView fd;
    CardView jointorneo;

    private SQLiteDatabase db;

    public MainActivity(){

        super(R.layout.activity_main);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,
                container, false);



        play2 = view.findViewById(R.id.button_start_2vs2);
        play2.setEnabled(true);
        createroom = view.findViewById(R.id.button_start_1vs1);
        createroom.setEnabled(true);
        iapartida = view.findViewById(R.id.button_start_offline);
        iapartida.setEnabled(true);
        fd = view.findViewById(R.id.button_start_private_room);
        fd.setEnabled(true);
        jointorneo = view.findViewById(R.id.button_join_private_room);
        jointorneo.setEnabled(true);
        MaterialToolbar toolbar = (MaterialToolbar) view.findViewById(R.id.topAppBar);


        MyOpenHelper dbHelper = new MyOpenHelper(getContext());
        db = dbHelper.getWritableDatabase();

        //toolbar.setTitle(getName());
        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play2.setEnabled(false);
                openActivity2();
            }
        });
        createroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createroom.setEnabled(false);
                openActivity3();
            }
        });
        jointorneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jointorneo.setEnabled(false);
                openActivity5();
            }
        });
        fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fd.setEnabled(false);
                openActivity4();
            }
        });
        iapartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iapartida.setEnabled(false);
                createPartidaIA();
            }
        });


        return view;

    }

    public void openActivity4(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app, FormularioPartida.class, null)
                .commit();
        //Intent intent = new Intent(getContext(), Torneo.class);
        //startActivity(intent);
    }


    public void openActivity2(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app, Lista2vs2.class, null)
                .commit();
    }
    public void openActivity5(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app, ListaTorneo.class, null)
                .commit();
    }


    public void openActivity3(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app, Lista1vs1.class, null)
                .commit();
    }


    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }

    private void createPartidaIA(){
        String postUrl = "https://las10ultimas-backend.herokuapp.com/api/partida/";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject postData = new JSONObject();
        try {
            postData.put("triunfo", "");
            postData.put("estado", 0);
            postData.put("tipo", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String ranking=response.getString("nombre");
                    Intent intent = new Intent(getActivity(),PantallaJuego1vs1.class);

                    Bundle b = new Bundle();
                    b.putString("key", ranking); //Your id
                    b.putInt("torneo", 2);
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                }

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
}
