package com.example.josu.inmoprovider;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contrato {
    
    private Contrato(){
    }

    public static abstract  class TablaInmueble implements BaseColumns{
        public static final String TABLA = "inmueble";
        public static final String LOCALIDAD = "localidad";
        public static final String DIRECCION = "direccion";
        public static final String TIPO = "tipo";
        public static final String HABITACIONES = "habitaciones";
        public static final String PRECIO = "precio";
        public static final String SUBIDO = "subido";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.josu.inmueble";//Inmueble en vez de Inmuebles?
        public static final Uri CONTENT_URI =Uri.parse("content://" + Proveedor.AUTORIDAD + "/" + TABLA);

    }

    public static abstract  class TablaFoto implements BaseColumns{
        public static final String TABLA = "foto";
        public static final String ID_INMUEBLE = "idinmueble";
        public static final String RUTA = "ruta";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.josu.foto";
        public static final Uri CONTENT_URI =Uri.parse("content://" + Proveedor.AUTORIDAD + "/" + TABLA);
    }
}