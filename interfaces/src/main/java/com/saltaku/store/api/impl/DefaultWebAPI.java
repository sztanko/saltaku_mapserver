package com.saltaku.store.api.impl;

import java.util.Map;

import com.saltaku.store.api.PartitioningType;
import com.saltaku.store.api.WebAPI;

public class DefaultWebAPI implements WebAPI {

	public Object getPartitions(String[] dataSetIds, String areaId, String numPartitions, PartitioningType pType) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getRawData(String[] dataSetIds, String areaId, String numPartitions, PartitioningType pType, int partitionNum) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getDataSetInfo(String dataSetId, String areaId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getRawData(String dataSetId, String areaId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getCorrelatedDataSets(String dataSetId, int maxNum) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getRelatedDataSets(String dataSetId, int maxNum) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object findByTags(Map<String, String> tags) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getAreaInfo(String areaId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getAreaGeomInfo(String areaId, String geomId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getNeighbours(String areaId, String geomId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getAvailableAreas(String dataSetId) {
		// TODO Auto-generated method stub
		return null;
	}

}
