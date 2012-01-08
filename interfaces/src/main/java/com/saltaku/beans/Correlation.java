package com.saltaku.beans;

import java.util.Date;

public class Correlation {
	public String ds1;
	public String ds2;
	public String type;
	public double value;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ds1 == null) ? 0 : ds1.hashCode());
		result = prime * result + ((ds2 == null) ? 0 : ds2.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Correlation other = (Correlation) obj;
		if (ds1 == null) {
			if (other.ds1 != null)
				return false;
		} else if (!ds1.equals(other.ds1))
			return false;
		if (ds2 == null) {
			if (other.ds2 != null)
				return false;
		} else if (!ds2.equals(other.ds2))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}
	
	
}
