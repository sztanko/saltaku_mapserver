package com.saltaku.store;

import java.util.Map;

import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;
import com.saltaku.beans.DataSet;
import com.saltaku.beans.DataSetData;
import com.saltaku.beans.DataSource;
import com.saltaku.beans.Tag;


public interface DBStore {
	
	//Getting datasets
	public DataSetData getRawData(int datasetId, int areaId, String aggregation)  throws DBStoreException;
	public AreaGeometry[] getAreaGeometry(int areaId, int[] ids, boolean fetchGeometry)  throws DBStoreException;
	public DataSet getDataSet(int datasetId)  throws DBStoreException;
	public Tag[] getTags(int dataSetId, String type)  throws DBStoreException;
	public DataSetData[] getAvailableDataForDataSet(int dataSetId)  throws DBStoreException;
	public Area getArea(int areaId)  throws DBStoreException;
	public DataSet[] getCorrelatedDataSets(int dataSetId, double maxCorrelation, int maxNum)  throws DBStoreException;
	
	//Inserting and changing datasets
	public String generateId(String ds)  throws DBStoreException;
	public void changeTags(int id, String type, Map<String,String> newTags) throws DBStoreException;
	public String insertDataSource(DataSource source) throws DBStoreException;
	public String insertDataSet(DataSet source) throws DBStoreException;
	public String insertDataSetData(DataSetData source) throws DBStoreException;
	
	public int lookupGeoKey(int areaId, String geoKey) throws DBStoreException;
	
	public void close() throws DBStoreException;
	
	
	
}
