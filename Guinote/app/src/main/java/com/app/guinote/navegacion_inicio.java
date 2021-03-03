package com.app.guinote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class navegacion_inicio extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    private BottomNavigationView mBtmView;
    private int mMenuId;
    private static int last=0;
    public navegacion_inicio() {
        super(R.layout.activity_navegacion_inicio);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBtmView = (BottomNavigationView) findViewById(R.id.navegacion_abajo);
        mBtmView.setOnNavigationItemSelectedListener(this);
        mBtmView.getMenu().findItem(R.id.page_1).setChecked(true);
        mBtmView.getMenu().setGroupCheckable(0,false,true);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // uncheck the other items.
        mMenuId = item.getItemId();
        for (int i = 0; i < mBtmView.getMenu().size(); i++) {
            MenuItem menuItem = mBtmView.getMenu().getItem(i);
            boolean isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }

        switch (item.getItemId()) {
            case R.id.page_1: {
                if(last==1){
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in,  // enter
                                    R.anim.fade_out,  // exit
                                    R.anim.fade_in,   // popEnter
                                    R.anim.slide_out
                            )
                            .setReorderingAllowed(true)
                            .hide(getSupportFragmentManager().getFragments().get(0))
                            .commit();
                    mBtmView.getMenu().setGroupCheckable(0,false,true);
                    last=0;
                }else{
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in,  // enter
                                    R.anim.fade_out,  // exit
                                    R.anim.fade_in,   // popEnter
                                    R.anim.slide_out
                            )
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmento_opcion, Login.class, null)
                            .commit();
                    mBtmView.getMenu().setGroupCheckable(0,true,true);
                    last=1;
                }
                return true;
            }
            case R.id.page_2: {
                if (last==2){
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in,  // enter
                                    R.anim.fade_out,  // exit
                                    R.anim.fade_in,   // popEnter
                                    R.anim.slide_out
                            )
                            .setReorderingAllowed(true)
                            .hide(getSupportFragmentManager().getFragments().get(0))
                            .commit();
                    mBtmView.getMenu().setGroupCheckable(0,false,true);
                    last=0;
                }else{
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in,  // enter
                                    R.anim.fade_out,  // exit
                                    R.anim.fade_in,   // popEnter
                                    R.anim.slide_out
                            )
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmento_opcion, Registro.class, null)
                            .commit();
                    mBtmView.getMenu().setGroupCheckable(0,true,true);
                    last=2;
                }
                return true;
            }
            default:
                return false;
        }
    }



}