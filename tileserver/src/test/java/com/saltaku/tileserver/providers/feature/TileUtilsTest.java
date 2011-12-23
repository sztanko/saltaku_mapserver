package com.saltaku.tileserver.providers.feature;

import org.junit.Test;


public class TileUtilsTest {

	@Test
	public void testTile2lat()
	{
		System.out.println(TileUtils.tile2lat(2,3));
		System.out.println(TileUtils.tile2lat(5,4));
		System.out.println(TileUtils.tile2lat(6,4));
		System.out.println(TileUtils.tile2lat(10,5));
		System.out.println(TileUtils.tile2lat(20,6));
		
	}
	
	@Test
	public void testEnvelope()
	{
		System.out.println(new TileUtils(null).getTileEnvelope(20,12,6));
		//System.out.println(TileUtils.g tile2lat(y, z)(20,6));
	}
	
}
