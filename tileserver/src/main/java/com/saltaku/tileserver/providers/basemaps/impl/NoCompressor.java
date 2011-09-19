package com.saltaku.tileserver.providers.basemaps.impl;

import com.saltaku.tileserver.providers.basemaps.BasemapCompressor;
import com.saltaku.tileserver.providers.basemaps.CompressionUtil;

public class NoCompressor implements BasemapCompressor{

	public byte[] compress(int[] in) {  //speed-optimized compression
		return CompressionUtil.int2byte(in);
	}

	public int[] decompress(byte[] in) {
		return CompressionUtil.byte2int(in);
	}

}
