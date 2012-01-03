package com.saltaku.api;

import java.util.Map;

import com.saltaku.api.annotations.API;
import com.saltaku.api.annotations.APICall;
import com.saltaku.api.annotations.APIParam;
import com.saltaku.api.beans.AreaGeometryData;
import com.saltaku.api.beans.DataSetInfo;
import com.saltaku.api.beans.PartitioningType;
import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;
import com.saltaku.beans.DataSet;
import com.saltaku.beans.DataSetData;

@API(name="api")
public interface WebAPI {

	@APICall(description="Get partitions of a dataset")
	public Map<String,Double>[] getPartitions(
			@APIParam(description="the id of dataset") 
				int dataSetId, 
				int areaId, 
			@APIParam(isMandatory=false, defaultValue="-1", description="Data set id for normalization. Currently not used.") 
				int normalizerId, 
				String aggregation,
			@APIParam(isMandatory=false, defaultValue="10") 
				int numPartitions,
			@APIParam(isMandatory=false, defaultValue="LABEL", description="Possible values: LABEL, VALUE") 
				PartitioningType pType) throws APIException;

	@APICall(description="Get partition of a comparition of two datasets")
	public Map<String,Double>[][][] getPartitions2(
				int dataSetId1, 
				int dataSetId2, 
			@APIParam(isMandatory=false, defaultValue="-1", description="Dataset id for normalization. Currently not used.") 
				int normalizerId, 
				int areaId, 
			@APIParam(isMandatory=false, defaultValue="avg") 
				String aggregation, 
			@APIParam(isMandatory=false, defaultValue="10") 
				int numPartitions, 
			@APIParam(isMandatory=false, defaultValue="LABEL", description="Possible values: LABEL, VALUE")
				PartitioningType pType) throws APIException;
	
	@APICall
	public AreaGeometryData[] getData(int dataSetId, int areaId, @APIParam(isMandatory=false, defaultValue="-1", description="Data set id for normalization. Currently not used.") int normalizerId, String aggregation, double min, double max) throws APIException;
	
	@APICall
	public AreaGeometryData[] getDataById(int dataSetId, int areaId, @APIParam(isMandatory=false, defaultValue="-1", description="Data set id for normalization. Currently not used.") int normalizerId, String aggregation, int[] idList) throws APIException;
	
	@APICall
	public DataSetInfo getDataSetInfo(int dataSetId) throws APIException;
	
	@APICall
	public DataSetData getRawData(int dataSetId, int areaId, @APIParam(isMandatory=false, defaultValue="-1", description="Data set id for normalization. Currently not used.") int normalizerId, String aggregation) throws APIException;
	
	@APICall
	public DataSet[] getCorrelatedDataSets(int dataSetId,@APIParam(isMandatory=false, defaultValue="10") int maxNum) throws APIException;
	
	@APICall
	public Object getRelatedDataSets(int dataSetId,@APIParam(isMandatory=false, defaultValue="10") int maxNum) throws APIException;

	@APICall
	public DataSet[] findByTags(Map<String,String> tags, int maxNum) throws APIException;
	
	@APICall
	public Area getAreaInfo(int areaId) throws APIException;
	
	@APICall
	public AreaGeometry getAreaGeometry(int areaId, int geomId) throws APIException;
	
	@APICall
	public AreaGeometry getNeighbours(int areaId, int geomId,@APIParam(isMandatory=false, defaultValue="10") int maxNum) throws APIException;
	
	public void close() throws APIException;
	
}
