/**
 * 
 */
package com.saltaku.tileserver.providers.basemaps.impl;


import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.geotools.feature.FeatureCollection;
import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.geometry.Envelope;

import com.saltaku.tileserver.providers.basemaps.BasemapCompressor;
import com.saltaku.tileserver.providers.feature.FeatureProviderException;
import com.saltaku.tileserver.providers.feature.TileUtils;
import com.saltaku.tileserver.providers.feature.impl.ShapeFileFeatureProvider;


/**
 * @author dimi
 *
 */
public class RLECompressorTest {

	private DefaultBasemapRenderer renderer;
	String wgs="GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]]";
	private final Logger log=Logger.getLogger("DefaultBasemapRendererTest");
	protected TileUtils tileUtils;
	protected ShapeFileFeatureProvider p;
	private  BasemapCompressor compressorRLE=new RLECompressor();
	private  BasemapCompressor compressorDeflate=new ZipCompressor();
	private  BasemapCompressor compressorNo=new NoCompressor();
	
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.renderer=new DefaultBasemapRenderer("aid", log);
		this.tileUtils=new TileUtils(new ReferencingObjectFactory().createFromWKT(wgs));
		this.p=new ShapeFileFeatureProvider("resources/test/shapefiles");
	}
	
	@Test
	public void simpleCompDecompTest()
	{
		int[] zero5={0,0,0,0,0};
		this.showOutput(compressorRLE, zero5);
		int[] zero1zero={0,0,1,0,0};
		this.showOutput(compressorRLE, zero1zero);
		int[] largeint={-16750537,2,-16750537,-16750537,-167505370,-167505370};
		this.showOutput(compressorRLE, largeint);
	}
	
	@Test
	public void testCompression() throws FeatureProviderException, IOException
	{
		this.compDecompImage(4105,2685,13); // 4 shapes
		this.compDecompImage(4105,2684,13); // 1 shape
		this.compDecompImage(126,83,8); // shitloads of shapes
		this.compDecompImage(31,20,6); // shitloads of shapes. I mean, the largest number of shapes ever
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=258&y=168&z=9
		this.compDecompImage(258,168,9); // 87 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=1031&y=672&z=11
		this.compDecompImage(1031,672,11); // 13 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=2065&y=1348&z=12
		this.compDecompImage(2065,1348,12); // 3 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=32749&y=21771&z=16
		this.compDecompImage(32749,21771,16); // 6 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=65495&y=43542&z=17
		this.compDecompImage(65495,43542,17); // 2 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=130986&y=87086&z=18
		this.compDecompImage(130986,87086,18); // 2 geom
		this.compDecompImage(2095776,1393391,22); //1 geom
	}
	
	
	@Test
	public void testDeCompressionSpeed() throws FeatureProviderException, IOException
	{
	
		this.repeatDecomp(126,83,8,compressorDeflate,compressorRLE,500); // 
		this.repeatDecomp(258,168,9,compressorDeflate,compressorRLE,500); //
		this.repeatDecomp(130986,87086,18,compressorDeflate,compressorRLE,500); // 
	}
	
	@Test
	public void testCompressionSpeed() throws FeatureProviderException, IOException
	{
	
		this.repeatComp(126,83,8,compressorDeflate,compressorRLE,50); // 
		this.repeatComp(258,168,9,compressorDeflate,compressorRLE,50); //
		this.repeatComp(130986,87086,18,compressorDeflate,compressorRLE,50); // 
	}
	
	@Test
	public void testCompressionOutput() throws FeatureProviderException, IOException
	{
	
		byte[] out=this.compressorDeflate.compress(this.draw(258,168,9));
		System.out.print("{");
		
		for(int i =0;i<out.length;i++)
		{
			System.out.print(out[i]+",");
		}
		System.out.print("}");
		//String s=new String(out);
		//System.out.println(s);
		
		//this.repeatComp(126,83,8,compressorDeflate,compressorRLE,50); // 
		//this.repeatComp(258,168,9,compressorDeflate,compressorRLE,50); //
		//this.repeatComp(130986,87086,18,compressorDeflate,compressorRLE,50); // 
	}
	
		
	protected void compDecompImage(int x,int y, int z) throws FeatureProviderException, IOException
	{
		int[] bytes=this.draw(x, y, z);
		this.compdecomp(compressorNo, bytes);
		this.compdecomp(compressorRLE, bytes);
		this.compdecomp(compressorDeflate, bytes);
	}
	
	
	protected int[] draw(int x, int y, int z) throws FeatureProviderException
	{
		long t1=System.currentTimeMillis();
		Envelope bbox=tileUtils.getTileEnvelope(x,y,z);
		FeatureCollection c=p.get("lsoa", bbox);
		int[] buff=this.renderer.drawBasemap(256,256, c, bbox);
		
		long t2=System.currentTimeMillis();
		int s=c.size();
		log.info("Drawing "+c.size()+" geometries for " +x+","+y+","+z+" took "+(t2-t1)+"ms "+((float)(t2-t1)/s)+"ms/geom");
		return buff;
	}
	
	protected void compdecomp( BasemapCompressor compressor, int[] data)
	{
		
		//long t1=System.currentTimeMillis();
		long t1=System.nanoTime();
		byte[] compressed=compressor.compress(data);
		long t2=System.nanoTime();
		int[] decompressed=compressor.decompress(compressed);
		long t3=System.nanoTime();
		System.out.println(compressor.getClass().getSimpleName() +":\t  "+(t2-t1)/1000+"mks/"+(t3-t2)/1000+"mks,\tlength:"+ compressed.length+",\tratio "+(compressed.length*10000/(2*data.length))/100.0+"%");
		//this.showOutput(data);
		Assert.assertArrayEquals(data, decompressed);
	}
	
	protected void showOutput( BasemapCompressor compressor,int[] in)
	{
		byte[] c=compressor.compress(in);
		int[] out=compressor.decompress(c);
		for(int i=0;i<in.length;i++)
		{
			System.out.println(i+": "+in[i]+"="+out[i]);
		}
	}

	protected void repeatDecomp(int x,int y, int z, BasemapCompressor compressor1,BasemapCompressor compressor2, int numTimes) throws FeatureProviderException
	{
		int[] in =this.draw(x, y, z);
		byte[] c1=compressor1.compress(in);
		byte[] c2=compressor2.compress(in);
		
		long t1=System.currentTimeMillis();
		for(int i=0;i<numTimes;i++){
			compressor1.decompress(c1);
		}
		long t2=System.currentTimeMillis();
		
		long t3=System.currentTimeMillis();
		for(int i=0;i<numTimes;i++){
			compressor2.decompress(c2);
		}
		long t4=System.currentTimeMillis();
		
		System.out.println(compressor1.getClass().getSimpleName() +":\t  "+(t2-t1)+"ms");
		System.out.println(compressor2.getClass().getSimpleName() +":\t  "+(t4-t3)+"ms");
	}
	
	
	protected void repeatComp(int x,int y, int z, BasemapCompressor compressor1,BasemapCompressor compressor2, int numTimes) throws FeatureProviderException
	{
		int[] in =this.draw(x, y, z);
		
		long t1=System.currentTimeMillis();
		for(int i=0;i<numTimes;i++){
			compressor1.compress(in);
		}
		long t2=System.currentTimeMillis();
		
		long t3=System.currentTimeMillis();
		for(int i=0;i<numTimes;i++){
			compressor2.compress(in);
		}
		long t4=System.currentTimeMillis();
		
		System.out.println(compressor1.getClass().getSimpleName() +":\t  "+(t2-t1)+"ms");
		System.out.println(compressor2.getClass().getSimpleName() +":\t  "+(t4-t3)+"ms");
	}

}
