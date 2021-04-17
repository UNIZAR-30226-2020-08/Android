package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Perfil extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener{


    private BottomNavigationView cartas;
    private int mMenuId;

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
        cartas.getMenu().findItem(R.id.cartasItem).setChecked(true);
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
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.cartasContainer, ranking.class, null)
                        .commit();
                return true;
            }
            case R.id.cartasItem: {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.cartasContainer, cartas.class, null)
                        .commit();
                return true;
            }
}