package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.List;

public class EditProfile extends Fragment {

    private View view;
    private SQLiteDatabase db;
    private String email;

    public EditProfile(){
        super(R.layout.activity_edit_profile);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_edit_profile,
                container, false);
        MyOpenHelper dbHelper = new MyOpenHelper(getContext());
        db = dbHelper.getWritableDatabase();
        GetData();
        View actualizar = view.findViewById(R.id.Actualizar);
        TextView namePerf = view.findViewById(R.id.nameUserPerfil);
        namePerf.setText(getName());
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
        return view;
    }

    private void GetData() {
        String url = "https://las10ultimas-backend.herokuapp.com/api/usuario/findUser/"+getName();
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Log.d("contenido de response",response.toString());
                    JSONObject contenido = response;
                    final TextInputEditText correo= (TextInputEditText) view.findViewById(R.id.emailfino);
                    final TextInputEditText name= (TextInputEditText) view.findViewById(R.id.usuario2);
                    final TextView puntos = (TextView) view.findViewById(R.id.puntosPerfil);
                    name.setText(contenido.getString("username"));
                    correo.setText(contenido.getString("email"));
                    puntos.setText(contenido.getString("copas"));
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

    public void openActivity(){
        final TextInputLayout correo= (TextInputLayout) view.findViewById(R.id.full_email);
        final TextInputLayout password= (TextInputLayout) view.findViewById(R.id.contraseña);
        String email= correo.getEditText().getText().toString();
        String passwd="";
        if (password.getEditText().getText()!=null) {
            Log.d("hola",password.getEditText().getText().toString());
            passwd = password.getEditText().getText().toString();
        }

        String url = "https://las10ultimas-backend.herokuapp.com/api/usuario/updateUser/"+getName();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            if (passwd !="") {
                Log.d("passwd",passwd);
                postData.put("password", passwd);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            if (postData.get("password")==""){
                postData.remove("password");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("request",postData.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, postData, new Response.Listener<JSONObject>() {
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
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.side_in_left,
                        R.anim.slide_out_left
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app,Perfil.class,null)
                .commit();
    }





        public String getName(){
            String query="SELECT user FROM auth";
            Cursor c=db.rawQuery(query,null);
            c.moveToNext();
            return c.getString(0);

        }

}
