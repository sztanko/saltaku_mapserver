package com.saltaku.data.area.writer.io.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.saltaku.data.area.writer.GeometryWriterException;
import com.saltaku.data.area.writer.io.DataStoreProvider;

public class LocalFSDataStoreProvider implements DataStoreProvider {
	private String fsLocation;

	@Inject
	public LocalFSDataStoreProvider(@Named("path.shapefiles") String fs) {
		this.fsLocation = fs;
	}

	@Override
	public DataStore createDataStore(String id) throws GeometryWriterException {
		try {
			ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
			Map<String, Serializable> create = new HashMap<String, Serializable>();
			File dir = new File(fsLocation + "/" + id);
			if (!dir.exists())
				dir.mkdirs();
			File file = new File(fsLocation + "/" + id, "data.shp");
			create.put("url", file.toURI().toURL());
			create.put("create spatial index", Boolean.TRUE);
			return factory.createNewDataStore(create);
		} catch (IOException e) {
			throw new GeometryWriterException(e);
		}

	}

	@Override
	public DataStore getDataStore(String id) throws GeometryWriterException {
		try {
			File dir = new File(fsLocation + "/" + id);
			if (!dir.exists())
				dir.mkdirs();
			File file = new File(fsLocation + "/" + id, "data.shp");

			Map map = new HashMap();
			map.put("url", file.toURI().toURL());
			return DataStoreFinder.getDataStore(map);
		} catch (IOException e) {
			throw new GeometryWriterException(e);
		}
	}

}
