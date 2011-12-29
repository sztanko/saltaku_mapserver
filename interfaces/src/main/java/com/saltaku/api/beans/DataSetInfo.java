package com.saltaku.api.beans;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opengis.geometry.Envelope;

import com.saltaku.beans.Area;
import com.saltaku.beans.DataSet;

public class DataSetInfo {
	public DataSet dataSet;
	public Map<String,String> tags;
	public Map<String,Area> availableAreas;
	public Map<String, Set<String>> availableAggregations;
}
