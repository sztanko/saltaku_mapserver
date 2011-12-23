package com.saltaku.area;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.geotools.feature.SchemaException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.saltaku.area.importer.Importer;
import com.saltaku.area.importer.exceptions.ImportException;
import com.saltaku.area.importer.propertymapper.impl.AttributePropertyMapper;
import com.saltaku.area.importer.store.geometry.impl.ShpGeometryWriter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ImportException, MalformedURLException
    {
    	 Injector injector = Guice.createInjector(new AreaModule());

    	    /*
    	     * Now that we've got the injector, we can build objects.
    	     */
    	    Importer i= injector.getInstance(Importer.class);
    	    
    	    
    	    /*i.exec(new URL("file:///home/dimi/workspace/core/resources/datasets/shapefiles/xkorvzhn/data_raw.shp"),"file:///tmp/test/test.shp",
    	    		new AttributePropertyMapper("LSOA04CD", "LSOA04NM", "LSOA04NM"), new ShpGeometryWriter());*/
    	   /* i.exec(new URL("file:///home/dimi/workspace/core/resources/datasets/shapefiles/xkorvzhn/data_raw.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/lsoa2/data.shp",
    	    		new AttributePropertyMapper("LSOA04CD", "LSOA04NM", "LSOA04NM"), new ShpGeometryWriter());
    	    */
    	    //i.exec(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/tr/data.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/lsoa2/data.shp",
    	    //		new AttributePropertyMapper("LSOA04CD", "LSOA04NM", "LSOA04NM"), new ShpGeometryWriter());
    	    /*i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/mystery/nuts/NUTS_RG_03M_2006.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/lsoa3/data.shp",
    	    		new AttributePropertyMapper("NUTS_ID", "NUTS_ID", "NUTS_ID"), new ShpGeometryWriter());*/
    	    /*i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/mystery/urau/URAU_CITY_RG_03M.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/urau/data.shp",
    	    		new AttributePropertyMapper("URAU_CITY_", "URAU_CITY_", "URAU_CITY_"), new ShpGeometryWriter());*/
    	    /*i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/tiger/tl_2010_36_zcta510.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/ny/data.shp",
    	    		new AttributePropertyMapper("Zcta5ce10", "Zcta5ce10", "Zcta5ce10"), new ShpGeometryWriter());*/
/*    	    i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/ny-blockgroup/tl_2010_36_bg10.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/ny_blockgroup/data.shp",
    	    		new AttributePropertyMapper("BLKGRPCE10", "NAMELSAD10", "NAMELSAD10"), new ShpGeometryWriter());*/
    	    /*i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/ny-block/tl_2010_36_tabblock10.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/ny_block/data.shp",
    	    		new AttributePropertyMapper("BLOCKCE10", "NAME10", "NAME10"), new ShpGeometryWriter());*/
    	    /*i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/ny-countysub/tl_2010_36_cousub10.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/ny_countysub/data.shp",
    	    		new AttributePropertyMapper("COUSUBFP10", "NAMELSAD10", "NAMELSAD10"), new ShpGeometryWriter());*/
    	    /*i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/ny-county/tl_2010_36_county10.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/ny_county/data.shp",
    	    		new AttributePropertyMapper("COUNTYNS10", "NAMELSAD10", "NAMELSAD10"), new ShpGeometryWriter());*/
    	    /*i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/iceland/ISL_adm2.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/is_2/data.shp",
    	    		new AttributePropertyMapper("ID_2", "NAME_2", "NAME_2"), new ShpGeometryWriter());*/
    	    i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/iceland/ISL_adm1.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/is_1/data.shp",
    	    		new AttributePropertyMapper("ID_1", "NAME_1", "NAME_1"), new ShpGeometryWriter());
    	    
    	    /*i.exec(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/msoa_raw/data.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/msoa/data.shp",
    	    		new AttributePropertyMapper("MSOA04CD", "MSOA04NM", "MSOA04NM"), new ShpGeometryWriter());*/
    	    /*i.exec(new URL("file:///home/dimi/workspace/core/resources/shapefiles/msoa/data_hi.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/msoa/data.shp",
    	    		new AttributePropertyMapper("MSOA04CD", "MSOA04NM", "MSOA04NM"), new ShpGeometryWriter());*/
    	    
    	    /*i.exec(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/lsoa_raw/data.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/lsoa/data.shp",
    	    		new AttributePropertyMapper("LSOA04CD", "LSOA04NM", "LSOA04NM"), new ShpGeometryWriter());
    	      */  
    	    //i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/admin/united_kingdom_administrative.shp"),"/tmp/test.shp");
    	    
    	    
    	    //These are diva shps:
    	    i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/gb/GBR_adm1.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/uk_1/data.shp",
    	    		new AttributePropertyMapper("ID_1", "NAME_1", "NAME_1"), new ShpGeometryWriter());
    	    i.exec(new URL("file:///home/dimi/workspace/area/data/shapefiles/gb/GBR_adm2.shp"),"file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/uk_2/data.shp",
    	    		new AttributePropertyMapper("ID_2", "NAME_2", "NAME_2"), new ShpGeometryWriter());
    }
}
