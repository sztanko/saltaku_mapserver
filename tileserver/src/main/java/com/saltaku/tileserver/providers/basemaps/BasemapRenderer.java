package com.saltaku.tileserver.providers.basemaps;

import org.geotools.feature.FeatureCollection;
import org.opengis.geometry.Envelope;

public interface BasemapRenderer {

	public int[] drawBasemap(int width, int height, FeatureCollection features, Envelope bbox);
	
}
