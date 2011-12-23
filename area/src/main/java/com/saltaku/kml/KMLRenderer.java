package com.saltaku.kml;

import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;


public interface KMLRenderer {

	public void render(Map<String,Geometry> features, String out);
	
}
