package edu.uclm.esi.listadecompra.dominio;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Lista implements Comparable<Lista>{
	private int idLista;
	private String nombre;
	private Usuario creador;
	public Vector<Producto> productos = new Vector();
	public Hashtable<String, Usuario> miembros = new Hashtable();
	public int getId() {
		return idLista;
	}
	public void setId(int id) {
		this.idLista = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Usuario getCreador() {
		return creador;
	}
	public void setCreador(Usuario creador) {
		this.creador = creador;
	}
	public Vector<Producto> getProductos() {
		return productos;
	}
	public void setProductos(Vector<Producto> productos) {
		this.productos = productos;
	}
	public Hashtable<String, Usuario> getMiembros() {
		return miembros;
	}
	public void setMiembros(Hashtable<String, Usuario> miembros) {
		this.miembros = miembros;
	}
	
	 public JSONObject toJSONObject() {
	        JSONObject jso = new JSONObject();
	        jso.put((Object)"id", (Object)this.idLista);
	        jso.put((Object)"nombre", (Object)this.nombre);
	        JSONArray jsaProductos = new JSONArray();
	        for (Producto producto : this.productos) {
	            jsaProductos.add((Object)producto.toJSONObject());
	        }
	        jso.put((Object)"productos", (Object)jsaProductos);
	        JSONArray jsaMiembros = new JSONArray();
	        Enumeration<Usuario> eMiembros = this.miembros.elements();
	        while (eMiembros.hasMoreElements()) {
	            Usuario usuario = eMiembros.nextElement();
	            jsaMiembros.add((Object)usuario.toJSONObject());
	        }
	        jso.put((Object)"miembros", (Object)jsaMiembros);
	        return jso;
	    }
	 
	 public void addProducto(Producto producto) {
	        this.productos.add(producto);
	    }
	
	 public void addUsuario(Usuario usuario) {
	      this.miembros.put(usuario.getEmail(), usuario);
		
	}
	@Override
	public int compareTo(Lista o) {
		if(idLista<o.idLista){
			return -1;
		}
		if(idLista>o.idLista){
			return 1;
		}
		
		return 0;
	}
}