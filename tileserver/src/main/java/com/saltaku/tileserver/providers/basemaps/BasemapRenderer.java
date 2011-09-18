package com.saltaku.tileserver.providers.basemaps;

import org.geotools.feature.FeatureCollection;
import org.opengis.geometry.Envelope;

public interface BasemapRenderer {

	public int[] drawBasemap(FeatureCollection features, Envelope bbox);
	
}
