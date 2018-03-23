package com.example.fernando.listadecompras.database.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.fernando.listadecompras.database.helper.ListaComprasElementHelper;
import com.example.fernando.listadecompras.database.model.Lista;
import com.example.fernando.listadecompras.database.model.Lista;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Fernando on 25/2/2018.
 */

public class ListaDB {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private ListaComprasElementHelper dbHelper;
    private Context context;

    public ListaDB(Context context) {
        dbHelper = new ListaComprasElementHelper(context);
    }

    public static abstract class ListaEntry implements BaseColumns {
        public static final String TABLE_NAME = "lista";
        public static final String COLUMN_NAME = "nombre";
        public static final String COLUMN_FECHA = "fecha";
        

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_FECHA + TEXT_TYPE  + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /**
     * Method to create new element in the database
     *
     * @param nombre
     * @param fecha
     */
    public void insertElement(String nombre,String fecha) {
        //TODO: add all the needed code to insert one item in database

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListaEntry.COLUMN_NAME, nombre);
        values.put(ListaEntry.COLUMN_FECHA, fecha);

      db.insert(ListaEntry.TABLE_NAME, null, values);

    }

    /**
     * Method to get all the shopping elements of the database
     *
     * @return Lista array
     */
    public ArrayList<Lista> getAllItems() {
        ArrayList<Lista> Listas = new ArrayList<>();
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                ListaEntry.COLUMN_NAME,
                ListaEntry._ID,
                String.valueOf(ListaEntry.COLUMN_FECHA),

        };


        String sortOrder =
                ListaEntry.COLUMN_NAME + " ASC";



        Cursor c = db.query(
                ListaEntry.TABLE_NAME,                     // The table to query
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
                String fechaString= c.getString(2);
                Lista s= new Lista(id,nombre,fechaString);
                Listas.add(s);
            } while(c.moveToNext());
        }
        return Listas;

    }

    public Lista getLista(String id) {
        Lista l = new Lista(5,"falla","12/12/12");
        //TODO: add all the needed code to get all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                ListaEntry.COLUMN_NAME,
                ListaEntry._ID,
                ListaEntry.COLUMN_FECHA,

        };





        Cursor c = db.query(
                ListaEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                ListaDB.ListaEntry._ID+" = "+id,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null// The sort order
        );

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombre= c.getString(0);
                Long ids= c.getLong(1);
                String fechaString= c.getString(2);
                l = new Lista(ids,nombre,fechaString);
            } while(c.moveToNext());
        }
        return l;

    }
    /**
     * Method to clear all the elements
     */
    public void clearAllItems() {
        //TODO: add all the needed code to clear all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(ListaEntry.TABLE_NAME, null, null);

    }
    public void clearItem(Lista l) {
        //TODO: add all the needed code to clear all the database items
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(ListaEntry.TABLE_NAME, ListaEntry.COLUMN_NAME + " = " +l.getId(), null);

    }

    /**
     * Method to update a database item
     *
     * @param lista
     */
    public void updateItem(Lista lista) {
        //TODO: add the needed code to update a database item
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ListaEntry.COLUMN_NAME, lista.getNombre());
        values.put(String.valueOf(ListaEntry.COLUMN_FECHA), lista.getFechaCreacion().toString());

// Which row to update, based on the title


        int count = db.update(
                ListaEntry.TABLE_NAME,
                values,
                ListaEntry._ID + "=" + lista.getId(),
                null);

    }
    /**
     * Method to delete one item
     *
     * @param lista
     */
    public void deleteItem(Lista lista) {
        //TODO: add all the needed code to delete a database item
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.delete(ListaEntry.TABLE_NAME,
                ListaEntry._ID + "=" + lista.getId(),
                null);
    }

}
