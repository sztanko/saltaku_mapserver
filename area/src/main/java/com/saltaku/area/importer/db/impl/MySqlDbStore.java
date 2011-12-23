package com.saltaku.area.importer.db.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.inject.Inject;
import com.mysql.jdbc.Statement;
import com.saltaku.area.importer.beans.AreaShape;
import com.saltaku.area.importer.db.DbStore;
import com.saltaku.area.importer.db.DbStoreException;
import com.vividsolutions.jts.geom.Geometry;

public class MySqlDbStore implements DbStore {
	private Connection conn;
	
	@Inject
	public MySqlDbStore(Connection conn){
		this.conn=conn;
		 try {
			this.conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String insertArea(String name, String source) throws DbStoreException {
		System.out.println("Inserting area "+name);
		String q = "insert into areas (name, source) values (?,?)";
		try {
			PreparedStatement p = conn.prepareStatement(q,Statement.RETURN_GENERATED_KEYS);
			p.setString(1, name);
			p.setString(2, source);
			p.executeUpdate();
			ResultSet rs=p.getGeneratedKeys();
			rs.next();
			return rs.getString(1);
			
			
		} catch (SQLException e) {
			throw new DbStoreException(e);
		}
	}

	public void updateAreaGeoFeatures(String areaId, Geometry bb, int numGeometries, double area, double minArea, double maxArea) throws DbStoreException {
		String q = "update areas set bbox=GeomFromText(?),centroid=GeomFromText(?), num_items=?, area=?, min_area=?, max_area=? where area_id=?";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setString(1, bb.toText());
			p.setString(2, bb.getCentroid().toText());
			p.setInt(3, numGeometries);
			p.setDouble(4, area);
			p.setDouble(5, minArea);
			p.setDouble(6, maxArea);
			p.setInt(7, Integer.parseInt(areaId));
			p.executeUpdate();
		} catch (SQLException e) {
			throw new DbStoreException(e);
		}

	}

	public void insertAreaGeometry(AreaShape geom) throws DbStoreException {
		System.out.println("Inserting area geometry "+geom.getFeatures().getCode()+" "+geom.getFeatures().getName());
		String q = "insert into area_geom (area_id,area_code, name, english_name, area, centroid, shape,simple_shape, bb,geom_id) values (?,?,?,?,?,GeomFromText(?),GeomFromText(?),GeomFromText(?),GeomFromText(?),?)";
		try {
			PreparedStatement p = conn.prepareStatement(q,Statement.RETURN_GENERATED_KEYS);
			p.setInt(1, Integer.parseInt(geom.getAreaId()));
			p.setString(2, geom.getFeatures().getCode());
			p.setString(3, geom.getFeatures().getName());
			p.setString(4, geom.getFeatures().getEnglishName());
			p.setDouble(5, geom.getArea());
			p.setString(6, geom.getCentroid().toText());
			p.setString(7, geom.getShape().toText());
			p.setString(8, geom.getSimplifiedGeometry().toText());
			p.setString(9, geom.getBbox().toText());
			p.setInt(10, geom.getFeatures().getId());
			/*System.out.println("insert into area_geom (area_id,area_code, name, english_name, centroid, shape,bb) values ("+geom.getAreaId()+
				",'"+geom.getFeatures().getCode()+
				"','"+geom.getFeatures().getName()+
				"','"+geom.getFeatures().getEnglishName()+
				"',GeomFromText('"+geom.getCentroid().toText()+"')"+
				",GeomFromText('"+geom.getShape().toText()+"')"+
				",GeomFromText('"+geom.getBbox().toText()+"')");*/	
			p.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbStoreException(e);
		}
	}

	public void commit() throws DbStoreException {
		try {
			this.conn.commit();
		} catch (SQLException e) {
			throw new DbStoreException(e);
		}
		
	}
	
	

}
