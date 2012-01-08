package com.saltaku.workflow;

import java.util.List;

import com.saltaku.workflow.beans.Workflow;
import com.saltaku.workflow.beans.WorkflowStatus;

public interface WorkflowManager {
public String createWorkflow(String sessionId);
public void addEntry(String workflowId, String entry);
public void setStatus(String workflowId, WorkflowStatus status);
public void setProgress(String workflowId, double progress);

public List<String> getLog(String workflowId);
public Workflow getProcess(String workflowId);
	
}
