package com.example.fernando.listadecompras.database.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.fernando.listadecompras.database.helper.ListaComprasElementHelper;
import com.example.fernando.listadecompras.database.model.Articulo;
import com.example.fernando.listadecompras.database.model.Tienda;

import java.util.ArrayList;

/**
 * Created by Fernando on 25/2/2018.
 */

public class ArticuloDB {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";


    private ListaComprasElementHelper dbHelper;
    private Context context;
    private TiendaDB tiendaDB;
    public ArticuloDB(Context context) {

        dbHelper = new ListaComprasElementHelper(context);
          tiendaDB=new TiendaDB(context);
    }

    //clase para definir variables
    public static abstract class ArticuloEntry implements BaseColumns {
        public static final String TABLE_NAME = "articulo";
        public static final String COLUMN_NAME = "nombre";
        public static final String COLUMN_CODIGO = "codigo";
        public static final String COLUMN_PRECIO = "precio";
        public static final String COLUMN_TIENDA = "tienda";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_CODIGO + TEXT_TYPE + COMMA_SEP +
                COLUMN_PRECIO + TEXT_TYPE + COMMA_SEP +
                COLUMN_TIENDA + TEXT_TYPE + " )";


        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /**
     * Method para insetar nuevo elemento
     *
     * @param nombre
     * @param codigo
     * @param precio
     * @param idTienda
     */
    public void insertElement(String nombre,String codigo,String precio,String idTienda) {
        //TODO: add all the needed code to insert one item in database


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ArticuloEntry.COLUMN_NAME, nombre);
        values.put(ArticuloEntry.COLUMN_CODIGO, codigo);
        values.put(ArticuloEntry.COLUMN_PRECIO, precio);
        values.put(ArticuloEntry.COLUMN_TIENDA, idTienda);


        db.insert(ArticuloEntry.TABLE_NAME, null, values);

    }

    /**
     * Metodo para obtener todos los items
     *
     * @return Tienda array
     */
    public ArrayList<Articulo> getAllItems() {
        ArrayList<Articulo> articulos = new ArrayList<>();
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                ArticuloEntry.COLUMN_NAME,
                ArticuloEntry._ID,
                String.valueOf(ArticuloEntry.COLUMN_CODIGO),
                String.valueOf(ArticuloEntry.COLUMN_PRECIO),
                String.valueOf(ArticuloEntry.COLUMN_TIENDA),
        };

        String sortOrder =
                ArticuloEntry.COLUMN_NAME + " ASC";



        Cursor c = db.query(
                ArticuloEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombre= c.getString(0);
                Long id= c.getLong(1);
                String codigo= c.getString(2);
                Double precio= c.getDouble(3);
                String id_tienda=c.getString(4);


                Tienda tienda= tiendaDB.getTienda(id_tienda);
                Articulo s= new Articulo(id,nombre,codigo,precio,tienda);
                articulos.add(s);
            } while(c.moveToNext());
        }
        return articulos;

    }

    //Obtener todos los nombres de los items
    public ArrayList<String> getAllItemsNombre() {
        ArrayList<String> articulos = new ArrayList<>();
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                ArticuloEntry.COLUMN_NAME,
                ArticuloEntry._ID,
                String.valueOf(ArticuloEntry.COLUMN_CODIGO),
                String.valueOf(ArticuloEntry.COLUMN_PRECIO),
                String.valueOf(ArticuloEntry.COLUMN_TIENDA),
        };

        String sortOrder =
                ArticuloEntry.COLUMN_NAME + " ASC";



        Cursor c = db.query(
                ArticuloEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombre= c.getString(0);

                articulos.add(nombre);
            } while(c.moveToNext());
        }
        return articulos;

    }
//obtener item por codigo
    public Articulo getItembyCodigo(String codigo) {
        Articulo articulo = null;
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                ArticuloEntry.COLUMN_NAME,
                ArticuloEntry._ID,
                String.valueOf(ArticuloEntry.COLUMN_CODIGO),
                String.valueOf(ArticuloEntry.COLUMN_PRECIO),
                String.valueOf(ArticuloEntry.COLUMN_TIENDA),
        };

        String[] args = new String[] {codigo};
        String sortOrder =
                ArticuloEntry.COLUMN_NAME + " ASC";



        Cursor c = db.query(
                ArticuloEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                "codigo=?",                                // The columns for the WHERE clause
                args,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombre= c.getString(0);
                Long id= c.getLong(1);
                String codigos= c.getString(2);
                Double precio= c.getDouble(3);
                String id_tienda=c.getString(4);


                Tienda tienda= tiendaDB.getTienda(id_tienda);
                 articulo= new Articulo(id,nombre,codigos,precio,tienda);

            } while(c.moveToNext());
        }
        return articulo;
    }
    //obtener item por nombre
    public Articulo getItembyNombre(String nombres) {
        Articulo articulo = null;
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                ArticuloEntry.COLUMN_NAME,
                ArticuloEntry._ID,
                String.valueOf(ArticuloEntry.COLUMN_CODIGO),
                String.valueOf(ArticuloEntry.COLUMN_PRECIO),
                String.valueOf(ArticuloEntry.COLUMN_TIENDA),
        };

        String[] args = new String[] {nombres};
        String sortOrder =
                ArticuloEntry.COLUMN_NAME + " ASC";



        Cursor c = db.query(
                ArticuloEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                "nombre=?",                                // The columns for the WHERE clause
                args,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombre= c.getString(0);
                Long id= c.getLong(1);
                String codigos= c.getString(2);
                Double precio= c.getDouble(3);
                String id_tienda=c.getString(4);


                Tienda tienda= tiendaDB.getTienda(id_tienda);
                articulo= new Articulo(id,nombre,codigos,precio,tienda);

            } while(c.moveToNext());
        }
        return articulo;
    }

    //obtener item por id
    public Articulo getItembyId(String ids) {
        Articulo articulo = null;
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                ArticuloEntry.COLUMN_NAME,
                ArticuloEntry._ID,
                String.valueOf(ArticuloEntry.COLUMN_CODIGO),
                String.valueOf(ArticuloEntry.COLUMN_PRECIO),
                String.valueOf(ArticuloEntry.COLUMN_TIENDA),
        };

        String[] args = new String[] {ids};
        String sortOrder =
                ArticuloEntry.COLUMN_NAME + " ASC";



        Cursor c = db.query(
                ArticuloEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                "_id=?",                                // The columns for the WHERE clause
                args,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombre= c.getString(0);
                Long id= c.getLong(1);
                String codigos= c.getString(2);
                Double precio= c.getDouble(3);
                String id_tienda=c.getString(4);
                Tienda tienda= tiendaDB.getTienda(id_tienda);
                articulo= new Articulo(id,nombre,codigos,precio,tienda);

            } while(c.moveToNext());
        }
        return articulo;
    }
    /**
     * Methodo para eliminar todos los items
     */
    public void clearAllItems() {
        //TODO: add all the needed code to clear all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(ArticuloEntry.TABLE_NAME, null, null);

    }

    /**
     * Method to actualizar item
     *
     * @param articulo
     */
    public void updateItem(Articulo articulo) {
        //TODO: add the needed code to update a database item
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();


        values.put(ArticuloEntry.COLUMN_NAME, articulo.getName());
        values.put(ArticuloEntry.COLUMN_CODIGO, articulo.getCodigo());
        values.put(ArticuloEntry.COLUMN_PRECIO, articulo.getPrecio());
        values.put(ArticuloEntry.COLUMN_TIENDA, articulo.getIdTienda().getId());
// Which row to update, based on the title


        int count = db.update(
                ArticuloDB.ArticuloEntry.TABLE_NAME,
                values,
                ArticuloEntry._ID + "=" + articulo.getId(),
                null);

    }
    /**
     * Method para borrar item
     *
     * @param articulo
     */
    public void deleteItem(Articulo articulo) {
        //TODO: add all the needed code to delete a database item
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.delete(ArticuloEntry.TABLE_NAME,
                ArticuloEntry.COLUMN_CODIGO + "=" + articulo.getCodigo(),
                null);
    }
}
