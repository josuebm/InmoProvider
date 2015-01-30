package com.example.josu.inmoprov;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Josué on 27/01/2015.
 */
public class Foto implements Parcelable {

    long id, idInmueble;
    String ruta;

    public Foto() {
        this(0, 0, null);
    }

    public Foto(long id, long idInmueble, String ruta) {
        this.id = id;
        this.idInmueble = idInmueble;
        this.ruta = ruta;
    }

    public Foto(long idInmueble, String ruta) {
        this.id = 0;
        this.idInmueble = idInmueble;
        this.ruta = ruta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(long idInmueble) {
        this.idInmueble = idInmueble;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public String toString() {
        return "Foto{" +
                "id=" + id +
                ", idInmueble=" + idInmueble +
                ", ruta='" + ruta + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeLong(this.idInmueble);
        parcel.writeString(this.ruta);
    }

    public Foto(Parcel p){
        this.id = p.readLong();
        this.idInmueble = p.readLong();
        this.ruta = p.readString();
    }

    public static final Creator <Foto> CREATOR =
            new Creator <Foto>() {
                @Override
                public Foto createFromParcel(Parcel parcel) {
                    return new Foto(parcel);
                }
                @Override
                public Foto[] newArray(int i) {
                    return new Foto[i];
                }
            };

}
