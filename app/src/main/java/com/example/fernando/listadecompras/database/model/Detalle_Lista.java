package com.example.fernando.listadecompras.database.model;

/**
 * Created by Fernando on 20/3/2018.
 */

public class Detalle_Lista {
    private long id;
    private Lista idLista;
    private Articulo idArticulo;
    private int cantidad;
    private String unidadMaedida;

    public Detalle_Lista(long id, Lista idLista, Articulo idArticulo, int cantidad, String unidadMaedida) {
        this.id = id;
        this.idLista = idLista;
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
        this.unidadMaedida = unidadMaedida;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Lista getIdLista() {
        return idLista;
    }

    public void setIdLista(Lista idLista) {
        this.idLista = idLista;
    }

    public Articulo getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Articulo idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadMaedida() {
        return unidadMaedida;
    }

    public void setUnidadMaedida(String unidadMaedida) {
        this.unidadMaedida = unidadMaedida;
    }
}
