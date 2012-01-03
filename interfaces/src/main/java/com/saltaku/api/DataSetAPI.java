package com.saltaku.api;

import java.util.Date;
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
import com.saltaku.beans.DataSource;

@API(name="change")
public interface DataSetAPI {

	@APICall(method="Post")
	public String uploadDataSource(
			String data, String uploaderName, boolean isPublic, String geoKeyType, int geoKeyColumn, int outputAreaId, 
			String[] aggregators, char separator, int start_line, Map<String,Integer> columns) 
				throws APIException;
	
	@APICall(method="Post")
	public DataSource guessDataSet(
			String data, String geoKeyType, int geoKeyColumn, int outputAreaId) 
				throws APIException;
	
	@APICall
	public String replaceDataSetTags(int dataSetId, Map<String,String> tag) throws APIException;
	
	@APICall
	public String replaceDataSetTag(int dataSetId, String tagName, String tagNewValue) throws APIException;
	
	@APICall
	public String replaceDataSourceTags(int dataSourceId, Map<String,String> tag) throws APIException;
	
	@APICall
	public String replaceDataSourceTag(int dataSourceId, String tagName, String tagNewValue) throws APIException;
	
	@APICall
	public String changeDataSet(int dataSetId, String name, Date startDate, Date endDate) throws APIException;
	
	@APICall
	public String addToDataSet(int dataSetId, String key, String val) throws APIException;
	
	public void close() throws APIException;
	
}
