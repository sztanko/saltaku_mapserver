package com.saltaku.area.importer.db.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bson.BSONObject;
import org.bson.types.ObjectId;

import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.saltaku.area.importer.beans.AreaShape;
import com.saltaku.area.importer.db.DbStore;
import com.saltaku.area.importer.db.DbStoreException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class EmptyDbStore implements DbStore {

	
	@Inject
	public EmptyDbStore(DB db)
	{
		
	}
	
	public String insertArea(String name, String source) throws DbStoreException {
	return "";
	    
	}

	public void updateAreaGeoFeatures(String areaId, Geometry bb, int numGeometries, double area, double minArea, double maxArea) throws DbStoreException {
		 


	}

	public void commit() throws DbStoreException {
	
		
	}

	public void insertAreaGeometry(AreaShape geom) throws DbStoreException {
	
	}

	
	
	
}
