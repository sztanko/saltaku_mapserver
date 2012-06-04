package com.saltaku.data.server.guice;

import java.util.Properties;

import javax.servlet.http.HttpServlet;

import com.google.inject.AbstractModule;

import com.google.inject.util.Modules;
import com.saltaku.MysqlModule;
import com.saltaku.api.WebAPI;
import com.saltaku.api.impl.SimpleWebAPI;
import com.saltaku.data.api.servlet.WebAPIServlets;
import com.saltaku.data.area.AreaModule;
import com.saltaku.data.area.writer.GeometryWriter;
import com.saltaku.data.area.writer.impl.ShpGeometryWriter;
import com.saltaku.store.DBStore;
import com.saltaku.store.mysql.MysqlDBStore;



//TODO: add rabbitMQ
//TODO: guice-ize tileserver
//TODO: integrate tileserver with other stuff
//TODO: problem here: Modules.combine() does not work.
public class DefaultModule  extends AbstractModule{
	
	Properties props;
	public DefaultModule(Properties props){
		this.props=props;
	}
	
	@Override
	protected void configure() {
	
		AbstractModule mysqlModule=new MysqlModule(props.getProperty("mysql.url", "jdbc:mysql://localhost:3306/test"), props.getProperty("mysql.user","test"), props.getProperty("mysql.password","test")); 
		this.install(mysqlModule);
		this.install(new AreaModule());
		bind(WebAPI.class).to(SimpleWebAPI.class);
		
		//bind(DBStore.class).to(MysqlDBStore.class);
				
	}

}
