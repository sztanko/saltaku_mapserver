package com.saltaku.store.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.google.inject.Inject;
import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;
import com.saltaku.beans.DataSet;
import com.saltaku.beans.DataSetData;
import com.saltaku.beans.Tag;
import com.saltaku.data.compress.DataCompressor;
import com.saltaku.data.serde.DataSerializer;
import com.saltaku.store.DBStore;
import com.saltaku.store.DBStoreException;

public class MysqlDBStore implements DBStore {
Connection conn;
CoordinateReferenceSystem crs;
DataSerializer serde;
DataCompressor compressor;

PreparedStatement getRawDataStm;

@Inject
public MysqlDBStore(Connection conn, CoordinateReferenceSystem crs) throws DBStoreException{
	this.conn=conn;
	this.crs=crs;
	
	
	try {
		this.getRawDataStm=conn.prepareStatement("select dataset_id, area_id,  X( STARTPOINT( EXTERIORRING( ENVELOPE( bb ) ) ) ) as x1 , Y( STARTPOINT( EXTERIORRING( ENVELOPE( bb ) ) ) ) as y1, X( POINTN( EXTERIORRING( ENVELOPE( bb ) ) , 3 ) ) as x1 , Y( POINTN( EXTERIORRING( ENVELOPE( bb ) ) , 3 ) ) as y1, aggregation, data from dataset_data where dataset_id=? and area_id=? and aggregation=? limit 1");
	} catch (SQLException e) {
		throw new DBStoreException(e);
	}
	
	
	}

	public DataSetData getRawData(int datasetId, int areaId, String aggregation) throws DBStoreException {
		
		try {
			this.getRawDataStm.setInt(1, datasetId);
			this.getRawDataStm.setInt(2, areaId);
			this.getRawDataStm.setString(3, aggregation);
		
			ResultSet rs=this.getRawDataStm.executeQuery();
			DataSetData data=new DataSetData();
			while(rs.next())
			{
				data.dataSet=rs.getInt("dataset_id");
				data.areaId=rs.getInt("area_id");
				Double x1=rs.getDouble("x1");
				Double x2=rs.getDouble("x2");
				Double y1=rs.getDouble("y1");
				Double y2=rs.getDouble("y2");
				data.bbox=new ReferencedEnvelope(x1, x2, y1, y2, this.crs);
				data.data=serde.deserialize(compressor.decompress(rs.getBytes("data")));
				data.aggregation=rs.getString("aggregation");
			}
		
		return data;
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
	}

	public AreaGeometry[] getAreaGeometry(int areaId, int[] ids, boolean fetchGeometry) {
		// TODO Auto-generated method stub
		return null;
	}

	public DataSet getDataSet(String datasetId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Tag[] getTags(String dataSetId) {
		// TODO Auto-generated method stub
		return null;
	}

	public DataSetData[] getAvailableDataForDataSet(String dataSetId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Area getArea(int areaId) {
		// TODO Auto-generated method stub
		return null;
	}

	public DataSet[] getCorrelatedDataSets(String dataSetId, double maxCorrelation, int maxNum) {
		// TODO Auto-generated method stub
		return null;
	}

}
