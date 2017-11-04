package edu.uclm.esi.listadecompra.ws;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.Serializable;

import org.json.simple.JSONObject;

import edu.uclm.esi.listadecompra.dominio.Manager;
import edu.uclm.esi.listadecompra.dominio.Usuario;

@ServerEndpoint(value="/servidorWebSocket")
public class WebSocketServer {
	@OnOpen
	public void open(Session sesion){
		Map pars = sesion.getRequestParameterMap();
		int idUsuario = Integer.parseInt((String)((List)pars.get("idUsuario")).get(0));
		Usuario usuario = Manager.get().getUsuario(idUsuario);
		usuario.setSession(sesion);
		System.out.println("Se ha conectado: " + usuario.getEmail());
	}
	
	@OnClose
	public void close(Session sesion){
		Map pars = sesion.getRequestParameterMap();
		int idUsuario = Integer.parseInt((String)((List)pars.get("idUsuario")).get(0));
		Usuario usuario = Manager.get().getUsuario(idUsuario);
		usuario.setSession(sesion);
		System.out.println("Se desconecto: " + usuario.getEmail());
	}
	
	private void broadcastActualizacionDeLista(int idLista, JSONObject msgSalida){
		Enumeration usuarios = Manager.get().getUsuarios().elements();
		while (usuarios.hasMoreElements()){
			Usuario usuario = (Usuario)usuarios.nextElement();
			try{
				if(usuario.loadListas().get(idLista) == null) continue;
				usuario.broadcast(msgSalida);
				continue;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}