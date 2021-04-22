package com.app.guinote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Mensajeria extends PantallaJuego {

    private RecyclerView rv;
    private String room="";
    private EditText bEscribirMensaje;
    private Button bEnviarMensaje;
    private MensajesAdapter adapter;
    private int TEXT_LINES=1;
    private Toolbar toolbar;
    private EditText mInputMessageView;



    Mensajeria(){
        mensajeDeTextos = new ArrayList<>();
        adapter = new MensajesAdapter(mensajeDeTextos);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);

        toolbar = (Toolbar) findViewById(R.id.toolbar_back_chat);
        bEscribirMensaje = (EditText) findViewById(R.id.edittextchat);
        bEnviarMensaje = (Button) findViewById(R.id.buttonchat);

        rv = (RecyclerView) findViewById(R.id.rv_mensajes);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        rv.setAdapter(adapter);

        bEscribirMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(bEscribirMensaje.getLayout().getLineCount() != TEXT_LINES){
                    setScrollbarChat();
                    TEXT_LINES = bEscribirMensaje.getLayout().getLineCount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto=bEscribirMensaje.getText().toString();
                CreateMensaje(getName(),bEscribirMensaje.getText().toString(),1);
                attemptSend(texto);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setScrollbarChat();
    }

    public void CreateMensaje(String user, String mensaje, Integer tipo){
        MensajeDeTexto mensajeDeTextoAuxiliar = new MensajeDeTexto("0",mensaje,tipo,user);
        mensajeDeTextos.add(mensajeDeTextoAuxiliar);
        adapter.notifyDataSetChanged();
        bEscribirMensaje.setText("");
        setScrollbarChat();
    }

    public void setScrollbarChat(){
        rv.scrollToPosition(adapter.getItemCount()-1);
    }

}


