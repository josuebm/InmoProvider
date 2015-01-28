package com.example.josu.inmoprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2dam on 17/11/2014.
 */
public class GestorInmueble {

    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorInmueble(Context c) {
        abd = new Ayudante(c);
    }
    public void open() {
        bd = abd.getWritableDatabase();
    }
    public void openRead() {
        bd = abd.getReadableDatabase();
    }
    public void close() {
        abd.close();
    }

    public long insert(Inmueble objeto) {
        //String id = "";
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmueble.LOCALIDAD, objeto.getLocalidad());
        valores.put(Contrato.TablaInmueble.DIRECCION, objeto.getDireccion());
        valores.put(Contrato.TablaInmueble.TIPO, objeto.getTipo());
        valores.put(Contrato.TablaInmueble.HABITACIONES, objeto.getHabitaciones());
        valores.put(Contrato.TablaInmueble.PRECIO, objeto.getPrecio());
        valores.put(Contrato.TablaInmueble.SUBIDO, objeto.getSubido());
        //try{
           long id = bd.insert(Contrato.TablaInmueble.TABLA, null, valores);
        //}catch (Exception e){
        //    id = e.toString();
        //}
        Log.v("INSERT", objeto.toString());
        Log.v("INSERT VALORES", valores.toString());

        //id es el código autonumérico
        return id;
    }

    public int delete(Inmueble objeto) {
        String condicion = Contrato.TablaInmueble._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = bd.delete(Contrato.TablaInmueble.TABLA, condicion,argumentos);
        return cuenta;
    }

    public int delete(int id){
        return delete(new Inmueble(id, 0, 0, 0, 0, null, null));
    }

    public int update(Inmueble objeto) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmueble.LOCALIDAD, objeto.getLocalidad());
        valores.put(Contrato.TablaInmueble.DIRECCION, objeto.getDireccion());
        valores.put(Contrato.TablaInmueble.TIPO, objeto.getTipo());
        valores.put(Contrato.TablaInmueble.HABITACIONES, objeto.getHabitaciones());
        valores.put(Contrato.TablaInmueble.PRECIO, objeto.getPrecio());
        valores.put(Contrato.TablaInmueble.SUBIDO, objeto.getSubido());
        String condicion = Contrato.TablaInmueble._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = bd.update(Contrato.TablaInmueble.TABLA, valores, condicion, argumentos);
        return cuenta;
    }

    public List<Inmueble> select(String condicion, String[] parametros, String orden) {
        List<Inmueble> lo = new ArrayList<Inmueble>();
        Cursor cursor = bd.query(Contrato.TablaInmueble.TABLA, null, condicion, parametros, null, null, orden);
        cursor.moveToFirst();
        Inmueble objeto;
        while (!cursor.isAfterLast()) {
            objeto = getRow(cursor);
            lo.add(objeto);
            cursor.moveToNext();
        }
        cursor.close();
        return lo;
    }

    public ArrayList<Inmueble> select() {
        ArrayList<Inmueble> lo = new ArrayList<Inmueble>();
        Cursor cursor = bd.query(Contrato.TablaInmueble.TABLA, null, null, null, null, null, null);
        cursor.moveToFirst();
        Inmueble objeto;
        while (!cursor.isAfterLast()) {
            objeto = getRow(cursor);
            lo.add(objeto);
            cursor.moveToNext();
        }
        cursor.close();
        return lo;
    }
    //long id, int habitaciones, int subido, float precio, String localidad, String direccion, String tipo
    public static Inmueble getRow(Cursor c) {
        Inmueble objeto = new Inmueble();
        objeto.setId(c.getLong(0));
        objeto.setLocalidad(c.getString(1));
        objeto.setDireccion(c.getString(2));
        objeto.setTipo(c.getInt(3));
        objeto.setHabitaciones(c.getInt(4));
        objeto.setPrecio(c.getFloat(5));
        objeto.setSubido(c.getInt(6));
        Log.v("GESTOR", objeto.toString());
        return objeto;
    }

    public Inmueble getRow(long id){
        List<Inmueble> inmuebles = select(Contrato.TablaInmueble._ID + " = ?", new String[]{id + ""}, null);
        Inmueble objeto = inmuebles.get(0);
        if (!inmuebles.isEmpty())
            return objeto;
        else
            return null;
    }

    public Cursor getCursor(String condicion, String[] parametros, String orden) {
        Cursor cursor = bd.query(Contrato.TablaInmueble.TABLA, null, condicion, parametros, null, null, orden);
        return cursor;
    }

    public Cursor getCursor() {
        Cursor cursor = bd.query(Contrato.TablaInmueble.TABLA, null, null, null, null, null, null);
        return cursor;
    }
}
