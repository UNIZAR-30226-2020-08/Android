package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Perfil extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener{


    private BottomNavigationView cartas;
    private int mMenuId;
    private View view;
    private SQLiteDatabase db;

    public Perfil(){
        super(R.layout.activity_perfil);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_perfil,
                container, false);
        View editar = view.findViewById(R.id.editperfil);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent (v.getContext(), EditProfile.class);
                startActivityForResult(intent2, 0);
            }
        });
        cartas=view.findViewById(R.id.navigation_rail);

        cartas.setOnNavigationItemSelectedListener(this);

        MyOpenHelper dbHelper = new MyOpenHelper(getContext());
        db = dbHelper.getWritableDatabase();

        ImageButton cerrar=view.findViewById(R.id.apagarSesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });

        TextView name=view.findViewById(R.id.nameUserPerfil);
        name.setText(getName());
        TextView puntos=view.findViewById(R.id.puntosPerfil);
        puntos.setText(getPuntos());
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.cartasContainer, cartas.class, null)
                .commit();
        return view;
    }

    public void openActivity(){
        db.execSQL("DELETE FROM auth");
        Intent intent = new Intent(view.getContext(),navegacion_inicio.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXIT",true);
        startActivity(intent);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mMenuId = item.getItemId();
        for (int i = 0; i < cartas.getMenu().size(); i++) {
            MenuItem menuItem = cartas.getMenu().getItem(i);
            boolean isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }

        switch (item.getItemId()) {
            case R.id.tapeteItem: {

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.side_in_left,
                                R.anim.slide_out_left
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.cartasContainer, Tapete.class, null)
                        .commit();
                return true;
            }
            case R.id.cartasItem: {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.side_in_left,
                                R.anim.slide_out_left
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.cartasContainer, cartas.class, null)
                        .commit();
                return true;
            }
            default:
                return false;
        }
    }

    public String getName() {
        String query="SELECT user FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }

    public String getPuntos() {
        String query="SELECT copas FROM auth";
        Cursor c=db.rawQuery(query,null);
        c.moveToNext();
        return c.getString(0);
    }

}