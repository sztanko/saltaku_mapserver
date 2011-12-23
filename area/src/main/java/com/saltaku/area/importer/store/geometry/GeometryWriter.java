package com.saltaku.area.importer.store.geometry;

import java.net.URL;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.saltaku.area.importer.propertymapper.AreaFeatures;
import com.vividsolutions.jts.geom.Geometry;

public interface GeometryWriter {

	public void init(String id, URL outputPath, CoordinateReferenceSystem cs) throws GeometryWriterException;
	public void writeFeature(Geometry g,AreaFeatures areaFeatures) throws GeometryWriterException;
	public void end() throws GeometryWriterException;
	
	
}
