package com.saltaku.api.beans;

import java.util.List;

import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;

public class AreaComparison {

	public Area parent;
	public Area child;
	public int[] mapping;
	public int[] matchHistogram;
	public int numOrphans;
	public double minOverlap;
	public List<AreaGeometry> orhpans;
	public List<AreaGeometryMapping> weakOverlaps;
	
}
