package com.saltaku.tileserver.providers.basemaps.impl;

import java.util.HashMap;
import java.util.Map;

import org.opengis.geometry.Envelope;

import com.saltaku.tileserver.providers.basemaps.BasemapRenderer;
import com.saltaku.tileserver.providers.basemaps.BasemapProvider;
import com.saltaku.tileserver.providers.feature.FeatureProvider;
import com.saltaku.tileserver.providers.feature.FeatureProviderException;
import com.saltaku.tileserver.providers.feature.TileUtils;

public class InMemoryBasemapProvider implements BasemapProvider {

	private final Map<String, int[]> buffer=new HashMap<String, int[]>();
	private BasemapRenderer renderer;
	private FeatureProvider featureProvider;
	private TileUtils tileUtils;
	
	public int[] getBitmap(String shapeId, int x, int y, int z) throws FeatureProviderException {
		String key=shapeId+"_"+x*1000000+y*100+"_"+z;
		int[] out=null;
		if(buffer.containsKey(key)){
			out=buffer.get(key);
		}
		else
		{
			Envelope bbox=tileUtils.getTileEnvelope(x, y, z);
			out = renderer.drawBasemap(featureProvider.get(shapeId, bbox),bbox);
			buffer.put(key,out);
		}
		return out;
	}

	
	
}
