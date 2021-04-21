package com.app.guinote;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Mensajeria extends AppCompatActivity {

    private RecyclerView rv;
    private EditText bEscribirMensaje;
    private Button bEnviarMensaje;
    private List<MensajeDeTexto> mensajeDeTextos;
    private MensajesAdapter adapter;
    private int TEXT_LINES=1;
    private EditText mInputMessageView;

    /*private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://chat.socket.io");
        } catch (URISyntaxException e) {}
    }*/

    /*@Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }*/

    /*private void attemptSend() {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }

        mInputMessageView.setText("");
        mSocket.emit("new message", message);
    }*/

    /*private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    addMessage(username, message);
                }
            });
        }
    };*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);


        //mSocket.on("new message", onNewMessage);
        //mSocket.connect();


        mensajeDeTextos = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_back_chat);
        bEscribirMensaje = (EditText) findViewById(R.id.edittextchat);
        bEnviarMensaje = (Button) findViewById(R.id.buttonchat);

        rv = (RecyclerView) findViewById(R.id.rv_mensajes);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        for(int i = 0;i<10;i++){
            MensajeDeTexto mensajeDeTextoAuxiliar = new MensajeDeTexto();
            mensajeDeTextoAuxiliar.setId(""+i);
            mensajeDeTextoAuxiliar.setMensaje("hola "+i);
            mensajeDeTextoAuxiliar.setTipoMensaje(2);
            mensajeDeTextoAuxiliar.setHoradelmensaje("10:34");
            mensajeDeTextos.add(mensajeDeTextoAuxiliar);
        }

        adapter = new MensajesAdapter(mensajeDeTextos);
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
                CreateMensaje(bEscribirMensaje.getText().toString());
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

    public void CreateMensaje(String mensaje){
        MensajeDeTexto mensajeDeTextoAuxiliar = new MensajeDeTexto();
        mensajeDeTextoAuxiliar.setId("0");
        mensajeDeTextoAuxiliar.setMensaje(mensaje);
        mensajeDeTextoAuxiliar.setTipoMensaje(1);
        mensajeDeTextoAuxiliar.setHoradelmensaje("10:34");
        mensajeDeTextos.add(mensajeDeTextoAuxiliar);
        adapter.notifyDataSetChanged();
        bEscribirMensaje.setText("");
        setScrollbarChat();
    }

    public void setScrollbarChat(){
        rv.scrollToPosition(adapter.getItemCount()-1);
    }
}


