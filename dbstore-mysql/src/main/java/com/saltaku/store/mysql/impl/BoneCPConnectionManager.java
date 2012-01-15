package com.saltaku.store.mysql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.jolbox.bonecp.BoneCPDataSource;
import com.saltaku.store.mysql.DbConnectionManager;

@Singleton
public class BoneCPConnectionManager implements DbConnectionManager{
	BoneCPDataSource ds;
	
	@Inject
	public BoneCPConnectionManager(@Named("mysql_driver") String driver, 
			@Named("mysql_url") String url, 
			@Named("mysql_user") String user, 
			@Named("mysql_password") String password) 
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
