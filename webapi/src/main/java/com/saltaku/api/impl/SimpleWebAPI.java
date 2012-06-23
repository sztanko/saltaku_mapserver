package com.saltaku.api.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.saltaku.api.APIException;
import com.saltaku.api.WebAPI;
import com.saltaku.api.beans.AreaGeometryData;
import com.saltaku.api.beans.DataSetInfo;
import com.saltaku.api.beans.DataType;
import com.saltaku.api.beans.PartitioningType;
import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;
import com.saltaku.beans.Correlation;
import com.saltaku.beans.DataSet;
import com.saltaku.beans.DataSetData;
import com.saltaku.beans.Tag;
import com.saltaku.data.AggregateDataUtils;
import com.saltaku.data.Aggregator;
import com.saltaku.data.CompareDataUtils;
import com.saltaku.data.PartitionDataUtils;
import com.saltaku.store.DBStore;
import com.saltaku.store.DBStoreException;


public class SimpleWebAPI implements WebAPI{

	DBStore dbStore;
	
	@Inject
	public SimpleWebAPI(DBStore dbStore)
	{
		this.dbStore=dbStore;
	}
	
	public Map<String,Double>[] getPartitions(String dataSetId, String areaId, String normalizerId, String aggregation, int numPartitions, PartitioningType pType) throws APIException {
		Map<String,Double>[] partitionsData=null;
		int[] partitions=null;
		try{
		DataSetData dsData=dbStore.getRawData(dataSetId, areaId, aggregation);
		
		if(pType.equals(PartitioningType.LABEL))
			partitions=PartitionDataUtils.partitionDataByEqualSize(dsData.data, numPartitions);
		else
		{
			partitions=PartitionDataUtils.partitionDataByValues(dsData.data, numPartitions);
		}
		
		double[][] map = AggregateDataUtils.remap(dsData.data, partitions);
		
		partitionsData=new Map[numPartitions];
		for(int i=0;i<numPartitions;i++) partitionsData[i]=new HashMap<String, Double>();
		for(Aggregator agg: AggregateDataUtils.availableAggregators){
			double[] aggregates=AggregateDataUtils.aggregate(map, agg);
			String aggName=agg.getName();
			for(int i=0;i<partitionsData.length;i++)
			{
				if(aggregates[i+1]!=Double.NEGATIVE_INFINITY)
					partitionsData[i].put(aggName, aggregates[i+1]);
			}
		}
		}
		catch(DBStoreException e)
		{
			throw new APIException(e);
		}
		return partitionsData;
	}


	public Map<String,Double>[][][] getPartitions2(String dataSetId1, String dataSetId2, String normalizerId, String areaId, String aggregation, int numPartitions, PartitioningType pType) throws APIException {
		try{
		DataSetData dsData1=dbStore.getRawData(dataSetId1, areaId, aggregation);
		DataSetData dsData2=dbStore.getRawData(dataSetId2, areaId, aggregation);
		int[][] partitions=null;
		if(pType.equals(PartitioningType.LABEL))
			partitions=CompareDataUtils.partitionDataByEqualSize(numPartitions, dsData1.data, dsData2.data);
		else
		{
			partitions=CompareDataUtils.partitionDataByValues(numPartitions, dsData1.data, dsData2.data);
		}
		
		Map<String,Double>[][][] partitionsData=new Map[numPartitions][numPartitions][2];
		double[][][][] map = AggregateDataUtils.remap( dsData1.data, dsData2.data, partitions);
		for(int ic=0;ic<numPartitions;ic++)
		{
		
		for(int i=0;i<numPartitions;i++){ 
			partitionsData[ic][i][0]=new HashMap<String, Double>(); 
			partitionsData[ic][i][1]=new HashMap<String, Double>();
			}
		
		for(Aggregator agg: AggregateDataUtils.availableAggregators){
			String aggName=agg.getName();
			
			//double[] aggregates=AggregateDataUtils.aggregate(map[ic][0], agg);
			for(int i=0;i<numPartitions;i++)
			{
				double a=AggregateDataUtils.aggregateSafe(map[ic+1][i+1][0],agg);
				if(a!=Double.NEGATIVE_INFINITY)
				partitionsData[ic][i][0].put(aggName, a);
			}
			//aggregates=AggregateDataUtils.aggregate(map[ic][1], agg);
			for(int i=0;i<numPartitions;i++)
			{
				double a=AggregateDataUtils.aggregateSafe(map[ic+1][i+1][1],agg);
				if(a!=Double.NEGATIVE_INFINITY)
					partitionsData[ic][i][1].put(aggName, a);
			}
		}
		}
		return partitionsData;
		}
		catch(DBStoreException e)
		{
			throw new APIException(e);
		}
	}


	public AreaGeometryData[] getData(String dataSetId, String areaId,String normalizerId, String aggregation, double min, double max) throws APIException {
		try{
		DataSetData dsData=dbStore.getRawData(dataSetId, areaId,aggregation);
		int c=0;
		for(double d: dsData.data) if(d>=min && d<=max) c++;
		int[] idList=new int[c];
		c=0;
		for(int i=0;i<dsData.data.length;i++) 
			if(dsData.data[i]>=min && dsData.data[i]<=max) idList[c++]=i;
		AreaGeometry[] geometries=dbStore.getAreaGeometry(areaId, idList,false);
		AreaGeometryData[] out=new AreaGeometryData[c];
		for(int i=0;i<c;i++)
		{
			out[i]=new AreaGeometryData();
			out[i].areaGeometry=geometries[i];
			out[i].value=dsData.data[idList[i]];
		}
		
		return out;
		}
		catch(DBStoreException e)
		{
			throw new APIException(e);
		}
	}
	
	
	public AreaGeometryData[] getDataById(String dataSetId, String areaId,String normalizerId, String aggregation, int[] idList) throws APIException {
	try{
		DataSetData dsData=dbStore.getRawData(dataSetId, areaId,aggregation);
		AreaGeometry[] geometries=dbStore.getAreaGeometry(areaId, idList,false);
		AreaGeometryData[] out=new AreaGeometryData[idList.length];
		for(int i=0;i<idList.length;i++)
		{
			out[i]=new AreaGeometryData();
			out[i].areaGeometry=geometries[i];
			out[i].value=dsData.data[idList[i]];
		}
		return out;
	}
	catch(DBStoreException e)
	{
		throw new APIException(e);
	}
		
	}


	public DataSetInfo getDataSetInfo(String dataSetId) throws APIException {
		try{
			DataSet dataSet=dbStore.getDataSet(dataSetId);
		Tag[] tags=dbStore.getTags(dataSetId,DataType.DATASET.toString());
		DataSetData[] availableData=dbStore.getAvailableDataForDataSet(dataSetId);
		DataSetInfo out=new DataSetInfo();
		out.dataSet=dataSet;
		out.tags=new HashMap<String,String>();
		for(Tag tag: tags)
		{
			out.tags.put(tag.name, tag.value);
		}
		Map<String, Set<String>> availableAggregations=new HashMap<String,Set<String>>();
		Map<String,Area> availableAreas=new HashMap<String,Area>();
		Map<String,String> availableAreaIds=new HashMap<String,String>();
		for(DataSetData data: availableData)
		{
			String areaName=availableAreaIds.get(data.areaId);
			if(areaName==null){
				Area area=dbStore.getArea(data.areaId);
				areaName=area.name;
				availableAreaIds.put(data.areaId,areaName);
				availableAreas.put(area.name, area);
				availableAggregations.put(areaName, new HashSet<String>());
			}
			availableAggregations.get(areaName).add(data.aggregation); 
			
		}
		out.availableAreas=availableAreas;
		out.availableAggregations=availableAggregations;
		
		return out;
	}
	catch(DBStoreException e)
	{
		throw new APIException(e);
	}
	}
	
	public DataSetData getRawData(String dataSetId, String areaId, String normalizerId,String aggregation) throws APIException{
	try{
		return dbStore.getRawData(dataSetId, areaId, aggregation);
	}
	catch(DBStoreException e)
	{
		throw new APIException(e);
	}
	}

	public List<Correlation> getCorrelatedDataSets(String dataSetId, String correlationType, int maxNum) throws APIException {
		try{
		return this.dbStore.getCorrelatedDataSets(dataSetId,correlationType, 0.0, maxNum);
		}
	catch(DBStoreException e)
	{
		throw new APIException(e);
	}
	}


	public Object getRelatedDataSets(String dataSetId, int maxNum) {
		// TODO Auto-generated method stub
		return null;
	}


	public DataSet[] findByTags(Map<String, String> tags, int maxNum) {
		// TODO Auto-generated method stub
		return null;
	}

	public Area getAreaInfo(String areaId) throws APIException {
	try{
		return dbStore.getArea(areaId);
	}
	catch(DBStoreException e)
	{
		throw new APIException(e);
	}
	}


	public AreaGeometry getAreaGeometry(String areaId, int geomId) throws APIException {
		try{
		int[] ids=new int[1];
		ids[0]=geomId;
		return dbStore.getAreaGeometry( areaId, ids, true)[0];
		}
		catch(DBStoreException e)
		{
			throw new APIException(e);
		}
	}


	public AreaGeometry getNeighbours(String areaId, int geomId, int maxNum) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void close() throws APIException
	{
		try {
			this.dbStore.close();
		} catch (DBStoreException e) {
			throw new APIException(e);
		}
	}
	
	
	

}
