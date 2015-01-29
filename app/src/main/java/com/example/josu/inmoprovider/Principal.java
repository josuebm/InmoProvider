package com.example.josu.inmoprovider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.view.MotionEvent;
import android.widget.CursorAdapter;
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


public class Principal extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    private Adaptador ad;
    private final int SECUNDARIA = 2;
    private final int ANADIR = 3;
    private final int EDITAR = 4;
    private ImageView ivFoto, iv;
    int posicion, contador;
    private ArrayList <Foto> fotos;
    private Loader loader;
    private GestorInmuebleProvider gip;
    private float punto = 0;
    private GestorFotoProvider gfp;
    private Inmueble inmuebleActual;
    private Cursor cur;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        fotos = new ArrayList();
        ivFoto = (ImageView)findViewById(R.id.ivFoto);

        gip = new GestorInmuebleProvider(this);
        gfp = new GestorFotoProvider(this);

        final ListView lv = (ListView) findViewById(R.id.listaInmuebles);
        final Context contexto = this;
        final DetalleListaInmuebles fragmentoDetalle = (DetalleListaInmuebles) getFragmentManager().findFragmentById(R.id.fragment4);
        final boolean horizontal = fragmentoDetalle != null && fragmentoDetalle.isInLayout();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = gip.getCursor();
                cursor.moveToPosition(position);
                Inmueble inmueble = GestorInmuebleProvider.getRow(cursor);
                cursor.close();

                if (horizontal) {
                    posicion = position;
                    contador = 0;
                    inmuebleActual = inmueble;
                    if(gfp.select(id) != null)
                        fotos = gfp.select(inmueble.getId());

                    cargarImagenes();
                } else {
                    Intent intent = new Intent(contexto, Secundaria.class);
                    intent.putExtra("id", inmueble.getId());
                    startActivityForResult(intent, SECUNDARIA);
                }
            }
        });
        registerForContextMenu(lv);
        ad = new Adaptador(this, null);
        lv.setAdapter(ad);
        loader = getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ANADIR || requestCode == EDITAR && resultCode == RESULT_OK){
            loader.onContentChanged();
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
        Cursor cursor = gip.getCursor();
        cursor.moveToPosition(index);
        Inmueble inmueble = GestorInmuebleProvider.getRow(cursor);
        cursor.close();
        if (id == R.id.action_eliminar) {
            gip.delete(inmueble);
            loader.onContentChanged();
            return true;
        }else if (id == R.id.action_editar) {
            Intent intent = new Intent(this, Anadir.class);
            intent.putExtra("inmueble", inmueble);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Contrato.TablaInmueble.CONTENT_URI;
        return new CursorLoader(
                this, uri, null, null, null,
                Contrato.TablaInmueble._ID +" collate localized asc");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ad.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ad.swapCursor(data);
    }

}