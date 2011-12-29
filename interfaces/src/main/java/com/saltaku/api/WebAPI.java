package com.saltaku.api;

import java.util.Map;

import com.saltaku.api.beans.AreaGeometryData;
import com.saltaku.api.beans.DataSetInfo;
import com.saltaku.api.beans.PartitioningType;
import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;
import com.saltaku.beans.DataSet;
import com.saltaku.beans.DataSetData;

public interface WebAPI {

	
public Map<String,Double>[] getPartitions(int dataSetId, int areaId, String aggregation, int numPartitions, PartitioningType pType) throws APIException;

	public Map<String,Double>[][][] getPartitions2(int dataSetId1, int dataSetId2, int areaId, String aggregation, int numPartitions, PartitioningType pType) throws APIException;
	public AreaGeometryData[] getData(int dataSetId, int areaId, String aggregation, double min, double max) throws APIException;
	public AreaGeometryData[] getDataById(int dataSetId, int areaId, String aggregation, int[] idList) throws APIException;
	public DataSetInfo getDataSetInfo(String dataSetId) throws APIException;
	public DataSetData getRawData(int dataSetId, int areaId, String aggregation) throws APIException;
	public DataSet[] getCorrelatedDataSets(String dataSetId, int maxNum) throws APIException;
	public Object getRelatedDataSets(String dataSetId, int maxNum) throws APIException;
	public DataSet[] findByTags(Map<String, String> tags, int maxNum) throws APIException;
	public Area getAreaInfo(int areaId) throws APIException;
	public AreaGeometry getAreaGeometry(int areaId, int geomId) throws APIException;
	public AreaGeometry getNeighbours(int areaId, int geomId, int maxNum) throws APIException;
	
	
	
	
}
