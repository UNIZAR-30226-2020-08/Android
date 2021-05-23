package com.app.guinote;

public class TipoLista {

    String _subjectName;
    String _numeros;
    public String contra;
    public TipoLista(String _subjectName, String _numeros) {
        this._subjectName = _subjectName;
        this._numeros = _numeros;
    }

    public String get_subjectName() {
        return _subjectName;
    }

    public void set_subjectName(String _subjectName) {
        this._subjectName = _subjectName;
    }

    public String get_numeros() {
        return _numeros;
    }

    public void set_numeros(String _numeros) {
        this._numeros = _numeros;
    }
}
