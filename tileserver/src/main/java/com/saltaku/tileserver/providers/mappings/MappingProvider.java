package com.saltaku.tileserver.providers.mappings;

public interface MappingProvider {
	public int[] getMapping(String mapId,String colorMappingId) throws MappingProviderException;
}
