package com.saltaku.beans;

import java.util.Arrays;


/*
Column	Type	Null	Default	Comments	MIME
datasource_id	varchar(64)	No 	 	 	 
column	int(11)	No 	 	 	 
name	varchar(250)	No 	 	 
 */
public class DataSourceDataSet {
	public String datasourceId;
	public int column;
	public String name;
	public double[] data;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + ((datasourceId == null) ? 0 : datasourceId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		DataSourceDataSet other = (DataSourceDataSet) obj;
		if (column != other.column)
			return false;
		if (!Arrays.equals(data, other.data))
			return false;
		if (datasourceId == null) {
			if (other.datasourceId != null)
				return false;
		} else if (!datasourceId.equals(other.datasourceId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
