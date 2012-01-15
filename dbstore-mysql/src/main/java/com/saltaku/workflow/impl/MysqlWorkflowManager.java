package com.saltaku.workflow.impl;

import java.util.List;

import com.google.inject.Inject;
import com.saltaku.store.mysql.DbConnectionManager;
import com.saltaku.workflow.WorkflowManager;
import com.saltaku.workflow.beans.Workflow;
import com.saltaku.workflow.beans.WorkflowStatus;

public class MysqlWorkflowManager implements WorkflowManager {
	DbConnectionManager mng;
	
	@Inject
	public MysqlWorkflowManager(DbConnectionManager mng)
	{
		this.mng=mng;
	}
	
	public String createWorkflow(String sessionId) {
		// TODO Auto-generated method stub
		return null;
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
