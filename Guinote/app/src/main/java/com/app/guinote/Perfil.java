package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Perfil extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener{


    private BottomNavigationView cartas;
    private int mMenuId;
    private SQLiteDatabase db;
    public Perfil(){
        super(R.layout.activity_perfil);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_perfil,
                container, false);
        cartas=view.findViewById(R.id.navigation_rail);

        cartas.setOnNavigationItemSelectedListener(this);

        MyOpenHelper dbHelper = new MyOpenHelper(getContext());
        db = dbHelper.getWritableDatabase();

        TextView name=view.findViewById(R.id.nameUserPerfil);
        name.setText(getName());
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.cartasContainer, cartas.class, null)
                .commit();
        return view;
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

}