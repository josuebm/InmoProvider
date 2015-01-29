package com.example.josu.inmoprovider;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.josu.inmoprovider.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DetalleListaInmuebles extends Fragment {

    private ImageView iv;

    private View v;

    public DetalleListaInmuebles() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_lista_inmuebles, container, false);
    }
}
