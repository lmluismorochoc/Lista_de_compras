package com.example.fernando.listadecompras.database.model;

/**
 * Created by Fernando on 25/2/2018.
 */

public class Tienda {
    private long id;
    private String name;
    private Double latitud;
    private Double longitud;

    public Tienda(long id, String name, Double latitud, Double longitud) {
        this.id = id;
        this.name = name;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }


}
