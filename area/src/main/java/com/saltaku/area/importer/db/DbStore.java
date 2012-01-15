package com.saltaku.area.importer.db;

import com.saltaku.area.importer.beans.AreaShape;
import com.vividsolutions.jts.geom.Geometry;
@Deprecated
public interface DbStore {

	public String insertArea(String name, String source) throws DbStoreException; 
    public void updateAreaGeoFeatures(String areaId, Geometry bb,int numGeometries, double area, double minArea, double maxArea ) throws DbStoreException;
	
	public void commit() throws DbStoreException; 
	//public int insertAreaGeometry(int area_id, String code, String name, String english_name, String centroid, String shape, String bb);
	
	public void insertAreaGeometry(AreaShape geom) throws DbStoreException;
}
