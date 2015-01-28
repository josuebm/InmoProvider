package com.example.josu.inmoprovider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class Principal extends Activity {

    //private ListView listaInmuebles;
    private ArrayList<Inmueble> lista;
    private Adaptador ad;
    private final int SECUNDARIA = 2;
    private final int ANADIR = 3;
    private final int EDITAR = 4;
    private ImageView ivFoto;
    int posicion, contador;
    private GestorInmueble gi;
    private GestorFoto gf;
    private ArrayList <Foto> fotos;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        fotos = new ArrayList();
        ivFoto = (ImageView)findViewById(R.id.ivFoto);
        gi = new GestorInmueble(this);
        final ListView lv = (ListView) findViewById(R.id.listaInmuebles);
        final Context contexto = this;
        final DetalleListaInmuebles fragmentoDetalle = (DetalleListaInmuebles) getFragmentManager().findFragmentById(R.id.fragment4);
        final boolean horizontal = fragmentoDetalle != null && fragmentoDetalle.isInLayout();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = gi.getCursor();
                cursor.moveToPosition(position);
                Inmueble inmueble = GestorInmueble.getRow(cursor);
                cursor.close();

                if (horizontal) {
                    posicion = position;
                    contador = 0;

                    gf = new GestorFoto(contexto);
                    gf.open();
                    if(gf.select(id) != null)
                        fotos = gf.select(inmueble.getId());

                    cargarImagenes();
                } else {
                    Intent intent = new Intent(contexto, Secundaria.class);
                    gi.open();
                    cursor.close();
                    intent.putExtra("id", inmueble.getId());
                    startActivityForResult(intent, SECUNDARIA);
                }
            }
        });

        gi.open();
        Cursor c = gi.getCursor();
        ad = new Adaptador(this, c);
        lv.setAdapter(ad);

        registerForContextMenu(lv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ANADIR || requestCode == EDITAR && resultCode == RESULT_OK){
            Cursor cursor = gi.getCursor();
            ad.changeCursor(cursor);
        }
        else
        if (requestCode == SECUNDARIA && resultCode == RESULT_OK) {
            posicion = (Integer)data.getExtras().get("posicion");
            contador = (Integer) data.getExtras().get("contador");
            final DetalleListaInmuebles fragmentoDetalle = (DetalleListaInmuebles) getFragmentManager().findFragmentById(R.id.fragment4);
            cargarImagenes();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_anadir) {
            Intent intent = new Intent(this, Anadir.class);
            intent.putExtra("accion", "anadir");
            startActivityForResult(intent, ANADIR);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.longclick_principal, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index= info.position;
        Cursor cursor = gi.getCursor();
        cursor.moveToPosition(index);
        Inmueble in = GestorInmueble.getRow(cursor);
        if (id == R.id.action_eliminar) {
            gi.delete(in);
            cursor.close();
            cursor = gi.getCursor();
            ad.changeCursor(cursor);
            return true;
        }else if (id == R.id.action_editar) {
            Intent intent = new Intent(this, Anadir.class);
            intent.putExtra("posicion", index);
            intent.putExtra("accion", "editar");
            startActivityForResult(intent, EDITAR);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    public void cargarImagenes(){
        if(!fotos.isEmpty()){
            Bitmap imagen = BitmapFactory.decodeFile(fotos.get(contador).getRuta());
            ivFoto.setImageBitmap(imagen);
        }
        else
            ivFoto.setImageResource(R.drawable.no_image_available);
    }

    public void cargarSiguiente(View v){
        contador++;
        if(fotos.size() == contador)
            contador = 0;
        if(fotos.size() > contador){
            Bitmap imagen = BitmapFactory.decodeFile(fotos.get(contador).getRuta());
            ivFoto.setImageBitmap(imagen);
        }
    }

    public void cargarAnterior(View v){
        contador--;
        if(contador < 0)
            contador = fotos.size() -1;
        if(fotos.size() > contador){
            Bitmap imagen = BitmapFactory.decodeFile(fotos.get(contador).getRuta());
            ivFoto.setImageBitmap(imagen);
        }
    }
}