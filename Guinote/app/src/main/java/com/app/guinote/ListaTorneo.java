package com.app.guinote;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ListaTorneo extends Fragment {

    View view;

    ListView listView16_2;
    ListView listView8_2;
    ListView listView16_1;
    ListView listView8_1;
    List<listItem> lista16_2;
    List<listItem> lista8_2;
    List<listItem> lista8_1;
    List<listItem> lista16_1;

    public ListaTorneo(){
        super(R.layout.activity_lista_torneo);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_lista_torneo,
                container, false);
        listView8_2 = view.findViewById(R.id.lista_torneo8_2);
        listView8_1 = view.findViewById(R.id.lista_torneo8_1);
        listView16_1 = view.findViewById(R.id.lista_torneo16_1);
        listView16_2 = view.findViewById(R.id.lista_torneo16_2);


        GetData8_1();
        GetData8_2();
        GetData16_1();
        GetData16_2();



        listView16_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItem item = lista16_2.get(position);


                String ranking=item.getName();
                Intent intent = new Intent(getActivity(),PantallaJuego.class);

                Bundle b = new Bundle();
                b.putString("key", ranking); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });


        return view;
    }

    private void GetData8_2() {
        lista8_2 = new ArrayList<>();


        String url = "https://las10ultimas-backend.herokuapp.com/api/torneo/findAllTournament/1/8";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray contenido = response;
                    for (int i=0;i<contenido.length();i++){
                        JSONObject objeto=contenido.getJSONObject(i);
                        String nombre=objeto.getString("nombre");
                        Integer puntuacion=objeto.getInt("jugadores_online");
                        Log.d("msg",nombre);
                        lista8_2.add(new listItem(i,nombre,R.drawable.sieteespadas,puntuacion.toString()+"/4"));
                    }
                    listAdapter adapter = new listAdapter(getActivity(),lista8_2);
                    listView8_2.setAdapter(adapter);
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

        requestQueue.add(jsonObjectRequest);
    }

    private void GetData16_2() {
        lista16_2 = new ArrayList<>();


        String url = "https://las10ultimas-backend.herokuapp.com/api/torneo/findAllTournament/1/16";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray contenido = response;
                    for (int i=0;i<contenido.length();i++){
                        JSONObject objeto=contenido.getJSONObject(i);
                        String nombre=objeto.getString("nombre");
                        Integer puntuacion=objeto.getInt("jugadores_online");
                        Log.d("msg",nombre);
                        lista16_2.add(new listItem(i,nombre,R.drawable.sieteespadas,puntuacion.toString()+"/4"));
                    }
                    listAdapter adapter = new listAdapter(getActivity(),lista16_2);
                    listView16_2.setAdapter(adapter);
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

        requestQueue.add(jsonObjectRequest);
    }

    private void GetData8_1() {
        lista8_1 = new ArrayList<>();


        String url = "https://las10ultimas-backend.herokuapp.com/api/torneo/findAllTournament/0/8";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray contenido = response;
                    for (int i=0;i<contenido.length();i++){
                        JSONObject objeto=contenido.getJSONObject(i);
                        String nombre=objeto.getString("nombre");
                        Integer puntuacion=objeto.getInt("jugadores_online");
                        Log.d("msg",nombre);
                        lista8_1.add(new listItem(i,nombre,R.drawable.sieteespadas,puntuacion.toString()+"/4"));
                    }
                    listAdapter adapter = new listAdapter(getActivity(),lista8_1);
                    listView8_1.setAdapter(adapter);
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

        requestQueue.add(jsonObjectRequest);
    }

    private void GetData16_1() {
        lista16_1 = new ArrayList<>();


        String url = "https://las10ultimas-backend.herokuapp.com/api/torneo/findAllTournament/0/16";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray contenido = response;
                    for (int i=0;i<contenido.length();i++){
                        JSONObject objeto=contenido.getJSONObject(i);
                        String nombre=objeto.getString("nombre");
                        Integer puntuacion=objeto.getInt("jugadores_online");
                        Log.d("msg",nombre);
                        lista16_1.add(new listItem(i,nombre,R.drawable.sieteespadas,puntuacion.toString()+"/4"));
                    }
                    listAdapter adapter = new listAdapter(getActivity(),lista16_1);
                    listView16_1.setAdapter(adapter);
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

        requestQueue.add(jsonObjectRequest);
    }
}