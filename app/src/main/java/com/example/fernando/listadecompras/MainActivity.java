package com.example.fernando.listadecompras;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.listadecompras.adapter.ArticuloAdapter;
import com.example.fernando.listadecompras.adapter.ListaAdapter;
import com.example.fernando.listadecompras.adapter.TiendaAdapter;
import com.example.fernando.listadecompras.database.entities.ArticuloDB;
import com.example.fernando.listadecompras.database.entities.ListaDB;
import com.example.fernando.listadecompras.database.entities.TiendaDB;
import com.example.fernando.listadecompras.database.model.Articulo;
import com.example.fernando.listadecompras.database.model.Lista;
import com.example.fernando.listadecompras.database.model.Tienda;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Definicion de variables
    EditText editTextNombre;
    Button btnGuardar;
    Button btnEditar;
    TextView txtTitulo;
    private AlertDialog dialog;
    private ListView mListView;
    private ListaDB listaDB;
    private ListaAdapter listaAdapter;
    private ArrayList<Lista> listas= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    //Se inician las vistas
        editTextNombre = (EditText) findViewById(R.id.editTextNombreLista);
        btnGuardar = (Button) findViewById(R.id.btnGuardarLista);
        btnEditar = (Button) findViewById(R.id.btnEditarLista);
        txtTitulo = (TextView) findViewById(R.id.txtTituloNuevaLista);

        //se define el onclick al boton flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: boton flotante agregar lista
                crearDialog();
            }
        });


        mListView = (ListView) findViewById(R.id.listViewListas);

        // inicia la base de datos
        listaDB = new ListaDB(this);

        // obtiene todos los items
        listas.addAll(listaDB.getAllItems());


        // inicia adaptador
        listaAdapter = new ListaAdapter(this, listas);
        mListView.setAdapter(listaAdapter);

        //define el click para cada item de la lista
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Desplegable.class);
                String id= String.valueOf(listas.get(i).getId());
                intent.putExtra("idLista",id);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });


        //Definicion del menu drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    //metodo sobreescrito al precionar el boton back

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Creacion del menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_tiendas, menu);
        return true;
    }

    //Definicion de las acciones del menu superior

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: opciones del menu superior
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_limpiar) {
            listaDB.clearAllItems();
            updateList();
        }

        return super.onOptionsItemSelected(item);
    }
//Acciones del menu drawer

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //TODO: acciones del menu lateral
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Listas) {
            // Handle the camera action
        } else if (id == R.id.nav_tiendas) {

            Intent intent = new Intent(this, activityTiendas.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);


        } else if (id == R.id.nav_articulos) {

            Intent intent = new Intent(this, ActivityArticulo.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Creacion del dialogo para crear nueva lista
    private void crearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.layout_lista_nueva, null);

        builder.setView(v);

        editTextNombre = (EditText) v.findViewById(R.id.editTextNombreLista);
        btnGuardar = (Button) v.findViewById(R.id.btnGuardarLista);
        btnEditar = (Button) v.findViewById(R.id.btnEditarLista);
        txtTitulo = (TextView) v.findViewById(R.id.txtTituloNuevaLista);
        btnEditar.setVisibility(View.GONE);


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtTitulo.getText().equals("Editar Lista")) {
//                    Tienda tienda = tiendaDB.getTiendaByName(spiner.getSelectedItem().toString());
//                    Articulo a= actual;
//                    a.setName(editTextnombre.getText().toString());
//                    a.setCodigo(editTextcodigo.getText().toString());
//                    a.setPrecio(Double.parseDouble(editTextprecio.getText().toString()));
//                    a.setIdTienda(tienda);
//                    articuloDB.updateItem(a);
//                    dialog.dismiss();
//                    updateList();
//                    Toast.makeText(ActivityArticulo.this, "Producto Actualizado!", Toast.LENGTH_SHORT).show();

                } else {
                    String nombre = editTextNombre.getText().toString();

                    if (TextUtils.isEmpty(nombre)) {
                        editTextNombre.setError("Ingrese el nombre de la Lista");
                        editTextNombre.requestFocus();
                    } else {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String date = sdf.format(new Date());
                        listaDB.insertElement(nombre,date);
                        dialog.dismiss();
                        updateList();
                        Toast.makeText(MainActivity.this, "Lista Registrada!", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    }


                }
            }
        });


        dialog = builder.create();

        dialog.show();
    }

    //actualiza la lista
    private void updateList() {
        // Get all the products
        listas.clear();
        listas.addAll(listaDB.getAllItems());
        listaAdapter.notifyDataSetChanged();
    }

}
