package com.saltaku.area.importer.propertymapper.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.opengis.feature.Property;

import com.google.inject.Inject;
import com.saltaku.area.importer.propertymapper.AreaFeatures;
import com.saltaku.area.importer.propertymapper.PropertyMapper;


public class AttributePropertyMapper implements PropertyMapper {

	//Map<String,AreaFeatures> mapping = new HashMap<String, AreaFeatures>();
	
	private String columnForCode, columnForName, columnForEnglishName;
	
	@Inject
	public AttributePropertyMapper(String columnForCode,String columnForName, String columnForEnglishName)
	{
		this.columnForCode=columnForCode;
		this.columnForName=columnForName;
		this.columnForEnglishName=columnForEnglishName;
	}
	
	public AreaFeatures getFeatures(Collection<Property> props) {
		AreaFeatures f=new AreaFeatures();
		for(Property p: props)
		{
			//System.out.println("Checking property "+p.getName());
			if(p.getName().toString().equals(this.columnForCode))
			{
				f.setCode(p.getValue().toString());
			//	System.out.println("Set "+f.getCode()+" as Code");
			}
			if(p.getName().toString().equals(this.columnForName))
			{
				f.setName(p.getValue().toString());
			}
			if(p.getName().toString().equals(this.columnForEnglishName))
			{
				f.setEnglishName(p.getValue().toString());
			}
		}
		
		if(this.columnForEnglishName==null) f.setEnglishName(f.getCode());
		if(this.columnForName==null) f.setName(f.getCode());
		return f;
	}
	
	

}
