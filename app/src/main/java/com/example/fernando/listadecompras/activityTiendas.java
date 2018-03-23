package com.example.fernando.listadecompras;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fernando.listadecompras.adapter.SimpleItemTouchHelperCallback;
import com.example.fernando.listadecompras.adapter.TiendaAdapter;
import com.example.fernando.listadecompras.database.entities.TiendaDB;
import com.example.fernando.listadecompras.database.model.Tienda;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class activityTiendas extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//creacion de variables
    private RecyclerView mListView;
    private AlertDialog dialog;
    private EditText editTextUbicacion;
    private EditText editTextnombre;
    private TiendaDB tiendaDB;
    private String nombreTienda="";
    private TiendaAdapter tiendaAdapter;
    private Double latitud;
    private boolean vieneDeMapa;
    private Double longitud;
    private ArrayList<Tienda> tiendas= new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);
        latitud = getIntent().getDoubleExtra("latitud",0.0);
        longitud = getIntent().getDoubleExtra("longitud",0.0);
        vieneDeMapa = getIntent().getBooleanExtra("map",false);
        nombreTienda = getIntent().getStringExtra("nombreTienda");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//inicializacion de vistas
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //regresar...
                finish();
            }
        });
//agregar onclick al boton flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                muestraDialogo();

            }
        });
        if(vieneDeMapa){
            muestraDialogo();
            vieneDeMapa=false;
        }
        LinearLayoutManager llmanager = new LinearLayoutManager(this);
        mListView = (RecyclerView) findViewById(R.id.listView);
        llmanager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(llmanager);

        // Init database shopping list
        tiendaDB = new TiendaDB(this);

        // Start with an empty database
        //tiendaDB.clearAllItems();

        // Insert items
        insertProducts();

        // Get all the items
        tiendas.addAll(tiendaDB.getAllItems());

        // Init adapter
        final TiendaAdapter tiendaAdapter= new TiendaAdapter(this, tiendas);

        tiendaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tienda tiendaFocus= tiendas.get(mListView.getChildAdapterPosition(v));



                Intent intent= new Intent(activityTiendas.this, MapsActivity.class);
                intent.putExtra("nombreTienda",nombreTienda);
                intent.putExtra("lat",tiendaFocus.getLatitud().doubleValue());
                intent.putExtra("lon",tiendaFocus.getLongitud().doubleValue());
                startActivity(intent);



            }





        });
        mListView.setAdapter(tiendaAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(tiendaAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mListView);

    }
    //mostrar dialogo para crear nueva tienda
    private void muestraDialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activityTiendas.this);

        LayoutInflater inflater = activityTiendas.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.layout_add_tienda, null);

        builder.setView(v);


        //editTextUbicacion= (EditText) v.findViewById(R.id.ubicacion);
        editTextnombre= (EditText) v.findViewById(R.id.nombre);
        if(nombreTienda!=null && !nombreTienda.equals("")){
            editTextnombre.setText(nombreTienda);
        }
        editTextnombre.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                nombreTienda=editTextnombre.getText().toString();
                return false;

            }
        });


        Button guardar =  v.findViewById(R.id.create_button);
        if(latitud!=0.0){
            guardar.setEnabled(true);
            guardar.setClickable(true);
            guardar.setBackgroundResource(android.R.color.holo_blue_dark);
            guardar.setTextColor(getResources().getColor(android.R.color.white));
        }
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombreTienda=editTextnombre.getText().toString();
                dialog.hide();
                insertarProducto();


            }
        });
        tiendaDB = new TiendaDB(activityTiendas.this);
        List<String> values = tiendaDB.getAllItemsTxt();
        ImageView imgMap = v.findViewById(R.id.imgMap);
        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(activityTiendas.this, MapsActivity.class);
                intent.putExtra("nombreTienda",nombreTienda);
                startActivity(intent);
                finish();
            }
        });

        dialog = builder.create();

        dialog.show();


        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //      .setAction("Action", null).show();
    }



    private void insertarProducto() {
        if(latitud!= 0.0 && nombreTienda!=null && !nombreTienda.equals("")){
            tiendaDB.insertElement(nombreTienda,String.valueOf(latitud),String.valueOf(longitud));
            //Toast.makeText(this,"OK nombretienda: "+nombreTienda+" latitud: "+latitud+ " longitud: "+longitud, //Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(this,"NOPE nombretienda: "+nombreTienda+" latitud: "+latitud+ " longitud: "+longitud, //Toast.LENGTH_SHORT).show();
        }
        tiendas.clear();

        tiendas.addAll(tiendaDB.getAllItems());


        // Init adapter
        TiendaAdapter tiendaAdapter= new TiendaAdapter(this, tiendas);
//        mListView.removeAllViews();
        mListView.setAdapter(tiendaAdapter);

        // tiendaDB.insertElement("Tia","100","100");
        // tiendaDB.insertElement("Farmacias EconÃ³micas","100","100");
        // tiendaDB.insertElement("Panaderia Agape ","100","100");

    }
    private void insertProducts() {
        // if(latitud!= 0.0){
        ///     //Toast.makeText(this,"latitud: "+latitud+ "longitud: "+longitud, //Toast.LENGTH_SHORT).show();
        // }
        // tiendaDB.insertElement("Zerimar Centro","100","100");
        // tiendaDB.insertElement("Tia","100","100");
        // tiendaDB.insertElement("Farmacias EconÃ³micas","100","100");
        // tiendaDB.insertElement("Panaderia Agape ","100","100");

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save in savedInstanceState.
        if(editTextnombre!=null) {
            savedInstanceState.putString("nombreTienda", editTextnombre.getText().toString());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state from the savedInstanceState.
        if(savedInstanceState != null) {
            nombreTienda = savedInstanceState.getString("nombreTienda");
            if(editTextnombre!=null) {
                editTextnombre.setText(nombreTienda);
            }
            // insertarProducto();
            if(nombreTienda!=null && !nombreTienda.equals("")){
                //insertarProducto();

            }
            //Toast.makeText(this,"nombre: "+nombreTienda+" latitud: "+latitud+ " longitud: "+longitud, //Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_tiendas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }
}
