package com.saltaku.beans;

import java.awt.Point;
import java.util.Date;

import org.opengis.geometry.Envelope;

/*
 * 
Column	Type	Null	Default	Comments	MIME
geom_id	int(11)	No 	 	 	 
area_id	int(11)	No 	 	 	 
area_code	varchar(20)	No 	 	 	 
name	varchar(250)	No 	 	 	 
english_name	varchar(255)	No 	 	 	 
area	double	No 	 	 	 
centroid	point	No 	 	 	 
shape	multipolygon	No 	 	 	 
simple_shape	multipolygon	Yes 	NULL 	 	 
bb	geometry	No 	 	 	 
insertTime	timestamp	No 	CURRENT_TIMESTAMP 
 */

public class AreaGeometry {
	public int id;
	public int areaId;
	public String area_code;
	public String name, english_name;
	public double area;
	public Point centroid;
	public String shape;
	public String simple_shape;
	public Envelope bb;
	public Date insertTime;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(area);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + areaId;
		result = prime * result + ((area_code == null) ? 0 : area_code.hashCode());
		result = prime * result + ((english_name == null) ? 0 : english_name.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		AreaGeometry other = (AreaGeometry) obj;
		if (Double.doubleToLongBits(area) != Double.doubleToLongBits(other.area)) {
			return false;
		}
		if (areaId != other.areaId) {
			return false;
		}
		if (area_code == null) {
			if (other.area_code != null) {
				return false;
			}
		} else if (!area_code.equals(other.area_code)) {
			return false;
		}
		if (english_name == null) {
			if (other.english_name != null) {
				return false;
			}
		} else if (!english_name.equals(other.english_name)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
