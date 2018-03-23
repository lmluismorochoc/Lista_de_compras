package com.example.fernando.listadecompras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fernando.listadecompras.R;
import com.example.fernando.listadecompras.database.model.Lista;
import com.example.fernando.listadecompras.database.model.Tienda;

import java.util.ArrayList;


/**
 * Created by Fernando on 25/2/2018.
 */
public class ListaAdapter extends ArrayAdapter<Lista> {
    //Definicion de variables

    private Context context;
    private ArrayList<Lista> listaItems;

    //Constructor
    public ListaAdapter(Context context, ArrayList<Lista> listaItems) {
        super(context, R.layout.lista_item);
        this.context = context;
        this.listaItems = listaItems;
    }
    //Sobreescritura de metodos

    @Override
    public int getCount() {
        return listaItems.size();
    }

    @Override
    public Lista getItem(int position) {
        return  listaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder viewHolder;


        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
//Agrega los items a la lista

            view = LayoutInflater.from(context).inflate(R.layout.lista_item, parent, false);
            viewHolder.mItemName = (TextView) view.findViewById(R.id.textViewListaNombre);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        // Set text with the item name
        viewHolder.mItemName.setText(listaItems.get(position).getNombre());

        return view;
    }

    static class ViewHolder {
        protected TextView mItemName;

    }
}
