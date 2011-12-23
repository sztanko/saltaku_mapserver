package com.saltaku.area.importer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.geotools.feature.SchemaException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import com.saltaku.area.importer.exceptions.ImportException;
import com.saltaku.area.importer.propertymapper.PropertyMapper;
import com.saltaku.area.importer.store.geometry.GeometryWriter;

public interface Importer {
	void exec(URL inputPath, String outputPath,PropertyMapper pMapper, GeometryWriter writer) throws ImportException;
}
