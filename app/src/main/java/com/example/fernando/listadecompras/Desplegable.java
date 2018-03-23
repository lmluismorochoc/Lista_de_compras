package com.example.fernando.listadecompras;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fernando.listadecompras.adapter.ExpandableListAdapter;
import com.example.fernando.listadecompras.database.entities.ArticuloDB;
import com.example.fernando.listadecompras.database.entities.Detalle_ListaDB;
import com.example.fernando.listadecompras.database.entities.TiendaDB;
import com.example.fernando.listadecompras.database.model.Articulo;
import com.example.fernando.listadecompras.database.model.Tienda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * Created by Fernando on 22/3/2018.
 */

public class Desplegable extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    private Detalle_ListaDB detalle_listaDB;
    private TiendaDB tiendaDB;
    private ArticuloDB articuloDB;
    ImageView imagen;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //regresar...
                finish();
                overridePendingTransition(R.anim.right_in,R.anim.right_out);
            }
        });


        detalle_listaDB= new Detalle_ListaDB(this);
        tiendaDB= new TiendaDB(this);
        articuloDB=new ArticuloDB(this);
        imagen = (ImageView) findViewById(R.id.imageViewPos);
        Intent intent1 = getIntent();
         id = intent1.getStringExtra("idLista");


        listView = (ExpandableListView)findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);



//clicj para mostrar las rutas de las tiendas
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Tienda> tiendas=new ArrayList<>();
                detalle_listaDB.getAllItems(id);
                for ( int i = 0; i < detalle_listaDB.getAllItems(id).size(); i ++ ) {
                    Articulo a=detalle_listaDB.getAllItems(id).get(i).getIdArticulo();
                    if (!tiendas.contains(a.getIdTienda())){
                        tiendas.add(a.getIdTienda());
                    }
                }
                mostrarMapa(tiendas);
            }
        });

    }

    //Metodo que agrega todos los puntos
    private void mostrarMapa(ArrayList<Tienda> tiendas) {

        ////////////////////////////////////////////////////////////////////////////////////
        //esto va en lista de compras
        String jsonURL = "https://maps.google.com/maps?";
        final StringBuffer sBuf = new StringBuffer(jsonURL);
                sBuf.append("saddr=");
                sBuf.append(-4.031409);
                sBuf.append(',');
                sBuf.append(-79.199734);

Integer count = 0;
        for(Tienda cadaRuta:tiendas){
            //for(Tienda cadaRuta:rutaOptima){
            // Log.i("cantidadDeTiendas",cadaRuta.getName()+" - "+cadaRuta.getId()+" - "+cadaRuta.getLatitud());

            //lat.add(cadaRuta.getLatitud().toString());
            // lon.add(cadaRuta.getLongitud().toString());
            //nam.add(rutaOptima.indexOf(cadaRuta)+" - "+cadaRuta.getName());
if(count==0){

    sBuf.append("&daddr=");
    sBuf.append(cadaRuta.getLatitud());
    sBuf.append(',');
    sBuf.append(cadaRuta.getLongitud());
}else{
            sBuf.append("+to:");
            sBuf.append(cadaRuta.getLatitud());
            sBuf.append(',');
            sBuf.append(cadaRuta.getLongitud());}
            count++;
        }
        sBuf.append("&waypoints=optimize:true");

        //Aqui va a la aplicacion de maps
        //Uri gmmIntentUri = Uri.parse("geo:"+tiendaFocus.getLatitud()+","+tiendaFocus.getLongitud()+"?z=19("+tiendaFocus.getName()+")");
        Uri gmmIntentUri=Uri.parse(sBuf.toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        startActivity(mapIntent);
        ////////////////////////////////////////////////////////////////////////////////////
    }

    //inicializar los datos del xpnadable list
    private void initData() {

        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        for ( int i = 0; i < detalle_listaDB.getAllItems(id).size(); i ++ ) {
            Articulo a=detalle_listaDB.getAllItems(id).get(i).getIdArticulo();
            if (!listDataHeader.contains(a.getIdTienda().getName())){
                listDataHeader.add(a.getIdTienda().getName());
            }
        }

        for ( int i = 0; i < listDataHeader.size(); i ++ ) {
            Tienda t= tiendaDB.getTiendaByName(listDataHeader.get(i));
            listHash.put(listDataHeader.get(i),getArticulos(String.valueOf(t.getId())));
        }


    }

    //obtnienes los articulo por id
    private ArrayList<String> getArticulos(String ids){
        ArrayList<String> articulos=new ArrayList<>();
        for ( int i = 0; i < detalle_listaDB.getAllItems(id).size(); i ++ ) {
            Articulo a=detalle_listaDB.getAllItems(id).get(i).getIdArticulo();
            if (Objects.equals(String.valueOf(a.getIdTienda().getId()), ids)){
                articulos.add(a.getName());
            }
        }

        return articulos;
    }

    //metodo sobreescrito al presionar atras
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
            overridePendingTransition(R.anim.right_in,R.anim.right_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_desplegable, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editar) {
            Intent intent = new Intent(Desplegable.this, DetalleListaActivity.class);
            intent.putExtra("idLista",this.id);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);

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
