package com.saltaku.tileserver.providers.basemaps.storage.impl;

import java.io.File;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.saltaku.tileserver.providers.basemaps.storage.BasemapStorage;
import com.saltaku.tileserver.providers.basemaps.storage.BasemapStorageException;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentLockedException;
import com.sleepycat.je.OperationStatus;

public class BerkleyBasemapStorage implements BasemapStorage{

	 private Environment env;
	 private Database tileDb;
	 
	 private final String dbName="tileDb";
	@Inject
	public BerkleyBasemapStorage(@Named("bdbPath") String  location) throws BasemapStorageException
	{
		 EnvironmentConfig envConfig = new EnvironmentConfig();
	        envConfig.setTransactional(false);
	        envConfig.setAllowCreate(true);

	        try {
				env = new Environment(new File(location), envConfig);
				DatabaseConfig dbConfig = new DatabaseConfig();
		        dbConfig.setTransactional(false);
		        dbConfig.setAllowCreate(true);
		        tileDb = env.openDatabase(null, dbName, dbConfig);
	        } catch (EnvironmentLockedException e) {
				throw new BasemapStorageException(e);
			} catch (DatabaseException e) {
				throw new BasemapStorageException(e);
			}
	        
	}
	
	public byte[] get(String shapeId, String key) throws BasemapStorageException {
		byte[] k=(shapeId+"_"+key).getBytes();
		DatabaseEntry out=new DatabaseEntry();
		try {
			OperationStatus st=tileDb.get(null, new DatabaseEntry(k), out, null);
			if(st.equals(OperationStatus.SUCCESS))
				return out.getData();
			else	return null;
		} catch (DatabaseException e) {
			throw new BasemapStorageException(e);
		}
		}

	public void put(String shapeId, String key, byte[] data) throws BasemapStorageException {
		byte[] k=(shapeId+"_"+key).getBytes();
		DatabaseEntry value=new DatabaseEntry(data);
		try {
			tileDb.put(null, new DatabaseEntry(k), value);
		} catch (DatabaseException e) {
			e.printStackTrace();
			throw new BasemapStorageException(e);
		}
	}

	public void shutdown() throws BasemapStorageException {
		try {
			tileDb.close();
			env.close();
		} catch (DatabaseException e) {
			e.printStackTrace();
			throw new BasemapStorageException(e);
		}
	}

	public void empty(String shapeId) throws BasemapStorageException {
	// TODO not implemented
		
	}

	
	
}
