package com.example.josu.inmoprovider;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Ayudante extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "inmobiliaria.sqlite";
    public static final int DATABASE_VERSION = 1;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql = "create table " + Contrato.TablaInmueble.TABLA + "( " +
                Contrato.TablaInmueble._ID + " integer primary key autoincrement, " +
                Contrato.TablaInmueble.LOCALIDAD + " text, " +
                Contrato.TablaInmueble.DIRECCION + " text, " +
                Contrato.TablaInmueble.TIPO + " integer," +
                Contrato.TablaInmueble.HABITACIONES + " integer," +
                Contrato.TablaInmueble.PRECIO + " float," +
                Contrato.TablaInmueble.SUBIDO + " integer default 0" +
                ")";
        db.execSQL(sql);

        sql = "create table " + Contrato.TablaFoto.TABLA + "( " +
                Contrato.TablaFoto._ID + " integer primary key autoincrement, " +
                Contrato.TablaFoto.ID_INMUEBLE + " integer, " +
                Contrato.TablaFoto.RUTA + " text, " + //no sé si aquí va coma o no
                " foreign key (" + Contrato.TablaFoto.ID_INMUEBLE + ") references " + Contrato.TablaInmueble.TABLA + "(" + Contrato.TablaInmueble._ID + ")" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}