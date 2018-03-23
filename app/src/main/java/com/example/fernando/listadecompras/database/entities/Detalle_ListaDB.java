package com.example.fernando.listadecompras.database.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.fernando.listadecompras.database.helper.ListaComprasElementHelper;
import com.example.fernando.listadecompras.database.model.Articulo;
import com.example.fernando.listadecompras.database.model.Detalle_Lista;
import com.example.fernando.listadecompras.database.model.Lista;
import com.example.fernando.listadecompras.database.model.Tienda;

import java.util.ArrayList;

/**
 * Created by Fernando on 25/2/2018.
 */

public class Detalle_ListaDB {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private ListaComprasElementHelper dbHelper;
    private Context context;
    public ListaDB listaDB;
    public ArticuloDB articuloDB;
    public TiendaDB tiendaDB;

    public Detalle_ListaDB(Context context) {
        dbHelper = new ListaComprasElementHelper(context);
        listaDB = new ListaDB(context);
        articuloDB = new ArticuloDB(context);
        tiendaDB = new TiendaDB(context);
    }


    public static abstract class Detalle_ListaEntry implements BaseColumns {
        public static final String TABLE_NAME = "detalle_lista";
        public static final String COLUMN_LISTA = "idLista";
        public static final String COLUMN_ARTICULO = "idArticulo";
        public static final String COLUMN_CANTIDAD = "idCantidad";
        public static final String COLUMN_UNIDAD = "idUnidad";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                COLUMN_LISTA + TEXT_TYPE + COMMA_SEP +
                COLUMN_ARTICULO + TEXT_TYPE + COMMA_SEP +
                COLUMN_CANTIDAD + TEXT_TYPE + COMMA_SEP +
                COLUMN_UNIDAD + TEXT_TYPE + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /**
     * Method to create new element in the database
     */
    public void insertElement(String idLista, String idArticulo, String cantidad, String unidad) {
        //TODO: add all the needed code to insert one item in database

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Detalle_ListaEntry.COLUMN_LISTA, idLista);
        values.put(Detalle_ListaEntry.COLUMN_ARTICULO, idArticulo);
        values.put(Detalle_ListaEntry.COLUMN_CANTIDAD, cantidad);
        values.put(Detalle_ListaEntry.COLUMN_UNIDAD, unidad);

        db.insert(Detalle_ListaEntry.TABLE_NAME, null, values);

    }

    /**
     * Method to get all the shopping elements of the database
     *
     * @return Lista array
     */
    public ArrayList<Detalle_Lista> getAllItems(String idLis) {
        ArrayList<Detalle_Lista> detalle_listas = new ArrayList<>();
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                Detalle_ListaEntry._ID,
                Detalle_ListaEntry.COLUMN_LISTA,
                Detalle_ListaEntry.COLUMN_ARTICULO,
                Detalle_ListaEntry.COLUMN_CANTIDAD,
                Detalle_ListaEntry.COLUMN_UNIDAD,


        };
        String sortOrder =
                Detalle_ListaEntry._ID+ " DESC";


        Cursor c = db.query(
                Detalle_ListaEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                Detalle_ListaEntry.COLUMN_LISTA + " = " + idLis,                            // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Long id = c.getLong(0);
                String idLista = c.getString(1);
                String idArticulo = c.getString(2);
                String cantidad = c.getString(3);
                String unidad = c.getString(4);
                Lista l = listaDB.getLista(idLista);
                Articulo a=articuloDB.getItembyId(idArticulo);
                Detalle_Lista detalle_lista= new Detalle_Lista(id, l, a, Integer.parseInt(cantidad), unidad);
                detalle_listas.add(detalle_lista);
            } while (c.moveToNext());
        }
        return detalle_listas;

    }




    public Detalle_Lista getDetalle_Lista(String id) {
        Detalle_Lista detalle_lista = null;
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                Detalle_ListaEntry._ID,
                Detalle_ListaEntry.COLUMN_LISTA,
                Detalle_ListaEntry.COLUMN_ARTICULO,
                Detalle_ListaEntry.COLUMN_CANTIDAD,
                Detalle_ListaEntry.COLUMN_UNIDAD,

        };


        Cursor c = db.query(
                Detalle_ListaEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                Detalle_ListaEntry._ID + " = " + id,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null// The sort order
        );

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Long ids = c.getLong(0);
                String idLista = c.getString(1);
                String idArticulo = c.getString(2);
                String cantidad = c.getString(3);
                String unidad = c.getString(4);
                Lista l = listaDB.getLista(idLista);
                Articulo a=articuloDB.getItembyId(idArticulo);
                 detalle_lista= new Detalle_Lista(ids, l, a, Integer.parseInt(cantidad), unidad);
            } while (c.moveToNext());
        }
        return detalle_lista;

    }

    /**
     * Method to clear all the elements
     */
    public void clearAllItems() {
        //TODO: add all the needed code to clear all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(Detalle_ListaEntry.TABLE_NAME, null, null);

    }

    /**
     * Method to update a database item
     *
     * @param detalle_lista
     */
    public void updateItem(Detalle_Lista detalle_lista) {
        //TODO: add the needed code to update a database item
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Detalle_ListaEntry.COLUMN_CANTIDAD, detalle_lista.getCantidad());
        values.put(Detalle_ListaEntry.COLUMN_UNIDAD, detalle_lista.getUnidadMaedida());

// Which row to update, based on the title


        int count = db.update(
                Detalle_ListaDB.Detalle_ListaEntry.TABLE_NAME,
                values,
                Detalle_ListaDB.Detalle_ListaEntry._ID + "=" + detalle_lista.getId(),
                null);

    }

    /**
     * Method to delete one item
     *
     * @param detalle_lista
     */
    public void deleteItem(Detalle_Lista detalle_lista) {
        //TODO: add all the needed code to delete a database item
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.delete(Detalle_ListaEntry.TABLE_NAME,
                Detalle_ListaEntry._ID + "=" + detalle_lista.getId(),
                null);
    }

}
