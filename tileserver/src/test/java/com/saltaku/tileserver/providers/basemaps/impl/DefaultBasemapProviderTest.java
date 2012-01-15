package com.saltaku.tileserver.providers.basemaps.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.print.attribute.standard.Finishings;

import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.referencing.FactoryException;

import com.saltaku.tileserver.providers.basemaps.BasemapCompressor;
import com.saltaku.tileserver.providers.basemaps.BasemapProviderException;
import com.saltaku.tileserver.providers.basemaps.storage.BasemapStorageException;
import com.saltaku.tileserver.providers.basemaps.storage.impl.LuceneBasemapStorage;
import com.saltaku.tileserver.providers.feature.FeatureProviderException;
import com.saltaku.tileserver.providers.feature.TileUtils;
import com.saltaku.tileserver.providers.feature.impl.ShapeFileFeatureProvider;


public class DefaultBasemapProviderTest {
private DefaultBasemapProvider provider;
	
String wgs="GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]]";

	@Before
	public void init() throws BasemapStorageException, FactoryException
	{
		provider=new DefaultBasemapProvider(new LuceneBasemapStorage("/tmp/lucenetest"), 
				new TileUtils(new ReferencingObjectFactory().createFromWKT(wgs)), 
				new ShapeFileFeatureProvider("resources/test/shapefiles" ), new ZipCompressor(), 
				new DefaultBasemapRenderer("aid", Logger.getLogger("DefaultBasemapRenderer")));
		
	}
	
	@After
	public void shut() throws BasemapProviderException
	{
		this.provider.stop();
	}
	
	@Test
	public void testSimple() throws BasemapProviderException, InterruptedException
	{
		this.provider.getBasemapForTile("lsoa", 126,83,8); // shitloads
		this.provider.getBasemapForTile("lsoa",31,20,6); // shitloads of shapes. I mean, the largest number of shapes ever
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=258&y=168&z=9
		this.provider.getBasemapForTile("lsoa",258,168,9); // 87 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=1031&y=672&z=11
		this.provider.getBasemapForTile("lsoa",1031,672,11); // 13 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=2065&y=1348&z=12
		this.provider.getBasemapForTile("lsoa",2065,1348,12); // 3 geom
		
		System.out.println("Sleeping for one sec, then trying once again");
		Thread.sleep(1000);
		
		this.provider.getBasemapForTile("lsoa", 126,83,8); // shitloads
		this.provider.getBasemapForTile("lsoa",31,20,6); // shitloads of shapes. I mean, the largest number of shapes ever
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=258&y=168&z=9
		this.provider.getBasemapForTile("lsoa",258,168,9); // 87 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=1031&y=672&z=11
		this.provider.getBasemapForTile("lsoa",1031,672,11); // 13 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=2065&y=1348&z=12
		this.provider.getBasemapForTile("lsoa",2065,1348,12); // 3 geom
	}
	
	@Test
	public void tryConcurrent() throws InterruptedException
	{
		//this.concurrentTest(100, 30);
		this.concurrentTest(1, 4);
		//this.concurrentTest(3, 10);
		System.gc();
		this.concurrentTest(5, 30); //83006.0ms, 13.83ms per tile
		this.concurrentTest(10, 20); //77889.0ms, 12.9815ms per tile
		//this.concurrentTest(60, 10); //77169.0ms, 12.8615ms per tile
		this.concurrentTest(120, 5); //72788.0ms, 12.131333333333334ms per tile
		this.concurrentTest(300, 2); //70168.0ms, 11.694666666666667ms per tile
		this.concurrentTest(600, 1); //120945.0ms, 20.1575ms per tile
	}
	
	
	public void concurrentTest(int numTries, int numThreads) throws InterruptedException
	{
		System.out.println("Generating tile names");
		List<Tile> tileList=new ArrayList<Tile>();
		this.retrieveRecursive(tileList, 31,20,6, 12);
		Thread c[]=new Thread[numThreads];
		long t1=System.nanoTime();
		for(int i=0;i<c.length;i++)
		{
			c[i]=new Thread(new TileClient(this.provider,tileList,numTries));
			c[i].start();
		}
		
		boolean isFinish=false;
		while(!isFinish){
			boolean isFinished=true;
			Thread.sleep(100);
			for(int i=0;i<c.length;i++)
			{
				if(c[i].isAlive()) isFinished=false;
			}
			isFinish=isFinished;
			Thread.sleep(50);
		}
		long t2=System.nanoTime();
		double time=Math.round((t2-t1)/1000)/1000;
		System.out.println("It all took "+time+"ms, "+time/(numTries*numThreads)+"ms per tile");
		System.out.println("Total num of tiles available: "+tileList.size());
		System.out.println("Number of misses: "+this.provider.getNumMisses()+" out of "+(numTries*c.length)+", miss ratio: "+this.provider.getNumMisses()*10000/(numTries*c.length)/100.0+"%");
		
	}
	
	class TileClient implements Runnable
	{
		private List<Tile> tileSet;
		private int numTries;
		private DefaultBasemapProvider provider;
		
		
		public TileClient(DefaultBasemapProvider p, List<Tile> tileSet, int numTries){
			this.provider=p;
			this.numTries=numTries;
			this.tileSet=tileSet;
		
		}
		public void run() {
	          int size=tileSet.size();
	          
	          for(int i=0;i<numTries;i++)
	          {
	        	  int index=new Random().nextInt(size);
	        	  
					this.get(tileSet.get(index));
				
	          }
	          
	          
	         }
		
		private void get(Tile t)
		{
			
				try {
					provider.getBasemapForTile("lsoa", t.x, t.y, t.z);
				} catch (BasemapProviderException e) { 
					e.printStackTrace();
				}
			
		}
	}
	
	protected int retrieveRecursive(List<Tile> tileList,int x, int y, int z, int maxDepth) 
	{
		if(z>maxDepth) return 0;
		//byte[] bm=compressor.compress(this.draw(x,y,z));
		//byteMap.put(x+"-"+y+"-"+z,bm);
		//if(z==maxDepth)	
			tileList.add(new Tile(x,y,z));
		int size=1;
		//maxTiles--;
		retrieveRecursive(tileList,x*2, y*2, z+1, maxDepth);
		retrieveRecursive(tileList,x*2+1, y*2, z+1, maxDepth);
		retrieveRecursive(tileList, x*2+1, y*2+1, z+1, maxDepth);
		retrieveRecursive(tileList,x*2, y*2+1, z+1, maxDepth);
		return size;
	}
	
	 class Tile{
		public int x, y, z;
		public Tile(int x, int y, int z){
			this.x=x;
			this.y=y;
			this.z=z;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + x;
			result = prime * result + y;
			result = prime * result + z;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Tile other = (Tile) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			if (z != other.z)
				return false;
			return true;
		}
		private DefaultBasemapProviderTest getOuterType() {
			return DefaultBasemapProviderTest.this;
		}
		
	};
	
	
	
}
