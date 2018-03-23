package com.example.fernando.listadecompras.database.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.fernando.listadecompras.database.helper.ListaComprasElementHelper;
import com.example.fernando.listadecompras.database.model.Tienda;

import java.util.ArrayList;

/**
 * Created by Fernando on 25/2/2018.
 */

public class TiendaDB {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private ListaComprasElementHelper dbHelper;
    private Context context;

    public TiendaDB(Context context) {
        dbHelper = new ListaComprasElementHelper(context);
    }

    public static abstract class TiendaEntry implements BaseColumns {
        public static final String TABLE_NAME = "tienda";
        public static final String COLUMN_NAME = "nombre";
        public static final String COLUMN_LATITUD = "latitud";
        public static final String COLUMN_LONGITUD = "longitud";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_LATITUD + TEXT_TYPE + COMMA_SEP +
                COLUMN_LONGITUD + TEXT_TYPE + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /**
     * Method to create new element in the database
     *
     * @param nombre
     * @param latitud
     * @param longitud
     */
    public void insertElement(String nombre,String latitud,String longitud) {
        //TODO: add all the needed code to insert one item in database

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TiendaEntry.COLUMN_NAME, nombre);
        values.put(TiendaEntry.COLUMN_LATITUD, latitud);
        values.put(TiendaEntry.COLUMN_LONGITUD, longitud);

      db.insert(TiendaEntry.TABLE_NAME, null, values);

    }

    /**
     * Method to get all the shopping elements of the database
     *
     * @return Tienda array
     */
    public ArrayList<Tienda> getAllItems() {
        ArrayList<Tienda> Tiendas = new ArrayList<>();
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                TiendaEntry.COLUMN_NAME,
                TiendaEntry._ID,
                String.valueOf(TiendaEntry.COLUMN_LATITUD),
                String.valueOf(TiendaEntry.COLUMN_LONGITUD),
        };


        String sortOrder =
                TiendaEntry.COLUMN_NAME + " ASC";



        Cursor c = db.query(
                TiendaEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                            // The columns for the WHERE clause
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
                Double lat= c.getDouble(2);
                Double lon= c.getDouble(3);
                Tienda s= new Tienda(id,nombre,lat,lon);
                Tiendas.add(s);
            } while(c.moveToNext());
        }
        return Tiendas;

    }
    public ArrayList<String> getAllItemsTxt() {
        ArrayList<String> tiendasTxt = new ArrayList<>();
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                TiendaEntry.COLUMN_NAME,
                TiendaEntry._ID,
                String.valueOf(TiendaEntry.COLUMN_LATITUD),
                String.valueOf(TiendaEntry.COLUMN_LONGITUD),
        };

        String sortOrder =
                TiendaEntry.COLUMN_NAME + " ASC";



        Cursor c = db.query(
                TiendaEntry.TABLE_NAME,                     // The table to query
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

                tiendasTxt.add(nombre);
            } while(c.moveToNext());
        }
        return tiendasTxt;

    }
    public Tienda getTienda(String id) {
        Tienda tienda = null ;
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                TiendaEntry.COLUMN_NAME,
                TiendaEntry._ID,
                String.valueOf(TiendaEntry.COLUMN_LATITUD),
                String.valueOf(TiendaEntry.COLUMN_LONGITUD),
        };

        String sortOrder =
                TiendaEntry.COLUMN_NAME + " ASC";



        Cursor c = db.query(
                TiendaEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                TiendaDB.TiendaEntry._ID+" = "+id,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombre= c.getString(0);
                Long ids= c.getLong(1);
                Double lat= c.getDouble(2);
                Double lon= c.getDouble(3);
                tienda= new Tienda(ids,nombre,lat,lon);

            } while(c.moveToNext());
        }
        return tienda;

    }
    public Tienda getTiendaByName(String nombre) {
        Tienda tienda = null ;
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                TiendaEntry.COLUMN_NAME,
                TiendaEntry._ID,
                String.valueOf(TiendaEntry.COLUMN_LATITUD),
                String.valueOf(TiendaEntry.COLUMN_LONGITUD),
        };

        String[] args = new String[] {nombre};
        String sortOrder =
                TiendaEntry.COLUMN_NAME + " ASC";



        Cursor c = db.query(
                TiendaEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                "nombre=?",                                   // The columns for the WHERE clause
                args,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombres= c.getString(0);
                Long ids= c.getLong(1);
                Double lat= c.getDouble(2);
                Double lon= c.getDouble(3);
                tienda= new Tienda(ids,nombres,lat,lon);

            } while(c.moveToNext());
        }
        return tienda;

    }

    /**
     * Method to clear all the elements
     */
    public void clearAllItems() {
        //TODO: add all the needed code to clear all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(TiendaEntry.TABLE_NAME, null, null);

    }

    /**
     * Method to update a database item
     *
     * @param tienda
     */
    public void updateItem(Tienda tienda) {
        //TODO: add the needed code to update a database item
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(TiendaEntry.COLUMN_NAME, tienda.getName());
        values.put(String.valueOf(TiendaEntry.COLUMN_LATITUD), tienda.getLatitud());
        values.put(String.valueOf(TiendaEntry.COLUMN_LONGITUD), tienda.getLongitud());

// Which row to update, based on the title


        int count = db.update(
                TiendaEntry.TABLE_NAME,
                values,
                TiendaEntry._ID + "=" + tienda.getId(),
                null);

    }
    /**
     * Method to delete one item
     *
     * @param tienda
     */
    public void deleteItem(Tienda tienda) {
        //TODO: add all the needed code to delete a database item
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.delete(TiendaEntry.TABLE_NAME,
                TiendaEntry._ID + "=" + tienda.getId(),
                null);
    }

}
