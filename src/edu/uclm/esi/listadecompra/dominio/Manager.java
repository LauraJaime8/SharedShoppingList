package edu.uclm.esi.listadecompra.dominio;


import edu.uclm.esi.listadecompra.dao.DAOLista;
import edu.uclm.esi.listadecompra.dao.DAOProducto;
import edu.uclm.esi.listadecompra.dao.DAOUsuario;
import edu.uclm.esi.listadecompra.dominio.Usuario;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Manager {
    private static Manager yo;
    private Hashtable<Integer, Usuario> usuarios = new Hashtable();
   

    private Manager() {
    }

    public static Manager get() {
        if (yo == null) {
            yo = new Manager();
        }
        return yo;
    }

    public Usuario registrar(String email, String pwd) throws Exception {
        Usuario usuario = new Usuario(email);
        usuario.insert(pwd);
        return usuario;
    }

    public Usuario conectar(int id) throws Exception {
        Usuario usuario = this.usuarios.get(id);
        if (usuario == null) {
            usuario = DAOUsuario.select((int)id);
            if (usuario == null) {
                throw new Exception("No se encuentra ese usuario");
            }          
            this.usuarios.put(id, usuario);
        }
        return usuario;
    }

    
    public Usuario identificar(String login, String pwd) throws Exception {
        Usuario usuario = DAOUsuario.select((String)login, (String)pwd);
        usuario.loadListas();
 
        this.usuarios.put(usuario.getId(), usuario);
        return usuario;
    }


    public JSONObject getLista(int idLista)throws Exception{
    	Lista lista = DAOLista.select((int)idLista);
    	return lista.toJSONObject();
    }
    
    
    
    public JSONObject getEmailUser(int id)throws Exception{
    	Usuario user = DAOUsuario.select((int)id);
    	return user.toJSONObject();	
    }
    
    
    
    
    public JSONArray getListasAsJSONArray(Usuario usuario) throws Exception {
        Hashtable listas = usuario.loadListas();
        Enumeration eListas = listas.elements();
        JSONArray result = new JSONArray();
        while (eListas.hasMoreElements()) {
            Lista lista = (Lista)eListas.nextElement();
            result.add((Object)lista.toJSONObject());
        }
        return result;
    }
    
    
    public Usuario getUsuario(int id) {
        return this.usuarios.get(id);
    }

    
    public Hashtable<Integer, Usuario> getUsuarios() {
        return this.usuarios;
    }
    
    
    public void crearLista(Usuario usuario, String nombreLista) throws Exception {
        Lista lista = new Lista();
        lista.setNombre(nombreLista);
        lista.setCreador(usuario);
        DAOLista.insert((Lista)lista);
        DAOUsuario.addToLista((Usuario)usuario, (int)lista.getId());
    }
   
    public void eliminarLista(int idLista) throws Exception {
        Lista lista = new Lista();
        lista.getId();
        lista.getCreador();
        DAOLista.delete((Lista)lista);
    }
    
    public void addProducto(Usuario emisor, int idLista, String nombre, int cantidadDeseada, int cantidadExistente) throws Exception {
        Producto producto = new Producto(nombre, cantidadDeseada, cantidadExistente);
        producto.insert(idLista);
        JSONObject jso = new JSONObject();
        jso.put((Object)"tipo", (Object)"ACTUALIZACION_DE_LISTA");
        jso.put((Object)"idLista", (Object)idLista);
        String msg = jso.toJSONString();
    }
    
    
    public void comprar(Usuario emisor, int idLista, int idProducto) throws Exception {
        Producto producto = DAOProducto.select((int)idProducto);
        producto.comprar();
        JSONObject jso = new JSONObject();
        jso.put((Object)"tipo", (Object)"ACTUALIZACION_DE_LISTA");
        jso.put((Object)"idLista", (Object)idLista);
        String msg = jso.toJSONString();
    }
    
    private void broadcast(Usuario emisor, int idLista, String msg) throws IOException {
        Enumeration<Usuario> usuarios = this.usuarios.elements();
        while (usuarios.hasMoreElements()) {
            Usuario usuario = usuarios.nextElement();
            if (usuario.getId() == emisor.getId() || usuario.getListas().get(idLista) == null) continue;
            usuario.getSession().getBasicRemote().sendText(msg);
        }
    }
    
    
    public void guardarProducto(Usuario emisor, int idLista, int idProducto, String nombre, int cantidadDeseada, int cantidadExistente) throws Exception {
    	Producto producto = DAOProducto.select((int)idProducto);
        producto.setNombre(nombre);
        producto.setCantidadDeseada(cantidadDeseada);
        producto.setCantidadExistente(cantidadExistente);
        producto.update();
        JSONObject jso = new JSONObject();
        jso.put((Object)"tipo", (Object)"ACTUALIZACION_DE_LISTA");
        jso.put((Object)"idLista", (Object)idLista);
        String msg = jso.toJSONString();
        this.broadcast(emisor, idLista, msg);
    }
}