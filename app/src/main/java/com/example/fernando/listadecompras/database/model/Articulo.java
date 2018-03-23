package com.example.fernando.listadecompras.database.model;

/**
 * Created by Fernando on 25/2/2018.
 */

public class Articulo {
    private long id;
    private String name;
    private String codigo;
    private Double precio;
    private Tienda idTienda;

    public Articulo(long id, String name, String codigo, Double precio, Tienda idTienda) {
        this.id = id;
        this.name = name;
        this.codigo = codigo;
        this.precio = precio;
        this.idTienda = idTienda;
    }

    public Tienda getIdTienda() {
        return idTienda;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCodigo() {
        return codigo;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setIdTienda(Tienda idTienda) {
        this.idTienda = idTienda;
    }
}
