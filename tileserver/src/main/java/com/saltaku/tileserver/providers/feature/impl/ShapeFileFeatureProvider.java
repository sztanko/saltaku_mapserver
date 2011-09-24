package com.saltaku.tileserver.providers.feature.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.saltaku.tileserver.providers.feature.FeatureProvider;
import com.saltaku.tileserver.providers.feature.FeatureProviderException;
import com.saltaku.tileserver.providers.feature.TileUtils;



public class ShapeFileFeatureProvider implements FeatureProvider {
	private String path;
	private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
	
	Map<String,Envelope> envelopes=new HashMap<String,Envelope>();
	
	@Inject
	public ShapeFileFeatureProvider(@Named("shapeFileStorepath") String shapeFileStorepath){
		this.path=shapeFileStorepath;
	}
	
	public FeatureCollection get(String shapeId, Envelope env) throws FeatureProviderException {
		try {
			FeatureSource source=this.loadShapeFile(shapeId);
			FeatureType schema = source.getSchema();
			String geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
		    CoordinateReferenceSystem targetCRS = schema.getGeometryDescriptor()
		            .getCoordinateReferenceSystem();
		    Filter filter = ff.bbox(
		    			geometryPropertyName,
		    			env.getMinimum(0),
		    			env.getMinimum(1),
		    			env.getMaximum(0),
		    			env.getMaximum(1),
		    			targetCRS.toString());// TODO I am worried about this a little bit
		    		//"THE_GEOM", env);
		    return source.getFeatures(filter);
			
		} catch (IOException e) {
			throw new FeatureProviderException(e);
		}
	}

	public Envelope getEnvelope(String shapeId) throws FeatureProviderException {
		Envelope env=null;
		try{
		if(this.envelopes.containsKey(shapeId))
		{
			env=this.envelopes.get(shapeId);
		}
		else
		{
			synchronized(this.envelopes)
			{
				if(this.envelopes.containsKey(shapeId))
				{
					return this.envelopes.get(shapeId);
				}		
				FeatureSource source=this.loadShapeFile(shapeId);
				Envelope bb=source.getBounds();
				this.envelopes.put(shapeId, bb);
			}
		}
		}
		catch(IOException e)
		{
			throw new FeatureProviderException(e);
		}
		
		return env;
	}
	
	protected FeatureSource loadShapeFile(String shapeId) throws IOException
	{
		File file = new File(path+"/"+shapeId,"data.shp");
		Map map = new HashMap();
		map.put("url", file.toURI().toURL());
		DataStore dataStore = DataStoreFinder.getDataStore( map );
		String typeName = dataStore.getTypeNames()[0];
		FeatureSource source = dataStore.getFeatureSource(typeName );
		return source;
	}


}
