package com.saltaku.store.mysql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCPDataSource;
import com.saltaku.store.mysql.DbConnectionManager;


public class BoneCPConnectionManager implements DbConnectionManager{
	BoneCPDataSource ds;
	
	public BoneCPConnectionManager(String driver, 
            String url, 
            String user, 
            String password) 
    throws  ClassNotFoundException,
            InstantiationException, 
            IllegalAccessException, 
            SQLException {

		Class.forName(driver); 	// load the DB driver
	 	ds = new BoneCPDataSource();  // create a new datasource object
	 	ds.setJdbcUrl(url);		// set the JDBC url
		ds.setUsername(user);				// set the username
		ds.setPassword(password);
}

	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	public void close() {
		if(ds!=null)
		{
			ds.close();
			
			ds=null;
		}
		
	}
	
}
