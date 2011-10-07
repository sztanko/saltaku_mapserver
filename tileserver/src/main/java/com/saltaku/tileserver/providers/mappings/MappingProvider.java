package com.saltaku.tileserver.providers.mappings;

public interface MappingProvider {
	public int[] getMapping(String mapId) throws MappingProviderException;
}
