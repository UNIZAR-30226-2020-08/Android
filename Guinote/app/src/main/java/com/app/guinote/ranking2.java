package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ranking2 extends Fragment {

    ListView listView;
    List<rank> lista;
    RankAdapter adapter;
    private SQLiteDatabase db;


    public ranking2(){
        super(R.layout.activity_ranking2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ranking2,
                container, false);
        listView = view.findViewById(R.id.lista_global);

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

        return view;
    }


    private List<rank> GetData() {
        lista = new ArrayList<>();

        String postUrl = "https://las10ultimas-backend.herokuapp.com/api/usuario/findAll/";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());



        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, postUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.length()!=0) {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dato;
                        try {
                            dato = response.getJSONObject(i);
                            if(!dato.getString("username").equals("IA") && !dato.getString("username").equals(getName())) {
                                Log.d("username",dato.getString("username"));
                                Integer copas = dato.getInt("copas");
                                lista.add(new rank(i, dato.getString("username"), R.drawable.ascopas, copas.toString()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    listView.setAdapter(adapter);
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
        return lista;


    }

    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }
}