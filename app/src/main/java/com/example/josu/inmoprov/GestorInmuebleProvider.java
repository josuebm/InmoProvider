package com.example.josu.inmoprov;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Josué on 28/01/2015.
 */
public class GestorInmuebleProvider {

    Context contexto;
    ContentResolver cr;
    Uri uri = Contrato.TablaInmueble.CONTENT_URI;

    public GestorInmuebleProvider(Context context){
        contexto = context;
        cr = contexto.getContentResolver();
    }

    public Uri insert(Inmueble objeto){
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmueble.LOCALIDAD, objeto.getLocalidad());
        valores.put(Contrato.TablaInmueble.DIRECCION, objeto.getDireccion());
        valores.put(Contrato.TablaInmueble.TIPO, objeto.getTipo());
        valores.put(Contrato.TablaInmueble.HABITACIONES, objeto.getHabitaciones());
        valores.put(Contrato.TablaInmueble.PRECIO, objeto.getPrecio());
        valores.put(Contrato.TablaInmueble.SUBIDO, objeto.getSubido());
        Uri u = cr.insert(uri, valores);
        return u;
    }

    public int update(Inmueble objeto){
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmueble.LOCALIDAD, objeto.getLocalidad());
        valores.put(Contrato.TablaInmueble.DIRECCION, objeto.getDireccion());
        valores.put(Contrato.TablaInmueble.TIPO, objeto.getTipo());
        valores.put(Contrato.TablaInmueble.HABITACIONES, objeto.getHabitaciones());
        valores.put(Contrato.TablaInmueble.PRECIO, objeto.getPrecio());
        valores.put(Contrato.TablaInmueble.SUBIDO, objeto.getSubido());
        String condicion = Contrato.TablaInmueble._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = cr.update(uri, valores, condicion, argumentos);
        return cuenta;
    }

    public int delete(Inmueble objeto) {
        String condicion = Contrato.TablaInmueble._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = cr.delete(uri, condicion,argumentos);
        return cuenta;
    }

    public Cursor getCursor() {
        Cursor cursor = cr.query(uri, null, null, null, null);
        return cursor;
    }

    public static Inmueble getRow(Cursor c) {
        Inmueble objeto = new Inmueble();
        objeto.setId(c.getLong(0));
        objeto.setLocalidad(c.getString(1));
        objeto.setDireccion(c.getString(2));
        objeto.setTipo(c.getInt(3));
        objeto.setHabitaciones(c.getInt(4));
        objeto.setPrecio(c.getFloat(5));
        objeto.setSubido(c.getInt(6));
        return objeto;
    }
}
