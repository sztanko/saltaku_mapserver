package com.saltaku.area.importer.impl;


import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.saltaku.area.importer.Importer;
import com.saltaku.area.importer.beans.AreaShape;
import com.saltaku.area.importer.db.DbStore;
import com.saltaku.area.importer.exceptions.ImportException;
import com.saltaku.area.importer.propertymapper.AreaFeatures;
import com.saltaku.area.importer.propertymapper.PropertyMapper;
import com.saltaku.area.importer.store.geometry.GeometryWriter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;

public class ShpImporter implements Importer{
private DbStore dbStore;
private final String targetRef;
private CRSFactory csFactory = new ReferencingObjectFactory();

	
@Inject 
public ShpImporter(@Named("wgs_84") String targetRef,DbStore st){
	//this.sql=sql;
	this.targetRef=targetRef;
	this.dbStore=st;
	
	//System.out.println(targetRef);
}


public void exec(URL inputPath, String outputPath,PropertyMapper pMapper, GeometryWriter geomWriter) throws ImportException
	{
	
	long total=0l;
	long storeTime=0l;
	long writerTime=0l;
	long transformTime=0;
	long t1, t2,t3;
	
	try{
		ShapefileDataStore inStore = new ShapefileDataStore(inputPath);
		
        String name = inStore.getTypeNames()[0];
        FeatureSource inSource = inStore.getFeatureSource(name);
        FeatureCollection inResults = inSource.getFeatures();
        FeatureType inSchema = inSource.getSchema();
        System.out.println(DataUtilities.spec(inSchema));
        
        for(PropertyDescriptor pd: inSchema.getDescriptors())
        {
        		System.out.println(pd.getName()+" "+pd.getType().getName()+" "+pd.getType().getDescription()+" "+pd.getType().getClass());
        	
        
        }
		  for(String s: inStore.getTypeNames())
		  {
			  System.out.println(s);
		  }
		  t1=System.nanoTime();
		  String areaId=dbStore.insertArea(inputPath.toString(), inputPath.toString());
		  t2=System.nanoTime();
		  storeTime+=(t2-t1);
		  
		  //System.out.println(inSchema.getCoordinateReferenceSystem().toWKT());
		  t1=System.nanoTime();
		  CoordinateReferenceSystem cs= csFactory.createFromWKT(this.targetRef);
		  //CoordinateFilter transFilter = new 
		  MathTransform transform = CRS.findMathTransform(inSchema.getCoordinateReferenceSystem(), cs, true);
		  t2=System.nanoTime();
		  transformTime+=(t2-t1);
		  
		  t1=System.nanoTime();
		  geomWriter.init(inputPath.toString(), new URL(outputPath), cs);
		  t2=System.nanoTime();
		  writerTime+=(t2-t1);
		  
		  Geometry bb=null;
          double minArea=Double.MAX_VALUE;
          double maxArea=0.0;
          double area=0.0;
          int geometryCount=0;
          
          FeatureIterator fIterator=inResults.features();
          int geomId=1;
          while (fIterator.hasNext()) {
              Feature inFeature = fIterator.next();
              t1=System.nanoTime();
              GeometryAttribute ga = inFeature.getDefaultGeometryProperty();
              Geometry g=(Geometry)ga.getValue();
              
              AreaFeatures f=pMapper.getFeatures(inFeature.getProperties());
              f.setId(geomId);
              Geometry g2=JTS.transform(g, transform);
              
              
              AreaShape shp = new AreaShape();
              shp.setAreaId(areaId);
              shp.setShape(g2);
              shp.setArea(g2.getArea());
              shp.setSimplifiedGeometry(TopologyPreservingSimplifier.simplify(g2, Math.sqrt(shp.getArea())/8));
              shp.setBbox(g2.getEnvelope());
              if(bb==null)
              {
            	  bb=shp.getBbox();
              }
              else
              {
            	  bb=shp.getBbox().union(bb).getEnvelope();
              }
              area+=shp.getArea();
              if(shp.getArea()<minArea) minArea=shp.getArea();
              if(shp.getArea()>maxArea) maxArea=shp.getArea();
              geometryCount++;
              if(geometryCount%100==0)
            	  System.out.println("shape #"+geometryCount);
              shp.setCentroid(g2.getCentroid());
              shp.setFeatures(f);
              t2=System.nanoTime();
              transformTime+=(t2-t1);
              
              t1=System.nanoTime();
              dbStore.insertAreaGeometry(shp);
              t2=System.nanoTime();
              storeTime+=(t2-t1);
              
              t1=System.nanoTime();
              geomWriter.writeFeature(g2, f);
              t2=System.nanoTime();
              writerTime+=(t2-t1);
              geomId++;
              }
          fIterator.close();
          t1=System.nanoTime();
          geomWriter.end();
          t2=System.nanoTime();
          writerTime+=(t2-t1);
          
          t1=System.nanoTime();
          dbStore.updateAreaGeoFeatures(areaId,bb, geometryCount, area, minArea, maxArea);
          dbStore.commit();   
          t2=System.nanoTime();
          storeTime+=(t2-t1);
          
          System.out.println("Store time: "+storeTime/1000000+"ms");
          System.out.println("Writer time: "+writerTime/1000000+"ms");
          System.out.println("Transform time: "+transformTime/1000000+"ms");
          
	}
	catch(Exception e)
	{
		throw new ImportException(e);
	}
          }
          
          
        	  

	
}
