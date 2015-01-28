package com.example.josu.inmoprovider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class Secundaria extends Activity {

    private ImageView iv;
    private int contador, posicion;
    private GestorFoto gf;
    private ArrayList<Foto> fotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contador = 0;
        fotos = new ArrayList();
        setContentView(R.layout.activity_secundaria);
        long id = (Long)getIntent().getExtras().get("id");
        gf = new GestorFoto(this);
        gf.open();
        if(gf.select(id) != null)
            fotos = gf.select(id);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.secundaria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
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
        if(fotos.size() > contador){
            Bitmap imagen = BitmapFactory.decodeFile(fotos.get(contador).getRuta());
            iv.setImageBitmap(imagen);
        }
    }

    public void cargarAnterior(View v){
        contador--;
        if(contador < 0)
            contador = fotos.size() -1;
        if(fotos.size() > contador){
            Bitmap imagen = BitmapFactory.decodeFile(fotos.get(contador).getRuta());
            iv.setImageBitmap(imagen);
        }
    }

    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
