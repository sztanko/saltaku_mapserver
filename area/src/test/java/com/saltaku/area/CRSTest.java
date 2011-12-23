package com.saltaku.area;

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.junit.Test;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;


public class CRSTest{
	private static final String wgs_84="GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295]]";
	private static final String osgb_1936="PROJCS[\"British_National_Grid\",    GEOGCS[\"GCS_OSGB_1936\",        DATUM[\"OSGB_1936\",            SPHEROID[\"Airy_1830\",6377563.396,299.3249646]],        PRIMEM[\"Greenwich\",0.0],        UNIT[\"Degree\",0.0174532925199433]],    PROJECTION[\"Transverse_Mercator\"],    PARAMETER[\"False_Easting\",400000.0],    PARAMETER[\"False_Northing\",-100000.0],    PARAMETER[\"Central_Meridian\",-2.0],    PARAMETER[\"Scale_Factor\",0.999601272],    PARAMETER[\"Latitude_Of_Origin\",49.0],    UNIT[\"Meter\",1.0]]";
	private CRSFactory csFactory = new ReferencingObjectFactory();
	private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );	
	
	
	@Test
	public void decodeSystems() throws NoSuchAuthorityCodeException, FactoryException
	{
		//CRS.decode("EPSG:4326");
		//CRS.decode("WGS84");
		
	}
	
	@Test
	public void charingCrossTest() throws ParseException, MismatchedDimensionException, FactoryException, TransformException
	{
		//this.transformG(0, 0, osgb_1936,wgs_84);
		this.transformG(134300, 25300, osgb_1936,wgs_84);
		this.transformG(651409.903, 313177.27, osgb_1936,wgs_84);
		/* Should be
		 * Converted to Lat/Long: (52.657570301933, 1.7179215806451) 
		 * or
		 * Converted to Lat/Long: (52.657975599534, 1.7160665447978) 
		 */


	}
	
	public Geometry transformG(double lon, double lat, String source, String target) throws FactoryException, MismatchedDimensionException, TransformException
	{
		CoordinateReferenceSystem source_cs= csFactory.createFromWKT(source);
		CoordinateReferenceSystem target_cs= csFactory.createFromWKT(target);
		MathTransform transform = CRS.findMathTransform(source_cs, target_cs, true);
		Coordinate coord = new Coordinate( lon, lat );
		Point point = geometryFactory.createPoint( coord );
		Point out=(Point)JTS.transform(point, transform);
		System.out.println(point.toText()+" transformed to "+out.toText());
		return out;
		
		
	}
	
}
