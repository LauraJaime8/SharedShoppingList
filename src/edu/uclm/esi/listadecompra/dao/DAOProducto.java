package edu.uclm.esi.listadecompra.dao;

import edu.uclm.esi.listadecompra.dao.BrokerPool;
import edu.uclm.esi.listadecompra.dominio.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAOProducto {
    public static Producto select(int id) throws Exception {
        Producto result = null;
        Connection bd = null;
        try {
            bd = BrokerPool.get().getConnectionSeleccion();
            String sql = "Select nombre, cantidadDeseada, cantidadExistente from Producto where idProducto=?";
            PreparedStatement ps = bd.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new Exception("Usuario dado de baja");
            }
            result = new Producto();
            result.setIdProducto(id);
            result.setNombre(rs.getString(1));
            result.setCantidadDeseada(rs.getInt(2));
            result.setCantidadExistente(rs.getInt(2));
            Producto producto = result;
            return producto;
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            bd.close();
        }
    }

    public static void update(Producto producto) throws Exception {
        Connection bd = null;
        try {
            bd = BrokerPool.get().getConnectionInsercion();
            String sql = "Update Producto set nombre=?, cantidadDeseada=?, cantidadExistente=? where idProducto=?";
            PreparedStatement ps = bd.prepareStatement(sql);
            ps.setString(1, producto.getNombre());
            ps.setInt(2, producto.getCantidadDeseada());
            ps.setInt(3, producto.getCantidadExistente());
            ps.setInt(4, producto.getIdProducto());
            ps.executeUpdate();
        }
        finally {
            bd.close();
        }
    }
    
   

    public static void insert(Producto producto, int idLista) throws Exception {
        Connection bd = null;
        try {
            bd = BrokerPool.get().getConnectionInsercion();
            String sql = "Insert into Producto (nombre, cantidadDeseada, cantidadExistente, idLista) values (?, ?, ?, ?)";
            PreparedStatement ps = bd.prepareStatement(sql, 1);
            ps.setString(1, producto.getNombre());
            ps.setInt(2, producto.getCantidadDeseada());
            ps.setInt(3, producto.getCantidadExistente());
            ps.setInt(4, idLista);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                producto.setIdProducto(rs.getInt(1));
            }
        }
        finally {
            bd.close();
        }
    }
}