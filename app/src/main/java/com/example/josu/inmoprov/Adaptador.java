package com.example.josu.inmoprov;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Josué on 04/12/2014.
 */
public class Adaptador extends CursorAdapter {

    Context contexto;

    public Adaptador (Context context, Cursor c) {
        super(context, c, true);
        contexto = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.detalle, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1, tv2, tv3, tv4;
        ImageView iv;
        tv1 = (TextView)view.findViewById(R.id.tvTipo);
        tv2 = (TextView)view.findViewById(R.id.tvDireccion);
        tv3 = (TextView)view.findViewById(R.id.tvLocalidad);
        tv4 = (TextView)view.findViewById(R.id.tvPrecio);
        iv = (ImageView)view.findViewById(R.id.iv);
        Inmueble inmueble = GestorInmuebleProvider.getRow(cursor);
        tv1.setText(tipo(inmueble));
        tv2.setText(inmueble.getDireccion());
        tv3.setText(inmueble.getLocalidad());
        tv4.setText((int)(inmueble.getPrecio()) + " €");
        switch(inmueble.getHabitaciones()){
            case 0:{
                if(inmueble.getSubido()==0)
                    iv.setImageResource(R.drawable.una);
                else
                    iv.setImageResource(R.drawable.una_check);
            }break;
            case 1:{
                if(inmueble.getSubido()==0)
                    iv.setImageResource(R.drawable.dos);
                else
                    iv.setImageResource(R.drawable.dos_check);
            }break;
            case 2:{
                if(inmueble.getSubido()==0)
                    iv.setImageResource(R.drawable.tres);
                else
                    iv.setImageResource(R.drawable.tres_check);
            }break;
            case 3:{
                if(inmueble.getSubido()==0)
                    iv.setImageResource(R.drawable.cuatro);
                else
                    iv.setImageResource(R.drawable.cuatro_check);
            }break;
            case 4:{
                if(inmueble.getSubido()==0)
                    iv.setImageResource(R.drawable.cinco);
                else
                    iv.setImageResource(R.drawable.cinco_check);
            }
        }
    }

    public String tipo (Inmueble inmueble){
        ArrayList <String> aux = new ArrayList (Arrays.asList(contexto.getResources().getStringArray(R.array.tipos)));
        return aux.get(inmueble.getTipo());
    }
}