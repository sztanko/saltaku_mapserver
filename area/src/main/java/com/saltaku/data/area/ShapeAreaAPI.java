package com.saltaku.data.area;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
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
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.saltaku.api.APIException;
import com.saltaku.api.AreaAPI;

import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;
import com.saltaku.data.area.writer.GeometryWriter;
import com.saltaku.data.area.writer.GeometryWriterException;
import com.saltaku.store.DBStore;
import com.saltaku.store.DBStoreException;
import com.saltaku.workflow.WorkflowManager;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;

public class ShapeAreaAPI implements AreaAPI {

	private String workflowId;
	private DBStore dbStore;
	private WorkflowManager wm;
	private final String targetRef;
	private CRSFactory csFactory = new ReferencingObjectFactory();
	private GeometryWriter geomWriter;
	public String outputPath;
	
	public ShapeAreaAPI(String workflowId, String targetRef, DBStore store, WorkflowManager wm, GeometryWriter geomWriter,String outputPath){
		this.targetRef=targetRef;
		this.dbStore=store;
		this.wm=wm;
		this.geomWriter=geomWriter;
		this.workflowId=workflowId;
	}
	
	@Override
	public String uploadArea(String shpZipData, String name, String source, String columnForCode, String columnForName, String columnForEnglishName) throws APIException {
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
		  
		  geomWriter.init(inputPath.toString(), new URL(outputPath+"/"+areaBean.id+"/"), cs);

		  
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
             return workflowId;
}
catch(DBStoreException e){
	throw new APIException(e);
} catch (GeometryWriterException e) {
	throw new APIException(e);
} catch (MalformedURLException e) {
	throw new APIException(e);
} catch (MismatchedDimensionException e) {
	throw new APIException(e);
} catch (TransformException e) {
	throw new APIException(e);
} catch (IOException e) {
	throw new APIException(e);
} catch (FactoryException e) {
	throw new APIException(e);
}
	
	
	}

	@Override
	public List<Area> suggestChildren(String areaId) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Area> suggestParents(String areaId) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AreaGeometry> assignChild(String childAreaId, String parentAreaId) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws APIException {
		// TODO Auto-generated method stub
		
	}

}
