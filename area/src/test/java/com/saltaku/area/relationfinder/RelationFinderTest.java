package com.saltaku.area.relationfinder;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.saltaku.data.area.relationfinder.RelationFinder;
import com.saltaku.data.area.relationfinder.RelationFinderException;
import com.saltaku.data.area.relationfinder.impl.ShpRelationFinder;

public class RelationFinderTest {
	
	@Test
	public void shpTest() throws MalformedURLException, RelationFinderException, FileNotFoundException
	{
		RelationFinder f=new ShpRelationFinder();
		System.setOut(new PrintStream("/tmp/out"));
		//System.out.println(f.findRelations(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/lsoa/data.shp"), new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/msoa/data.shp")).length);;
		//System.out.println(f.findRelations(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/ny_block/data.shp"), new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/ny_blockgroup/data.shp")).length);
		//System.out.println(f.findRelations(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/ny_blockgroup/data.shp"), new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/ny_countysub/data.shp")).length);
		//System.out.println(f.findRelations(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/ny_countysub/data.shp"), new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/ny_county/data.shp")).length);
		//System.out.println(f.findRelations(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/is_2/data.shp"), new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/is_1/data.shp")).length);
		//System.out.println(f.findRelations(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/msoa/data.shp"), new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/uk_2/data.shp")).length);
		//System.out.println(f.findRelations(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/uk_2/data.shp"), new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/uk_1/data.shp")).length);
		System.out.println(f.findRelations(new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/lsoa/data.shp"), new URL("file:///home/dimi/git/saltaku/tileserver/resources/test/shapefiles/uk_2/data.shp")).relations.length);
	}
}
