package com.saltaku.tileserver.providers.feature;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.google.inject.Inject;
import com.google.inject.name.Named;



public class TileUtils {
	
	//x=126&y=83&z=8  -- lots of shapes
	//x=8194&y=5445&z=14 -- medium number of shapes
	// x=4105&y=2684&z=13 - 1 shape
	// x=4105&y=2685&z=13 - shapes
	
private CoordinateReferenceSystem crs;

@Inject
public TileUtils(@Named("coordinateReferenceSystem") CoordinateReferenceSystem crs)
{
	this.crs=crs;
}


public Envelope getTileEnvelope(int x, int y, int z)
{
	double north = tile2lat(y, z);
	double south = tile2lat(y + 1, z);
	double west = tile2lon(x, z);
	double east = tile2lon(x + 1, z);
	Envelope bb = new ReferencedEnvelope(west, east, north, south, crs);
	
	return bb;
	
}

static double tile2lon(int x, int z) {
 return x / Math.pow(2.0, z) * 360.0 - 180;
}

static double tile2lat(int y, int z) {
double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
return Math.toDegrees(Math.atan(Math.sinh(n)));
}

public static String getEnvelopeKey(Envelope env)
{
	double[] c=env.getLowerCorner().getCoordinate();
	StringBuilder b=new StringBuilder();
	b.append(c[0]).append('|').append(c[1]).append('|');
	c=env.getUpperCorner().getCoordinate();
	b.append(c[0]).append('|').append(c[1]);
	return b.toString();
}
}
