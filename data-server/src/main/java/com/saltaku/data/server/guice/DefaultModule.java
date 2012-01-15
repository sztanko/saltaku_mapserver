package com.saltaku.data.server.guice;

import java.util.Properties;

import com.google.inject.AbstractModule;

import com.google.inject.util.Modules;
import com.saltaku.MysqlModule;
import com.saltaku.data.area.AreaModule;



//TODO: add rabbitMQ
//TODO: guice-ize tileserver
//TODO: integrate tileserver with other stuff
public class DefaultModule  extends AbstractModule{
	
	Properties props;
	public DefaultModule(Properties props){
		this.props=props;
	}
	
	@Override
	protected void configure() {
	
		Modules.combine(new MysqlModule(props.getProperty("mysql.url", "jdbc:mysql://localhost:3306/test"), props.getProperty("mysql.user","test"), props.getProperty("mysql.password","test")));
		Modules.combine(new AreaModule());
		
		
		
	}

}
