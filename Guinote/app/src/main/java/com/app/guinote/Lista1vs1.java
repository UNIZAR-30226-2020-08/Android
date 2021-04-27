package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Lista1vs1 extends Fragment {

    View view;

    ListView listView;
    List<listItem> lista;

    public Lista1vs1(){
        super(R.layout.activity_lista1vs1);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_lista1vs1,
                container, false);
        listView = view.findViewById(R.id.lista1vs1);


        GetData();


        FloatingActionButton anadir= view.findViewById(R.id.anadirPartida);

        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Bundle tipo = new Bundle();
                tipo.putInt("tipoPartida", 0 ); //Your id
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragmento_anadir_partida, anadir_partida.class, tipo)
                        .commit();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItem item = lista.get(position);


                String ranking=item.getName();
                Intent intent = new Intent(getActivity(),PantallaJuego1vs1.class);

                Bundle b = new Bundle();
                b.putString("key", ranking); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });


        return view;
    }

    private void GetData() {
        lista = new ArrayList<>();


        String url = "http://192.168.56.1:8080/api/partida/findAllGames/0";
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
                        lista.add(new listItem(i,nombre,R.drawable.sieteespadas,puntuacion.toString()+"/2"));
                    }
                    listAdapter adapter = new listAdapter(getActivity(),lista);
                    listView.setAdapter(adapter);
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