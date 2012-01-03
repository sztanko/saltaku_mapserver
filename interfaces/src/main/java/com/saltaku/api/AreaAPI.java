package com.saltaku.api;

import java.util.Date;
import java.util.List;
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

@API(name="area")
public interface AreaAPI {

	@APICall(method="Post")
	public String uploadArea(
			String shpZipData,String name, String source, String columnForCode, String columnForName, String columnForEnglishName) 
				throws APIException;
	@APICall
	public List<Area> suggestChildren(int areaId) throws APIException;
	
	@APICall
	public List<Area> suggestParents(int areaId) throws APIException;
	
	@APICall
	public List<AreaGeometry> assignChild(int childAreaId, int parentAreaId) throws APIException;
	
	public void close() throws APIException;
	
}
