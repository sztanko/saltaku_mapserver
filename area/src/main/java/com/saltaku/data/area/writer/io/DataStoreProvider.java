package com.saltaku.data.area.writer.io;

import java.net.URL;

import org.geotools.data.DataStore;

import com.saltaku.data.area.writer.GeometryWriterException;

public interface DataStoreProvider {
	public DataStore createDataStore(String id) throws GeometryWriterException;
	public DataStore getDataStore(String id) throws GeometryWriterException;
	public URL getDataStoreURL(String id);
	
}
