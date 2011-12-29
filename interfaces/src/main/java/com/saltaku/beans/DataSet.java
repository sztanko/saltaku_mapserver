package com.saltaku.beans;

import java.util.Date;

import org.opengis.geometry.Envelope;

/*
 * 
Field	Type	Null	Key	Default	Extra
dataset_id	int(11)	NO	PRI	NULL	
datasource_id	varchar(64)	NO	MUL	NULL	
name	varchar(255)	NO		NULL	
date_validity_start	datetime	NO		NULL	
date_validity_end	datetime	NO		NULL	
description	text	NO		NULL	
size	int(11)	NO		NULL	
area_id	int(11)	NO		NULL	
aggregation	varchar(64)	NO		NULL	
bbox	geometry	NO		NULL	
 */

public class DataSet {
	public int id;
	public String dataSourceId;
	public String name;
	public Date start, end;
	public String description;
	public int initialAreaId;
	public String initialAggregation;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataSourceId == null) ? 0 : dataSourceId.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + id;
		result = prime * result + ((initialAggregation == null) ? 0 : initialAggregation.hashCode());
		result = prime * result + initialAreaId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		DataSet other = (DataSet) obj;
		if (dataSourceId == null) {
			if (other.dataSourceId != null) {
				return false;
			}
		} else if (!dataSourceId.equals(other.dataSourceId)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (end == null) {
			if (other.end != null) {
				return false;
			}
		} else if (!end.equals(other.end)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (initialAggregation == null) {
			if (other.initialAggregation != null) {
				return false;
			}
		} else if (!initialAggregation.equals(other.initialAggregation)) {
			return false;
		}
		if (initialAreaId != other.initialAreaId) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (start == null) {
			if (other.start != null) {
				return false;
			}
		} else if (!start.equals(other.start)) {
			return false;
		}
		return true;
	}
	
}
