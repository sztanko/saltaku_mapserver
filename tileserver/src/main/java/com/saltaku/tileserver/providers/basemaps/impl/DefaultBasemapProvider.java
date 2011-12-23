package com.saltaku.tileserver.providers.basemaps.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import org.opengis.geometry.Envelope;

import com.google.inject.Inject;
import com.saltaku.tileserver.providers.basemaps.BasemapCompressor;
import com.saltaku.tileserver.providers.basemaps.BasemapProviderException;
import com.saltaku.tileserver.providers.basemaps.BasemapRenderer;
import com.saltaku.tileserver.providers.basemaps.BasemapProvider;
import com.saltaku.tileserver.providers.basemaps.CompressionUtil;
import com.saltaku.tileserver.providers.basemaps.storage.BasemapStorage;
import com.saltaku.tileserver.providers.basemaps.storage.BasemapStorageException;
import com.saltaku.tileserver.providers.feature.FeatureProvider;
import com.saltaku.tileserver.providers.feature.FeatureProviderException;
import com.saltaku.tileserver.providers.feature.TileUtils;

public class DefaultBasemapProvider implements BasemapProvider {

	protected BasemapStorage st;
	protected TileUtils tileUtils;
	protected FeatureProvider featureProvider;
	private BasemapCompressor compressor;
	private BasemapRenderer renderer;
	private ConcurrentHashMap<String, ReadWriteLock> keyLockMap = new ConcurrentHashMap<String, ReadWriteLock>();
	private Logger log=Logger.getLogger("DefaultBasemapProvider");
	private int numMisses=0;
	
	private final int empty[]=new int[256*256];
	
	@Inject
	public DefaultBasemapProvider(BasemapStorage st, TileUtils tileUtils, FeatureProvider featureProvider, BasemapCompressor compressor, BasemapRenderer renderer) {
		super();
		this.st = st;
		this.tileUtils = tileUtils;
		this.featureProvider = featureProvider;
		this.compressor = compressor;
		this.renderer = renderer;
		for(int i=0;i<empty.length;i++) empty[i]=0;
		
	}

	public int[] getBasemapForTile(String shapeId, int x, int y, int z) throws BasemapProviderException {
		//String key = "t" + x + ";" + y + ";" + z;
		 
		Envelope env=tileUtils.getTileEnvelope(x, y, z);
		try {
			if(!featureProvider.inside(shapeId, env))
			{
				//System.out.println("Empty "+x+", "+y+", "+z);
				return empty;
			}
		} catch (FeatureProviderException e) {
			throw new BasemapProviderException(e);		}
		String key = new StringBuilder(12).append("t").append(x).append(";").append(y).append(";").append(z).toString();
		
		return this.getBasemap(shapeId, key, env, 256,256);

		/*int[] out = null;
		byte[] b;
		try {
			b = st.get(shapeId, key);
		if (b == null) {
			out= this.generateMap(shapeId,key,, 256, 256);
		}
		else
		{
			out=compressor.decompress(b);
		}
		} catch (BasemapStorageException e) {
		throw new BasemapProviderException(e);
		}
		return out;*/
	}
	
	protected int[] getBasemap(String shapeId, String key, Envelope env, int width, int height) throws BasemapProviderException
	{
		int[] out = null;
		byte[] b;
		try {
			b = st.get(shapeId, key);
		if (b == null) {
			out= this.generateMap(shapeId,key, env, width, height);
		}
		else
		{
			out=compressor.decompress(b);
		}
		} catch (BasemapStorageException e) {
			throw new BasemapProviderException(e);
		}
		return out;
	}

	protected int[] generateMap(String shapeId,String key, Envelope env, int width, int height) throws BasemapProviderException
	{
		String sKey = shapeId+"|"+key;
		//this.log.info("Miss for "+sKey);
		int[] out=null;
		keyLockMap.putIfAbsent(sKey, new ReentrantReadWriteLock());
		Lock writeLock = keyLockMap.get(sKey).writeLock();
		ReadWriteLock reLock=keyLockMap.get(sKey);
		try {
			writeLock.lock ();
			byte[] b = st.get(shapeId, key); 
			if (b == null) {
				//this.log.info("Drawing "+sKey);
				out = renderer.drawBasemap(width, height, featureProvider.get(shapeId, env), env);
				st.put(shapeId, key, compressor.compress(out));
				this.numMisses++;
			} else {
				out = compressor.decompress(b);
			}
		} catch (BasemapStorageException e) {
			throw new BasemapProviderException(e);
		} catch (FeatureProviderException e) {
			throw new BasemapProviderException(e);
		} finally {
			writeLock.unlock();
			keyLockMap.remove(key,reLock);
		}
		return out;
	}
	
	public int[] getBasemapForMap(String shapeId, Envelope env, int width, int height) throws BasemapProviderException {
		return this.getBasemap(shapeId, TileUtils.getEnvelopeKey(env), env, width, height);
	}

	public int getNumMisses() {
		return numMisses;
	}

	public void stop() throws BasemapProviderException {
		try {
			this.st.shutdown();
		} catch (BasemapStorageException e) {
			throw new BasemapProviderException();
		}
		
	}

}
