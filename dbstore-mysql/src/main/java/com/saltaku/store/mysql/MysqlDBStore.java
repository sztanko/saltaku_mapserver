package com.saltaku.store.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.inject.Singleton;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.google.inject.Inject;
import com.saltaku.beans.Area;
import com.saltaku.beans.AreaGeometry;
import com.saltaku.beans.Correlation;
import com.saltaku.beans.DataSet;
import com.saltaku.beans.DataSetData;
import com.saltaku.beans.DataSource;
import com.saltaku.beans.DataSourceDataSet;
import com.saltaku.beans.Tag;
import com.saltaku.data.compress.DataCompressor;
import com.saltaku.data.serde.DataSerializer;
import com.saltaku.store.DBStore;
import com.saltaku.store.DBStoreException;

public class MysqlDBStore implements DBStore {
DbConnectionManager connManager;
DataSerializer serde;
DataCompressor compressor;

@Singleton
@Inject
public MysqlDBStore(DbConnectionManager mng,  DataSerializer serde, DataCompressor compressor) throws DBStoreException{
	this.connManager=mng;
	this.serde=serde;
	this.compressor=compressor;
	}

	
	public DataSetData getRawData(String datasetId, String areaId, String aggregation) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			stm = conn.prepareStatement("select dataset_id, area_id, asText( ENVELOPE( bb ) ) as bb, aggregation, data from dataset_data where dataset_id=? and area_id=? and aggregation=? limit 1");
			stm.setString(1, datasetId);
			stm.setString(2, areaId);
			stm.setString(3, aggregation);
		
			ResultSet rs=stm.executeQuery();
			DataSetData data=new DataSetData();
			while(rs.next())
			{
				data.dataSet=rs.getString("dataset_id");
				data.areaId=rs.getString("area_id");
				data.bbox=rs.getString("bb");
				data.data=serde.deserialize(compressor.decompress(rs.getBytes("data")));
				data.aggregation=rs.getString("aggregation");
			}
		return data;
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	public AreaGeometry[] getAreaGeometry(String areaId, int[] ids, boolean fetchGeometry) throws DBStoreException {
		Connection conn = null;
		if(ids==null || ids.length==0) return new AreaGeometry[0];
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="select geom_id, area_id, area_code, name, english_name, area, AsText(centroid) as cntr, AsText(envelope(bb)) as bb, insertTime from area_geom where area_id=? and geom_id in ("+MysqlDBStore.join(ids, ',')+")";
			if(fetchGeometry) q="select geom_id, area_id, area_code, name, english_name, area, AsText(centroid) as cntr, AsText(envelope(bb)) as bb, insertTime, AsText(shape) as shape from area_geom where area_id=? and geom_id in ("+MysqlDBStore.join(ids, ',')+")";
			stm = conn.prepareStatement(q);
			stm.setString(1, areaId);
			ResultSet rs=stm.executeQuery();
			List<AreaGeometry> out=new ArrayList<AreaGeometry>();
			while(rs.next())
			{
				AreaGeometry ag=new AreaGeometry();
				ag.id =rs.getInt("geom_id");
				ag.areaId =rs.getString("area_id");
				ag.area_code =rs.getString("area_code");
				ag.name =rs.getString("name");
				ag.area=rs.getDouble("area");
				ag.centroid=rs.getString("ctr");
				ag.bb=rs.getString("bb");
				ag.insertTime=rs.getDate("insertTime");
				if(fetchGeometry)
				{
					ag.shape=rs.getString("shape");
				}
				out.add(ag);
			}
		return (AreaGeometry[])out.toArray();
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	public DataSet getDataSet(String datasetId) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			stm = conn.prepareStatement("select dataset_id, datasource_id, name, date_validity_start, date_validity_end, size, area_id, aggregation from datasets where dataset_id=? limit 1");
			stm.setString(1, datasetId);
			
			ResultSet rs=stm.executeQuery();
			DataSet data=new DataSet();
			while(rs.next())
			{
				data.id=rs.getString("dataset_id");
				data.dataSourceId=rs.getString("datasource_id");
				data.name=rs.getString("name");
				data.start=rs.getDate("date_validity_start");
				data.end=rs.getDate("date_validity_end");
				data.initialAreaId=rs.getString("area_id");
				data.initialAggregation=rs.getString("aggregation");
				
			}
		return data;
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	public Tag[] getTags(String id, String type) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="select data_id,data_type,  tag_name, tag_value from tags where dataset_id=? and data_type=? order by tag_name";
			stm = conn.prepareStatement(q);
			stm.setString(1, id);
			stm.setString(2, type);
			ResultSet rs=stm.executeQuery();
			List<Tag> out=new ArrayList<Tag>();
			while(rs.next())
			{
				Tag tag =new Tag();
				tag.id =rs.getString("data_id");
				tag.type =rs.getString("data_type");
				tag.name=rs.getString("tag_name");
				tag.value=rs.getString("tag_value");
				
				out.add(tag);
			}
		return (Tag[])out.toArray();
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	public DataSetData[] getAvailableDataForDataSet(String dataSetId) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="select dataset_id, area_id, asText(envelope(bbox)) as bbox, aggregation, data from dataset_data where dataset_id=?";
			stm = conn.prepareStatement(q);
			stm.setString(1, dataSetId);
			ResultSet rs=stm.executeQuery();
			List<DataSetData> out=new ArrayList<DataSetData>();
			while(rs.next())
			{
				DataSetData ddata =new DataSetData();
				ddata.dataSet =rs.getString("dataset_id");
				ddata.areaId=rs.getString("area_id");
				ddata.bbox=rs.getString("bbox");
				ddata.aggregation=rs.getString("aggregation");
				ddata.data=serde.deserialize(compressor.decompress(rs.getBytes("data")));
				out.add(ddata);
			}
		return (DataSetData[])out.toArray();
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	public Area getArea(String areaId) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="select area_id, parent_id, name, source, asText(envelope(bbox)) as bbox, asText(centroid) as centroid, num_items, area, min_area, max_area, insertTime from areas where area_id=?";
			stm = conn.prepareStatement(q);
			stm.setString(1, areaId);
			ResultSet rs=stm.executeQuery();
			Area area=new Area();
			while(rs.next())
			{
				area.id=rs.getString("area_id");
				area.parentId=rs.getString("parent_id");
				area.source=rs.getString("source");
				area.bbox=rs.getString("bbox");
				area.centroid=rs.getString("centroid");
				area.numItems=rs.getInt("num_items");
				area.area=rs.getDouble("area");
				area.min_area=rs.getDouble("min_area");
				area.max_area=rs.getDouble("max_area");
				area.insertTime=rs.getDate("insertTime");
			}
		return area;
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	public List<Correlation> getCorrelatedDataSets(String dataSetId, String correlationType, double maxCorrelation, int maxNum) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			List<Correlation> out=new ArrayList<Correlation>();
			conn=connManager.getConnection();
			stm = conn.prepareStatement("select if(dataset1_id=?,dataset2_id,dataset1_id) as dataset_id, correlation from datasets where (dataset1_id=? or dataset2_id=?) and correlationType=? and correlation<=? order by correlation desc limit maxNum");
			stm.setString(1, dataSetId);
			stm.setString(2, dataSetId);
			stm.setString(3, dataSetId);
			stm.setString(4, dataSetId);
			stm.setString(5, correlationType);
			
			ResultSet rs=stm.executeQuery();
			while(rs.next())
			{
			Correlation c=new Correlation();
				c.ds1=dataSetId;
				c.ds2=rs.getString("dataset_id");
				c.type=correlationType;
				c.value=rs.getDouble("correlation");
				out.add(c);
			}
		return out;
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}
	
	protected static String join(int[] array, char separator)
	{
		StringBuilder b=new StringBuilder();
		int i;
		for(i=0;i<array.length-1;i++)
		{
			b.append(array[i]).append(separator);
		}
		b.append(array[i]);
		return b.toString();
	}

	public void changeTags(String id, String type, Map<String, String> newTags) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="delete from tags where data_id=? and data_type=?";
			stm = conn.prepareStatement(q);
			stm.setString(1,id);
			stm.setString(2,type);
		
			stm.executeUpdate();
			q="insert into tags(data_id,data_type,tag_name,tag_value) values (?,?,?,?)";
			stm = conn.prepareStatement(q);
			for(Entry<String,String> e: newTags.entrySet())
			{
				stm.setString(1,id);
				stm.setString(2,type);
				stm.setString(3,e.getKey());
				stm.setString(4,e.getValue());
				stm.executeUpdate();
			}
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}


	public String insertDataSource(DataSource source) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="INSERT INTO  `datasources` "+
			"(`datasource_id` ,`upload_time` ,`uploader` ,`is_public` ,`geokey_type` ,`geokey_column_num` ,`output_area_id` ,`aggregators` ,`separator` ,`start_line`) "+
			"VALUES (?, ? ,?, ?, ?, ?, ?,?,?, ?)";
			stm = conn.prepareStatement(q);
			stm.setString(1,source.datasourceId);
			stm.setDate(2,new java.sql.Date(source.uploadTime.getTime()));
			stm.setString(3, source.uploader);
			stm.setBoolean(4, source.isPublic);
			stm.setString(5, source.geoKeyType);
			stm.setInt(6, source.geoKeyColumn);
			stm.setString(7, source.outputAreaId);
			stm.setString(8, Arrays.toString(source.aggregators));
			stm.setString(9, source.separator);
			stm.setInt(10, source.startLine);
		
			stm.executeUpdate();
			return "ok";
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	public String insertDataSet(DataSet source) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="INSERT INTO  `datasets` (`dataset_id` ,`datasource_id` ,`name` ,`date_validity_start` ,`date_validity_end` ,`size` ,`area_id` ,`aggregation`,bbox)"+
			"VALUES (?,?,?,?,?,?,?,?,GeomFromText(?));";
			stm = conn.prepareStatement(q);
			stm.setString(1,source.id);
			stm.setString(2,source.dataSourceId);
			stm.setString(3, source.name);
			stm.setDate(4, new java.sql.Date(source.start.getTime()));
			stm.setDate(5, new java.sql.Date(source.end.getTime()));
			stm.setString(6, source.initialAreaId);
			stm.setString(7, source.initialAggregation);
			stm.setString(8, source.bbox);
				stm.executeUpdate();
			return "ok";
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	public String insertDataSetData(DataSetData source) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="INSERT INTO  `dataset_data` (`dataset_id` ,`area_id` ,`bbox` ,`aggregation` ,`data`)"+
			"VALUES (?,?,GeomFromText(?),?,?);";
			stm = conn.prepareStatement(q);
			stm.setString(1,source.dataSet);
			stm.setString(2,source.areaId);
			stm.setString(3, source.bbox);
			stm.setBytes(4, compressor.compress(serde.serialize(source.data)));
				stm.executeUpdate();
			return "ok";
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}
	
	

	public String generateId(String ds) throws DBStoreException {
		return UUID.randomUUID().toString();
	}


	public String insertDataSourceData(DataSourceDataSet source) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="INSERT INTO  `datasource_datasets` (`datasource_id` ,`column` ,`name`,`data`)"+
			"VALUES (?,?,GeomFromText(?),?,?);";
			stm = conn.prepareStatement(q);
			stm.setString(1,source.datasourceId);
			stm.setInt(2,source.column);
			stm.setString(3, source.name);
			stm.setBytes(4, compressor.compress(serde.serialize(source.data)));
				stm.executeUpdate();
			return "ok";
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	public int lookupGeoKey(String areaId, String geoKey) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="select geom_id from area_geom where area_id=? and (code=? or name=? or english_name=?) limit 1";
			stm = conn.prepareStatement(q);
			stm.setString(1, areaId);
			stm.setString(2, geoKey);
			stm.setString(3, geoKey);
			stm.setString(4, geoKey);
			ResultSet rs=stm.executeQuery();
			int result=-1;
			while(rs.next())
			{
				result=rs.getInt("geom_id");
			}
		return result;
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	public void changeTag(String id, String type, String tagKey, String tagValue) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="delete from tags where data_id=? and data_type=? and tag_name=?";
			stm = conn.prepareStatement(q);
			stm.setString(1,id);
			stm.setString(2,type);
			stm.setString(2,tagKey);
		
			stm.executeUpdate();
			q="insert into tags(data_id,data_type,tag_name,tag_value) values (?,?,?,?)";
			stm = conn.prepareStatement(q);
				stm.setString(1,id);
				stm.setString(2,type);
				stm.setString(3,tagKey);
				stm.setString(4,tagValue);
				stm.executeUpdate();
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
		
	}

	public void changeDataSet(DataSet dataSet) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="update datasets set name=?, date_validity_start=?, date_validity_end=? where data_id=? and dataset_id=? and datasource_id=?";
			stm = conn.prepareStatement(q);
			stm.setString(1,dataSet.name);
			stm.setDate(2,new java.sql.Date(dataSet.start.getTime()));
			stm.setDate(3,new java.sql.Date(dataSet.end.getTime()));
			stm.setString(4,dataSet.id);
			stm.setString(5,dataSet.dataSourceId);
			stm.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
		
	}
	



	public List<Correlation> getCorrelations(String[] dataSets, String areaId) {
		// TODO Auto-generated method stub
		return null;
	}


	public DataSet[] getDataSetsOfSource(String dataSourceId) throws DBStoreException {
			Connection conn = null;
			PreparedStatement  stm = null;
			try {
				conn=connManager.getConnection();
				stm = conn.prepareStatement("select dataset_id, datasource_id, name, date_validity_start, date_validity_end, size, area_id, aggregation from datasets where datasource_id=?");
				stm.setString(1, dataSourceId);
				
				ResultSet rs=stm.executeQuery();
				List<DataSet> out=new ArrayList<DataSet>();
				while(rs.next())
				{
					DataSet data=new DataSet();
					data.id=rs.getString("dataset_id");
					data.dataSourceId=rs.getString("datasource_id");
					data.name=rs.getString("name");
					data.start=rs.getDate("date_validity_start");
					data.end=rs.getDate("date_validity_end");
					data.initialAreaId=rs.getString("area_id");
					data.initialAggregation=rs.getString("aggregation");
					out.add(data);
					
				}
			return out.toArray(new DataSet[out.size()]);
			} catch (SQLException e) {
				throw new DBStoreException(e);
			}
			finally {
	            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
	            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
	        }
		
		
	}


	public String[] getParentAreas(String areaId) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			stm = conn.prepareStatement("select parent_area_id from area_mappings where child_area_id=?");
			stm.setString(1, areaId);
			
			ResultSet rs=stm.executeQuery();
			List<String> out=new ArrayList<String>();
			while(rs.next())
			{
				out.add(rs.getString("parent_area_id"));
			}
		return out.toArray(new String[out.size()]);
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}


	public int[] getAreaMapping(String childAreaId, String parentAreaId) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			stm = conn.prepareStatement("select mapping from area_mappings where child_area_id=? and parent_area_id=?");
			stm.setString(1, childAreaId);
			stm.setString(2, parentAreaId);
			int[] out=null;
			ResultSet rs=stm.executeQuery();
			
			while(rs.next())
			{
				out=serde.deserializeAsIntArray(compressor.decompress(rs.getBytes("mapping")));
			}
		return out;
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	
	public void close() throws DBStoreException {
		connManager.close();
		
	}


	public void insertArea(Area area) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="INSERT INTO  `areas` (`area_id` ,`parent_id` ,`name` ,`bbox` ,`centroid` ,`num_items` ,`area` ,`min_area`,max_area, insertTime)"+
			"VALUES (?, ?, ?, GeomFromText(?),GeomFromText(?),?,?,?,?,?);";
			stm = conn.prepareStatement(q);
			stm.setString(1,area.id);
			stm.setString(2,area.parentId);
			stm.setString(3, area.name);
			stm.setString(4, area.bbox);
			stm.setString(5, area.centroid);
			stm.setInt(6, area.numItems);
			stm.setDouble(7, area.area);
			stm.setDouble(8, area.min_area);
			stm.setDouble(9, area.max_area);
			stm.setDate(10, new java.sql.Date(area.insertTime.getTime()));
			stm.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
		
	}


	public void insertAreaGeometry(AreaGeometry geom) throws DBStoreException {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			String q="INSERT INTO `area_geom` (`area_id`, `geom_id`, `area_code`, `name`, `english_name`, `area`, `centroid`, `shape`, `simple_shape`, `bb`, `insertTime`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			stm = conn.prepareStatement(q);
			stm.setString(1,geom.areaId);
			stm.setInt(2,geom.id);
			stm.setString(3, geom.area_code);
			stm.setString(4, geom.name);
			stm.setString(5, geom.english_name);
			stm.setDouble(6, geom.area);
			stm.setString(7, geom.centroid);
			stm.setString(8, geom.shape);
			stm.setString(9, geom.simple_shape);
			stm.setString(10, geom.bb);
			stm.setDate(11, new java.sql.Date(geom.insertTime.getTime()));
			stm.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
		}
/*
 * (non-Javadoc)
 * @see com.saltaku.store.DBStore#getMatchingGeometry(java.lang.String, double, double)
 * SET @center=GeomFromText('Point(-0.114928  51.578234)');
SELECT geom_id FROM `area_geom` where contains(bb,GeomFromText(?))
SELECT area_id, geom_id, area_code, name,asText(centroid) as cntr, contains(shape,@center ) bbb, contains(bb,@center) shp, SQRT(POW(  X(centroid) - X(@center), 2) + POW( Y(centroid) - Y(@center),2))*1000 as distance FROM `area_geom` ORDER BY `distance` ASC
 */

	public int[] getMatchingGeometry(String areaId, double x, double y) throws DBStoreException {
		String pointWKT="Point("+x+" "+y+")";
		String q="SELECT geom_id FROM `area_geom` where contains(bb,GeomFromText(?)) and area_Id=?";
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			stm = conn.prepareStatement(q);
			stm.setString(1, pointWKT);
			stm.setString(2, areaId);
			List<Integer> outList=new ArrayList<Integer>();
			ResultSet rs=stm.executeQuery();
			
			while(rs.next())
			{
				outList.add(rs.getInt("geom_id"));
			}
			int[] out=new int[outList.size()];
			int i=0;
			for(int id: outList) out[i++]=id;
			return out;
		} catch (SQLException e) {
			throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}
}
