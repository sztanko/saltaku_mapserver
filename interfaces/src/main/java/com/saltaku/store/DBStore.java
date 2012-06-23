package com.saltaku.store;

import java.util.List;
import java.util.Map;

import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;
import com.saltaku.beans.Correlation;
import com.saltaku.beans.DataSet;
import com.saltaku.beans.DataSetData;
import com.saltaku.beans.DataSource;
import com.saltaku.beans.DataSourceDataSet;
import com.saltaku.beans.Tag;
import com.saltaku.beans.relationfinder.DatasetRelation;


public interface DBStore {
	
	//Getting datasets
	public DataSetData getRawData(String datasetId, String areaId, String aggregation)  throws DBStoreException;
	public AreaGeometry[] getAreaGeometry(String areaId, int[] ids, boolean fetchGeometry)  throws DBStoreException;
	public DataSet getDataSet(String datasetId)  throws DBStoreException;
	public Tag[] getTags(String dataSetId, String type)  throws DBStoreException;
	public DataSetData[] getAvailableDataForDataSet(String dataSetId)  throws DBStoreException;
	public Area getArea(String areaId)  throws DBStoreException;
	public List<Correlation> getCorrelatedDataSets(String dataSetId, String correlationType, double maxCorrelation, int maxNum)  throws DBStoreException;
	public List<Correlation> getCorrelations(String[] dataSets,String areaId);
	public DataSet[] getDataSetsOfSource(String dataSourceId) throws DBStoreException;
	
	//Inserting and changing datasets
	public String generateId(String ds) throws DBStoreException;
	public void changeTags(String id, String type, Map<String,String> newTags) throws DBStoreException;
	public void changeTag(String id, String type, String tagKey, String tagValue) throws DBStoreException;
	public String insertDataSource(DataSource source) throws DBStoreException;
	public String insertDataSourceData(DataSourceDataSet source) throws DBStoreException;
	public String insertDataSet(DataSet source) throws DBStoreException;
	public String insertDataSetData(DataSetData source) throws DBStoreException;
	
	public void changeDataSet(DataSet dataSetId) throws DBStoreException;
	
	//Areas
	public String[] getParentAreas(String areaId)  throws DBStoreException;
	public int[] getAreaMapping(String childAreaId,String parentAreaId)  throws DBStoreException;
	
	public void insertArea(Area area) throws DBStoreException;
	public void insertAreaGeometry(AreaGeometry geom) throws DBStoreException;
	public void insertAreaMapping(String childAreaId, String parentAreaId, DatasetRelation rel) throws DBStoreException;
	
	public int lookupGeoKey(String areaId, String geoKey) throws DBStoreException;
	
	public void close() throws DBStoreException;
	public int[] getMatchingGeometry(String areaId, double x, double y)  throws DBStoreException;
	
	
	
}
