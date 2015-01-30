package com.example.josu.inmoprov;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;


public class Secundaria extends Activity {

    private ImageView iv;
    private int contador, posicion;
    private ArrayList<Foto> fotos;
    private GestorFotoProvider gfp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gfp = new GestorFotoProvider(this);
        contador = 0;
        fotos = new ArrayList();
        setContentView(R.layout.activity_secundaria);
        long id = (Long)getIntent().getExtras().get("id");
        if(gfp.select(id) != null)
            fotos = gfp.select(id);
        iv = (ImageView)findViewById(R.id.ivFoto);
        cargarImagenes();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Intent i = new Intent();
        i.putExtra("posicion", posicion);
        i.putExtra("contador", contador);
        setResult(RESULT_OK, i);
        finish();
    }

    public void cargarImagenes(){
        if(!fotos.isEmpty()){
            Bitmap imagen = BitmapFactory.decodeFile(fotos.get(contador).getRuta());
            iv.setImageBitmap(imagen);
            contador++;
        }
    }

    public void cargarSiguiente(View v){
        contador++;
        if(fotos.size() == contador)
            contador = 0;
        else if(fotos.size() > contador){
            Bitmap imagen = BitmapFactory.decodeFile(fotos.get(contador).getRuta());
            iv.setImageBitmap(imagen);
        }
    }

    public void cargarAnterior(View v){
        contador--;
        if(contador < 0)
            contador = fotos.size() -1;
        else if(fotos.size() > contador){
            Bitmap imagen = BitmapFactory.decodeFile(fotos.get(contador).getRuta());
            iv.setImageBitmap(imagen);
        }
    }

    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
