package com.saltaku.beans;

import java.util.Date;

/*
 * Column	Type	Null	Default	Comments	MIME
datasource_id	varchar(64)	No 	 	 	 
upload_time	timestamp	No 	CURRENT_TIMESTAMP 	 	 
uploader	varchar(255)	No 	 	 	 
is_public	tinyint(1)	No 	 	 	 
geokey_type	enum('ID', 'GEO', 'ADDR')	No 	 	 	 
output_area_id	int(11)	No 	 	 	 
aggregators	varchar(50)	No 	 	 	 
separator	varchar(5)	No 	 	 	 
start_line	int(11)	No 	 	 	 
 */
public class DataSource {
	public String datasourceId;
	public Date uploadTime;
	public String uploader;
	public boolean isPublic;
	public String geoKeyType;
	public String[] aggregators;
	public String separator;
	public int startLine;
}
