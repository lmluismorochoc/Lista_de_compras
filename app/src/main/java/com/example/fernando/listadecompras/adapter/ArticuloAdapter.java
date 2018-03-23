package com.example.fernando.listadecompras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fernando.listadecompras.R;
import com.example.fernando.listadecompras.database.model.Articulo;
import com.example.fernando.listadecompras.database.model.Tienda;

import java.util.ArrayList;

/**
 * Created by Fernando on 25/2/2018.
 */

public class ArticuloAdapter extends ArrayAdapter<Articulo> {

    //Definicion de variables
    private Context context;
    private ArrayList<Articulo> articulos;

    //constructor del adaptador
    public ArticuloAdapter(Context context, ArrayList<Articulo> articulos) {
        super(context, R.layout.tienda_item);
        this.context = context;
        this.articulos = articulos;
    }

    //implementacio de los metdos del array adapter

    @Override
    public int getCount() {
        return articulos.size();
    }

    @Override
    public Articulo getItem(int position) {
        return  articulos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return articulos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final ArticuloAdapter.ViewHolder viewHolder;

        //se agrega los itema a la lista
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ArticuloAdapter.ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.articulo_item, parent, false);
            viewHolder.mItemName = (TextView) view.findViewById(R.id.textViewItemName);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ArticuloAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }

        // Set text with the item name
        viewHolder.mItemName.setText(String.format("%s    $ %s", articulos.get(position).getName(), articulos.get(position).getPrecio()));

        return view;
    }

    static class ViewHolder {
        protected TextView mItemName;

    }
}
