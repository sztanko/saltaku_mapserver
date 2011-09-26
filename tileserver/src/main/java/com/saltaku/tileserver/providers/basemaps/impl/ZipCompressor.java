package com.saltaku.tileserver.providers.basemaps.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.saltaku.tileserver.providers.basemaps.BasemapCompressor;
import com.saltaku.tileserver.providers.basemaps.CompressionUtil;

public class ZipCompressor implements BasemapCompressor {
	
	@Inject
	public ZipCompressor()
	{
		
	}
	
	public byte[] compress(int[] in) {
		return this.compress(CompressionUtil.int2byte(in), Deflater.BEST_COMPRESSION);
	}
	
	public byte[] compress(byte[] input, int compressionLevel) {
		//byte[] input=(in);
		// Create the compressor with highest level of compression
		Deflater compressor = new Deflater();
		compressor.setLevel(compressionLevel);// BEST_COMPRESSION);
		//compressor.setLevel(Deflater.BEST_COMPRESSION);
		compressor.setInput(input);
		compressor.finish();

		ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
		try{
		bos.write(CompressionUtil.int2byte(input.length));
		} catch (IOException e) {
		}

		byte[] buf = new byte[10000];
		while (!compressor.finished()) {
		    int count = compressor.deflate(buf);
		    bos.write(buf, 0, count);
		}
		try {
		    bos.close();
		} catch (IOException e) {
		}

		// Get the compressed data
		byte[] compressedData = bos.toByteArray();
		return compressedData;
	}
	
	public int[] decompress(byte[] in) {
		Inflater decompressor = new Inflater();
		int len=CompressionUtil.byte2int(in[0], in[1], in[2], in[3]);
		byte[] out =new byte[len];
		decompressor.setInput(Arrays.copyOfRange(in, 4, in.length-4));  // Copying an array and passing here is faster then using
		//decompressor.setInput(in, 4, in.length-4);  // this setInput
		try {
			decompressor.inflate(out);
		} catch (DataFormatException e) {

		}

		// Get the decompressed data
		return CompressionUtil.byte2int(out);
	}

	
}
