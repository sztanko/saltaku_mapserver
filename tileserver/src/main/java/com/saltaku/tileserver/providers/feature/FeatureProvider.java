package com.saltaku.tileserver.providers.feature;

import org.geotools.feature.FeatureCollection;
import org.opengis.geometry.Envelope;


public interface FeatureProvider {
public FeatureCollection get(String shapeId, Envelope bbox) throws FeatureProviderException;
public Envelope getEnvelope(String shapeId) throws FeatureProviderException;
//public boolean inside(String shapeId, int x, int y, int z);
}
