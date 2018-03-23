package com.example.fernando.listadecompras.database.model;

import java.util.Date;

/**
 * Created by Fernando on 20/3/2018.
 */

public class Lista {
    private long id;
    private String nombre;
    private String fechaCreacion;

    public Lista(long id, String nombre, String fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
