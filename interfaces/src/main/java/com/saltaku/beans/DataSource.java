package com.saltaku.beans;

import java.util.Arrays;
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
	public int geoKeyColumn;
	public String outputAreaId;
	public String[] aggregators;
	public String separator;
	public int startLine;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(aggregators);
		result = prime * result + ((datasourceId == null) ? 0 : datasourceId.hashCode());
		result = prime * result + geoKeyColumn;
		result = prime * result + ((geoKeyType == null) ? 0 : geoKeyType.hashCode());
		result = prime * result + (isPublic ? 1231 : 1237);
		result = prime * result + ((outputAreaId == null) ? 0 : outputAreaId.hashCode());
		result = prime * result + ((separator == null) ? 0 : separator.hashCode());
		result = prime * result + startLine;
		result = prime * result + ((uploadTime == null) ? 0 : uploadTime.hashCode());
		result = prime * result + ((uploader == null) ? 0 : uploader.hashCode());
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
		DataSource other = (DataSource) obj;
		if (!Arrays.equals(aggregators, other.aggregators))
			return false;
		if (datasourceId == null) {
			if (other.datasourceId != null)
				return false;
		} else if (!datasourceId.equals(other.datasourceId))
			return false;
		if (geoKeyColumn != other.geoKeyColumn)
			return false;
		if (geoKeyType == null) {
			if (other.geoKeyType != null)
				return false;
		} else if (!geoKeyType.equals(other.geoKeyType))
			return false;
		if (isPublic != other.isPublic)
			return false;
		if (outputAreaId == null) {
			if (other.outputAreaId != null)
				return false;
		} else if (!outputAreaId.equals(other.outputAreaId))
			return false;
		if (separator == null) {
			if (other.separator != null)
				return false;
		} else if (!separator.equals(other.separator))
			return false;
		if (startLine != other.startLine)
			return false;
		if (uploadTime == null) {
			if (other.uploadTime != null)
				return false;
		} else if (!uploadTime.equals(other.uploadTime))
			return false;
		if (uploader == null) {
			if (other.uploader != null)
				return false;
		} else if (!uploader.equals(other.uploader))
			return false;
		return true;
	}
}
