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
	public String areaId;
	public String area_code;
	public String name, english_name;
	public double area;
	public String centroid;
	public String shape;
	public String simple_shape;
	public String bb;
	public Date insertTime;
	public AreaGeometry() {
		this.insertTime=new Date();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(area);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((areaId == null) ? 0 : areaId.hashCode());
		result = prime * result + ((area_code == null) ? 0 : area_code.hashCode());
		result = prime * result + ((bb == null) ? 0 : bb.hashCode());
		result = prime * result + ((centroid == null) ? 0 : centroid.hashCode());
		result = prime * result + ((english_name == null) ? 0 : english_name.hashCode());
		result = prime * result + id;
		result = prime * result + ((insertTime == null) ? 0 : insertTime.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((shape == null) ? 0 : shape.hashCode());
		result = prime * result + ((simple_shape == null) ? 0 : simple_shape.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AreaGeometry other = (AreaGeometry) obj;
		if (Double.doubleToLongBits(area) != Double.doubleToLongBits(other.area))
			return false;
		if (areaId == null) {
			if (other.areaId != null)
				return false;
		} else if (!areaId.equals(other.areaId))
			return false;
		if (area_code == null) {
			if (other.area_code != null)
				return false;
		} else if (!area_code.equals(other.area_code))
			return false;
		if (bb == null) {
			if (other.bb != null)
				return false;
		} else if (!bb.equals(other.bb))
			return false;
		if (centroid == null) {
			if (other.centroid != null)
				return false;
		} else if (!centroid.equals(other.centroid))
			return false;
		if (english_name == null) {
			if (other.english_name != null)
				return false;
		} else if (!english_name.equals(other.english_name))
			return false;
		if (id != other.id)
			return false;
		if (insertTime == null) {
			if (other.insertTime != null)
				return false;
		} else if (!insertTime.equals(other.insertTime))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (shape == null) {
			if (other.shape != null)
				return false;
		} else if (!shape.equals(other.shape))
			return false;
		if (simple_shape == null) {
			if (other.simple_shape != null)
				return false;
		} else if (!simple_shape.equals(other.simple_shape))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AreaGeometry [id=").append(id).append(",\n ");
		if (areaId != null)
			builder.append("areaId=").append(areaId).append(",\n ");
		if (area_code != null)
			builder.append("area_code=").append(area_code).append(",\n ");
		if (name != null)
			builder.append("name=").append(name).append(",\n ");
		if (english_name != null)
			builder.append("english_name=").append(english_name).append(",\n ");
		builder.append("area=").append(area).append(",\n ");
		if (centroid != null)
			builder.append("centroid=").append(centroid).append(",\n ");
		if (shape != null)
			builder.append("shape=").append(shape).append(",\n ");
		if (simple_shape != null)
			builder.append("simple_shape=").append(simple_shape).append(",\n ");
		if (bb != null)
			builder.append("bb=").append(bb).append(",\n ");
		if (insertTime != null)
			builder.append("insertTime=").append(insertTime);
		builder.append("]");
		return builder.toString();
	}
	
	
}
