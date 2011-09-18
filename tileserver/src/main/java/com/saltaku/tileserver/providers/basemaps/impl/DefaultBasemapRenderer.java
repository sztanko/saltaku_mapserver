package com.saltaku.tileserver.providers.basemaps.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.LiteShape;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Stroke;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.spatial.Dimension;

import com.google.inject.Inject;
import com.saltaku.tileserver.providers.basemaps.BasemapRenderer;
import com.vividsolutions.jts.geom.Geometry;

public class DefaultBasemapRenderer implements BasemapRenderer {
	private Logger log;
	private String idFieldName;
	
	@Inject
	public DefaultBasemapRenderer(String idFieldName, Logger log)
	{
		this.idFieldName=idFieldName;
		this.log=log;
	}
	
	public int[] drawBasemap(FeatureCollection features, Envelope bbox) {

		AffineTransform at=this.getAffineTransform(bbox);  
		BufferedImage bi=new BufferedImage(256, 256, 
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g=(Graphics2D)bi.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		 FeatureIterator fIterator=features.features();
		 while (fIterator.hasNext()) {
        	 Feature f=fIterator.next();
        	 Geometry geometry=(Geometry) f.getDefaultGeometryProperty().getValue();
        	 Integer id=(Integer)f.getProperty(idFieldName).getValue();
        	 //log.info("area id "+id+" "+Integer.toBinaryString(id));
        	 Color c=new Color(id);
        	 g.setColor(c);
        	 Shape s=new LiteShape(geometry, at,false );
        	 g.fill(s);
         }
		 
		return bi.getRGB(0, 0, 256, 256, null,0,256);
	}
	
	AffineTransform getAffineTransform(Envelope bbox)
	{
		double scalex=256.0/(bbox.getMaximum(0)-bbox.getMinimum(0));
		double scaley=256.0/(bbox.getMinimum(1)-bbox.getMaximum(1));
		double transx = bbox.getMinimum(0);
		double transy = bbox.getMaximum(1);
		AffineTransform at=new AffineTransform();
		at.scale(scalex, scaley);
		at.translate(-transx, -transy);
		return at;
	}

}
