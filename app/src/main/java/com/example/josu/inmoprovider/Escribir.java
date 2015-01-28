package com.example.josu.inmoprovider;

import android.content.Context;
import android.os.Environment;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Josué on 05/12/2014.
 */
public class Escribir {

    private ArrayList<Inmueble> lista;
    private Context contexto;

    Escribir(ArrayList<Inmueble> lista, Context contexto){
        this.lista = lista;
        this.contexto = contexto;
    }
/*
    public void escribir() {
        FileOutputStream fos = null;
        XmlSerializer doc = android.util.Xml.newSerializer();
        if (isModificable()) {
            try {
                fos = new FileOutputStream(new File(contexto.getExternalFilesDir(null), "archivo.xml"));
                doc.setOutput(fos, "UTF-8");
                doc.startDocument(null, Boolean.valueOf(true));
                doc.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                doc.startTag(null, "inmuebles");
                for(int i=0; i<lista.size(); i++){
                    doc.startTag(null, "inmueble");
                    doc.attribute(null, "id", "id" + lista.get(i).getId());
                    doc.attribute(null, "localidad", lista.get(i).getLocalidad());
                    doc.attribute(null, "direccion", lista.get(i).getDireccion());
                    doc.attribute(null, "tipo", "tipo" + lista.get(i).getTipo());
                    doc.attribute(null, "habitaciones", "hab" + (lista.get(i).getHabitaciones()));
                    doc.attribute(null, "precio", "precio" + lista.get(i).getPrecio());
                    if(lista.get(i).getFotos() != null)
                        for(int j=0; j<lista.get(i).getFotos().size(); j++){
                            doc.startTag(null, "foto");
                            doc.attribute(null, "ruta", lista.get(i).getFotos().get(j));
                            doc.endTag(null, "foto");
                        }
                    doc.endTag(null, "inmueble");
                }
                doc.endDocument();
                doc.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isModificable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }*/
}
