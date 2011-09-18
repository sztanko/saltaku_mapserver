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
import org.junit.Before;
import org.junit.Test;
import org.opengis.geometry.Envelope;

import com.saltaku.tileserver.providers.feature.FeatureProviderException;
import com.saltaku.tileserver.providers.feature.TileUtils;
import com.saltaku.tileserver.providers.feature.impl.ShapeFileFeatureProvider;


/**
 * @author dimi
 *
 */
public class DefaultBasemapRendererTest {

	private DefaultBasemapRenderer renderer;
	String wgs="GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]]";
	private final Logger log=Logger.getLogger("DefaultBasemapRendererTest");
	protected TileUtils tileUtils;
	protected ShapeFileFeatureProvider p;
	
	
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
	public void testRenderer() throws FeatureProviderException, IOException
	{
		this.drawImage(4105,2685,13); // 4 shapes
		this.drawImage(4105,2684,13); // 1 shape
		this.drawImage(126,83,8); // shitloads of shapes
		this.drawImage(31,20,6); // shitloads of shapes. I mean, the largest number of shapes ever
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=258&y=168&z=9
		this.drawImage(258,168,9); // 87 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=1031&y=672&z=11
		this.drawImage(1031,672,11); // 13 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=2065&y=1348&z=12
		this.drawImage(2065,1348,12); // 3 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=32749&y=21771&z=16
		this.drawImage(32749,21771,16); // 6 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=65495&y=43542&z=17
		this.drawImage(65495,43542,17); // 2 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=130986&y=87086&z=18
		this.drawImage(130986,87086,18); // 2 geom
	}
	
	@Test
	public void testAffineTransform()
	{
		this.transform(130986,87086,18); // 2 geom
	}
	
	protected void drawImage(int x,int y, int z) throws FeatureProviderException, IOException
	{
		int[] bytes=this.draw(x, y, z);
		int count=0;
		for(int pixel: bytes)
		{
			if(pixel!=0){
				count++;
				if(count<30){ 
					this.printC(pixel);
				}
			}
		}
		System.out.println();
		System.out.println(bytes);
		System.out.println();
		long t1=System.currentTimeMillis();
		BufferedImage bi=new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
		bi.setRGB(0, 0, 256, 256, bytes, 0, 256);
		File f=new File("resources/test/tiles");
		if(!f.exists()) f.mkdirs();
		File outputfile = new File(f,x+"_"+y+"_"+z+".png");
		ImageIO.write(bi, "png", outputfile);
		long t2=System.currentTimeMillis();
		log.info("Writing took "+(t2-t1)+"ms");
	}
	
	
	protected int[] draw(int x, int y, int z) throws FeatureProviderException
	{
		long t1=System.currentTimeMillis();
		Envelope bbox=tileUtils.getTileEnvelope(x,y,z);
		FeatureCollection c=p.get("lsoa", bbox);
		int[] buff=this.renderer.drawBasemap( c, bbox);
		
		long t2=System.currentTimeMillis();
		int s=c.size();
		log.info("Drawing "+c.size()+" geometries for " +x+","+y+","+z+" took "+(t2-t1)+"ms "+((float)(t2-t1)/s)+"ms/geom");
		
		return buff;
	}
	
	protected void transform(int x, int y, int z)
	{
		
		Envelope e=tileUtils.getTileEnvelope(x, y, z);
		log.info("Transforming "+e.toString());
		AffineTransform at=renderer.getAffineTransform(e);
		log.info("Transformation matrix is: "+at.toString());
		Point2D p1=new Point2D.Double(e.getMinimum(0), e.getMaximum(1));
		log.info(p1.toString());
		log.info(at.transform(p1, null).toString());
		Point2D p2=new Point2D.Double(e.getMaximum(0), e.getMinimum(1));
		log.info(p2.toString());
		log.info(at.transform(p2, null).toString());
			
			//new Point2d();
	}
	
	protected void printC(int pixel)
	{
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel ) & 0xff;
		System.out.print(Integer.toBinaryString(pixel)+":("+alpha+","+red+","+green+","+blue+") ");
	}
	
	

}
