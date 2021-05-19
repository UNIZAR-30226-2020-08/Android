package com.app.guinote;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chat extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private EditText bEscribirMensaje;
    private Button bEnviarMensaje,bP1,bP2,bP3;
    private MensajesAdapter adapter;
    private RecyclerView rv;
    private SQLiteDatabase db;
    private Toolbar toolbar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Chat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chat.
     */
    // TODO: Rename and change types and number of parameters
    public static Chat newInstance(String param1, String param2) {
        Chat fragment = new Chat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_chat, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_back_chat);
        bEscribirMensaje = (EditText) view.findViewById(R.id.edittextchat);
        bEnviarMensaje = (Button) view.findViewById(R.id.buttonchat);
        bP1 = (Button) view.findViewById(R.id.buttonchatpredefined1);
        bP2 = (Button) view.findViewById(R.id.buttonchatpredefined2);
        bP3 = (Button) view.findViewById(R.id.buttonchatpredefined3);



        adapter = new MensajesAdapter(((PantallaJuego)getActivity()).mensajeDeTextos);
        rv = (RecyclerView) view.findViewById(R.id.rv_mensajes);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);

        rv.setAdapter(adapter);

        bEscribirMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(bEscribirMensaje.getLayout().getLineCount() != ((PantallaJuego)getActivity()).TEXT_LINES){
                    setScrollbarChat();
                    ((PantallaJuego)getActivity()).TEXT_LINES = bEscribirMensaje.getLayout().getLineCount();
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
                MensajeDeTexto mensajeDeTextoAuxiliar = new MensajeDeTexto("0",bEscribirMensaje.getText().toString(),1,((PantallaJuego)getActivity()).getName());
                ((PantallaJuego)getActivity()).mensajeDeTextos.add(mensajeDeTextoAuxiliar);
                CreateMensaje(((PantallaJuego)getActivity()).getName(),bEscribirMensaje.getText().toString(),1);
                ((PantallaJuego)getActivity()).attemptSend(texto);
                bEscribirMensaje.setText("");
            }
        });

        bP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MensajeDeTexto mensajeDeTextoAuxiliar = new MensajeDeTexto("0","👍",1,((PantallaJuego1vs1)getActivity()).getName());
                ((PantallaJuego1vs1)getActivity()).mensajeDeTextos.add(mensajeDeTextoAuxiliar);
                CreateMensaje(((PantallaJuego1vs1)getActivity()).getName(),"👍",1);
                ((PantallaJuego1vs1)getActivity()).attemptSend("👍");
            }
        });

        bP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto=bEscribirMensaje.getText().toString();
                MensajeDeTexto mensajeDeTextoAuxiliar = new MensajeDeTexto("0","😡",1,((PantallaJuego1vs1)getActivity()).getName());
                ((PantallaJuego1vs1)getActivity()).mensajeDeTextos.add(mensajeDeTextoAuxiliar);
                CreateMensaje(((PantallaJuego1vs1)getActivity()).getName(),"😡",1);
                ((PantallaJuego1vs1)getActivity()).attemptSend("😡");
            }
        });

        bP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto=bEscribirMensaje.getText().toString();
                MensajeDeTexto mensajeDeTextoAuxiliar = new MensajeDeTexto("0","😂",1,((PantallaJuego1vs1)getActivity()).getName());
                ((PantallaJuego1vs1)getActivity()).mensajeDeTextos.add(mensajeDeTextoAuxiliar);
                CreateMensaje(((PantallaJuego1vs1)getActivity()).getName(),"😂",1);
                ((PantallaJuego1vs1)getActivity()).attemptSend("😂");
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();


                Chat fragment = (Chat) fm.findFragmentById(R.id.fragmento_chat);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .hide(fragment)
                        .commit();
            }
        });
        //adapter.notifyDataSetChanged();
        setScrollbarChat();
        return view;
    }

    public void CreateMensaje(String user, String mensaje, Integer tipo){
        adapter.notifyDataSetChanged();
        //bEscribirMensaje.setText("");
        setScrollbarChat();
    }

    public void setScrollbarChat(){
        rv.scrollToPosition(adapter.getItemCount()-1);
    }
}