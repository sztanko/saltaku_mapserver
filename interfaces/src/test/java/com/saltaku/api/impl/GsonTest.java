package com.saltaku.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.gson.Gson;
import com.saltaku.api.APIException;
import com.saltaku.api.WebAPI;
import com.saltaku.api.beans.PartitioningType;
import com.saltaku.api.servlet.beans.APIResult;

public class GsonTest {

	@Test
	public void testGson() throws APIException
	{
		WebAPI api=new TestWebAPI();
		Gson g=new Gson();
		Object obj=api.getPartitions("1", "1", "", "avg", 10, PartitioningType.LABEL);
		APIResult result=new APIResult();
		result.response=obj;
		System.out.println(g.toJson(result));
	}
	
}
