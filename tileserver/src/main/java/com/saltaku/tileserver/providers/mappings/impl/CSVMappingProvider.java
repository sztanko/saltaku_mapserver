package com.saltaku.tileserver.providers.mappings.impl;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import au.com.bytecode.opencsv.CSVReader;

import com.google.inject.Inject;
import com.saltaku.tileserver.providers.mappings.MappingProvider;
import com.saltaku.tileserver.providers.mappings.MappingProviderException;

public class CSVMappingProvider implements MappingProvider {
	private String path;
	private long maxItems;
	private long numItems=0;
	private char delimiter;
	private int valueField;
	private Logger log;
	int[] colorMapping;
	
	private Map<String, int[]> buffer = new HashMap<String, int[]>();

	@Inject
	public CSVMappingProvider(String path, char delimiter,int valueField,long maxItems, int[] colorMapping, Logger log){
		this.path=path;
		this.delimiter=delimiter;
		this.valueField=valueField;
		this.maxItems=maxItems;
		this.colorMapping=colorMapping;
		this.log=log;
		this.log.info("Initialized CSVMapping provider with base path "+path);
	}
	
	// private Map<String,Long> timestamps=new HashMap<String,Long>();

	public int[] getMapping(String mapId, String colorMappingId) throws MappingProviderException {
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
					// not Thread Safe potentially, but happens very, very rarely. Rewrite if we become famous.
					this.buffer=new HashMap<String, int[]>(); 
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
		int[] out = null;
		List<Integer> list = new ArrayList<Integer>();
		CSVReader reader = null;
		try {
			File f = new File(path + "/" + mapId + "/data.csv.gz");
			if(f.exists()){
			reader = new CSVReader(new InputStreamReader(
					new GZIPInputStream(new FileInputStream(f), delimiter)));
			}
			else{
			f = new File(path + "/" + mapId + "/data.csv");
			reader = new CSVReader(new FileReader(f), delimiter);
			}
			
			String[] l = null;
			while ((l = reader.readNext()) != null) {
				list.add(new Integer(l[valueField]));
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
		for (Integer num : list) {
			out[i++] = num;
			
		}
		return out;
	}

}
