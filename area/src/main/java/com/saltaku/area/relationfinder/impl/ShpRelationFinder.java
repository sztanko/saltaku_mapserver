package com.saltaku.area.relationfinder.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.saltaku.area.relationfinder.RelationFinder;
import com.saltaku.area.relationfinder.RelationFinderException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

@Deprecated
public class ShpRelationFinder implements RelationFinder {

	String columnForCode="aid";
	String columnForName="name";
	String geometryPropertyName="data";
	double threshold=0.3;
	private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
	
	
	public ShpRelationFinder()
	{

	}
	
	public int[] findRelations(URL childArea, URL parentArea) throws RelationFinderException {
		
		Map<String, Geometry> kmlSet=new HashMap<String, Geometry>();
		int out[]=null;
		int numOrphans=0;
		int numGeoms=0;
		double minArea=Double.MAX_VALUE;
		FeatureSource childSource;
		FeatureSource parentSource;
		try {
			childSource = this.loadShapeFile(childArea);
			parentSource=this.loadShapeFile(parentArea);
		//, parentSource;
		
		FeatureIterator fIterator=childSource.getFeatures().features();
		out=new int[childSource.getFeatures().size()+1];
		while(fIterator.hasNext())// && numGeoms++<2000)
		{
			Feature inFeature = fIterator.next();
            GeometryAttribute ga = inFeature.getDefaultGeometryProperty();
            Geometry childG=(Geometry)ga.getValue();
            int childCode=(Integer)this.findAttribute(inFeature, this.columnForCode);
            String childName=(String)this.findAttribute(inFeature, this.columnForName).toString();
           // System.out.println("Checking "+childName);
           // if(numGeoms++%100==0) System.out.println(numGeoms);
            
            com.vividsolutions.jts.geom.Envelope bbox=childG.getEnvelopeInternal();
            //double d=Math.abs(bbox.getMaxX()-bbox.getMinX())+Math.abs(bbox.getMaxY()-bbox.getMinY());
            /*Coordinate[] c=childG.getEnvelope().getCoordinates();
            double minX=c[0].x;
            double minY=c[0].y;
            double maxX=c[2].x;
            double maxY=c[2].y;*/
            FeatureIterator pIterator=this.get(parentSource, bbox.getMinX(),bbox.getMinY(),bbox.getMaxX(), bbox.getMaxY()).features();
            //FeatureIterator pIterator=this.get(parentSource, minX,minY, maxX, maxY).features();
            boolean hasMatch=false;
            out[childCode]=0;
            double maxArea=-1;
            String winnerParent=null;
            Geometry winnerParentG=null;
            long t1=System.nanoTime();
            while(pIterator.hasNext())
            {
            	Feature parentCandidate= pIterator.next();
            Geometry candidateG=(Geometry)(parentCandidate.getDefaultGeometryProperty().getValue());
            int parentCode=(Integer)this.findAttribute(parentCandidate, this.columnForCode);
            String parentName=(String)this.findAttribute(parentCandidate, this.columnForName);
            //System.out.println("Candidate for "+childName+" is "+parentName);
            double area = childG.intersection(candidateG).getArea();
            double areaq=area/childG.getArea();
            if(areaq>maxArea && areaq>=threshold)
            {
            	  out[childCode]=parentCode;
            	  maxArea=areaq;
            	  winnerParent=parentName;
            	  winnerParentG=candidateG;
            	// System.out.println("Winner candidate for "+childName+" is "+parentName+" with intersection of "+(int)(100*areaq)+"%");           
           	  }
            
            }
            long t2=System.nanoTime();
            if(maxArea<=0)
            {
            	  System.out.println(childName+" is orphan");
            	  numOrphans++;
            }
            else
            {
            //	  System.out.println("Parent for "+childName+" is "+winnerParent+" - "+ (int)(maxArea*100)+"%");
            	  System.out.println(childCode+"\t"+out[childCode]+"\t"+childName+"\t"+winnerParent+"\t"+ Math.round(maxArea*10000)/100.0+"\t"+childName.contains(winnerParent)+"\t"+(t2-t1)/1000);
            	/*  if(!childName.contains(winnerParent))
            	  {
            		  kmlSet.put(childName+"(child) has bad parent "+winnerParent+" "+Math.round(maxArea*10000)/100.0, childG);
            		  kmlSet.put(winnerParent+"is a bad parent for"+childName+" "+Math.round(maxArea*10000)/100.0, winnerParentG);
            		  
            	  }else
            		  if(maxArea<=0.4)
            		  {
            			  kmlSet.put(childName+"(child) small intersection with parent "+winnerParent+" "+Math.round(maxArea*10000)/100.0, childG);
                		  kmlSet.put(winnerParent+" (parent) small intersection with child "+childName+" "+Math.round(maxArea*10000)/100.0, winnerParentG);
                		    
            		  }*/
            	  
            	  if(maxArea<minArea) minArea=maxArea;
            }
            //ff.contains(arg0, g.toText());
            
            
		}
		
		} catch (MalformedURLException e) {
		throw new RelationFinderException(e);
		} catch (IOException e) {
			throw new RelationFinderException(e);
		}
		//KMLRenderer r=new DefaultKMLRenderer();
		//r.render(kmlSet, "/tmp/out.kml");
		System.out.println("Number of orphans: "+numOrphans+", min area is: "+(int)(minArea*100)+"%");
		return out;
	}

	private Object findAttribute(Feature f, String name)
	{
		for(Property p: f.getProperties())
		{
        	if(p.getName().toString().equals(name))
			{
				return p.getValue();
				
			}
		}
		return null;
	}
	
	public FeatureCollection get(FeatureSource source, double minX, double minY, double maxX, double maxY) throws RelationFinderException {
		try {
			FeatureType schema = source.getSchema();
			String geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
		    CoordinateReferenceSystem targetCRS = schema.getGeometryDescriptor()
		            .getCoordinateReferenceSystem();
		    Filter filter = ff.bbox(
		    			geometryPropertyName,
		    			minX,
		    			minY,
		    			maxX,
		    			maxY,
		    			targetCRS.toString());
		    		//"THE_GEOM", env);
		    return source.getFeatures(filter);
			
		} catch (IOException e) {
			throw new RelationFinderException(e);
		}
	}
	
	protected FeatureSource loadShapeFile(URL u) throws IOException
	{
		Map map = new HashMap();
		map.put("url", u);
		DataStore dataStore = DataStoreFinder.getDataStore( map );
		String typeName = dataStore.getTypeNames()[0];
		FeatureSource source = dataStore.getFeatureSource(typeName );
		return source;
	}
	


}
