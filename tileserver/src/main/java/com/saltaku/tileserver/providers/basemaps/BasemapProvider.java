package com.saltaku.tileserver.providers.basemaps;

import org.opengis.geometry.Envelope;

import com.saltaku.tileserver.providers.feature.FeatureProviderException;

public interface BasemapProvider {
	
	public int[] getBasemapForTile(String shapeId, int x, int y, int z) throws BasemapProviderException;
	public int[] getBasemapForMap(String shapeId, Envelope env,int width, int height) throws BasemapProviderException;
	public void stop() throws BasemapProviderException;
}
