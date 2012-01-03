package com.saltaku.api.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import com.google.inject.Inject;
import com.saltaku.api.APIException;
import com.saltaku.api.DataSetAPI;
import com.saltaku.beans.Area;
import com.saltaku.beans.DataSource;
import com.saltaku.store.DBStore;
import com.saltaku.store.DBStoreException;

public class SimpleDataSetApi implements DataSetAPI {

	DBStore store;
	
	@Inject
	public SimpleDataSetApi(DBStore store)
	{
		this.store=store;
	}
	
	public String uploadDataSource(String data, String uploaderName, boolean isPublic, String geoKeyType, int geoKeyColumn, int outputAreaId, String[] aggregators, char separator,
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
		d=new double[columns.size()][ar.numItems+1];
		String line[]=null;
		int lineNum=0;
			while((line=r.readNext())!=null)
			{
				if(lineNum++<=start_line) continue;
				String geoKey=line[geoKeyColumn];
				int id=store.lookupGeoKey(outputAreaId, geoKey);
				int ei=0;
				for(Entry<String,Integer> e: columns.entrySet())
				{
				d[ei++][id]=Double.parseDouble(line[e.getValue()]);	
				}
			}
		} catch (IOException e) {
			throw new APIException(e);
		} catch (DBStoreException e) {
			throw new APIException(e);
		}
		
		return null;
	}

	public DataSource guessDataSet(String data, String geoKeyType, int geoKeyColumn, int outputAreaId) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	public String replaceDataSetTags(int dataSetId, Map<String, String> tag) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	public String replaceDataSetTag(int dataSetId, String tagName, String tagNewValue) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	public String replaceDataSourceTags(int dataSourceId, Map<String, String> tag) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	public String replaceDataSourceTag(int dataSourceId, String tagName, String tagNewValue) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	public String changeDataSet(int dataSetId, String name, Date startDate, Date endDate) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	public String addToDataSet(int dataSetId, String key, String val) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() throws APIException {
		// TODO Auto-generated method stub

	}

}
