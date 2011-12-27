package com.saltaku.data.compress;

public interface DataCompressor {

	public byte[] compress(byte[] in);
	public byte[] decompress(byte[] in);
	
}
