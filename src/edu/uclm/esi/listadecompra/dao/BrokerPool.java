package edu.uclm.esi.listadecompra.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class BrokerPool {
	private static BrokerPool yo;
	private String url="jdbc:mysql://localhost:3306/listadelacompra?noAccessToProcedureBodies=true";

	private GenericObjectPool<PoolableConnection> poolSelects;
	private GenericObjectPool<PoolableConnection> poolInserts;
	private GenericObjectPool<PoolableConnection> poolDeletes;

	
	private BrokerPool() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			GenericObjectPoolConfig poolConfig=new GenericObjectPoolConfig();
			poolConfig.setMaxTotal(50); 
			
			PoolableConnectionFactory poolableConnectionFactorySelects=
				new PoolableConnectionFactory(new DriverManagerConnectionFactory(url, "selecterLista", ""), null); 
			this.poolSelects=new GenericObjectPool<PoolableConnection>(poolableConnectionFactorySelects, poolConfig);
			poolableConnectionFactorySelects.setPool(poolSelects);
			
			PoolableConnectionFactory poolableConnectionFactoryInserts=
					new PoolableConnectionFactory(new DriverManagerConnectionFactory(url, "inserterLista", "root"), null);
			this.poolInserts=new GenericObjectPool<PoolableConnection>(poolableConnectionFactoryInserts, poolConfig);
			poolableConnectionFactoryInserts.setPool(poolInserts);
			
			
			PoolableConnectionFactory poolableConnectionFactoryDeletes=
					new PoolableConnectionFactory(new DriverManagerConnectionFactory(url, "deleterLista", "root"), null);
			this.poolInserts=new GenericObjectPool<PoolableConnection>(poolableConnectionFactoryInserts, poolConfig);
			poolableConnectionFactoryDeletes.setPool(poolDeletes);
		}
		catch (Exception e) {}
	}
	
	private static class BrokerHolder {
		private static BrokerPool broker=new BrokerPool();
	}

	public static BrokerPool get() throws Exception {
		return BrokerHolder.broker;
	}

	public Connection getConnectionSeleccion() throws Exception {
		return this.poolSelects.borrowObject();
	}
	
	public Connection getConnectionInsercion() throws Exception {
		return this.poolInserts.borrowObject();
	}
	
	
	public Connection getConnectionDelete() throws Exception {
		return this.poolDeletes.borrowObject();
	}
	
	public Connection getConnection(String userName, String pwd) throws Exception {
		Connection bd=DriverManager.getConnection(url, userName, pwd);
		bd.close();
		return this.poolInserts.borrowObject();
	}
}