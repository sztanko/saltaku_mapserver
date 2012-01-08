package com.saltaku.data.server.guice;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.geotools.renderer.lite.RendererUtilities;
import org.opengis.referencing.FactoryException;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;



public class DefaultModule  extends AbstractModule{
	
	Properties props;
	public DefaultModule(Properties props){
		this.props=props;
	}
	
	@Override
	protected void configure() {
	
		//	bind(TileUtils.class).toInstance(new TileUtils(new ReferencingObjectFactory().createFromWKT(wgs) ));
	
		
		bind(String.class).annotatedWith(Names.named("idFieldName")).toInstance("aid");
		bind(String.class).annotatedWith(Names.named("lucenePath")).toInstance("/tmp/tileserver/test");
		bind(String.class).annotatedWith(Names.named("bdbPath")).toInstance("/tmp/bdbtest");
		
		bind(String.class).annotatedWith(Names.named("shapeFileStorepath")).toInstance("resources/test/shapefiles");
		bind(String.class).annotatedWith(Names.named("csvPath")).toInstance("resources/test/datasets");
		bind(Character.class).annotatedWith(Names.named("csvDelimiter")).toInstance(';');
		bind(Integer.class).annotatedWith(Names.named("csvValueField")).toInstance(2);
		bind(Long.class).annotatedWith(Names.named("csvCacheSize")).toInstance(20l);
		/*try {
			bind(int[].class).annotatedWith(Names.named("colorMapping")).toInstance(TranslatorUtils.translateRgb(new FileInputStream("resources/colours/standard.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		
		
		
		
	}

}
