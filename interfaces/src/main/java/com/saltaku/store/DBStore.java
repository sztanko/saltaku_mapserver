package com.saltaku.store;

import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;
import com.saltaku.beans.DataSet;
import com.saltaku.beans.DataSetData;
import com.saltaku.beans.Tag;


public interface DBStore {
	public DataSetData getRawData(int datasetId, int areaId, String aggregation)  throws DBStoreException;
	public AreaGeometry[] getAreaGeometry(int areaId, int[] ids, boolean fetchGeometry)  throws DBStoreException;
	public DataSet getDataSet(String datasetId)  throws DBStoreException;
	public Tag[] getTags(String dataSetId)  throws DBStoreException;
	public DataSetData[] getAvailableDataForDataSet(String dataSetId)  throws DBStoreException;
	public Area getArea(int areaId)  throws DBStoreException;
	public DataSet[] getCorrelatedDataSets(String dataSetId, double maxCorrelation, int maxNum)  throws DBStoreException;
}
