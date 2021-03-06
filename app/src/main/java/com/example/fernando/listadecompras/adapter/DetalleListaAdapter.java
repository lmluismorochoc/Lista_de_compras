package com.example.fernando.listadecompras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fernando.listadecompras.R;
import com.example.fernando.listadecompras.database.model.Detalle_Lista;
import com.example.fernando.listadecompras.database.model.Lista;

import java.util.ArrayList;


/**
 * Created by Fernando on 25/2/2018.
 */
public class DetalleListaAdapter extends ArrayAdapter<Detalle_Lista> {

    //Definicion de variables

    private Context context;
    private ArrayList<Detalle_Lista> listaItems;
//constructor
    public DetalleListaAdapter(Context context, ArrayList<Detalle_Lista> listaItems) {
        super(context, R.layout.lista_item);
        this.context = context;
        this.listaItems = listaItems;
    }

    @Override
    public int getCount() {
        return listaItems.size();
    }

    @Override
    public Detalle_Lista getItem(int position) {
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

//carga item a la lista
            view = LayoutInflater.from(context).inflate(R.layout.detalle_lista_item, parent, false);
            viewHolder.mItemName = (TextView) view.findViewById(R.id.tvdetallelistanombre);
            viewHolder.mItemPrecio = (TextView) view.findViewById(R.id.tvdetallelistaprecio);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        // Set text with the item name
        viewHolder.mItemName.setText(listaItems.get(position).getIdArticulo().getName() + " ( "+ listaItems.get(position).getCantidad()+" " +listaItems.get(position).getUnidadMaedida()+" )");
        viewHolder.mItemPrecio.setText("$ "+listaItems.get(position).getIdArticulo().getPrecio().toString());

        return view;
    }

    static class ViewHolder {
        protected TextView mItemName;
        protected TextView mItemPrecio;

    }
}
