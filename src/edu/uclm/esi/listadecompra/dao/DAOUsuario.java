package edu.uclm.esi.listadecompra.dao;

import edu.uclm.esi.listadecompra.dao.BrokerPool;
import edu.uclm.esi.listadecompra.dominio.Lista;
import edu.uclm.esi.listadecompra.dominio.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

public class DAOUsuario {
    public static Usuario select(int id) throws Exception {
        Usuario result = null;
        Connection bd = null;
        try {
        	bd=BrokerPool.get().getConnectionSeleccion();
            String sql = "Select email from Usuario where id=?";
            PreparedStatement ps = bd.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new Exception("Usuario dado de baja");
            }
            result = new Usuario(rs.getString(1));
            result.setId(id);
            Usuario usuario = result;
            return usuario;
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            bd.close();
        }
    }

    public static Usuario select(String email) throws Exception {
        Usuario result = null;
        Connection bd = null;
        try {
            bd = BrokerPool.get().getConnectionSeleccion();
            String sql = "Select id from Usuario where email=?";
            PreparedStatement ps = bd.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new Exception("Usuario dado de baja");
            }
            result = new Usuario(email);
            result.setId(rs.getInt(1));
            Usuario usuario = result;
            return usuario;
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            bd.close();
        }
    }

    public static Usuario select(String email, String pwd) throws Exception {
        Usuario result = null;
        Connection bd = null;
        try {
            bd = BrokerPool.get().getConnectionSeleccion();
            String sql = "Select id from Usuario where email=? and pwd=?";
            PreparedStatement ps = bd.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new Exception("Error en tu email o password. "
                		+ "Quiza no estes registrado.");
            }
            result = new Usuario(email);
            result.setId(rs.getInt(1));
            Usuario usuario = result;
            return usuario;
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            bd.close();
        }
    }

    public static int insert(Usuario usuario, String pwd) throws Exception {
        Connection bd = null;
        int nuevoId = -1;
        try {
            bd = BrokerPool.get().getConnectionInsercion();
            String sql = "Insert into Usuario (email, pwd) values (?, ?)";
            PreparedStatement ps = bd.prepareStatement(sql, 1);
            ps.setString(1, usuario.getEmail());
            ps.setString(2, pwd);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                nuevoId = rs.getInt(1);
                usuario.setId(nuevoId);
            }
            int n = nuevoId;
            return n;
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            bd.close();
        }
    }

  
    

    public static void insertarInvitacion(int token, String emailInvitador, String emailInvitado, int idLista) throws Exception {
        Connection bd = null;
        try {
            bd = BrokerPool.get().getConnectionInsercion();
            String sql = "Insert into Invitacion (token, emailInvitador, emailInvitado, idLista) values (?, ?, ?, ?)";
            PreparedStatement ps = bd.prepareStatement(sql);
            ps.setInt(1, token);
            ps.setString(2, emailInvitador);
            ps.setString(3, emailInvitado);
            ps.setInt(4, idLista);
            ps.executeUpdate();
        }
        finally {
            bd.close();
        }
    }

    
    public static int getInvitacion(String token, String[] emailInvitado) throws Exception {
        Connection bd = null;
        int result = -1;
        try {
            bd = BrokerPool.get().getConnectionSeleccion();
            String sql = "Select emailInvitado, idLista from Invitacion where token=?";
            PreparedStatement ps = bd.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(token));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                emailInvitado[0] = rs.getString(1);
                result = rs.getInt(2);
            }
            int n = result;
            return n;
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            bd.close();
        }
    }

    
    public static Hashtable<Integer,Lista> getListas(Usuario usuario)throws Exception{
    	Hashtable<Integer,Lista> resultado = null;
    	Connection bd = null;
    	try{
    		bd = BrokerPool.get().getConnectionSeleccion();
    		String sql = "Select Lista.idLista from Lista inner join Miembro on Miembro.idLista=Lista.idLista where Miembro.email=?";
    		PreparedStatement ps = bd.prepareStatement(sql);
            ps.setString(1, usuario.getEmail());
            ResultSet rs = ps.executeQuery();
            resultado = new Hashtable<Integer, Lista>();
            while(rs.next()){
            	int idLista = rs.getInt(1);
            	Lista lista = DAOLista.select(idLista);
            	resultado.put(idLista, lista);
            }
            Hashtable<Integer, Lista> hashtable = resultado;
            return hashtable;
    	}catch(Exception e){
    		throw e;
    	}finally{
    		bd.close();
    	}
    }
    
    
    
    public static void addToLista(Usuario usuario, int idLista) throws Exception {
        Connection bd = null;
        try {
            bd = BrokerPool.get().getConnectionInsercion();
            String sql = "Insert into Miembro (email, idLista) values (?, ?)";
            PreparedStatement ps = bd.prepareStatement(sql);
            ps.setString(1, usuario.getEmail());
            ps.setInt(2, idLista);
            ps.executeUpdate();
        }
        finally {
            bd.close();
        }
    }
    
    public static void eliminarLista(Usuario usuario, int idLista) throws Exception {
        Connection bd = null;
        try {
            bd = BrokerPool.get().getConnectionDelete();
            String sql = "Delete email, idLista from Miembro values (?, ?)";
            PreparedStatement ps = bd.prepareStatement(sql);
            ps.setString(1, usuario.getEmail());
            ps.setInt(2, idLista);
            ps.executeUpdate();
        }
        finally {
            bd.close();
        }
    }
}