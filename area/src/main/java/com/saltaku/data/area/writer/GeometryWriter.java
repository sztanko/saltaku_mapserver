package com.saltaku.data.area.writer;

import java.net.URL;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.saltaku.area.importer.propertymapper.AreaFeatures;
import com.saltaku.beans.AreaGeometry;
import com.vividsolutions.jts.geom.Geometry;

public interface GeometryWriter {

	public void init(String id, URL outputPath, CoordinateReferenceSystem cs) throws GeometryWriterException;
	public void writeFeature(Geometry g,AreaGeometry areaFeatures) throws GeometryWriterException;
	public void end() throws GeometryWriterException;
	
	
}
