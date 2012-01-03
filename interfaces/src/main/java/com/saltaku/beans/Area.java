package com.saltaku.beans;

import java.util.Date;

/*
 * 
Column	Type	Null	Default	Comments	MIME
area_id	int(11)	No 	 	 	 
parent_id	int(11)	No 	-1 	 	 
name	varchar(255)	No 	unnamed 	 	 
source	varchar(512)	No 	no source 	 	 
bbox	geometry	Yes 	NULL 	 	 
centroid	point	Yes 	NULL 	 	 
num_items	int(11)	No 	0 	 	 
area	double	No 	-1 	 	 
min_area	double	No 	-1 	 	 
max_area	double	No 	-1 	 	 
insertTime	timestamp	No 	CURRENT_TIMESTAMP 
 */

public class Area {
	public int id;
	public int parentId;
	public String name;
	public String source;
	public String bbox;
	public String centroid;
	public int numItems;
	public double area;
	public double min_area, max_area;
	public Date insertTime;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(area);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + id;
		temp = Double.doubleToLongBits(max_area);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(min_area);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numItems;
		result = prime * result + parentId;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Area other = (Area) obj;
		if (Double.doubleToLongBits(area) != Double.doubleToLongBits(other.area)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (Double.doubleToLongBits(max_area) != Double.doubleToLongBits(other.max_area)) {
			return false;
		}
		if (Double.doubleToLongBits(min_area) != Double.doubleToLongBits(other.min_area)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (numItems != other.numItems) {
			return false;
		}
		if (parentId != other.parentId) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		return true;
	}
}
