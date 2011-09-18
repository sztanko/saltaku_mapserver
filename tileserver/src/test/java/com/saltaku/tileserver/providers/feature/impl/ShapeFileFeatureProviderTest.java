package com.saltaku.tileserver.providers.feature.impl;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.eclipse.jetty.util.log.Log;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;

import com.saltaku.tileserver.providers.feature.FeatureProviderException;
import com.saltaku.tileserver.providers.feature.TileUtils;
import com.saltaku.tileserver.providers.feature.impl.ShapeFileFeatureProvider;
import com.vividsolutions.jts.geom.Geometry;

public class ShapeFileFeatureProviderTest {
	private final Logger log=Logger.getLogger("test");
	String wgs="GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]]";
	protected TileUtils tileUtils;
	protected ShapeFileFeatureProvider p;
	protected int geomCount=0;
	
	@Before
	public void init() throws FactoryException
	{
		this.tileUtils=new TileUtils(new ReferencingObjectFactory().createFromWKT(wgs));
		this.p=new ShapeFileFeatureProvider("resources/test/shapefiles");
	}
	
	@Test
	public void test1() throws FeatureProviderException {
		
		//x=126&y=83&z=8  -- lots of shapes
		//x=8194&y=5445&z=14 -- medium number of shapes
		// x=4105&y=2684&z=13 - 1 shape
		// x=4105&y=2685&z=13 - shapes
		this.getCollectionSize(4105,2685,13); // 4 shapes
		this.getCollectionSize(4105,2684,13); // 1 shape
		this.getCollectionSize(126,83,8); // shitloads of shapes
		this.getCollectionSize(31,20,6); // shitloads of shapes. I mean, the largest number of shapes ever
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=258&y=168&z=9
		this.getCollectionSize(258,168,9); // 87 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=1031&y=672&z=11
		this.getCollectionSize(1031,672,11); // 13 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=2065&y=1348&z=12
		this.getCollectionSize(2065,1348,12); // 3 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=32749&y=21771&z=16
		this.getCollectionSize(32749,21771,16); // 6 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=65495&y=43542&z=17
		this.getCollectionSize(65495,43542,17); // 2 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=130986&y=87086&z=18
		this.getCollectionSize(130986,87086,18); // 2 geom
	}
	
	@Test
	public void test2() throws FeatureProviderException {
		
		long t1=System.currentTimeMillis();
		int numTiles=this.retrieveRecursive(31,20,6,10);
		//log.info("Retrieved "+numTiles+" in overall"); // shitloads of shapes. I mean, the largest number of shapes ever
		long t2=System.currentTimeMillis();
		log.info("Retrieving "+this.geomCount+" geoms for "+numTiles+" tiles took "+(t2-t1)+"ms"+" "+(t2-t1)/numTiles+"ms per Tile");
		log.info("In average, "+this.geomCount/numTiles+" geoms per tile");
	}
	
	@Test
	public void test3() throws FeatureProviderException {
		int x=130986, y=87086, z=18;
		long t1=System.currentTimeMillis();
		Envelope env=tileUtils.getTileEnvelope(x,y,z);
		log.info("Envelope is "+env.toString());
		FeatureCollection features = p.get("lsoa", env);
		 FeatureIterator fIterator=features.features();
		 while (fIterator.hasNext()) {
        	 Feature f=fIterator.next();
        	 Geometry g=(Geometry) f.getDefaultGeometryProperty().getValue();
        	 //log.info(f.getName().toString());
        	 log.info("Geometry type: "+g.getGeometryType());
        	 log.info(g.toText());
		 }
		long t2=System.currentTimeMillis();
		
	}
	
	@Test
	public void test4() throws FeatureProviderException {
		
		long t1=System.currentTimeMillis();
		int numTiles=this.retrieveRecursive(130986,87086,18,22);
		//log.info("Retrieved "+numTiles+" in overall"); // shitloads of shapes. I mean, the largest number of shapes ever
		long t2=System.currentTimeMillis();
		log.info("Retrieving "+this.geomCount+" geoms for "+numTiles+" tiles took "+(t2-t1)+"ms"+" "+(t2-t1)/numTiles+"ms per Tile");
		log.info("In average, "+this.geomCount/numTiles+" geoms per tile");
	}
	
	protected int getCollectionSize(int x, int y, int z) throws FeatureProviderException
	{
		long t1=System.currentTimeMillis();
		FeatureCollection coll = p.get("lsoa", tileUtils.getTileEnvelope(x,y,z));
		int size=coll.size();
		long t2=System.currentTimeMillis();
		log.info("Getting "+size+" geometries for " +x+","+y+","+z+" took "+(t2-t1)+"ms");
		geomCount+=size;
		return size;
	}
	
	protected int retrieveRecursive(int x, int y, int z, int maxDepth) throws FeatureProviderException
	{
		if(z>maxDepth) return 0;
		this.getCollectionSize(x,y,z);
		int size=1;
		size+=retrieveRecursive(x*2, y*2, z+1, maxDepth);
		size+=retrieveRecursive(x*2+1, y*2, z+1, maxDepth);
		size+=retrieveRecursive(x*2+1, y*2+1, z+1, maxDepth);
		size+=retrieveRecursive(x*2, y*2+1, z+1, maxDepth);
		return size;
	}

	
	
}
