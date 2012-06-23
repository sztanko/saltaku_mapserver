package com.saltaku.api.util;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.saltaku.beans.Tag;

public class Request2JavaUtils {

	public static final int STRING_MAX_LENGTH=50000;
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
public static int[] toIntArray(String[] vals)
{
	int[] out=new int[vals.length];
		for(int i=0;i<vals.length;i++) out[i]=Integer.parseInt(vals[i]);
	return out;
}

public static Map<String,String> toStringMap(String[] vals)
{
	
	Map<String,String> tags=new HashMap<String,String>();
		for(String ts:vals) {
				int pos=ts.indexOf('=');
				if(pos<0)
				{
					tags.put(ts,"");
				}
				else{
					tags.put(ts.substring(0,pos),ts.substring(pos+1));		
				}
		}
	return tags;
}

public static Map<String,Integer> toIntMap(String[] vals)
{
	Map<String,Integer> tags=new HashMap<String,Integer>();
		for(String ts:vals) {
				int pos=ts.indexOf('=');
				tags.put(ts.substring(0,pos),Integer.parseInt(ts.substring(pos+1)));		
				
		}
	return tags;
}

public static Date parseDate(String date) throws ParseException
{
	return df.parse(date);
}

public static Gson buildGson()
{
	return new GsonBuilder().registerTypeAdapter(String.class, new StringSerializer()).serializeSpecialFloatingPointValues().serializeNulls().registerTypeAdapter(double[].class, new DoubleArraySerializer()).create();
}

	
static public class StringSerializer implements JsonSerializer<String> {

	public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
		if(src.length()<STRING_MAX_LENGTH) return new JsonPrimitive(src);
		else
		return new JsonPrimitive(src.substring(0,STRING_MAX_LENGTH)+"... element is too long ("+src.length()+")");
	}
	  
}

static public class DoubleArraySerializer implements JsonSerializer<double[]> {

	public JsonElement serialize(double[] src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(Arrays.toString(src).replace("-Infinity", "-"));
	}
	  
}
}
