package com.saltaku.workflow.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.saltaku.beans.DataSet;
import com.saltaku.store.DBStoreException;
import com.saltaku.store.mysql.DbConnectionManager;
import com.saltaku.workflow.WorkflowManager;
import com.saltaku.workflow.beans.Workflow;
import com.saltaku.workflow.beans.WorkflowStatus;

public class MysqlWorkflowManager implements WorkflowManager {
	DbConnectionManager connManager;
	
	@Inject
	public MysqlWorkflowManager(DbConnectionManager connManager)
	{
		this.connManager=connManager;
	}
	
	public String createWorkflow(String sessionId) {
		Connection conn = null;
		PreparedStatement  stm = null;
		try {
			conn=connManager.getConnection();
			stm = conn.prepareStatement("insert into workflows (workflow_id, session_od, owner,description, related_dataset, start_date, end_date, status, progress, result values (?, ?, '', now(), now(), 'RUNNING', 0.0, 'started workflow'");
			stm.setString(1, sessionId);
			
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
		return null;//(new DataSet[out.size()]);
		} catch (SQLException e) {
			return null;
			//throw new DBStoreException(e);
		}
		finally {
            if (stm != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { stm.close(); } catch (SQLException logOrIgnore) {}
        }
	}

	public void addEntry(String workflowId, String entry) {
		// TODO Auto-generated method stub

	}

	public void setStatus(String workflowId, WorkflowStatus status) {
		// TODO Auto-generated method stub

	}

	public void setProgress(String workflowId, double progress) {
		// TODO Auto-generated method stub

	}

	public List<String> getLog(String workflowId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Workflow getWorkflow(String workflowId) {
		// TODO Auto-generated method stub
		return null;
	}

}
