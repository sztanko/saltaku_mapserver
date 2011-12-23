package com.saltaku.area.importer.store.geometry.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.saltaku.area.importer.propertymapper.AreaFeatures;
import com.saltaku.area.importer.store.geometry.GeometryWriter;
import com.saltaku.area.importer.store.geometry.GeometryWriterException;
import com.vividsolutions.jts.geom.Geometry;

public class ShpGeometryWriter implements GeometryWriter {
private  FeatureWriter<SimpleFeatureType,SimpleFeature> outFeatureWriter;


	public void init(String id, URL outputPath, CoordinateReferenceSystem cs) throws GeometryWriterException {
		try{
		 SimpleFeatureType type=SimpleFeatureTypeBuilder.retype(DataUtilities.createType("Area","area:MultiPolygon,aid:Integer,code:String,name:String"),cs);
		  ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
		  Map<String, Serializable> create = new HashMap<String, Serializable>();
		  create.put("url", outputPath);
		  create.put("create spatial index", Boolean.TRUE);
		  DataStore outStore;
		  outStore = factory.createNewDataStore(create);
		  outStore.createSchema(type);
         outFeatureWriter = outStore.getFeatureWriter(outStore.getTypeNames()[0], Transaction.AUTO_COMMIT);
         
		}
		 catch (SchemaException e) {
			throw new GeometryWriterException(e);
		} catch (IOException e) {
			throw new GeometryWriterException(e);
		}

	}

	public void writeFeature(Geometry g, AreaFeatures f) throws GeometryWriterException {
        SimpleFeature outFeature;
		try {
		outFeature = outFeatureWriter.next();
		outFeature.setDefaultGeometry(g);
        outFeature.setAttribute("aid", f.getId());
        outFeature.setAttribute("code", f.getCode());
        outFeature.setAttribute("name", f.getEnglishName());
        outFeatureWriter.write();
		} catch (IOException e) {
			throw new GeometryWriterException(e);
		}
	}

	public void end() throws GeometryWriterException {
		   try {
			outFeatureWriter.close();
		} catch (IOException e) {
			throw new GeometryWriterException(e);
		}

	}

}
