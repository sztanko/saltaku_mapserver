package com.saltaku.tileserver.providers.basemaps.storage;

public interface BasemapStorage {

	public byte[] get(String shapeId, String key) throws BasemapStorageException;
	public void put(String shapeId, String key, byte[] data) throws BasemapStorageException;
	public void shutdown() throws BasemapStorageException;
	public void empty(String shapeId) throws BasemapStorageException;
	
}
