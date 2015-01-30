package com.example.josu.inmoprov;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Josué on 28/01/2015.
 */
public class Proveedor extends ContentProvider {

    private Ayudante abd;
    static String AUTORIDAD = "com.example.josu.inmoprov";
    private static final UriMatcher convierteUri2Int;
    private static final int INMUEBLES = 1;
    private static final int INMUEBLE_ID = 2;
    private static final int FOTOS = 4;
    private static final int FOTO_ID = 5;

    static {
        convierteUri2Int = new UriMatcher(UriMatcher.NO_MATCH);
        convierteUri2Int.addURI(AUTORIDAD, Contrato.TablaInmueble.TABLA, INMUEBLES);
        convierteUri2Int.addURI(AUTORIDAD, Contrato.TablaInmueble.TABLA + "/#", INMUEBLE_ID);
        convierteUri2Int.addURI(AUTORIDAD, Contrato.TablaFoto.TABLA, FOTOS);
        convierteUri2Int.addURI(AUTORIDAD, Contrato.TablaFoto.TABLA + "/#", FOTO_ID);
    }


    @Override
    public boolean onCreate() {
        abd = new Ayudante(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] proyeccion, String condicion, String[] parametros, String orderBy) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        if(convierteUri2Int.match(uri) == INMUEBLES || convierteUri2Int.match(uri) == INMUEBLE_ID)
            qb.setTables(Contrato.TablaInmueble.TABLA);
        else
            qb.setTables(Contrato.TablaFoto.TABLA);
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLES: break;
            case INMUEBLE_ID: condicion = condicion + "_id = " + uri.getLastPathSegment();
                break;
            case FOTOS: break;
            case FOTO_ID: condicion = condicion + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("URI " + uri);
        }
        SQLiteDatabase db = abd.getReadableDatabase();
        Cursor c = qb.query(db, proyeccion, condicion, parametros, null, null, orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLES:
                return Contrato.TablaInmueble.CONTENT_TYPE;
            case FOTOS:
                return Contrato.TablaFoto.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues valores) {
        Log.v("INSERT", uri.toString());
        Log.v("INSERT 2", convierteUri2Int.match(uri)+"");
        if (convierteUri2Int.match(uri) != INMUEBLES && convierteUri2Int.match(uri) != FOTOS) {
            throw new IllegalArgumentException("URI " + uri);
        }
        else if(convierteUri2Int.match(uri) == INMUEBLES) {
            SQLiteDatabase db = abd.getWritableDatabase();
            long id = db.insert(Contrato.TablaInmueble.TABLA, null, valores);
            if (id > 0) {
                Uri uriElemento = ContentUris.withAppendedId(Contrato.TablaInmueble.CONTENT_URI, id);
                getContext().getContentResolver().notifyChange(uriElemento, null);
                return uriElemento;
            }
        }else{
            SQLiteDatabase db = abd.getWritableDatabase();
            long id = db.insert(Contrato.TablaFoto.TABLA, null, valores);
            if (id > 0) {
                Uri uriElemento = ContentUris.withAppendedId(Contrato.TablaFoto.CONTENT_URI, id);
                getContext().getContentResolver().notifyChange(uriElemento, null);
                return uriElemento;
            }
        }
        throw new SQLException("Insert" + uri);
    }

    @Override
    public int delete(Uri uri, String condicion, String[] parametros) {
        SQLiteDatabase db = abd.getWritableDatabase();
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLES: break;
            case INMUEBLE_ID: condicion = condicion + "_id = " + uri.getLastPathSegment();
                break;
            case FOTOS: break;
            case FOTO_ID: condicion = condicion + "_id = " + uri.getLastPathSegment();
                break;
            default: throw new IllegalArgumentException("URI " + uri);
        }
        if(convierteUri2Int.match(uri) == INMUEBLES || convierteUri2Int.match(uri) == INMUEBLE_ID){
            int cuenta = db.delete(Contrato.TablaInmueble.TABLA, condicion, parametros);
            getContext().getContentResolver().notifyChange(uri, null);
            return cuenta;
        }else{
            int cuenta = db.delete(Contrato.TablaFoto.TABLA, condicion, parametros);
            getContext().getContentResolver().notifyChange(uri, null);
            return cuenta;
        }
    }

    @Override
    public int update(Uri uri, ContentValues valores, String condicion, String[] parametros) {
        SQLiteDatabase db = abd.getWritableDatabase();
        int cuenta;
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLES: cuenta = db.update(Contrato.TablaInmueble.TABLA, valores, condicion, parametros);
                break;
            case FOTOS: cuenta = db.update(Contrato.TablaFoto.TABLA, valores, condicion, parametros);
                break;
            default: throw new IllegalArgumentException("URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cuenta;
    }
}
