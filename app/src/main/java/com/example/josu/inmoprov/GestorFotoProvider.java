package com.example.josu.inmoprov;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.util.ArrayList;

/**
 * Created by Josué on 28/01/2015.
 */
public class GestorFotoProvider {

    Context contexto;
    ContentResolver cr;
    Uri uri = Contrato.TablaFoto.CONTENT_URI;

    public GestorFotoProvider(Context context){
        contexto = context;
        cr = contexto.getContentResolver();
    }

    public Uri insert(Foto objeto) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaFoto.ID_INMUEBLE, objeto.getIdInmueble());
        valores.put(Contrato.TablaFoto.RUTA, objeto.getRuta());
        Uri u = cr.insert(uri, valores);
        return u;
    }

    public int update(Foto objeto) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaFoto.ID_INMUEBLE, objeto.getIdInmueble());
        valores.put(Contrato.TablaFoto.RUTA, objeto.getRuta());
        String condicion = Contrato.TablaFoto._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = cr.update(uri, valores, condicion, argumentos);
        return cuenta;
    }

    public int delete(Foto objeto) {
        String condicion = Contrato.TablaFoto._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = cr.delete(uri, condicion,argumentos);
        return cuenta;
    }

    public int delete(long idInmueble) {
        String condicion = Contrato.TablaFoto.ID_INMUEBLE + " = ?";
        String[] argumentos = { idInmueble + "" };
        int cuenta = cr.delete(uri, condicion,argumentos);
        return cuenta;
    }

    public static Foto getRow(Cursor c) {
        Foto objeto = new Foto();
        objeto.setId(c.getLong(0));
        objeto.setIdInmueble(c.getLong(1));
        objeto.setRuta(c.getString(2));
        return objeto;
    }

    public ArrayList<Foto> select(long idInmueble) {
        ArrayList<Foto> lo = new ArrayList();
        String condicion = Contrato.TablaFoto.ID_INMUEBLE + " = ?";
        String[] argumentos = { idInmueble + "" };
        Cursor c = cr.query(uri, null, condicion, argumentos, null);
        c.moveToFirst();
        Foto objeto;
        while (!c.isAfterLast()) {
            objeto = getRow(c);
            lo.add(objeto);
            c.moveToNext();
        }
        c.close();
        return lo;
    }

    public Cursor select(Inmueble inmueble) {
        long idInmueble = inmueble.getId();
        String condicion = Contrato.TablaFoto.ID_INMUEBLE + " = ?";
        String[] argumentos = { idInmueble + "" };
        Cursor c = cr.query(uri, null, condicion, argumentos, null);
        return c;
    }


}
