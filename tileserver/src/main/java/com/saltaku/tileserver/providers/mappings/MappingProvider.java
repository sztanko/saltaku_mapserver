package com.saltaku.tileserver.providers.mappings;

//TODO implement mysql mapping provider
public interface MappingProvider {
	public int[] getMapping(String mapId) throws MappingProviderException;
}
