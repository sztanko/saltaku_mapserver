package com.saltaku.api.test.insert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.saltaku.api.APIException;
import com.saltaku.api.DataSetAPI;
import com.saltaku.beans.relationfinder.DatasetRelation;
import com.saltaku.data.area.relationfinder.RelationFinder;
import com.saltaku.data.area.relationfinder.RelationFinderException;
import com.saltaku.data.area.writer.io.DataStoreProvider;
import com.saltaku.data.server.guice.DefaultModule;
import com.saltaku.geo.GeoException;
import com.saltaku.geo.GeoProcessor;
import com.saltaku.store.DBStore;
import com.saltaku.store.DBStoreException;

public class InsertSampleData {

	String lsoaCode="3079949128780070862";
	String msoaCode="3222430369643083796";
	String countyCode="8485954552965056384";
	Injector injector;
	GeoProcessor gp;
	DBStore db;
	DataStoreProvider dsp;
	RelationFinder f;
	DataSetAPI dataApi;
	/**
	 * @param args
	 * @throws GeoException 
	 * @throws RelationFinderException 
	 * @throws DBStoreException 
	 * @throws APIException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws GeoException, RelationFinderException, DBStoreException, IOException, APIException {
		
	InsertSampleData ids = new InsertSampleData();
	//ids.createArea();
	//ids.findRelations();
		ids.insertDs();

	}
	
	public InsertSampleData()
	{
		injector = Guice.createInjector(new DefaultModule(new Properties()) );
		gp=injector.getInstance(GeoProcessor.class);
		db=injector.getInstance(DBStore.class);
		dsp=injector.getInstance(DataStoreProvider.class);
		f=injector.getInstance(RelationFinder.class);
		dataApi=injector.getInstance(DataSetAPI.class);
	}
	
	public void createArea() throws GeoException
	{
		lsoaCode=gp.uploadArea("/tmp/shapefiles/lsoa/data.shp", "lsoa", "http://data.gov.uk/dataset/lower_layer_super_output_area_lsoa_boundaries", "code", "name", "name");
		msoaCode=gp.uploadArea("/tmp/shapefiles/msoa/data.shp", "msoa", "http://data.gov.uk/dataset/lower_layer_super_output_area_lsoa_boundaries", "code", "name", "name");
		countyCode=gp.uploadArea("/tmp/shapefiles/uk_2/data.shp", "uk counties", "http://data.gov.uk/dataset/lower_layer_super_output_area_lsoa_boundaries", "code", "name", "name");
	
	}
	
	public void findRelations() throws RelationFinderException, FileNotFoundException, DBStoreException
	{
		System.setOut(new PrintStream("/tmp/out"));
		DatasetRelation rel=f.findRelations(dsp.getDataStoreURL(lsoaCode), dsp.getDataStoreURL(msoaCode));
		//System.setOut(new PrintStream("/dev/stdout"));
		
		db.insertAreaMapping(lsoaCode,msoaCode,rel);
		//System.setOut(new PrintStream("/tmp/out"));
		 rel=f.findRelations(dsp.getDataStoreURL(msoaCode), dsp.getDataStoreURL(countyCode));
		 //System.setOut(new PrintStream("/dev/stdout"));
		 db.insertAreaMapping(msoaCode,countyCode,rel);
	}
	
	public void insertDs() throws IOException, APIException
	{
		String[] aggregators={"id"};
		Map<String, Integer> columns=new HashMap<String, Integer>();
		String data=FileUtils.readFileToString(new File("src/test/resources/data/imd/2010/imd20100.CSV"));
		columns.put("Index of Multiple Deprivation", 14);
		columns.put("Income", 16);
		columns.put("Employment", 18);
		columns.put("Health Deprivation and Disability", 20);
		columns.put("Education Skills and Training", 22);
		columns.put("Barriers to Housing and Services", 24);
		columns.put("Crime", 26);
		columns.put("Living Environment", 28);
		dataApi.uploadDataSource(data, "Dimi", true, "ID",10 , lsoaCode, aggregators, '\t', 6, columns);
		
	}

}
