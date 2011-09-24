package com.saltaku.tileserver.guice;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.geotools.renderer.lite.RendererUtilities;
import org.opengis.referencing.FactoryException;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.saltaku.tileserver.providers.basemaps.BasemapCompressor;
import com.saltaku.tileserver.providers.basemaps.BasemapProvider;
import com.saltaku.tileserver.providers.basemaps.BasemapRenderer;
import com.saltaku.tileserver.providers.basemaps.CompressionUtil;
import com.saltaku.tileserver.providers.basemaps.impl.DefaultBasemapProvider;
import com.saltaku.tileserver.providers.basemaps.impl.DefaultBasemapRenderer;
import com.saltaku.tileserver.providers.basemaps.impl.ZipCompressor;
import com.saltaku.tileserver.providers.basemaps.storage.BasemapStorage;
import com.saltaku.tileserver.providers.basemaps.storage.impl.LuceneBasemapStorage;
import com.saltaku.tileserver.providers.feature.FeatureProvider;
import com.saltaku.tileserver.providers.feature.TileUtils;
import com.saltaku.tileserver.providers.feature.impl.ShapeFileFeatureProvider;
import com.saltaku.tileserver.providers.mappings.MappingProvider;
import com.saltaku.tileserver.providers.mappings.impl.CSVMappingProvider;
import com.saltaku.tileserver.providers.translator.TranslatorProvider;
import com.saltaku.tileserver.providers.translator.TranslatorUtils;
import com.saltaku.tileserver.providers.translator.impl.DefaultTranslator;
import com.saltaku.tileserver.render.BitmapRenderer;
import com.saltaku.tileserver.render.impl.DefaultBitmapRenderer;
import com.saltaku.tileserver.render.impl.FastBitmapRenderer;


public class DefaultTestModule  extends AbstractModule{
	String wgs="GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]]";
	
	
	@Override
	protected void configure() {
		bind(BasemapCompressor.class).to(ZipCompressor.class).in(Singleton.class);
		bind(BasemapProvider.class).to(DefaultBasemapProvider.class).in(Singleton.class);
		bind(BasemapRenderer.class).to(DefaultBasemapRenderer.class).in(Singleton.class);
		bind(CompressionUtil.class).toInstance(new CompressionUtil());
		try {
			bind(TileUtils.class).toInstance(new TileUtils(new ReferencingObjectFactory().createFromWKT(wgs) ));
		} catch (FactoryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bind(BasemapStorage.class).to(LuceneBasemapStorage.class).in(Singleton.class);
		bind(FeatureProvider.class).to(ShapeFileFeatureProvider.class).in(Singleton.class);
		bind(MappingProvider.class).to(CSVMappingProvider.class).in(Singleton.class);
		bind(TranslatorProvider.class).to(DefaultTranslator.class).in(Singleton.class);;
		//bind(BitmapRenderer.class).to(FastBitmapRenderer.class).in(Singleton.class);;
		bind(BitmapRenderer.class).to(DefaultBitmapRenderer.class).in(Singleton.class);;
		//bind(Logger.class).toInstance(Logger.getLogger("Test"));
		
		bind(String.class).annotatedWith(Names.named("idFieldName")).toInstance("aid");
		bind(String.class).annotatedWith(Names.named("lucenePath")).toInstance("/tmp/tileserver/test");
		bind(String.class).annotatedWith(Names.named("shapeFileStorepath")).toInstance("resources/test/shapefiles");
		bind(String.class).annotatedWith(Names.named("csvPath")).toInstance("resources/test/datasets");
		bind(Character.class).annotatedWith(Names.named("csvDelimiter")).toInstance(';');
		bind(Integer.class).annotatedWith(Names.named("csvValueField")).toInstance(2);
		bind(Long.class).annotatedWith(Names.named("csvCacheSize")).toInstance(20l);
		try {
			bind(int[].class).annotatedWith(Names.named("colorMapping")).toInstance(TranslatorUtils.translateRgb(new FileInputStream("resources/colours/standard.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
