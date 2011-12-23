package com.saltaku.area;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.saltaku.area.importer.Importer;
import com.saltaku.area.importer.db.DbStore;
import com.saltaku.area.importer.db.impl.EmptyDbStore;
import com.saltaku.area.importer.db.impl.MongoDbStore;
import com.saltaku.area.importer.db.impl.MySqlDbStore;
import com.saltaku.area.importer.impl.ShpImporter;
import com.saltaku.area.importer.propertymapper.PropertyMapper;
import com.saltaku.area.importer.propertymapper.impl.AttributePropertyMapper;

public class AreaModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(Importer.class).to(ShpImporter.class);
//		bind(DbStore.class).to(MySqlDbStore.class);
		bind(DbStore.class).to(MongoDbStore.class);
//		bind(DbStore.class).to(EmptyDbStore.class);

		//This is real mercator
		bind(String.class).annotatedWith(Names.named("wgs_84")).toInstance("GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]]");
		
		//This is actually the EPSG:3857, gmaps projection
		//bind(String.class).annotatedWith(Names.named("wgs_84")).toInstance("PROJCS[\"WGS 84 / Pseudo-Mercator\",		       GEOGCS[\"WGS 84\",		         DATUM[\"World Geodetic System 1984\",		           SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]],		           AUTHORITY[\"EPSG\",\"6326\"]],		         PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],		         UNIT[\"degree\", 0.017453292519943295],		         AXIS[\"Geodetic latitude\", NORTH],		         AXIS[\"Geodetic longitude\", EAST],		         AUTHORITY[\"EPSG\",\"4326\"]],		       PROJECTION[\"Popular Visualisation Pseudo Mercator\", AUTHORITY[\"EPSG\",\"1024\"]],		       PARAMETER[\"Latitude of natural origin\", 0.0],		       PARAMETER[\"Longitude of natural origin\", 0.0],		       PARAMETER[\"False easting\", 0.0],		       PARAMETER[\"False northing\", 0.0],		       UNIT[\"metre\", 1.0],		       AXIS[\"Easting\", EAST],		       AXIS[\"Northing\", NORTH],		       AUTHORITY[\"EPSG\",\"3857\"]]");
		
		bind(PropertyMapper.class).annotatedWith(Names.named("lsoa")).toInstance(new AttributePropertyMapper("LSOA04CD", "LSOA04NM", "LSOA04NM"));
	}
	
@Provides
public Connection provideConnection()
{
	try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/area", "saltaku", "saltaku");
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}

@Provides
public DB provideMongoDB()
{
	try {
		return new Mongo().getDB("area");
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MongoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
	
}
	
}
