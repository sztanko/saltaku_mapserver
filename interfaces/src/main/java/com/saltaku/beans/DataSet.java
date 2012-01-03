package com.saltaku.beans;

import java.util.Date;

import org.opengis.geometry.Envelope;

/*
 * 
Field	Type	Null	Key	Default	Extra
dataset_id	int(11)	NO	PRI	NULL	
datasource_id	varchar(64)	NO	MUL	NULL	
name	varchar(255)	NO		NULL	
date_validity_start	datetime	NO		NULL	
date_validity_end	datetime	NO		NULL	
description	text	NO		NULL	
size	int(11)	NO		NULL	
area_id	int(11)	NO		NULL	
aggregation	varchar(64)	NO		NULL	
bbox	geometry	NO		NULL	
 */

public class DataSet {
	public int id;
	public String dataSourceId;
	public String name;
	public Date start, end;
	public int initialAreaId;
	public String initialAggregation;
}
