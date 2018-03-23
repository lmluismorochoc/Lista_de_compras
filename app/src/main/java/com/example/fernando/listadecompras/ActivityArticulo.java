package com.example.fernando.listadecompras;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.example.fernando.listadecompras.database.entities.ArticuloDB;
import com.example.fernando.listadecompras.database.entities.TiendaDB;
import com.example.fernando.listadecompras.database.model.Articulo;
import com.example.fernando.listadecompras.database.model.Tienda;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

public class ActivityArticulo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//definicion de variables
    private ListView mListView;
    private ArticuloDB articuloDB;
    private TiendaDB tiendaDB;
    private ArticuloAdapter articuloAdapter;
    private ArrayList<Articulo> articulos = new ArrayList<>();
    private AlertDialog dialog;
    private EditText editTextcodigo;
    private EditText editTextnombre;
    private EditText editTextprecio;
    private TextView txtTitulo;
    private Spinner spiner;
    private Button btnGuardar;
    private Button btnEditar;
    private ImageView leerCodigo;
    private boolean edit=false;
    private Articulo actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               crearDialog();


            }
        });


        mListView = (ListView) findViewById(R.id.listView);

        // Init database shopping list
        articuloDB = new ArticuloDB(this);


        // Get all the items
        articulos.addAll(articuloDB.getAllItems());

        // Init adapter
        articuloAdapter = new ArticuloAdapter(this, articulos);
        mListView.setAdapter(articuloAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                crearDialog();
                cargarDatos(articulos.get(i));
                actual=articulos.get(i);

            }
        });

    }

    //crear dialogo para agregar nuevo producto
    private void crearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityArticulo.this);

        LayoutInflater inflater = ActivityArticulo.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.layout_add_articulo, null);

        builder.setView(v);

        leerCodigo = (ImageView) v.findViewById(R.id.imgBarcode);
        editTextcodigo = (EditText) v.findViewById(R.id.codigo);
        editTextnombre = (EditText) v.findViewById(R.id.nombre);
        editTextprecio = (EditText) v.findViewById(R.id.precio);
        spiner = (Spinner) v.findViewById(R.id.spnTiendas);
        btnGuardar = (Button) v.findViewById(R.id.btnGuardarArticulo);
        btnEditar = (Button) v.findViewById(R.id.btnEditarArticulo);
        txtTitulo=(TextView) v.findViewById(R.id.txtTitulo);
        btnEditar.setVisibility(View.GONE);


        tiendaDB = new TiendaDB(ActivityArticulo.this);


        List<String> values = tiendaDB.getAllItemsTxt();


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityArticulo.this, android.R.layout.simple_spinner_item, values);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(dataAdapter);

        //iniciar scan de codigo de barras
        leerCodigo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Crear Cuenta...
                        initializeScan();
                    }
                }
        );

//iniciar guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtTitulo.getText().equals("Editar Producto")){
                    Tienda tienda = tiendaDB.getTiendaByName(spiner.getSelectedItem().toString());
                    Articulo a= actual;
                    a.setName(editTextnombre.getText().toString());
                    a.setCodigo(editTextcodigo.getText().toString());
                    a.setPrecio(Double.parseDouble(editTextprecio.getText().toString()));
                    a.setIdTienda(tienda);
                    articuloDB.updateItem(a);
                    dialog.dismiss();
                    updateList();
                    Toast.makeText(ActivityArticulo.this, "Producto Actualizado!", Toast.LENGTH_SHORT).show();

                }else{
                    String nombre= editTextnombre.getText().toString();
                    String precio= editTextprecio.getText().toString();

                    if (TextUtils.isEmpty(nombre)) {
                        editTextnombre.setError("Ingrese el nombre del producto");
                        editTextnombre.requestFocus();
                    }else{
                        if (TextUtils.isEmpty(precio)){
                            editTextprecio.setError("Ingrese el precio del producto");
                            editTextprecio.requestFocus();
                        }else{
                            Tienda tienda = tiendaDB.getTiendaByName(spiner.getSelectedItem().toString());
                            guardarArticulo(editTextnombre.getText().toString(), editTextcodigo.getText().toString(), editTextprecio.getText().toString(), String.valueOf(tienda.getId()));
                            dialog.dismiss();
                            updateList();
                            Toast.makeText(ActivityArticulo.this, "Producto Registrado!", Toast.LENGTH_SHORT).show();
                        }

                    }



                }
            }
        });


        dialog = builder.create();

        dialog.show();

    }

//al precionar boon atras
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.right_in,R.anim.right_out);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_comprar, menu);
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
            updateList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        return true;
    }

//inicializar esca
    private void initializeScan() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            callBarcodeScan();

        } else {
            solicitarPermisoCamara();
        }

    }
//abrir scan
    private void callBarcodeScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        List<String> target = new ArrayList<String>();
        target.add("com.example.fernando.listadecompras");
        integrator.setTargetApplications(target);
        integrator.initiateScan();
    }
//solicitar permiso de camara
    private void solicitarPermisoCamara() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                5);

    }
//resultados del scaner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 49374) {
            if (resultCode == RESULT_OK) {
                String contenido = data.getStringExtra("SCAN_RESULT");
                String formato = data.getStringExtra("SCAN_RESULT_FORMAT");
                // Hacer algo con los datos obtenidos.
                Articulo art = articuloDB.getItembyCodigo(contenido);
                if (art == null) {
                    editTextcodigo.setText(contenido);
                    editTextnombre.requestFocus();
                } else {
                    cargarDatos(art);
                    Toast.makeText(this, "El Producto ya se encuentra registrado", Toast.LENGTH_SHORT).show();
                }


            } else if (resultCode == RESULT_CANCELED) {
                // Si se cancelo la captura.
                Log.d("luisfer", "se cancelo");
            }
        } else {
            Log.d("luisfer", "no leyo");
        }
    }

    //busca posiciion en el spiner
    public int buscarPosicionSpiner(String nombre) {
        int pos = 0;
        int i = 0;
        String nomb;
        while (i < spiner.getCount()) {

            if (nombre.equals(spiner.getItemAtPosition(i).toString())) {
                pos = i;
                break;
            }
            i++;
        }


        return pos;
    }

    //guadar articulo
    public void guardarArticulo(String nombre, String codigo, String precio, String idTienda) {
        articuloDB.insertElement(nombre, codigo, precio, idTienda);
    }
//actualizar lista
    private void updateList() {
        // Get all the products
        articulos.clear();
        articulos.addAll(articuloDB.getAllItems());
        articuloAdapter.notifyDataSetChanged();
    }
//carga datos al dialog
    public void cargarDatos(Articulo art){
        editTextcodigo.setText(art.getCodigo());
        editTextnombre.setText(art.getName());
        editTextprecio.setText(art.getPrecio().toString());
        spiner.setSelection(buscarPosicionSpiner(art.getIdTienda().getName()));


        editTextprecio.setEnabled(false);
        editTextcodigo.setEnabled(false);
        editTextnombre.setEnabled(false);
        spiner.setEnabled(false);
        leerCodigo.setEnabled(false);
        btnGuardar.setEnabled(false);
        txtTitulo.setText("Producto");
        btnGuardar.setVisibility(View.GONE);
        btnEditar.setVisibility(View.VISIBLE);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextprecio.setEnabled(true);
                editTextcodigo.setEnabled(true);
                editTextnombre.setEnabled(true);
                spiner.setEnabled(true);
                btnGuardar.setEnabled(true);
                txtTitulo.setText("Editar Producto");
                btnGuardar.setVisibility(View.VISIBLE);
                edit=true;
                btnEditar.setVisibility(View.GONE);
                leerCodigo.setEnabled(true);
            }
        });

    }

}
