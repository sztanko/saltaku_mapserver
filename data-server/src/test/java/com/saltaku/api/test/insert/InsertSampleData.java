package com.saltaku.api.test.insert;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.saltaku.area.importer.db.DbStore;
import com.saltaku.data.area.relationfinder.RelationFinder;
import com.saltaku.data.area.relationfinder.RelationFinderException;
import com.saltaku.data.server.guice.DefaultModule;
import com.saltaku.geo.GeoException;
import com.saltaku.geo.GeoProcessor;
import com.saltaku.store.DBStore;
import com.saltaku.store.DBStoreException;

public class InsertSampleData {

	/**
	 * @param args
	 * @throws GeoException 
	 * @throws RelationFinderException 
	 * @throws MalformedURLException 
	 * @throws DBStoreException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws GeoException, MalformedURLException, RelationFinderException, DBStoreException, FileNotFoundException {
		Injector injector = Guice.createInjector(new DefaultModule(new Properties()) );
		// Insert LSOA, MSOA, BOROUGHS - GeoProcessor
		GeoProcessor gp=injector.getInstance(GeoProcessor.class);
		DBStore db=injector.getInstance(DBStore.class);
		
		String lsoaCode=gp.uploadArea("/tmp/shapefiles/lsoa/data.shp", "lsoa", "http://data.gov.uk/dataset/lower_layer_super_output_area_lsoa_boundaries", "code", "name", "name");
		String msoaCode=gp.uploadArea("/tmp/shapefiles/msoa/data.shp", "msoa", "http://data.gov.uk/dataset/lower_layer_super_output_area_lsoa_boundaries", "code", "name", "name");
		String countyCode=gp.uploadArea("/tmp/shapefiles/uk_2/data.shp", "uk counties", "http://data.gov.uk/dataset/lower_layer_super_output_area_lsoa_boundaries", "code", "name", "name");
		// Set relations
		RelationFinder f=injector.getInstance(RelationFinder.class);
		System.setOut(new PrintStream("/tmp/out"));
		int[] rel=f.findRelations(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/"+lsoaCode+"/data.shp"), new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/"+msoaCode+"/data.shp"));
		System.setOut(new PrintStream("/dev/stdout"));
		db.insertAreaMapping(lsoaCode,msoaCode,rel);
		System.setOut(new PrintStream("/tmp/out"));
		 rel=f.findRelations(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/"+msoaCode+"/data.shp"), new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/"+countyCode+"/data.shp"));
		 System.setOut(new PrintStream("/dev/stdout"));
		 db.insertAreaMapping(msoaCode,countyCode,rel);
		 //DBStore db=injector.getInstance(DBStore.class);
		//db.
		//f.findRelations(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/msoa/data.shp"), new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/uk_counties/data.shp"));
		// TODO Insert datasource
		// TODO Aggregate up
		

	}

}
