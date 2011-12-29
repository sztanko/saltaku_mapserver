package com.saltaku.beans;


/*

Column	Type	Null	Default	Comments	MIME
child_area_id	int(11)	No 	 	 	 
parent_area_id	int(11)	No 	 	 	 
mapping	mediumblob	No 	 	 	 	 
 */
public class AreaGeometryMapping {
public int childAreaId;
public int childGeometryId;
public int[] maping;
}
