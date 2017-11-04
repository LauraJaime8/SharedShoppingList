package edu.uclm.esi.listadecompra.dominio;

import org.json.simple.JSONObject;

import edu.uclm.esi.listadecompra.dao.DAOProducto;

public class Producto {
	private int idProducto;
    private String nombre;
    private int cantidadExistente;
    private int cantidadDeseada;

    public Producto() {
    }

    public Producto(String nombre, int cantidadDeseada, int cantidadExistente) {
        this.nombre = nombre;
        this.cantidadDeseada = cantidadDeseada;
        this.cantidadExistente = cantidadExistente;
    }

    public void comprar(int cantidad) throws Exception {
        this.cantidadExistente += cantidad;
        DAOProducto.update((Producto)this);
    }

    public int getIdProducto() {
        return this.idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadExistente() {
        return this.cantidadExistente;
    }

    public void setCantidadExistente(int cantidadExistente) {
        this.cantidadExistente = cantidadExistente;
    }

    public int getCantidadDeseada() {
        return this.cantidadDeseada;
    }

    public void setCantidadDeseada(int cantidadDeseada) {
        this.cantidadDeseada = cantidadDeseada;
    }

    public JSONObject toJSONObject() {
        JSONObject jso = new JSONObject();
        jso.put((Object)"idProducto", (Object)this.idProducto);
        jso.put((Object)"nombre", (Object)this.nombre);
        jso.put((Object)"cantidadExistente", (Object)this.cantidadExistente);
        jso.put((Object)"cantidadDeseada", (Object)this.cantidadDeseada);
        return jso;
    }

    public void insert(int idLista) throws Exception {
        DAOProducto.insert((Producto)this, (int)idLista);
    }

    public void comprar() throws Exception {
        this.setCantidadExistente(this.cantidadDeseada);
        DAOProducto.update((Producto)this);
    }

    public void update() throws Exception {
        DAOProducto.update((Producto)this);
    }
}