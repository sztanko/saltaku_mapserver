package com.saltaku.geo;

import java.util.List;

import com.saltaku.api.APIException;
import com.saltaku.api.beans.AreaComparison;
import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;
import com.saltaku.beans.DataSet;

public interface GeoProcessor {

	public String getBboxOfData(int[] geomIds, String areaId) throws GeoException;
	
	public AreaGeometry getMatchingGeometry(String areaId, double x, double y) throws GeoException;
	
	public List<Area> suggestChildren(String areaId) throws GeoException;
	
	public List<Area> suggestParents(String areaId) throws GeoException;
	
	public AreaComparison mapAreas(String parentId, String childId) throws GeoException;
	
	public String uploadArea(String shpZipData, String name, String source, String columnForCode, String columnForName, String columnForEnglishName) throws GeoException;
	
	public void setWorkflowId(String w);
}
