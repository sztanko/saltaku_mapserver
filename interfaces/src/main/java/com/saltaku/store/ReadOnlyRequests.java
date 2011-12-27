package com.saltaku.store;

import java.util.Map;

public interface ReadOnlyRequests {

	public double[] getData(String datasetId, String areaId);
	public Map<String,String> getInfo(String datasetId);
	
	
	
}
