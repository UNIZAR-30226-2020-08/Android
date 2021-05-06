package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
        //Button actualizar = (Button)view.findViewById(R.id.Actualizar);
        /*actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextInputEditText correo= (TextInputEditText) view.findViewById(R.id.full_email);
                final TextInputEditText password= (TextInputEditText) view.findViewById(R.id.contraseña);
                String email= correo.getText().toString();
                String passwd=password.getText().toString();

                MyOpenHelper dbHelper = new MyOpenHelper(getContext());
                final SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM auth");
                String url = "http://192.168.56.1:8081/api/auth/signin";
                final List<String> jsonResponses = new ArrayList<>();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("email", email);
                    postData.put("password", passwd);

                } catch (JSONException e) {
                    e.printStackTrace();
                }





                Intent intent = new Intent(getActivity(),Perfil.class);
                startActivity(intent);
            }
        });*/
        return view;
    }
    
    private void GetData() {
        String url = "http://192.168.1.36:8080/api/usuario/findUser/"+getName();
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