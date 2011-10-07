package com.saltaku.tileserver.providers.mappings.impl;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import au.com.bytecode.opencsv.CSVReader;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.saltaku.tileserver.providers.mappings.MappingProvider;
import com.saltaku.tileserver.providers.mappings.MappingProviderException;

public class CSVMappingProvider implements MappingProvider {
	private String path;
	private long maxItems;
	private long numItems=0;
	private char delimiter;
	private int valueField;
	private Logger log;
	
	
	private Map<String, int[]> buffer = new ConcurrentHashMap<String, int[]>();

	@Inject
	public CSVMappingProvider(
				@Named("csvPath") String path, 
				@Named("csvDelimiter") char delimiter, 
				@Named("csvValueField") int valueField, 
				@Named("csvCacheSize") long maxItems, Logger log){
		this.path=path;
		this.delimiter=delimiter;
		this.valueField=valueField;
		this.maxItems=maxItems;
		this.log=log;
		this.log.info("Initialized CSVMapping provider with base path "+path);
	}
	
	// private Map<String,Long> timestamps=new HashMap<String,Long>();

	public int[] getMapping(String mapId) throws MappingProviderException {
		//TODO: support variable color mappings;
		String key = mapId;// + "|" + colorMappingId;
		
		int[] out = null;
		
		if (this.buffer.containsKey(key)) {
			out = buffer.get(key);
		} else {
			synchronized (buffer) {
				// Check it once again, maybe it has been initialised already while waiting
				if (this.buffer.containsKey(key)) {
					return buffer.get(key);
				} 
				
				if(numItems>maxItems)
				{
					//TODO not Thread Safe potentially, but happens very, very rarely. Rewrite if we become famous.
					this.buffer=new ConcurrentHashMap<String, int[]>(); 
					numItems=0;
				}
				out=this.loadMapping(mapId);
				numItems+=out.length;
				this.buffer.put(key,out);
			}
		}
		return out;

	}

	protected int[] loadMapping(String mapId) throws MappingProviderException {
		this.log.info("Could not find data for "+ mapId+", loading it");
		long t1=System.currentTimeMillis();
		int[] out = null;
		List<Integer> list = new ArrayList<Integer>();
		CSVReader reader = null;
		try {
			File f = new File(path + "/" + mapId + "/data.csv.gz");
			if(f.exists()){
			//	System.out.println("Delimiter is "+delimiter);
			reader = new CSVReader(new InputStreamReader(
					new GZIPInputStream(new FileInputStream(f))), delimiter);
			}
			else{
			f = new File(path + "/" + mapId + "/data.csv");
			//System.out.println("Delimiter is "+delimiter);
			reader = new CSVReader(new FileReader(f), delimiter);
			}
			
			String[] l = null;
			while ((l = reader.readNext()) != null) {
				
				list.add(new Integer(l[valueField].trim()));
			}
		} catch (IOException e) {
			throw new MappingProviderException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new MappingProviderException(e);
				}
			}
		}

		out = new int[list.size()];
		int i = 0;
		for (Integer num : list) { out[i++]=num+1;
			/*if(num>=0){
			out[i++] = this.colorMapping[num];
			}
			else
			{
				out[i++]=0;
			}*/
			
		}
		long t2=System.currentTimeMillis();
		System.out.println("File loaded in "+(t2-t1)+"ms");
		return out;
	}

}
