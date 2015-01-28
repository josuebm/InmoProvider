package com.example.josu.inmoprovider;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Josué on 04/12/2014.
 */
public class Inmueble implements Parcelable, Comparable<Inmueble>{

    long id;
    int habitaciones, subido, tipo;
    float precio;
    String localidad, direccion;


    public Inmueble() {
        this(0, 0, 0, 0, 0, null, null);
    }

    public Inmueble(long id, int habitaciones, int subido, int tipo, float precio, String localidad, String direccion) {
        this.id = id;
        this.habitaciones = habitaciones;
        this.subido = subido;
        this.tipo = tipo;
        this.precio = precio;
        this.localidad = localidad;
        this.direccion = direccion;
    }

    public Inmueble(int habitaciones, int subido, int tipo, float precio, String localidad, String direccion) {
        this.id = 0;
        this.habitaciones = habitaciones;
        this.subido = subido;
        this.tipo = tipo;
        this.precio = precio;
        this.localidad = localidad;
        this.direccion = direccion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(int habitaciones) {
        this.habitaciones = habitaciones;
    }

    public int getSubido() {
        return subido;
    }

    public void setSubido(int subido) {
        this.subido = subido;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "id=" + id +
                ", habitaciones=" + habitaciones +
                ", subido=" + subido +
                ", tipo=" + tipo +
                ", precio=" + precio +
                ", localidad='" + localidad + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
//long id, int habitaciones, int subido, int tipo, float precio, String localidad, String direccion
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeInt(this.habitaciones);
        parcel.writeInt(this.subido);
        parcel.writeInt(this.tipo);
        parcel.writeFloat(this.precio);
        parcel.writeString(this.localidad);
        parcel.writeString(this.direccion);
    }

    public Inmueble (Parcel p){
        this.id=p.readLong();
        this.habitaciones=p.readInt();
        this.subido=p.readInt();
        this.tipo = p.readInt();
        this.precio=p.readFloat();
        this.localidad = p.readString();
        this.direccion = p.readString();
    }

    public static final Creator <Inmueble> CREATOR =
            new Creator <Inmueble>() {
                @Override
                public Inmueble createFromParcel(Parcel parcel) {
                    return new Inmueble(parcel);
                }
                @Override
                public Inmueble[] newArray(int i) {
                    return new Inmueble[i];
                }
            };

    @Override
    public int compareTo(Inmueble another) {//NO SÉ SI AQUÍ TENGO QUE HACER ALGO
        long a = getId();
        long b = another.getId();
        if(a < b)
            return -1;
        else
        if(b > a)
            return 1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Inmueble.class != o.getClass()) return false;

        Inmueble inmueble = (Inmueble) o;

        if (id != inmueble.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
