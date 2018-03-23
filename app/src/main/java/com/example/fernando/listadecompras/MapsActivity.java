package com.example.fernando.listadecompras;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.fernando.listadecompras.database.model.Tienda;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String nombreTienda;

    //    private String [] lat;
//    private String [] lon;
    private String [] name;
    private Boolean editar;
    private Double lat;
    private Double lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //nombreTienda = getIntent().getStringExtra("nombreTienda");

//        lat = getIntent().getStringArrayExtra("lat");
//        lon = getIntent().getStringArrayExtra("lon");

        lat = getIntent().getDoubleExtra("lat",0.0);
        lon = getIntent().getDoubleExtra("lon",0.0);
        //editar = getIntent().getBooleanExtra("editar",false);

        name = getIntent().getStringArrayExtra("name");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.clear();
//        for(Integer i = 0; i<name.length;i++) {
//
//            LatLng marca = new LatLng(Double.valueOf(lat[i]), Double.valueOf(lon[i]));
//
//            mMap.addMarker(new MarkerOptions().position(marca).title(name[i]));
//
//
//        }
//
//        LatLng lojas = new LatLng(-3.9931300, -79.2042200);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(lojas));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lojas, 18.0f));

        if(lat==0.0 && lon ==0.0) {
            // Add a marker in Sydney and move the camera
            LatLng loja = new LatLng(-3.9931300, -79.2042200);

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Aqui"));
                    Intent intent = new Intent(MapsActivity.this, activityTiendas.class);
                    intent.putExtra("latitud", latLng.latitude);
                    intent.putExtra("longitud", latLng.longitude);
                    intent.putExtra("nombreTienda", nombreTienda);
                    intent.putExtra("map", true);
                    startActivity(intent);
                    finish();
                }
            });

            mMap.moveCamera(CameraUpdateFactory.newLatLng(loja));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loja, 15.0f));
        }else{
            LatLng marca = new LatLng(lat, lon);


            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(marca).title("Aqui"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marca));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marca, 18.0f));
        }
    }
}
