package com.saltaku.api.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import com.google.inject.Inject;
import com.saltaku.api.APIException;
import com.saltaku.api.DataSetAPI;
import com.saltaku.beans.Area;
import com.saltaku.beans.DataSet;
import com.saltaku.beans.DataSetData;
import com.saltaku.beans.DataSource;
import com.saltaku.beans.DataSourceDataSet;
import com.saltaku.data.AggregateDataUtils;
import com.saltaku.data.Aggregator;
import com.saltaku.data.PartitionDataUtils;
import com.saltaku.store.DBStore;
import com.saltaku.store.DBStoreException;

public class SimpleDataSetApi implements DataSetAPI {

	DBStore store;
	
	@Inject
	public SimpleDataSetApi(DBStore store)
	{
		this.store=store;
	}
	
	public String uploadDataSource(String data, String uploaderName, boolean isPublic, String geoKeyType, int geoKeyColumn, String outputAreaId, String[] aggregators, char separator,
			int start_line, Map<String,Integer> columns) throws APIException {
		//lets first think that is simple tsv file and we have only ID geoKeyType
		CSVReader r=new CSVReader(new StringReader(data));
		double[][] d=null;
		try {
		Area ar=store.getArea(outputAreaId);
		DataSource ds=new DataSource();
		ds.aggregators=aggregators;
		ds.datasourceId=store.generateId("dataSource");
		ds.geoKeyColumn=geoKeyColumn;
		ds.geoKeyType=geoKeyType;
		ds.isPublic=isPublic;
		ds.separator=Character.toString(separator);
		ds.startLine=start_line;
		store.insertDataSource(ds);
		d=new double[columns.size()][ar.numItems+1];
		String line[]=null;
		int lineNum=0;
			while((line=r.readNext())!=null)
			{
				if(lineNum++<=start_line) continue;
				String geoKey=line[geoKeyColumn];
				int id=store.lookupGeoKey(outputAreaId, geoKey);
				if(id>=0){
				int ei=0;
				for(Entry<String,Integer> e: columns.entrySet())
				{
					d[ei++][id]=Double.parseDouble(line[e.getValue()]);	
				}
				}
			}
		int ei=0;
		String[] parentAreaIds=store.getParentAreas(outputAreaId);
		Map<String,int[]> areaMappings=new HashMap<String,int[]>();
		for(String parentAreaId: parentAreaIds)
		{
			int[] mapping=store.getAreaMapping(outputAreaId, parentAreaId);
			areaMappings.put(parentAreaId, mapping); 
		}
		
		for(Entry<String,Integer> e: columns.entrySet())
		{
			DataSourceDataSet dsd=new DataSourceDataSet();
			dsd.column=e.getValue();
			dsd.name=e.getKey();
			dsd.datasourceId=ds.datasourceId;
			dsd.data=d[ei];	
			store.insertDataSourceData(dsd);
			
			DataSet dataSet=new DataSet();
			dataSet.dataSourceId=ds.datasourceId;
			dataSet.end=new Date();
			dataSet.start=new Date();
			dataSet.id=store.generateId("dataSource");
			dataSet.initialAggregation="ID";
			dataSet.initialAreaId=outputAreaId;
			dataSet.name=e.getKey();
			store.insertDataSet(dataSet);
			
			DataSetData dd=new DataSetData();
			dd.aggregation="ID";
			dd.areaId=outputAreaId;
			dd.bbox=ar.bbox;
			dd.data=d[ei++];
			dd.dataSet=dataSet.id;
			
			store.insertDataSetData(dd);
			ei++;
			
			// make data for all parent datasets
			for(Entry<String,int[]> areaMappingEntry: areaMappings.entrySet())
			{
				double[][] remapping=AggregateDataUtils.remap(dd.data, areaMappingEntry.getValue());
				for(Aggregator agg : AggregateDataUtils.availableAggregators)
				{
					DataSetData dmap=new DataSetData();
					dmap.aggregation=agg.getName();
					dmap.areaId=areaMappingEntry.getKey();
					dmap.bbox=ar.bbox;
					dmap.data=AggregateDataUtils.aggregate(remapping, agg);
					dmap.dataSet=dataSet.id;
					store.insertDataSetData(dmap);
				}
			}
			// for each dataset data, calculate correlations
			
		}
		} catch (IOException e) {
			throw new APIException(e);
		} catch (DBStoreException e) {
			throw new APIException(e);
		}
		
		return "ok";
	}

	public DataSource guessDataSet(String data, String geoKeyType, int geoKeyColumn, String outputAreaId) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	public String replaceDataSetTags(String dataSetId, Map<String, String> tag) throws APIException {
		try {
			store.changeTags(dataSetId, "dataSet", tag);
			return "ok";
		} catch (DBStoreException e) {
			throw new APIException(e);
		}
		
	}

	public String replaceDataSetTag(String dataSetId, String tagName, String tagNewValue) throws APIException {
		try {
			store.changeTag(dataSetId, "dataSet", tagName, tagNewValue);
			return "ok";
		} catch (DBStoreException e) {
			throw new APIException(e);
		}
	}

	public String replaceDataSourceTags(String dataSourceId, Map<String, String> tag) throws APIException {
		try {
			store.changeTags(dataSourceId, "dataSource", tag);
			return "ok";
		} catch (DBStoreException e) {
			throw new APIException(e);
		}
	}

	public String replaceDataSourceTag(String dataSourceId, String tagName, String tagNewValue) throws APIException {
		// TODO implement replaceDataSourceTag
		return null;
	}

	public String changeDataSet(String dataSetId, String name, Date startDate, Date endDate) throws APIException {
		try {
			DataSet ds=store.getDataSet(dataSetId);
			ds.name=name;
			ds.start=startDate;
			ds.end=endDate;
			store.changeDataSet(ds);
			return "ok";
		} catch (DBStoreException e) {
			throw new APIException(e);
		}
			}

	public String addToDataSet(String dataSetId, String key, String val) throws APIException {
		// TODO implement addToDataSet
		return null;
	}

	public void close() throws APIException {
		try {
			store.close();
		} catch (DBStoreException e) {
			throw new APIException(e);
		}
		
	}

}
