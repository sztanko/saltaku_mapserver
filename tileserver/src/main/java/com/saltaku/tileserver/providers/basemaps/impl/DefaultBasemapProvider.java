package com.saltaku.tileserver.providers.basemaps.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import org.opengis.geometry.Envelope;

import com.saltaku.tileserver.providers.basemaps.BasemapCompressor;
import com.saltaku.tileserver.providers.basemaps.BasemapProviderException;
import com.saltaku.tileserver.providers.basemaps.BasemapRenderer;
import com.saltaku.tileserver.providers.basemaps.BasemapProvider;
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
	private Logger log;
	private int numMisses=0;
	
	
	public DefaultBasemapProvider(BasemapStorage st, TileUtils tileUtils, FeatureProvider featureProvider, BasemapCompressor compressor, BasemapRenderer renderer,Logger log) {
		super();
		this.st = st;
		this.tileUtils = tileUtils;
		this.featureProvider = featureProvider;
		this.compressor = compressor;
		this.renderer = renderer;
		this.log=log;
	}

	public int[] getBasemapForTile(String shapeId, int x, int y, int z) throws BasemapProviderException {
		String key = "t" + x + ";" + y + ";" + z;
		//String key = new StringBuilder(12).append("t").append(x).append(";").append(y).append(";").append(z);
		

		int[] out = null;
		byte[] b;
		try {
			b = st.get(shapeId, key);
		if (b == null) {
			this.generateMap(shapeId,key, tileUtils.getTileEnvelope(x, y, z), 256, 256);
		}
		} catch (BasemapStorageException e) {
		throw new BasemapProviderException(e);
		}
		return out;
	}

	protected int[] generateMap(String shapeId,String key, Envelope env, int width, int height) throws BasemapProviderException
	{
		String sKey = shapeId+"|"+key;
		int[] out=null;
		keyLockMap.putIfAbsent(sKey, new ReentrantReadWriteLock());
		Lock writeLock = keyLockMap.get(sKey).writeLock();
		ReadWriteLock reLock=keyLockMap.get(sKey);
		try {
			writeLock.lock ();
			byte[] b = st.get(shapeId, key); 
			if (b == null) {
				//this.log.info("Miss for "+sKey);
				out = renderer.drawBasemap(256, 256, featureProvider.get(shapeId, env), env);
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
		// TODO Auto-generated method stub
		return null;
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
