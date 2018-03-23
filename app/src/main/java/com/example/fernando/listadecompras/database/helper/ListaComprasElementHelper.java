package com.example.fernando.listadecompras.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.fernando.listadecompras.database.entities.ArticuloDB;
import com.example.fernando.listadecompras.database.entities.Detalle_ListaDB;
import com.example.fernando.listadecompras.database.entities.ListaDB;
import com.example.fernando.listadecompras.database.entities.TiendaDB;
import com.example.fernando.listadecompras.database.model.Detalle_Lista;

/**
 * Created by Fernando on 25/2/2018.
 */

public class ListaComprasElementHelper extends SQLiteOpenHelper {
//definicion de la bd
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ListasCompradb.db";

    public ListaComprasElementHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//creacion de las tablas

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TiendaDB.TiendaEntry.CREATE_TABLE);
        db.execSQL(ArticuloDB.ArticuloEntry.CREATE_TABLE);
        db.execSQL(ListaDB.ListaEntry.CREATE_TABLE);
        db.execSQL(Detalle_ListaDB.Detalle_ListaEntry.CREATE_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TiendaDB.TiendaEntry.DELETE_TABLE);
        db.execSQL(ArticuloDB.ArticuloEntry.DELETE_TABLE);
        db.execSQL(ListaDB.ListaEntry.DELETE_TABLE);
        db.execSQL(Detalle_ListaDB.Detalle_ListaEntry.DELETE_TABLE);

        onCreate(db);
    }
}
