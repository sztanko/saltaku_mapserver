package com.saltaku.area.relationfinder;

import java.net.URL;



public interface RelationFinder {

	public int[] findRelations(URL childAreaId, URL parentAreaId) throws RelationFinderException;
	
}
