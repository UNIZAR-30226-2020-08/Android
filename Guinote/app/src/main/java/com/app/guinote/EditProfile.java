package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditProfile extends AppCompatActivity {

    private View view;
    private SQLiteDatabase db;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        GetData();
        /*final TextInputEditText correo= (TextInputEditText) view.findViewById(R.id.full_email);
        final TextInputEditText passwd= (TextInputEditText) view.findViewById(R.id.contraseña);
        Button actualizar = (Button) view.findViewById(R.id.login_boton);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Intent intent = new Intent(getActivity(), Perfil.class);
                correo.setText(nombre);
                //startActivity(intent);
            }
        }*/
    }
    
    private void GetData() {
        String url = "http://192.168.1.36:8080/api/usuario/findUser/:"+ getName();
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray contenido = response;
                    final TextInputEditText correo= (TextInputEditText) view.findViewById(R.id.full_email);
                    correo.setText(response.getString(Integer.parseInt("email")));
                    email=response.getString(Integer.parseInt("email"));

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





        public String getName(){
            String query="SELECT user FROM auth";
            Cursor c=db.rawQuery(query,null);
            c.moveToNext();
            return c.getString(0);
        }

}