package com.saltaku.tileserver.providers.basemaps;

public interface BasemapCompressor {
public byte[] compress(int[] in);
public int[] decompress(byte[] in);
}
