package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.AsyncQueryHandler;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.Observable;
import android.database.sqlite.SQLiteDatabase;
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
import com.app.guinote.ActivityTorneo.Torneo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Lista1vs1 extends Fragment {

    View view;
    private SQLiteDatabase db;
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

        MyOpenHelper dbHelper = new MyOpenHelper(getContext());
        db = dbHelper.getWritableDatabase();

        GetData();


        FloatingActionButton anadir= view.findViewById(R.id.anadirPartida);
        FloatingActionButton pausadas= view.findViewById(R.id.listaPartidaPausada);

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

        pausadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                newActivity1();
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
                b.putInt("torneo", 0);
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });


        return view;
    }

    private void GetData() {
        lista = new ArrayList<>();


        String url = "https://las10ultimas-backend.herokuapp.com/api/partida/findAllGames/0";
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

    private void newActivity1(){
        final List<String> listaPausadastipo = new ArrayList<String>();




        String url = "https://las10ultimas-backend.herokuapp.com/api/partida/"+getName()+"/0";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray contenido = response;
                    for (int i=0;i<contenido.length();i++){
                        JSONObject objeto=contenido.getJSONObject(i);
                        String nombre=objeto.getString("nombre");
                        listaPausadastipo.add(nombre);
                    }

                    final CharSequence[] listaPausadas = listaPausadastipo.toArray(new CharSequence[listaPausadastipo.size()]);



                    final MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
                    builder.setTitle("Partidas Pausadas");

                    builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setItems(listaPausadas,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getContext(), PantallaJuego1vs1.class);
                            Bundle b = new Bundle();
                            b.putString("key", listaPausadastipo.get(which)); //Your id
                            b.putInt("torneo", 3);
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                        }
                    });



                    builder.show();
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


    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }
}