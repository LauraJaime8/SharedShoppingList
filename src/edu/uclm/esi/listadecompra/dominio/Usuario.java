package edu.uclm.esi.listadecompra.dominio;

import edu.uclm.esi.listadecompra.dao.DAOUsuario;
import edu.uclm.esi.listadecompra.mail.Mail;

import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.Future;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import org.json.simple.JSONObject;

public class Usuario {
    private int id;
    private String email;
    private Session session;
    private Hashtable<Integer, Lista> listas;
    

    public Usuario(String login) {
        this.email = login;
        
    }

    public void insert(String pwd) throws Exception {
        DAOUsuario.insert((Usuario)this, (String)pwd);
    }

    public String getEmail() {
        return this.email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        result.put((Object)"id", (Object)this.id);
        result.put((Object)"email", (Object)this.email);
        return result;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return this.session;
    }

    public void broadcast(JSONObject msgSalida) {
        this.session.getAsyncRemote().sendText(msgSalida.toJSONString());
    }
    
    public Hashtable<Integer, Lista> getListas(){
    	return this.listas;
    }
    
    public Hashtable<Integer, Lista> loadListas() throws Exception{
    	this.listas = DAOUsuario.getListas((Usuario)this);
    	return this.listas;
    }
    
    public void invitar(String emailInvitado, Lista lista) throws Exception {
        int token = Math.abs(new Random().nextInt());
        DAOUsuario.insertarInvitacion((int)token, (String)this.email, (String)emailInvitado, (int)lista.getId()); //Funciona en la bd
        String texto = "Hola! Tu amigo " + this.email + " te quiere invitar a que te unas a la lista de la compra compartida " + lista.getNombre() + ".\n" + "Haz clic aqui: http://localhost:8080/listaDeCompra/unirse.jsp?p=" + token;
        Mail.enviarMail((String)emailInvitado, (String)"Invitacion para unirte a la lista de la compra", (String)texto);
    }
    


    public static Usuario unirsePorToken(String token) throws Exception {
        String[] emailInvitado = new String[1];
        int idLista = DAOUsuario.getInvitacion((String)token, (String[])emailInvitado);
        if (idLista == -1) {
            throw new Exception("No se encuentra el token. Quizas es muy antiguo y ha expirado");
        }
        Usuario invitado = null;
        try {
            invitado = DAOUsuario.select((String)emailInvitado[0]);
        }
        catch (Exception e) {
            invitado = new Usuario(emailInvitado[0]);
            invitado.insert("");
        }
        DAOUsuario.addToLista((Usuario)invitado, (int)idLista);
        return invitado;
    }
    
    
    


}