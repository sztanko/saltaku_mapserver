package com.saltaku;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.saltaku.data.compress.DataCompressor;
import com.saltaku.data.compress.impl.ZipDataCompressor;
import com.saltaku.data.serde.DataSerializer;
import com.saltaku.data.serde.impl.SimpleDataSerializer;
import com.saltaku.store.DBStore;
import com.saltaku.store.mysql.DbConnectionManager;
import com.saltaku.store.mysql.MysqlDBStore;
import com.saltaku.store.mysql.impl.BoneCPConnectionManager;
import com.saltaku.workflow.WorkflowManager;
import com.saltaku.workflow.impl.MysqlWorkflowManager;

public class MysqlModule extends AbstractModule{
String url, user, password;
	public MysqlModule(String url, String user, String password ){
	this.url=url;
	this.password=password;
	this.user=user;
	}
	
	@Override
	protected void configure() {
		bind(DbConnectionManager.class).to(BoneCPConnectionManager.class);

		bind(DataSerializer.class).to(SimpleDataSerializer.class);
		bind(DataCompressor.class).to(ZipDataCompressor.class);
		bind(String.class).annotatedWith(Names.named("mysql_driver")).toInstance("com.mysql.jdbc.Driver");
		bind(String.class).annotatedWith(Names.named("mysql_url")).toInstance(url);
		bind(String.class).annotatedWith(Names.named("mysql_password")).toInstance(password);
		bind(String.class).annotatedWith(Names.named("mysql_user")).toInstance(user);
		bind(DbConnectionManager.class).to(BoneCPConnectionManager.class);
		
		bind(DBStore.class).to(MysqlDBStore.class);
		bind(WorkflowManager.class).to(MysqlWorkflowManager.class);
		
		//("com.mysql.jdbc.Driver", url, user, password));
		
	}
		
}
