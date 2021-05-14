package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class FormularioPartida extends Fragment {

    private View view;
    AutoCompleteTextView menumodalidad;
    AutoCompleteTextView menuparticipantes;

    public FormularioPartida(){
        super(R.layout.activity_formulario_partida);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_formulario_partida,
                container, false);

        menumodalidad=  view.findViewById(R.id.menuModalidad);
        String lista[]={"Parejas","Individual"};
        ArrayAdapter adaptador= new ArrayAdapter(getContext(),R.layout.itemdropdown,lista);
        menumodalidad.setAdapter(adaptador);

        menuparticipantes=  view.findViewById(R.id.menuNumGente);
        String lista2[]={"16 equipos","8 equipos"};
        ArrayAdapter adaptadorGente= new ArrayAdapter(getContext(),R.layout.itemdropdown,lista2);
        menuparticipantes.setAdapter(adaptadorGente);

        Button creacion = view.findViewById(R.id.CrearTorneo);

        creacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creacion();
            }
        });
        return view;
    }


    private void creacion(){
        final TextInputLayout cajaNombre= (TextInputLayout) view.findViewById(R.id.nombretorneo);
        String nombrePartida=cajaNombre.getEditText().getText().toString();

        final TextInputLayout cajaNombre2= (TextInputLayout) view.findViewById(R.id.contrasenatorneo);
        String passwd=cajaNombre2.getEditText().getText().toString();
        int modalidad=0;
        int participantes=0;

        if (menuparticipantes.getText().toString().equals("8 equipos")){
            participantes=8;
        }else{
            participantes=16;
        }
        if (menumodalidad.getText().toString().equals("Parejas")){
            modalidad=1;
        }

        String postUrl = "https://las10ultimas-backend.herokuapp.com/api/torneo/";

        JSONObject postData = new JSONObject();
        try {
            postData.put("nombre", nombrePartida);
            postData.put("tipo", modalidad);
            postData.put("nparticipantes", participantes);
            postData.put("password", passwd);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("envio",postData.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    menumodalidad.clearListSelection();
                    menuparticipantes.clearListSelection();
                    cajaNombre.getEditText().setText("");
                    cajaNombre2.getEditText().setText("");
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
        Log.d("datos",modalidad+" "+nombrePartida+" "+participantes);
    }

}