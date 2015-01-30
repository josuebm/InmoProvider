package com.example.josu.inmoprov;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Anadir extends Activity {

    private static final int FOTO = 1;
    private EditText etLocalidad, etDireccion, etPrecio;
    private Spinner spHabitaciones, spTipo;
    private Button boton;
    private String accion;
    private int numFotos;
    private TextView tvFotos;
    private GestorInmuebleProvider gip;
    private Inmueble inmuebleActual;
    private GestorFotoProvider gfp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir);

        gip = new GestorInmuebleProvider(this);
        gfp = new GestorFotoProvider(this);

        accion = (String)getIntent().getExtras().get("accion");
        numFotos = 0;
        initComponents();

        if(accion.equals("editar")){
            inmuebleActual = (Inmueble)getIntent().getExtras().get("inmueble");
            etLocalidad.setText(inmuebleActual.getLocalidad());
            etDireccion.setText(inmuebleActual.getDireccion());
            etPrecio.setText(inmuebleActual.getPrecio() + " €");
            spHabitaciones.setSelection(inmuebleActual.getHabitaciones());
            spTipo.setSelection(inmuebleActual.getTipo());
            boton.setText(getResources().getString(R.string.action_editar));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(accion.equals("editar")){
            getMenuInflater().inflate(R.menu.anadir, menu);
            setTitle(getResources().getString(R.string.title_activity_editar));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camara) {
            camara();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void anadir(View v){
        if(!accion.equals("editar")){
            Inmueble inmueble = new Inmueble(spHabitaciones.getSelectedItemPosition(), 0, spTipo.getSelectedItemPosition(), Float.valueOf(etPrecio.getText().toString()), etLocalidad.getText().toString(), etDireccion.getText().toString());
            gip.insert(inmueble);
            tostada(getResources().getString(R.string.anadir_ok));
        } else{
            inmuebleActual.setLocalidad(etLocalidad.getText().toString());
            inmuebleActual.setDireccion(etDireccion.getText().toString());
            inmuebleActual.setTipo(spTipo.getSelectedItemPosition());
            inmuebleActual.setHabitaciones(spHabitaciones.getSelectedItemPosition());
            if(etPrecio.getText().toString().endsWith("€"))
                etPrecio.setText(etPrecio.getText().toString().replace("€", ""));
            inmuebleActual.setPrecio(Float.valueOf(etPrecio.getText().toString()));
            int actualizado = gip.update(inmuebleActual);
            if(actualizado == 1)
                tostada(getResources().getString(R.string.actualizar_ok));
            else
                tostada(getResources().getString(R.string.actualizar_no));
        }
        finish();
    }

    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    public void camara(){
        Intent i = new Intent ("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(i, FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FOTO && resultCode == RESULT_OK){
            Bitmap foto = data.getParcelableExtra("data");
            try {
                guardarFoto(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void guardarFoto(Bitmap foto) throws IOException {
        FileOutputStream fos = null;
        String nombre = nombreFoto();
        File archivo = null;
        if(espacioSuficiente(getExternalFilesDir(Environment.DIRECTORY_DCIM))){
            archivo = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), nombre);
            fos = new FileOutputStream(archivo);
            fos.flush();
            foto.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            numFotos++;
            if (tvFotos.getVisibility() == View.INVISIBLE)
                tvFotos.setVisibility(View.VISIBLE);
            tvFotos.setText(numFotos + " " + getResources().getString(R.string.fotos_anadidas));
            Foto f = new Foto(inmuebleActual.getId(), archivo.getAbsolutePath());
            gfp.insert(f);
        }
    }

    public boolean espacioSuficiente(File f) {
        double eTotal, eDisponible, porcentaje;
        eTotal = (double) f.getTotalSpace();
        eDisponible = (double) f.getFreeSpace();
        porcentaje = (eDisponible / eTotal) * 100;
        return porcentaje > 10;
    }

    public String nombreFoto(){
        GregorianCalendar fecha = new GregorianCalendar();
        String nombre;
        String year = String.valueOf(fecha.get(Calendar.YEAR));
        String mes = String.valueOf(fecha.get(Calendar.MONTH));
        if(mes.length()==1)
            mes = "0" + mes;
        String dia = String.valueOf(fecha.get(Calendar.DAY_OF_MONTH));
        if(dia.length() == 1)
            dia = "0" + dia;
        String hora = String.valueOf(fecha.get(Calendar.HOUR_OF_DAY));
        if(hora.length() == 1)
            hora = "0" + hora;
        String minuto = String.valueOf(fecha.get(Calendar.MINUTE));
        if(minuto.length() == 1)
            minuto = "0" + minuto;
        String segundo = String.valueOf(fecha.get(Calendar.SECOND));
        if(segundo.length() == 1)
            segundo = "0" + segundo;
        nombre = "Inmueble_" + inmuebleActual.getId() + "_" + year + "_" + mes + "_" + dia + "_" + hora + "_" + minuto + "_" + segundo + ".jpg";
        return nombre;

    }

    public void initComponents(){
        etLocalidad = (EditText)findViewById(R.id.etLocalidad);
        etDireccion = (EditText)findViewById(R.id.etDireccion);
        etPrecio = (EditText)findViewById(R.id.etPrecio);
        spHabitaciones = (Spinner)findViewById(R.id.spHabitaciones);
        ArrayAdapter<CharSequence> adapterHabitaciones = ArrayAdapter.createFromResource(this, R.array.habitaciones, android.R.layout.simple_spinner_item);
        adapterHabitaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHabitaciones.setAdapter(adapterHabitaciones);
        spTipo = (Spinner)findViewById(R.id.spTipo);
        ArrayAdapter<CharSequence> adapterTipos = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item);
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapterTipos);
        boton = (Button)findViewById(R.id.btAnadir);
        tvFotos = (TextView)findViewById(R.id.tvFoto);
        tvFotos.setVisibility(View.INVISIBLE);
    }
}
