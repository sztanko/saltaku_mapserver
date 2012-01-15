package com.saltaku.area.importer.beans;

import com.saltaku.area.importer.propertymapper.AreaFeatures;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
@Deprecated
public class AreaShape {
	private String areaId;
private Geometry shape;
private Geometry simplifiedGeometry;
private Geometry bbox;
private Point centroid;
private double area;
private AreaFeatures features;

public String getAreaId() {
	return areaId;
}
public void setAreaId(String areaId) {
	this.areaId = areaId;
}
public Geometry getShape() {
	return shape;
}
public void setShape(Geometry shape) {
	this.shape = shape;
}
public Geometry getSimplifiedGeometry() {
	return simplifiedGeometry;
}
public void setSimplifiedGeometry(Geometry simplifiedGeometry) {
	this.simplifiedGeometry = simplifiedGeometry;
}
public Geometry getBbox() {
	return bbox;
}
public void setBbox(Geometry bbox) {
	this.bbox = bbox;
}
public Point getCentroid() {
	return centroid;
}
public void setCentroid(Point centroid) {
	this.centroid = centroid;
}
public double getArea() {
	return area;
}
public void setArea(double area) {
	this.area = area;
}
public AreaFeatures getFeatures() {
	return features;
}
public void setFeatures(AreaFeatures features) {
	this.features = features;
}


@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	long temp;
	temp = Double.doubleToLongBits(area);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime * result + ((areaId == null) ? 0 : areaId.hashCode());
	result = prime * result + ((bbox == null) ? 0 : bbox.hashCode());
	result = prime * result + ((centroid == null) ? 0 : centroid.hashCode());
	result = prime * result + ((features == null) ? 0 : features.hashCode());
	result = prime * result + ((shape == null) ? 0 : shape.hashCode());
	result = prime * result + ((simplifiedGeometry == null) ? 0 : simplifiedGeometry.hashCode());
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
	AreaShape other = (AreaShape) obj;
	if (Double.doubleToLongBits(area) != Double.doubleToLongBits(other.area))
		return false;
	if (areaId != other.areaId)
		return false;
	if (bbox == null) {
		if (other.bbox != null)
			return false;
	} else if (!bbox.equals(other.bbox))
		return false;
	if (centroid == null) {
		if (other.centroid != null)
			return false;
	} else if (!centroid.equals(other.centroid))
		return false;
	if (features == null) {
		if (other.features != null)
			return false;
	} else if (!features.equals(other.features))
		return false;
	if (shape == null) {
		if (other.shape != null)
			return false;
	} else if (!shape.equals(other.shape))
		return false;
	if (simplifiedGeometry == null) {
		if (other.simplifiedGeometry != null)
			return false;
	} else if (!simplifiedGeometry.equals(other.simplifiedGeometry))
		return false;
	return true;
}
}
