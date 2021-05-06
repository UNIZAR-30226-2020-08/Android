package com.app.guinote;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Tipocarta {

    int _imagen;
    String _subjectName;
    boolean _elegido;

    public void set_imagen(int _imagen) {
        this._imagen = _imagen;
    }

    public void set_subjectName(String _subjectName) {
        this._subjectName = _subjectName;
    }

    public int get_imagen() {
        return _imagen;
    }

    public String get_subjectName() {
        return _subjectName;
    }

    public boolean is_elegido() {
        return _elegido;
    }

    public void set_elegido(boolean _elegido) {
        this._elegido = _elegido;
    }

    Tipocarta(String subjectName, int imagen, boolean elegido){
        _subjectName=subjectName;
        _imagen=imagen;
        _elegido=elegido;
    }
}
