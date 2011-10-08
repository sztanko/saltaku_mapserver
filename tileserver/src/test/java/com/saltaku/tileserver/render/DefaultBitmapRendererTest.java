package com.saltaku.tileserver.render;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.saltaku.tileserver.guice.DefaultTestModule;
import com.saltaku.tileserver.providers.basemaps.BasemapCompressor;
import com.saltaku.tileserver.providers.basemaps.BasemapProvider;
import com.saltaku.tileserver.providers.basemaps.BasemapProviderException;
import com.saltaku.tileserver.providers.mappings.MappingProvider;
import com.saltaku.tileserver.providers.mappings.MappingProviderException;
import com.saltaku.tileserver.providers.palette.PaletteProvider;
import com.saltaku.tileserver.providers.translator.TranslatorProvider;
import com.saltaku.tileserver.render.impl.DefaultBitmapRenderer;


public class DefaultBitmapRendererTest {
	private BasemapProvider basemapProvider;
	private MappingProvider mappingProvider;
	private BitmapRenderer bitmapRenderer;
	private BitmapRenderer slowBitmapRenderer;
	private TranslatorProvider translatorProvider;
	private BasemapCompressor compressor;
	private PaletteProvider paletteProvider;
	
	@Before
	public void before()
	{
		Injector injector = Guice.createInjector(new DefaultTestModule());
		this.bitmapRenderer=injector.getInstance(BitmapRenderer.class);
		this.basemapProvider=injector.getInstance(BasemapProvider.class);
		this.mappingProvider=injector.getInstance(MappingProvider.class);
		this.translatorProvider=injector.getInstance(TranslatorProvider.class);
		this.compressor=injector.getInstance(BasemapCompressor.class);
		this.paletteProvider=injector.getInstance(PaletteProvider.class);
		this.slowBitmapRenderer=new DefaultBitmapRenderer();
	}
	
	@After
	public void after() throws BasemapProviderException
	{
		this.basemapProvider.stop();
	}
	
	@Test
	public void renderTileTest() throws FileNotFoundException, BasemapProviderException, MappingProviderException, BitmapRendererException
	{
		System.out.println("Rendering to file");
		this.renderTile(4105,2685,13); // 4 shapes
		this.renderTile(4105,2684,13); // 1 shape
		this.renderTile(126,83,8); // shitloads of shapes
		this.renderTile(31,20,6); // shitloads of shapes. I mean, the largest number of shapes ever
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=258&y=168&z=9
		this.renderTile(258,168,9); // 87 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=1031&y=672&z=11
		this.renderTile(1031,672,11); // 13 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=2065&y=1348&z=12
		this.renderTile(2065,1348,12); // 3 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=32749&y=21771&z=16
		this.renderTile(32749,21771,16); // 6 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=65495&y=43542&z=17
		this.renderTile(65495,43542,17); // 2 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=130986&y=87086&z=18
		this.renderTile(130986,87086,18); // 2 geom
	}
	
	@Test
	public void renderTileSpeedTest() throws FileNotFoundException, BasemapProviderException, MappingProviderException, BitmapRendererException
	{
		System.out.println("Rendering to file");
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=130986&y=87086&z=18
		this.compareRenderSpeedTile(130986,87086,18); // 2 geom
		this.compareRenderSpeedTile(4105,2685,13); // 4 shapes
		this.compareRenderSpeedTile(4105,2684,13); // 1 shape
		this.compareRenderSpeedTile(126,83,8); // shitloads of shapes
		this.compareRenderSpeedTile(31,20,6); // shitloads of shapes. I mean, the largest number of shapes ever
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=258&y=168&z=9
		this.compareRenderSpeedTile(258,168,9); // 87 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=1031&y=672&z=11
		this.compareRenderSpeedTile(1031,672,11); // 13 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=2065&y=1348&z=12
		this.compareRenderSpeedTile(2065,1348,12); // 3 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=32749&y=21771&z=16
		this.compareRenderSpeedTile(32749,21771,16); // 6 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=65495&y=43542&z=17
		this.compareRenderSpeedTile(65495,43542,17); // 2 geom
		
	}
	
	@Test
	public void renderTileInMemoryTest() throws FileNotFoundException, BasemapProviderException, MappingProviderException, BitmapRendererException
	{
		System.out.println("Rendering to memory");
		this.renderTileInMemory(4105,2685,13); // 4 shapes
		this.renderTileInMemory(4105,2684,13); // 1 shape
		this.renderTileInMemory(126,83,8); // shitloads of shapes
		this.renderTileInMemory(31,20,6); // shitloads of shapes. I mean, the largest number of shapes ever
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=258&y=168&z=9
		this.renderTileInMemory(258,168,9); // 87 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=1031&y=672&z=11
		this.renderTileInMemory(1031,672,11); // 13 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=2065&y=1348&z=12
		this.renderTileInMemory(2065,1348,12); // 3 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=32749&y=21771&z=16
		this.renderTileInMemory(32749,21771,16); // 6 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=65495&y=43542&z=17
		this.renderTileInMemory(65495,43542,17); // 2 geom
		//http://www.saltaku.com/cache/tile.php?id=zkkq5jb0&x=130986&y=87086&z=18
		this.renderTileInMemory(130986,87086,18); // 2 geom
	}
	
	public void renderTile(int x, int y, int z) throws BasemapProviderException, MappingProviderException, FileNotFoundException, BitmapRendererException
	{
		long t1=System.nanoTime();
		int[]  baseMap = basemapProvider.getBasemapForTile("lsoa", x, y, z);

		int[] mapping = mappingProvider.getMapping("test");
		int[] bitmap = translatorProvider.translateBaseMap(baseMap, mapping);
		int[] palette = paletteProvider.getPalette("default");
		long t2=System.nanoTime();
		this.bitmapRenderer.writeBitmap(256, 256, bitmap, palette, new FileOutputStream("resources/test/tiles/"+z+"_"+x+"_"+y+".png"));
		long t3=System.nanoTime();
		int s=compressor.compress(bitmap).length;
		long t4=System.nanoTime();
		System.out.println("Took "+(t2-t1)/1000+"mks to generate, "+(t3-t2)/1000+" to draw, "+(t4-t3)/1000+" to compress(+"+s+"b), "+z+"_"+x+"_"+y+".png");
	}
	
	public void compareRenderSpeedTile(int x, int y, int z) throws BasemapProviderException, MappingProviderException, FileNotFoundException, BitmapRendererException
	{
		long t1=System.nanoTime();
		int[]  baseMap = basemapProvider.getBasemapForTile("lsoa", x, y, z);

		int[] mapping = mappingProvider.getMapping("test");
		int[] bitmap = translatorProvider.translateBaseMap(baseMap, mapping);
		int[] palette = paletteProvider.getPalette("default");
		long t2=System.nanoTime();
		this.bitmapRenderer.writeBitmap(256, 256, bitmap, palette, new FileOutputStream("resources/test/tiles/"+z+"_"+x+"_"+y+".png"));
		long t3=System.nanoTime();
		this.slowBitmapRenderer.writeBitmap(256, 256, bitmap, palette, new FileOutputStream("resources/test/tiles/"+z+"_"+x+"_"+y+".slow.png"));
		long t4=System.nanoTime();
		int s=compressor.compress(bitmap).length;
		long t5=System.nanoTime();
		System.out.println((t3-t1)/1000000+" ms in total. Took "+(t2-t1)/1000+"mks to generate, "+(t3-t2)/1000+" fast, "+(t4-t3)/1000+" slow, "+(t4-2*t3+t2)/(t3-t2)+"x speedup, "+z+"_"+x+"_"+y+".png");
		//System.out.println("Took "+(t2-t1)/1000+"mks to generate, "+(t3-t2)/1000+" to draw, "+(t4-t3)/1000+" to compress(+"+s+"b), "+z+"_"+x+"_"+y+".png");
	}
	
	public void renderTileInMemory(int x, int y, int z) throws BasemapProviderException, MappingProviderException, FileNotFoundException, BitmapRendererException
	{
		long t1=System.nanoTime();
		int[]  baseMap = basemapProvider.getBasemapForTile("lsoa", x, y, z);

		int[] mapping = mappingProvider.getMapping("test");
		int[] bitmap = translatorProvider.translateBaseMap(baseMap, mapping);
		int[] palette = paletteProvider.getPalette("default");
		long t2=System.nanoTime();
		this.bitmapRenderer.writeBitmap(256, 256, bitmap, palette, new ByteArrayOutputStream(1000));
		long t3=System.nanoTime();
		int s=compressor.compress(bitmap).length;
		long t4=System.nanoTime();
		System.out.println("Took "+(t2-t1)/1000+"mks to generate, "+(t3-t2)/1000+" to draw, "+(t4-t3)/1000+" to compress(+"+s+"b), "+z+"_"+x+"_"+y+".png");
	}
}
