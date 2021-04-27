package com.app.guinote;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListRanking} factory method to
 * create an instance of this fragment.
 *
 */
public class ListRanking extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ListView listView;
    List<rank> lista;
    TextView contextMenuTextView;
    RankAdapter adapter;
    TextView noamigos;
    private View view;

    private SQLiteDatabase db;
    private List<String> solicitudes;

    public ListRanking(){
        super(R.layout.fragment_list_ranking);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_ranking,
                container, false);
        listView = view.findViewById(R.id.lista_amigos);

        solicitudes = new ArrayList<>();


        MyOpenHelper dbHelper = new MyOpenHelper(getContext());
        db = dbHelper.getWritableDatabase();


        adapter = new RankAdapter(getActivity(),GetData());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rank  ranking = lista.get(position);
                Toast.makeText(getActivity(),ranking.name,Toast.LENGTH_SHORT).show();

            }
        });

        getSolicitudes();

        Button anadir= view.findViewById(R.id.anadir_amigo);



        contextMenuTextView = view.findViewById(R.id.solicitudes);
        noamigos = view.findViewById(R.id.usuariosNoencontrados);
        registerForContextMenu(contextMenuTextView);

        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAnadirAmigo();
            }
        });

        return view;
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        for(int i =0;i<solicitudes.size();i++){
            menu.add(solicitudes.get(i));
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);

        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Solicitud de amistad");
        final String name=item.toString();
        builder.setMessage(item.toString());


        builder.setNegativeButton("Rechazar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                aceptarSolicitud(name);
                dialog.dismiss();
            }
            });
        builder.show();
        Log.d("user",item.toString());
        return true;
    }

    @Override
    public void onOptionsMenuClosed(@NonNull Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    private void openAnadirAmigo(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_form_anadir,form_nuevoamigo.class, null)
                .commit();
    }


    private List<rank> GetData() {
        lista = new ArrayList<>();

        String postUrl = "http://192.168.1.36:8080/api/amigo/findAll/"+getName();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());



        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, postUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.length()!=0) {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dato;
                        try {
                            dato = response.getJSONObject(i);
                            Integer copas = dato.getInt("copas");
                            lista.add(new rank(i, dato.getString("username"), R.drawable.ascopas, copas.toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    String titulo="No tienes amigos";
                    noamigos.setText(titulo);
                }
                listView.setAdapter(adapter);
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String titulo="No tienes amigos";
                noamigos.setText(titulo);
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);


        /*lista.add(new rank(1,"FERNANDO08",R.drawable.asoros,"150"));
        lista.add(new rank(2,"DIEGOL10",R.drawable.dosoros,"140"));
        lista.add(new rank(3,"JAMONERO",R.drawable.tresoros,"130"));
        lista.add(new rank(4,"DRESPIN",R.drawable.cuatrooros,"120"));*/

        return lista;


    }

    private void getSolicitudes(){
        String postUrl = "http://192.168.1.36:8080/api/amigo/listRequest/"+getName();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());



        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, postUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i=0;i<response.length();i++){
                    JSONObject dato;
                    try {
                        dato = response.getJSONObject(i);
                        solicitudes.add(dato.getString("usuario"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                String titulo="Solicitudes de amistad ("+solicitudes.size()+")";
                contextMenuTextView.setText(titulo);
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

    private void aceptarSolicitud(String amigo){
        String postUrl = "http://192.168.1.36:8080/api/amigo/acceptFriend/"+getName()+"/"+amigo;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, postUrl, null, new Response.Listener<JSONObject>() {
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

    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }
}