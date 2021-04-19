package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Pantalla_app extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView mBtmView;
    private int mMenuId;
    public Pantalla_app(){
        super(R.layout.activity_pantalla_app);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_app);
        mBtmView = (BottomNavigationView) findViewById(R.id.navegacion_app);
        mBtmView.setOnNavigationItemSelectedListener(this);

        mBtmView.getMenu().findItem(R.id.inicio_juego).setChecked(true);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmento_app, MainActivity.class, null)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mMenuId = item.getItemId();
        for (int i = 0; i < mBtmView.getMenu().size(); i++) {
            MenuItem menuItem = mBtmView.getMenu().getItem(i);
            boolean isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }

        switch (item.getItemId()) {
            case R.id.ranking_juego: {

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.side_in_left,
                                R.anim.slide_out_left
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmento_app, ranking.class, null)
                        .commit();
                return true;
            }
            case R.id.inicio_juego: {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.side_in_left,
                                R.anim.slide_out_left
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmento_app, MainActivity.class, null)
                        .commit();
                return true;
            }
            case R.id.perfil_juego: {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.side_in_left,
                                R.anim.slide_out_left
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmento_app, Perfil.class, null)
                        .commit();
                return true;
            }
            case R.id.historial_juego_opt: {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.side_in_left,
                                R.anim.slide_out_left
                        )
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmento_app, HistorialPartidas.class, null)
                        .commit();
            }
            default:
                return false;
        }
    }

}