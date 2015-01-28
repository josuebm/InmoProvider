package com.example.josu.inmoprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josué on 08/12/2014.
 */
public class GestorFoto {

    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorFoto(Context c) {
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

    public long insert(Foto objeto) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaFoto.ID_INMUEBLE, objeto.getIdInmueble());
        valores.put(Contrato.TablaFoto.RUTA, objeto.getRuta());
        long id = bd.insert(Contrato.TablaFoto.TABLA, null, valores);
        //id es el código autonumérico
        return id;
    }

    public int delete(Foto objeto) {
        String condicion = Contrato.TablaFoto._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = bd.delete(Contrato.TablaFoto.TABLA, condicion,argumentos);
        return cuenta;
    }

    public int delete(int id){
        return delete(new Foto(id, 0, null));
    }

    public int update(Foto objeto) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaFoto.ID_INMUEBLE, objeto.getIdInmueble());
        valores.put(Contrato.TablaFoto.RUTA, objeto.getIdInmueble());
        String condicion = Contrato.TablaFoto._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = bd.update(Contrato.TablaFoto.TABLA, valores, condicion, argumentos);
        return cuenta;
    }

    public List<Foto> select(String condicion, String[] parametros, String orden) {
        List<Foto> lo = new ArrayList<Foto>();
        Cursor cursor = bd.query(Contrato.TablaFoto.TABLA, null, condicion, parametros, null, null, orden);
        cursor.moveToFirst();
        Foto objeto;
        while (!cursor.isAfterLast()) {
            objeto = getRow(cursor);
            lo.add(objeto);
            cursor.moveToNext();
        }
        cursor.close();
        return lo;
    }

    public ArrayList<Foto> select() {
        ArrayList<Foto> lo = new ArrayList<Foto>();
        Cursor cursor = bd.query(Contrato.TablaFoto.TABLA, null, null, null, null, null, null);
        cursor.moveToFirst();
        Foto objeto;
        while (!cursor.isAfterLast()) {
            objeto = getRow(cursor);
            lo.add(objeto);
            cursor.moveToNext();
        }
        cursor.close();
        return lo;
    }

    public static Foto getRow(Cursor c) {
        Foto objeto = new Foto();
        objeto.setId(c.getLong(0));
        objeto.setIdInmueble(c.getLong(1));
        objeto.setRuta(c.getString(2));
        return objeto;
    }

    public Foto getRow(long id){
        List<Foto> fotos = select(Contrato.TablaFoto._ID + " = ?", new String[]{id + ""}, null);
        Foto objeto = fotos.get(0);
        if (!fotos.isEmpty())
            return objeto;
        else
            return null;
    }

    public Cursor getCursor(String consulta) {
        Cursor cursor = bd.rawQuery(consulta, null);
        return cursor;
    }

    public Cursor getCursor(String condicion, String[] parametros, String orden) {
        Cursor cursor = bd.query(Contrato.TablaFoto.TABLA, null, condicion, parametros, null, null, orden);
        return cursor;
    }

    public Cursor getCursor() {
        Cursor cursor = bd.query(Contrato.TablaFoto.TABLA, null, null, null, null, null, null);
        return cursor;
    }

    public ArrayList<Foto> select(long idInmueble) {
        ArrayList<Foto> lo = new ArrayList();
        String[] parametros = new String[] { idInmueble+"" };
        Cursor c = bd.rawQuery("select * from "+
                Contrato.TablaFoto.TABLA
                + " where " + Contrato.TablaFoto.ID_INMUEBLE + " = ?", parametros);
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
}
