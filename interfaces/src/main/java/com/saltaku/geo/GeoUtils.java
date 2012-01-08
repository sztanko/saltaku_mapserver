package com.saltaku.geo;

import java.util.List;

import com.saltaku.beans.Area;
import com.saltaku.beans.DataSet;

public interface GeoUtils {

	public String getBboxOfData(double[] data, String areaId);
	public int getMatchingGeometry(String areaId, double x, double y);
	
	public List<Area> suggestChildren(String areaId);
	
	public List<Area> suggestParents(String areaId);
}
