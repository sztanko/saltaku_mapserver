package com.saltaku.store.api;

import java.util.Collection;
import java.util.Map;

public interface WebAPI {

	
	//Ranges
	public Object getPartitions(String[] dataSetIds, String areaId, String numPartitions, PartitioningType pType);
	public Object getRawData(String[] dataSetIds, String areaId, String numPartitions, PartitioningType pType, int partitionNum);

	//Dataset
	public Object getDataSetInfo(String dataSetId, String areaId);
	public Object getRawData(String dataSetId, String areaId);
	public Object getCorrelatedDataSets(String dataSetId, int maxNum);
	public Object getRelatedDataSets(String dataSetId, int maxNum);
	public Object findByTags(Map<String,String> tags);
	public Object getAvailableAreas(String dataSetId);
	
	
	
	//Area metainformation
	public Object getAreaInfo(String areaId);
	public Object getAreaGeomInfo(String areaId,String geomId);
	
	public Object getNeighbours(String areaId,String geomId);
	
	
	
	
	
}
