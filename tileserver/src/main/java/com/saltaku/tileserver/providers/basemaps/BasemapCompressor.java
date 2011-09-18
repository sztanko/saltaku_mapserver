package com.saltaku.tileserver.providers.basemaps;

public interface BasemapCompressor {
public int[] compress(int[] in);
public int[] decompress(int[] in);
}
