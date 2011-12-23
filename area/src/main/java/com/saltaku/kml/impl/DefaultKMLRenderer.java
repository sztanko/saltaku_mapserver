package com.saltaku.kml.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;


import com.saltaku.kml.KMLRenderer;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;


public class DefaultKMLRenderer implements KMLRenderer{

	public void render(Map<String, Geometry> features, String out) {
		StringBuilder buf=new StringBuilder();
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<kml xmlns=\"http://www.opengis.net/kml/2.2\"><Document>");
		
		for(Entry<String,Geometry> entry: features.entrySet())
		{
			buf.append("\n<Placemark>\n");
			buf.append("<name>").append(entry.getKey()).append("</name>\n");
			buf.append("<LinearRing>\n");
			buf.append("<extrude>0</extrude>\n");
			buf.append("<coordinates>\n");
			for(Coordinate c: entry.getValue().getCoordinates())
			{
			buf.append(c.x).append(",").append(c.y).append("\n");	
			}
			//buf.append("</outerBoundaryIs>");
			buf.append("</coordinates>\n</LinearRing>\n");
			buf.append("</Placemark>\n");
			
			
			
		}
		buf.append("</Document></kml>");
		OutputStream w;
		try {
			w = new FileOutputStream(out,false);
			w.write(buf.toString().getBytes());
			w.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
