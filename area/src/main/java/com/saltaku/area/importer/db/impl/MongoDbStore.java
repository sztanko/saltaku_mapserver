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

public class MongoDbStore implements DbStore {
	private DB db;
	private DBCollection areaColl;
	private DBCollection areaGeomsColl;
	private DBObject[] cache=new DBObject[5000];
	private int cacheSize=0;
	
	@Inject
	public MongoDbStore(DB db)
	{
		this.db=db;
		areaColl = db.getCollection("areas");
		areaGeomsColl = db.getCollection("area_geom");
		MongoDbStore.ensureIndex(areaColl, false, "2d", "bounding_box");
		MongoDbStore.ensureIndex(areaColl, false, "1", "name");
		
		MongoDbStore.ensureIndex(areaGeomsColl,false, "1", "id");
		MongoDbStore.ensureIndex(areaGeomsColl,false, "1", "area_id");
		MongoDbStore.ensureIndex(areaGeomsColl,true, "1", "id","area_id");
		MongoDbStore.ensureIndex(areaGeomsColl,false, "1", "name");
		MongoDbStore.ensureIndex(areaGeomsColl,false, "2d", "centroid");
		MongoDbStore.ensureIndex(areaGeomsColl,false, "2d", "bb");
		
	}
	
	public String insertArea(String name, String source) throws DbStoreException {
		 BasicDBObject doc = new BasicDBObject();
	        doc.put("name", name);
	        doc.put("source", source);
	     areaColl.insert(doc);
	     return doc.get("_id").toString();
	    
	}

	public void updateAreaGeoFeatures(String areaId, Geometry bb, int numGeometries, double area, double minArea, double maxArea) throws DbStoreException {
		 
		 BasicDBObject query = new BasicDBObject();
		 ObjectId id=new ObjectId(areaId);
		 query.put("_id", new ObjectId(areaId));
		 DBObject areaObj=areaColl.findOne(query);
		 areaObj.put("numGeometries",numGeometries);
		 areaObj.put("area",area);
		 areaObj.put("minArea", minArea);
		 areaObj.put("maxArea", maxArea);
		 areaObj.put("bounding_box", MongoDbStore.geom2bson(bb));
		 areaObj.put("centroid", MongoDbStore.geom2bson(bb.getCentroid()));
		 areaObj.put("insert_ts", System.currentTimeMillis());
		 //areaObj.p
		 areaColl.findAndModify(query, areaObj);
		 //areaColl.insert(areaObj);

	}

	public void commit() throws DbStoreException {
		areaGeomsColl.insert(Arrays.copyOf(cache, cacheSize));
		cache=new DBObject[cache.length];
		cacheSize=0;
		
	}

	public void insertAreaGeometry(AreaShape geom) throws DbStoreException {
		BasicDBObject ob = new BasicDBObject();
		ob.put("id",geom.getFeatures().getId());
		ob.put("area_id",new ObjectId(geom.getAreaId()));
		ob.put("area_code",geom.getFeatures().getCode());
		ob.put("name",geom.getFeatures().getName());
		ob.put("english_name",geom.getFeatures().getEnglishName());
		ob.put("area",geom.getArea());
		ob.put("centroid",MongoDbStore.geom2bson(geom.getCentroid()));
		ob.put("shape",MongoDbStore.geom2bson(geom.getShape()));
		ob.put("simple_shape",MongoDbStore.geom2bson(geom.getSimplifiedGeometry()));
		ob.put("bb",MongoDbStore.geom2bson(geom.getBbox()));
		
		//areaGeomsColl.insert(ob);
		
		cache[cacheSize++]=ob;
		if(cacheSize>=cache.length)
		{
			areaGeomsColl.insert(cache);
			cache=new DBObject[cacheSize];
			cacheSize=0;
		}
		
	}

	public static BSONObject geom2bson(Geometry geom)
	{
		
		//System.out.println("Dimension is "+geom.getDimension()+" for obj "+geom.toText());
		BSONObject out=new BasicDBObject();
		switch(geom.getDimension())
		{
		case 0: 
				out=MongoDbStore.point2bson(geom.getCoordinate());
				break;
		case 1: 
		case 2:
				Coordinate[] coords=geom.getCoordinates();
				for(int i=0;i<coords.length;i++) { out.put(Integer.toString(i), MongoDbStore.point2bson(coords[i])); }
				break;
				
		}
		
		return out;
	}
	
	public static BSONObject point2bson(Coordinate c)
	{
		BSONObject out=new BasicDBObject();
		out.put("lon", c.x);
		out.put("lat", c.y);
		return out;
	}
	
	protected static void ensureIndex(DBCollection coll, boolean isUnique, String type, String... keys)
	{
		DBObject index=new BasicDBObject();
		for(String k: keys)
		{
			index.put(k,type);
		}
		
		if(isUnique)
		{
			DBObject c=new BasicDBObject();
			c.put("unique", true);
			coll.ensureIndex(index,c);
		}
		else{
		coll.ensureIndex(index);
		}
	}
	
	
	
}
