package com.example.josu.inmoprov;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class Principal extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final int SECUNDARIA = 2;
    private Adaptador ad;
    private ImageView ivFoto;
    int posicion, contador;
    private ArrayList <Foto> fotos;
    private GestorInmuebleProvider gip;
    private GestorFotoProvider gfp;
    private MenuItem menuItem;
    private String archivoASubir = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        getLoaderManager().initLoader(0, null, this);


        fotos = new ArrayList();
        ivFoto = (ImageView)findViewById(R.id.ivFoto);

        gip = new GestorInmuebleProvider(this);
        gfp = new GestorFotoProvider(this);

        final ListView lv = (ListView) findViewById(R.id.listaInmuebles);
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
                    if (gfp.select(id) != null)
                        fotos = gfp.select(inmueble.getId());
                    cargarImagenes();
                } else {
                    Intent intent = new Intent(Principal.this, Secundaria.class);
                    intent.putExtra("id", inmueble.getId());
                    startActivityForResult(intent, SECUNDARIA);
                }
            }
        });
        registerForContextMenu(lv);
        ad = new Adaptador(this, null);
        lv.setAdapter(ad);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        String usuario = getPreferenciasCompartidas();
        menuItem = menu.findItem(R.id.preferencias_compartidas);
        menuItem.setTitle(usuario);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_anadir) {
            Intent intent = new Intent(this, Anadir.class);
            intent.putExtra("accion", "anadir");
            startActivity(intent);
            return true;
        }else if(id == R.id.preferencias_compartidas){
            final EditText input = new EditText(this);
            input.setText(getPreferenciasCompartidas());
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.cambio_usuario))
                    .setView(input)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            setPreferenciasCompartidas(input.getText().toString());
                            String usuario = getPreferenciasCompartidas();
                            menuItem.setTitle(usuario);
                        }
                    })
                    .setIcon(getResources().getDrawable(R.drawable.ic_action_person))
                    .show();
            return true;
        }else if(id == R.id.action_sincronizar){
            ArrayList <Inmueble> lista = gip.select();
            boolean sincronizado = true;
            for(int i=0; i<lista.size(); i++){
                if(lista.get(i).getSubido() == 0){
                    sincronizado = false;
                    new SubirInmueble().execute(lista.get(i));
                }
            }
            if(sincronizado)
                tostada("Todos los inmuebles están sincronizados");
            else
                tostada("Se han sincronizado todos los inmuebles");
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
            int inmuebleEliminado = gip.delete(inmueble);
            eliminarFotos(gfp.select(inmueble.getId()));
            int fotoEliminada = gfp.delete(inmueble.getId());
            if(inmuebleEliminado == 1 && fotoEliminada == 1)
                tostada(getResources().getString(R.string.eliminar_todo));
            else if(inmuebleEliminado == 1)
                tostada(getResources().getString(R.string.eliminar_inmueble));
            else
                tostada(getResources().getString(R.string.eliminar_no));
            return true;
        }else if (id == R.id.action_editar) {
            Intent intent = new Intent(this, Anadir.class);
            intent.putExtra("inmueble", inmueble);
            intent.putExtra("accion", "editar");
            startActivity(intent);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
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
        else if(fotos.size() > contador){
            Bitmap imagen = BitmapFactory.decodeFile(fotos.get(contador).getRuta());
            ivFoto.setImageBitmap(imagen);
        }
    }

    public void cargarAnterior(View v){
        contador--;
        if(contador < 0)
            contador = fotos.size() -1;
        else if(fotos.size() > contador){
            Bitmap imagen = BitmapFactory.decodeFile(fotos.get(contador).getRuta());
            ivFoto.setImageBitmap(imagen);
        }
    }

    public void eliminarFotos(ArrayList <Foto> fotos){
        File file = null;
        for(int i=0; i<fotos.size(); i++){
            file= new File(fotos.get(i).getRuta());
            if(file.exists())
                file.delete();
        }
    }

    public String getPreferenciasCompartidas(){
        SharedPreferences pc;
        pc = getPreferences(Context.MODE_PRIVATE);
        String r = pc.getString("usuario", "Josué");
        return r;
    }

    public void setPreferenciasCompartidas(String usuario){
        SharedPreferences pc;
        pc = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pc.edit();
        ed.putString("usuario", usuario);
        ed.commit();
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

    class SubirInmueble extends AsyncTask<Inmueble, Integer, String> {

        Inmueble inmueble;

        public String postInmueble(String urlPeticion, Inmueble inmueble) throws IOException {
            HttpClient clienteHttp = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlPeticion);
            List<NameValuePair> pairs = new ArrayList();
            pairs.add(new BasicNameValuePair("idAndroid", String.valueOf(inmueble.getId())));
            pairs.add(new BasicNameValuePair("localidad", inmueble.getLocalidad()));
            pairs.add(new BasicNameValuePair("direccion", inmueble.getDireccion()));
            pairs.add(new BasicNameValuePair("tipo", String.valueOf(inmueble.getTipo())));
            pairs.add(new BasicNameValuePair("habitaciones", String.valueOf(inmueble.getHabitaciones())));
            pairs.add(new BasicNameValuePair("precio", String.valueOf(inmueble.getPrecio())));
            pairs.add(new BasicNameValuePair("usuario", getPreferenciasCompartidas()));
            post.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse respuestaHttp = clienteHttp.execute(post);
            String respuesta = EntityUtils.toString(respuestaHttp.getEntity());
            return respuesta;
        }

        String url = "http://192.168.1.102:8080/inmobiliaria/control?target=inmueble&op=insert&action=op";

        @Override
        protected String doInBackground(Inmueble... params) {
            inmueble = params[0];
            String r = null;
            try {
                r = postInmueble(url, inmueble);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            inmueble.setSubido(1);
            Log.v("Sincronizado", inmueble.getId()+"");
            gip.update(inmueble);
            s = s.substring(s.lastIndexOf("<tr>"), s.lastIndexOf("</tr>"));
            s = s.substring(s.indexOf("<td>"), s.indexOf("</td>"));
            s = s.substring(s.indexOf(">")+1);

            if (gfp.select(inmueble.getId()) != null){
                fotos = gfp.select(inmueble.getId());
                for(int i=0; i<fotos.size(); i++){
                    archivoASubir = fotos.get(i).getRuta();
                    new SubirFoto().execute(s);
                }

            }

        }
    }

    class SubirFoto extends AsyncTask<String, Integer, String> {

        public String postFile(String urlPeticion, String nombreParametro, String uriArchivo) {
            //urlPeticion es la URL de envío
            //nombreParametro es el name del input del html
            //nombreArchivo es el uri.getPath()
            String resultado="";
            int status=0;
            try {
                URL url = new URL(urlPeticion);
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setDoOutput(true);
                conexion.setRequestMethod("POST");
                FileBody fileBody = new FileBody(new File(uriArchivo));
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
                multipartEntity.addPart(nombreParametro, fileBody);
                conexion.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());
                OutputStream out = conexion.getOutputStream();
                try {
                    multipartEntity.writeTo(out);
                }catch(Exception e){
                    return e.toString();
                }finally {
                    out.close();
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String decodedString;
                while ((decodedString = in.readLine()) != null) {
                    resultado+=decodedString+"\n";
                }
                in.close();
                status = conexion.getResponseCode();
            } catch (MalformedURLException ex) {
                return ex.toString();
            } catch (IOException ex) {
                return ex.toString();
            }
            return resultado+"\n"+status;
        }

        String url = "http://192.168.1.102:8080/inmobiliaria/control?target=foto&op=insert&action=op";

        @Override
        protected String doInBackground(String... params) {
            url += "&idinmueble=" + params[0];
            String r = postFile(url, "file", archivoASubir);
            //Toast.makeText(Principal.this, "entra en doInBackground", Toast.LENGTH_LONG).show();
            return r;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Principal.this, "hecho", Toast.LENGTH_SHORT).show();
        }
    }

}
