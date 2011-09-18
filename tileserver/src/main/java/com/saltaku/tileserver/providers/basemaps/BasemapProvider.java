package com.saltaku.tileserver.providers.basemaps;

import com.saltaku.tileserver.providers.feature.FeatureProviderException;

public interface BasemapProvider {
	
	public int[] getBitmap(String shapeId, int x, int y, int z) throws FeatureProviderException;
}
