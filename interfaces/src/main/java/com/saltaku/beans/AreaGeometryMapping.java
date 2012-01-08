package com.saltaku.beans;

import java.util.Arrays;


/*

Column	Type	Null	Default	Comments	MIME
child_area_id	int(11)	No 	 	 	 
parent_area_id	int(11)	No 	 	 	 
mapping	mediumblob	No 	 	 	 	 
 */
public class AreaGeometryMapping {
public String childAreaId;
public int childGeometryId;
public int[] maping;
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((childAreaId == null) ? 0 : childAreaId.hashCode());
	result = prime * result + childGeometryId;
	result = prime * result + Arrays.hashCode(maping);
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
	AreaGeometryMapping other = (AreaGeometryMapping) obj;
	if (childAreaId == null) {
		if (other.childAreaId != null)
			return false;
	} else if (!childAreaId.equals(other.childAreaId))
		return false;
	if (childGeometryId != other.childGeometryId)
		return false;
	if (!Arrays.equals(maping, other.maping))
		return false;
	return true;
}
}
