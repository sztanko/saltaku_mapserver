package com.saltaku.data.area;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.saltaku.data.area.writer.GeometryWriter;
import com.saltaku.data.area.writer.impl.ShpGeometryWriter;
import com.saltaku.data.area.writer.io.DataStoreProvider;
import com.saltaku.data.area.writer.io.impl.LocalFSDataStoreProvider;
import com.saltaku.geo.GeoProcessor;

public class AreaModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(GeometryWriter.class).to(ShpGeometryWriter.class);
		bind(DataStoreProvider.class).to(LocalFSDataStoreProvider.class);
		bind(GeoProcessor.class).to(ShapeAreaProcessor.class);
		bind(GeometryWriter.class).to(ShpGeometryWriter.class);
		bind(String.class).annotatedWith(Names.named("targetRef")).toInstance("GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]]");
		
	}
	
	
}
