package com.saltaku.data.area;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.saltaku.api.beans.AreaComparison;
import com.saltaku.api.beans.AreaGeometryMapping;

import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;
import com.saltaku.data.area.writer.GeometryWriter;
import com.saltaku.data.area.writer.GeometryWriterException;
import com.saltaku.data.area.writer.io.DataStoreProvider;
import com.saltaku.geo.GeoException;
import com.saltaku.geo.GeoProcessor;
import com.saltaku.store.DBStore;
import com.saltaku.store.DBStoreException;
import com.saltaku.workflow.WorkflowManager;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;

public class ShapeAreaProcessor implements GeoProcessor {

	private String workflowId;
	private DBStore dbStore;
	private DataStoreProvider dataStoreProvider;
	private WorkflowManager wm;
	private final String targetRef;
	private CRSFactory csFactory = new ReferencingObjectFactory();
	private GeometryWriter geomWriter;
	private double threshold=0.9;
	private String columnForCode;
	private String columnForName;
	private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
	private GeometryFactory gf;
	
	@Inject
	public ShapeAreaProcessor(@Named("targetRef") String targetRef, DBStore store, WorkflowManager wm, GeometryWriter geomWriter) {
		this.targetRef=targetRef;
		this.dbStore=store;
		this.wm=wm;
		this.geomWriter=geomWriter;
		this.gf=new GeometryFactory();
	}
	
	public void setWorkflowId(String w)
	{
		this.workflowId=w;
	}
	
	public String uploadArea(String shpZipData, String name, String source, String columnForCode, String columnForName, String columnForEnglishName)  throws GeoException{
try{
		String inputPath=ShpUtils.unzipShp(shpZipData);
		
		ShapefileDataStore inStore = new ShapefileDataStore(new URL("file://"+inputPath));
		
		Area areaBean=new Area();
		areaBean.id=dbStore.generateId("area");
		areaBean.parentId="";
		areaBean.insertTime=new Date();
		areaBean.name=name;
		
		
        String typeName = inStore.getTypeNames()[0];
        FeatureSource inSource = inStore.getFeatureSource(typeName);
        FeatureCollection inResults = inSource.getFeatures();
        FeatureType inSchema = inSource.getSchema();

		  
		  //System.out.println(inSchema.getCoordinateReferenceSystem().toWKT());
		  
		  CoordinateReferenceSystem cs= csFactory.createFromWKT(this.targetRef);
		  //CoordinateFilter transFilter = new 
		  MathTransform transform = CRS.findMathTransform(inSchema.getCoordinateReferenceSystem(), cs, true);
		  
		  geomWriter.init(areaBean.id, cs);

		  
		  Geometry bb=null;
          double minArea=Double.MAX_VALUE;
          double maxArea=0.0;
          double area=0.0;
          int geometryCount=0;
          
          FeatureIterator fIterator=inResults.features();
          int geomId=1;
          while (fIterator.hasNext()) {
        	  Feature inFeature = fIterator.next();
                           
              GeometryAttribute ga = inFeature.getDefaultGeometryProperty();
              Geometry g=(Geometry)ga.getValue();
              
              AreaGeometry f=new AreaGeometry();
              for(Property p: inFeature.getProperties())
      			{
      			//System.out.println("Checking property "+p.getName());
      			if(p.getName().toString().equals(columnForCode))
      			{
      				f.area_code=p.getValue().toString();
      			//	System.out.println("Set "+f.getCode()+" as Code");
      			}
      			if(p.getName().toString().equals(columnForName))
      			{
      				f.name=p.getValue().toString();
      			}
      			if(p.getName().toString().equals(columnForEnglishName))
      			{
      				f.english_name= p.getValue().toString();
      			}
      		}
              f.id=geomId;
              Geometry g2=JTS.transform(g, transform);
              
              
              
              f.areaId=areaBean.id;
              f.shape=g2.toText();
              f.area=g2.getArea();
              f.simple_shape=TopologyPreservingSimplifier.simplify(g2, Math.sqrt(f.area)/8).toText();
              f.bb=g2.getEnvelope().toText();
              if(bb==null)
              {
            	  bb=g2.getEnvelope();
              }
              else
              {
            	  bb=g2.getEnvelope().union(bb).getEnvelope();
              }
              area+=f.area;
              if(f.area<minArea) minArea=f.area;
              if(f.area>maxArea) maxArea=f.area;
              geometryCount++;
              if(geometryCount%100==0)
            	  wm.addEntry(this.workflowId,"shape #"+geometryCount);
              f.centroid=g2.getCentroid().toText();
              //System.out.println("Before insert "+geomId);
              
              dbStore.insertAreaGeometry(f);
              geomWriter.writeFeature(g2, f);
              geomId++;
              }
          fIterator.close();
          geomWriter.end();
          areaBean.bbox=bb.toText();
          areaBean.numItems=geometryCount;
          areaBean.area=area;
          areaBean.max_area=maxArea;
          areaBean.min_area=minArea;
          
          dbStore.insertArea(areaBean);
          return areaBean.id;
            
}
catch(DBStoreException e){
	throw new GeoException(e);
} catch (GeometryWriterException e) {
	throw new GeoException(e);
} catch (MalformedURLException e) {
	throw new GeoException(e);
} catch (MismatchedDimensionException e) {
	throw new GeoException(e);
} catch (TransformException e) {
	throw new GeoException(e);
} catch (IOException e) {
	throw new GeoException(e);
} catch (FactoryException e) {
	throw new GeoException(e);
}
	
	
	}

	@Override
		public String getBboxOfData(int[] data, String areaId) throws GeoException {
		Geometry envelope=null;
		WKTReader wktReader = new WKTReader(this.gf);
		try {
			AreaGeometry[] geoms=this.dbStore.getAreaGeometry(areaId, data, false);
			for(AreaGeometry geom: geoms)
			{
				
				Geometry bb=wktReader.read(geom.bb).getEnvelope();
				if(envelope==null)
				{
					envelope=bb;
				}
				else
				{
					envelope=envelope.union(bb);
				}
			}
		} catch (DBStoreException e) {
			throw new GeoException(e);
		} catch (ParseException e) {
			throw new GeoException(e);
		}
		
			return envelope.getEnvelope().toText();
		}

	@Override
		public AreaGeometry getMatchingGeometry(String areaId, double x, double y) throws GeoException {
			try {
				AreaGeometry[] candidates=this.dbStore.getAreaGeometry(areaId, this.dbStore.getMatchingGeometry(areaId, x,y),true);
				if(candidates.length==1) return candidates[0]; //if there is only one matching geometry, just return it. Approximate result will be enough
				WKTReader wktReader = new WKTReader(this.gf);
				Point p=this.gf.createPoint(new Coordinate(x,y));
				for(AreaGeometry candidate: candidates)
				{
					wktReader.read(candidate.shape);
					if(wktReader.read(candidate.shape).contains(p))
						return candidate;
				}
			} catch (DBStoreException e) {
			throw new GeoException(e);
			} catch (ParseException e) {
				throw new GeoException(e);
			}
			return null;

		}

	@Override
	public List<Area> suggestChildren(String areaId) throws GeoException {
			// TODO Auto-generated method stub
		
			return null;
		}

	@Override
	public List<Area> suggestParents(String areaId) throws GeoException {
			try {
				String[] parents=this.dbStore.getParentAreas(areaId);
				List<Area> out =new ArrayList<Area>();
				for(String pareaId: parents)
					out.add(this.dbStore.getArea(pareaId));
				return out;
			} catch (DBStoreException e) {
				throw new GeoException(e);
			}
	}

	@Override
	public AreaComparison mapAreas(String parentId, String childId) throws GeoException {

		AreaComparison out=new AreaComparison();
		out.orhpans = new ArrayList<AreaGeometry>();
		out.matchHistogram=new int[100];
		out.weakOverlaps=new ArrayList<AreaGeometryMapping>();
		
		out.minOverlap=Double.MAX_VALUE;
		FeatureSource childSource;
		FeatureSource parentSource;
		try {
			childSource = this.loadShapeFile(childId);
			parentSource=this.loadShapeFile(parentId);
		//, parentSource;
		
		FeatureIterator fIterator=childSource.getFeatures().features();
		out.mapping=new int[childSource.getFeatures().size()+1];
		List<Integer> orphanIds=new ArrayList<Integer>();
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
            out.mapping[childCode]=0;
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
            	  out.mapping[childCode]=parentCode;
            	  maxArea=areaq;
            	  winnerParent=parentName;
            	  winnerParentG=candidateG;
           	 }
            else
            {
            	AreaGeometryMapping overlapMapping=new AreaGeometryMapping();
            	int[] childCodes={childCode};
            	overlapMapping.child=this.dbStore.getAreaGeometry(childId, childCodes, true)[0];
            	int[] parentCodes={parentCode};
            	overlapMapping.parent=this.dbStore.getAreaGeometry(parentId, parentCodes, true)[0];
            	overlapMapping.overlap=areaq;
            	out.weakOverlaps.add(overlapMapping);
            }
            
            }
            long t2=System.nanoTime();
            if(maxArea<=0)
            {
            	  System.out.println(childName+" is orphan");
            	  out.numOrphans++;
            	  orphanIds.add(childCode);
            }
            else
            {
            	  System.out.println(childCode+"\t"+out.mapping[childCode]+"\t"+childName+"\t"+winnerParent+"\t"+ Math.round(maxArea*10000)/100.0+"\t"+childName.contains(winnerParent)+"\t"+(t2-t1)/1000);
            	  out.matchHistogram[(int)Math.floor(maxArea)]++;
            	  if(maxArea<out.minOverlap) out.minOverlap=maxArea;
            }
            if(orphanIds.size()>0){
            int[] orphans=new int[orphanIds.size()];
            out.orhpans=new ArrayList<AreaGeometry>();
            for(int i=0;i<orphans.length;i++) orphans[i]=orphanIds.get(i);
            for(AreaGeometry g: dbStore.getAreaGeometry(childId, orphans, false) )
            	out.orhpans.add(g);
            }
            
		}
		
		} catch (MalformedURLException e) {
			throw new GeoException(e);
		} catch (IOException e) {
			throw new GeoException(e);
		} catch (GeometryWriterException e) {
			throw new GeoException(e);
		} catch (DBStoreException e) {
			throw new GeoException(e);
		}
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
	
	private FeatureCollection get(FeatureSource source, double minX, double minY, double maxX, double maxY) throws IOException {
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
			
	}
	
	protected FeatureSource loadShapeFile(String id) throws GeometryWriterException, IOException
	{
		DataStore dataStore = this.dataStoreProvider.getDataStore(id);
		String typeName = dataStore.getTypeNames()[0];
		FeatureSource source = dataStore.getFeatureSource(typeName );
		return source;
	}

	
	

}
