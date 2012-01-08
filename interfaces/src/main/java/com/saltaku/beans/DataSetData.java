package com.saltaku.beans;

import java.util.Arrays;


public class DataSetData {
	public String dataSet;
	public String areaId;
	public String bbox;
	public String aggregation;
	public double[] data;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aggregation == null) ? 0 : aggregation.hashCode());
		result = prime * result + ((areaId == null) ? 0 : areaId.hashCode());
		result = prime * result + ((bbox == null) ? 0 : bbox.hashCode());
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + ((dataSet == null) ? 0 : dataSet.hashCode());
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
		DataSetData other = (DataSetData) obj;
		if (aggregation == null) {
			if (other.aggregation != null)
				return false;
		} else if (!aggregation.equals(other.aggregation))
			return false;
		if (areaId == null) {
			if (other.areaId != null)
				return false;
		} else if (!areaId.equals(other.areaId))
			return false;
		if (bbox == null) {
			if (other.bbox != null)
				return false;
		} else if (!bbox.equals(other.bbox))
			return false;
		if (!Arrays.equals(data, other.data))
			return false;
		if (dataSet == null) {
			if (other.dataSet != null)
				return false;
		} else if (!dataSet.equals(other.dataSet))
			return false;
		return true;
	}
	

}
