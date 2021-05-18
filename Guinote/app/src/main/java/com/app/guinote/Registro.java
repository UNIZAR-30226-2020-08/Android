package com.app.guinote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Registro extends Fragment {

    public Registro(){
        super(R.layout.activity_registro);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_registro,
                container, false);
        Button registro = view.findViewById(R.id.registro_boton);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout username= (TextInputLayout) view.findViewById(R.id.usernameRegister);
                TextInputLayout email = (TextInputLayout) view.findViewById(R.id.emailRegister);
                TextInputLayout passwd= (TextInputLayout) view.findViewById(R.id.passwdRegister);
                if(validar(view,username,email,passwd)==true) {
                    Intent intent = new Intent(getActivity(), carga_registro.class);
                    Bundle b = new Bundle();
                    b.putString("username", username.getEditText().getText().toString()); //Your id
                    b.putString("email", email.getEditText().getText().toString()); //Your id
                    b.putString("passwd", passwd.getEditText().getText().toString()); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.anim.side_in_left,
                                    R.anim.slide_out_left
                            )
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmento_opcion, Login.class, null)
                            .commit();
                }
            }
        });
        return view;
    }
    public boolean validar(View view, TextInputLayout username,TextInputLayout email,TextInputLayout passwd){
        boolean validar=true;
        String nombreusuario=username.getEditText().getText().toString();
        String mail=email.getEditText().getText().toString();
        String contra=passwd.getEditText().getText().toString();

        if(nombreusuario.isEmpty()){
            username.setError("Escribe un nombre de usuario");
            validar=false;
        }
        else{
            username.setError(null);
        }
        if(mail.isEmpty()){
            email.setError("Escribe un correo electrónico");
            validar=false;
        }
        else{
            email.setError(null);
        }
        if(contra.isEmpty()){
            passwd.setError("Escribe su password");
            validar=false;
        }
        else{
            passwd.setError(null);
        }
        return validar;
    }


}