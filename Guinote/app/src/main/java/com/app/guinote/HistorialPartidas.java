package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class HistorialPartidas extends Fragment {
    ListView lista;
    private SQLiteDatabase db;

    public HistorialPartidas(){
        super(R.layout.activity_historial_partidas);
    }

    String[][] datos=new String[100][5];
    int[] datosImg = {R.drawable.caballobastos, R.drawable.asoros};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_historial_partidas,
                container, false);
        super.onCreate(savedInstanceState);

        MyOpenHelper dbHelper = new MyOpenHelper(getContext());
        db = dbHelper.getWritableDatabase();

        getData();
        lista = (ListView) view.findViewById(R.id.lista_historial);


        return view;
    }

    private void getData(){


        String url = "https://las10ultimas-backend.herokuapp.com/api/partida/listarHistorial/"+getName();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray contenido = response;
                    int num=0;
                    for (int i=0;i<contenido.length();i++){
                        JSONObject objeto=contenido.getJSONObject(i);
                        datos[i][0]=objeto.getString("estado");
                        datos[i][1]=objeto.getString("partida");
                        Integer modalidad=objeto.getInt("tipo");
                        if(modalidad==0){
                            datos[i][2]="Individual";
                        }else{
                            datos[i][2]="Por parejas";
                        }
                        Integer puntos_e0=objeto.getInt("puntos_e0");
                        datos[i][3]=puntos_e0.toString();
                        Integer puntos_e1=objeto.getInt("puntos_e1");
                        datos[i][4]=puntos_e1.toString();
                        Log.d("historial",datos[i].toString());
                        num++;
                    }
                    lista.setAdapter(new HistAdapter(getContext(), datos, datosImg,num));
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