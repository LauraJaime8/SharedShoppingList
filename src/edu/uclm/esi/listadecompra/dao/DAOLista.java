package edu.uclm.esi.listadecompra.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import edu.uclm.esi.listadecompra.dominio.Lista;
import edu.uclm.esi.listadecompra.dominio.Producto;
import edu.uclm.esi.listadecompra.dominio.Usuario;

public class DAOLista {

	
	public static Lista select(int idLista) throws Exception {
		Lista resultado = null;
        Connection bd = null;
        try {
            bd = BrokerPool.get().getConnectionSeleccion();
            String sql = "Select nombre, emailCreador from Lista where idLista=?";
            PreparedStatement ps = bd.prepareStatement(sql);
            ps.setInt(1, idLista);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            	resultado = new Lista();
            	resultado.setId(idLista);
            	resultado.setNombre(rs.getString(1));
            	resultado.setCreador(DAOUsuario.select((String)rs.getString(2)));
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            bd.close();
        }
        DAOLista.cargarProductos(resultado);
    	DAOLista.cargarMiembros(resultado);
    	return resultado;
    }
	
	
	
	 private static void cargarMiembros(Lista lista)throws Exception {
	        Connection bd = null;
	        try {
	            bd = BrokerPool.get().getConnectionSeleccion();
	            String sql = "Select email from Miembro where Miembro.idLista=?";
	            PreparedStatement ps = bd.prepareStatement(sql);
	            ps.setInt(1, lista.getId());
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                String email = rs.getString(1);
	                Usuario usuario = DAOUsuario.select((String)email);
	                lista.addUsuario(usuario);
	            }
	        }
	        finally {
	            bd.close();
	        }
	    }

	

	private static void cargarProductos(Lista lista) throws Exception{
		   Connection bd = null;
	        try {
	            String sql = "select idProducto, nombre, cantidadExistente, cantidadDeseada from Producto where idLista=? order by (cantidadExistente-cantidadDeseada)";
	            bd = BrokerPool.get().getConnectionSeleccion();
	            PreparedStatement ps = bd.prepareStatement(sql);
	            ps.setInt(1, lista.getId());
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                Producto producto = new Producto();
	                producto.setIdProducto(rs.getInt(1));
	                producto.setNombre(rs.getString(2));
	                producto.setCantidadExistente(rs.getInt(3));
	                producto.setCantidadDeseada(rs.getInt(4));
	                lista.addProducto(producto);
	            }
	        }
	        finally {
	            bd.close();
	        }
		
	}

	public static void insert(Lista lista) throws Exception {
	        Connection bd = null;
	        try {
	            String sql = "insert into Lista (nombre, emailCreador) values (?, ?)";
	            bd = BrokerPool.get().getConnectionInsercion();
	            PreparedStatement ps = bd.prepareStatement(sql, 1);
	            ps.setString(1, lista.getNombre());
	            ps.setString(2, lista.getCreador().getEmail());
	            ps.executeUpdate();
	            ResultSet rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                lista.setId(rs.getInt(1));
	            }
	        }
	        finally {
	            bd.close();
	        }
	 }



	public static void delete(Lista lista) throws Exception {
		Connection bd = null;
		 try {
	            bd = BrokerPool.get().getConnectionDelete();
	            String sql = "Delete nombre, emailCreador from Lista where idLista=?";
	            PreparedStatement ps = bd.prepareStatement(sql);
	            ps.executeQuery();
	        }
	        finally {
	            bd.close();
	        }
	}
}