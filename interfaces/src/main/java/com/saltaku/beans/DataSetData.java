package com.saltaku.beans;

import java.util.Arrays;

import org.opengis.geometry.Envelope;

public class DataSetData {
	public int dataSet;
	public int areaId;
	public String bbox;
	public String aggregation;
	public double[] data;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aggregation == null) ? 0 : aggregation.hashCode());
		result = prime * result + areaId;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + dataSet;
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
		DataSetData other = (DataSetData) obj;
		if (aggregation == null) {
			if (other.aggregation != null) {
				return false;
			}
		} else if (!aggregation.equals(other.aggregation)) {
			return false;
		}
		if (areaId != other.areaId) {
			return false;
		}
		if (!Arrays.equals(data, other.data)) {
			return false;
		}
		if (dataSet != other.dataSet) {
			return false;
		}
		return true;
	}

}
