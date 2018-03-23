package com.example.fernando.listadecompras.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fernando.listadecompras.R;
import com.example.fernando.listadecompras.database.model.Tienda;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Fernando on 25/2/2018.
 */

public class TiendaAdapter extends RecyclerView.Adapter<TiendaAdapter.MyViewHolder> implements View.OnClickListener, ItemTouchHelperAdapter {
    //Definicion de variables

    private List<Tienda> items;
    //private ArrayList<Tienda> tiendaItems;
    private static String indiceLista;
    //////////
    private Context context;
    //Reproductor sound=new Reproductor();
    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);
    }

    //private static OnItemClickListener onItemClickListener;

//    public static interface OnItemClickListener {
//        public void onItemClick(View view, int position);
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout contCada;

        public ViewHolder(View itemRootView) {
            super(itemRootView);

            contCada = (LinearLayout) itemRootView.findViewById(R.id.contenedor_tienda_item);

            itemRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = ViewHolder.super.getAdapterPosition();
                    //onItemClickListener.onItemClick(view,position);
                }
            });
        }
    }


    public TiendaAdapter(Context context, ArrayList<Tienda> tiendaItems) {
//    public TiendaAdapter(Context context, List<String> items,String indiceLista) {
        this.context = context;
        this.items = tiendaItems;
        Log.i("cantidadDeTiendas","Hay "+tiendaItems.size());
        //this.indiceLista=indiceLista;
    }

    //////////////////
    @Override
    public int getItemCount() {
        return items.size();
    }

    ///////////
    @Override
    public void onBindViewHolder(final MyViewHolder itemsViewHolder, int i) {

        Tienda tiendaActual= items.get(i);

        // Insertar el texto de la tienda
        itemsViewHolder.nombre.setText(tiendaActual.getName());


    }

    ///////////////
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.tienda_item, viewGroup, false);
        itemView.setOnClickListener(this);

        //Canciones.ActualizarLista(context,,ind.getText().toString(),indiceLista);
        return new MyViewHolder(itemView);

    }

    /////////////
    public static class MyViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {


        protected TextView nombre;

        public MyViewHolder(View v) {
            super(v);

            nombre= (TextView) v.findViewById(R.id.textViewItemName);

        }

        @Override
        public void onItemSelected() {
            //itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.colorTresD));
        }

        @Override
        public void onItemClear() {
            //itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.nada));
        }

        @Override
        public void onChildDraw() {

        }
    }
///my view hol

    @Override
    public void onItemDismiss(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);

            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);

            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}



