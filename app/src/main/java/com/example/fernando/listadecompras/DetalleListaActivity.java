package com.example.fernando.listadecompras;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.listadecompras.adapter.ArticuloAdapter;
import com.example.fernando.listadecompras.adapter.DetalleListaAdapter;
import com.example.fernando.listadecompras.database.entities.ArticuloDB;
import com.example.fernando.listadecompras.database.entities.Detalle_ListaDB;
import com.example.fernando.listadecompras.database.entities.ListaDB;
import com.example.fernando.listadecompras.database.entities.TiendaDB;
import com.example.fernando.listadecompras.database.model.Articulo;
import com.example.fernando.listadecompras.database.model.Detalle_Lista;
import com.example.fernando.listadecompras.database.model.Lista;
import com.example.fernando.listadecompras.database.model.Tienda;
import com.google.zxing.integration.android.IntentIntegrator;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class DetalleListaActivity extends AppCompatActivity {

    //definicion de variables
    private TextView textViewNombre;
    private TextView textViewFecha;
    private ListaDB listaDB;
    private ImageView btnBarcode;
    private ImageView btnAgrgar;
    private ListView mListView;
    private ArticuloDB articuloDB;
    private TiendaDB tiendaDB;
    private Detalle_ListaDB detalle_listaDB;
    private ArrayList<Detalle_Lista> articulos_lista = new ArrayList<>();
    private DetalleListaAdapter detalleListaAdapter;
    private AutoCompleteTextView textView;
    private EditText editTextCantidad;
    private Spinner spnUnidad;
    private Button btnGuardarCambios;

    private TextView resumen;
    private Lista l;
    private Detalle_Lista actual;
    private AlertDialog dialog;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_lista);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //inicializacion de variables
        textViewNombre = (TextView) findViewById(R.id.detalla_lista_nombre);

        textViewFecha = (TextView) findViewById(R.id.detalle_lista_fecha);
        btnBarcode = (ImageView) findViewById(R.id.imgBarcodeBuscar);
        btnAgrgar = (ImageView) findViewById(R.id.imgadd);
        resumen=(TextView) findViewById(R.id.detalle_lista_resumen);
        listaDB = new ListaDB(this);
        articuloDB = new ArticuloDB(this);
        detalle_listaDB = new Detalle_ListaDB(this);
        Intent intent1 = getIntent();
        id = intent1.getStringExtra("idLista");
        l = listaDB.getLista(id);
        textViewNombre.setText(l.getNombre());
        textViewFecha.setText(l.getFechaCreacion());

        //definicion del clic ala imagen de barcode
        btnBarcode.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Crear Cuenta...
                        initializeScan();
                    }
                }
        );
        //definicion del clic al boton agregar
        btnAgrgar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Articulo art = articuloDB.getItembyNombre(textView.getText().toString());
                        if (art == null) {
                            crearDialog();
                        } else {
                            detalle_listaDB.insertElement(String.valueOf(l.getId()), String.valueOf(art.getId()), "1", "u");
                            updateList();
                            textView.setText("");
                        }

                    }
                }
        );
        mListView = (ListView) findViewById(R.id.listview_detalle_lista);

        // Get all the items
        articulos_lista.addAll(detalle_listaDB.getAllItems(String.valueOf(l.getId())));

        detalleListaAdapter = new DetalleListaAdapter(this, articulos_lista);
        mListView.setAdapter(detalleListaAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //regresar...
                finish();
                Intent intent = new Intent(DetalleListaActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.right_out);

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                actual = articulos_lista.get(i);
                crearDialogo();

            }
        });
        inicializarBucador();

        //adaptar al uto complete edit TExt
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, articuloDB.getAllItemsNombre());
        textView = (AutoCompleteTextView)
                findViewById(R.id.txtBuscarArticulo);
        textView.setAdapter(adapter);

        resumen.setText("Productos en la lista: "+articulos_lista.size()+"                Total: "+calcularTotal());
        textViewNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalleListaActivity.this, Desplegable.class);
                intent.putExtra("idLista",id);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });
    }

//creacion de dialogo para crear nuevo producto
    private void crearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.layout_add_articulo, null);

        builder.setView(v);

        ImageView leerCodigo = (ImageView) v.findViewById(R.id.imgBarcode);
        final EditText editTextcodigo = (EditText) v.findViewById(R.id.codigo);
        final EditText editTextnombre = (EditText) v.findViewById(R.id.nombre);
        editTextnombre.setText(textView.getText());
        final EditText editTextprecio = (EditText) v.findViewById(R.id.precio);
        final Spinner spiner = (Spinner) v.findViewById(R.id.spnTiendas);
        Button btnGuardar = (Button) v.findViewById(R.id.btnGuardarArticulo);
        Button btnEditar = (Button) v.findViewById(R.id.btnEditarArticulo);
        final TextView txtTitulo = (TextView) v.findViewById(R.id.txtTitulo);
        btnEditar.setVisibility(View.GONE);
        leerCodigo.setVisibility(View.GONE);
        editTextcodigo.setVisibility(View.GONE);
        editTextprecio.requestFocus();
        tiendaDB = new TiendaDB(this);


        List<String> values = tiendaDB.getAllItemsTxt();


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(dataAdapter);


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String nombre = editTextnombre.getText().toString();
                String precio = editTextprecio.getText().toString();

                if (TextUtils.isEmpty(nombre)) {
                    editTextnombre.setError("Ingrese el nombre del producto");
                    editTextnombre.requestFocus();
                } else {
                    if (TextUtils.isEmpty(precio)) {
                        editTextprecio.setError("Ingrese el precio del producto");
                        editTextprecio.requestFocus();
                    } else {
                        Tienda tienda = tiendaDB.getTiendaByName(spiner.getSelectedItem().toString());
                        articuloDB.insertElement(editTextnombre.getText().toString(), editTextcodigo.getText().toString(), editTextprecio.getText().toString(), String.valueOf(tienda.getId()));
                        dialog.dismiss();
                        Toast.makeText(DetalleListaActivity.this, "Producto Registrado!", Toast.LENGTH_SHORT).show();
                        Articulo art=articuloDB.getItembyNombre(editTextnombre.getText().toString());
                        detalle_listaDB.insertElement(String.valueOf(l.getId()), String.valueOf(art.getId()), "1", "u");
                        updateList();
                        textView.setText("");
                    }

                }


            }
        });


        dialog = builder.create();

        dialog.show();

    }

    //inicializar buscador
    private void inicializarBucador() {

        AutoCompleteTextView textFind = (AutoCompleteTextView) findViewById(R.id.txtBuscarArticulo);
        textFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ImageView imgBarcode = (ImageView) findViewById(R.id.imgBarcodeBuscar);
                ImageView imgAdd = (ImageView) findViewById(R.id.imgadd);


                if (charSequence != null && charSequence.length() > 0) {
                    imgBarcode.setVisibility(View.GONE);
                    imgAdd.setVisibility(View.VISIBLE);
                } else {
                    imgBarcode.setVisibility(View.VISIBLE);
                    imgAdd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //creacion de dialogo para editar cantidad y unida de medida
    private TextView titulo;
    private void crearDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.layout_editar_datos_pro, null);

        builder.setView(v);

         titulo=(TextView)v.findViewById(R.id.txtTituloEditar);
        titulo.setText("Editar " + actual.getIdArticulo().getName());
        editTextCantidad = (EditText) v.findViewById(R.id.editTextCantidadEditar);
        spnUnidad = (Spinner) v.findViewById(R.id.spinnerEditar);
        btnGuardarCambios = (Button) v.findViewById(R.id.btnGuardarCambios);


        List<String> values = new ArrayList<>();
        values.add("u");
        values.add("kg");
        values.add("lb");
        values.add("l");
        values.add("m");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUnidad.setAdapter(dataAdapter);


        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cantidad = editTextCantidad.getText().toString();
                String unidad = spnUnidad.getSelectedItem().toString();

                if (TextUtils.isEmpty(cantidad)) {
                    editTextCantidad.setError("Ingrese una Cantidad");
                    editTextCantidad.requestFocus();
                } else {
                    Detalle_Lista a = actual;
                    a.setCantidad(Integer.parseInt(cantidad));
                    a.setUnidadMaedida(unidad);
                    detalle_listaDB.updateItem(a);
                    updateList();
                    Toast.makeText(DetalleListaActivity.this, "Producto Actualizado!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });


        dialog = builder.create();

        dialog.show();
    }

    // metodo para inicalizar el scan
    private void initializeScan() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            callBarcodeScan();

        } else {
            solicitarPermisoCamara();
        }

    }

    //abrir barcode
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

    //se obtiene los resultados de el scaner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 49374) {
            if (resultCode == RESULT_OK) {
                String contenido = data.getStringExtra("SCAN_RESULT");
                String formato = data.getStringExtra("SCAN_RESULT_FORMAT");
                // Hacer algo con los datos obtenidos.
                Articulo art = articuloDB.getItembyCodigo(contenido);
                if (art == null) {
                    Toast.makeText(this, "El Producto no esta registrado", Toast.LENGTH_SHORT).show();
                } else {
                    detalle_listaDB.insertElement(String.valueOf(l.getId()), String.valueOf(art.getId()), "1", "u");
                    updateList();
                }


            } else if (resultCode == RESULT_CANCELED) {
                // Si se cancelo la captura.
                Log.d("luisfer", "se cancelo");
            }
        } else {
            Log.d("luisfer", "no leyo");
        }
    }
//Metodo sobreescrito al precionar el boton back

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }
//Actualiza la lista
    private void updateList() {
        // Get all the products
        articulos_lista.clear();
        articulos_lista.addAll(detalle_listaDB.getAllItems(String.valueOf(l.getId())));
        detalleListaAdapter.notifyDataSetChanged();
        resumen.setText("Productos en la lista: "+articulos_lista.size()+"                Total: "+calcularTotal());
    }


    //calcula el total de los productos agregados
    
    public double calcularTotal(){
        double total=0;
        for (Detalle_Lista detalle_lista:articulos_lista) {
            total+=(detalle_lista.getCantidad()*detalle_lista.getIdArticulo().getPrecio());
        }
        NumberFormat formatter = new DecimalFormat("#0.00");
        formatter.format(total);
        return total;
    }
    
}
