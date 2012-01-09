package com.saltaku.data.area.writer.io;

import org.geotools.data.DataStore;

import com.saltaku.data.area.writer.GeometryWriterException;



public interface DataStoreProvider {
	public DataStore createtDataStore(String id) throws GeometryWriterException;
	public DataStore getDataStore(String id) throws GeometryWriterException;
	
}
