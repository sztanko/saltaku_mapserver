package com.saltaku.data.compress.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.saltaku.data.compress.DataCompressor;

@Singleton
public class ZipDataCompressor implements DataCompressor {

	private int compression;
	
	@Inject
	public ZipDataCompressor()
	{
		this.compression=7; //Fastest decompression for this kind of data is achieved at level 7
	}
	
	public ZipDataCompressor(int comp)
	{
		this.compression=comp;
	}
	
	public byte[] compress(byte[] in) {
		return this.compress(in, compression);// BEST_COMPRESSION);
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
		bos.write(this.int2byte(input.length));
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
	
	public byte[] decompress(byte[] in) {
		Inflater decompressor = new Inflater();
		int len=this.byte2int(in[0], in[1], in[2], in[3]);
		byte[] out =new byte[len];
		decompressor.setInput(Arrays.copyOfRange(in, 4, in.length-4));  // Copying an array and passing here is faster then using
		//decompressor.setInput(in, 4, in.length-4);  // this setInput
		try {
			decompressor.inflate(out);
		} catch (DataFormatException e) {

		}
		// Get the decompressed data
		return out;
	}
	
	
	  
	  private byte[] int2byte(int x)
	  {
		  byte[] out=new byte[4];
		  out[0] = (byte) ((x >>> 0) & 0xff);           
		  out[1] = (byte) ((x >>> 8) & 0xff);
		  out[2] = (byte) ((x >>> 16) & 0xff);
		  out[3]= (byte) ((x >>> 24) & 0xff);
		  return out;
	  }
	  
	  private int byte2int(byte b1, byte b2,byte b3, byte b4)
	  {
		  int x = 0;
          x += (b1 & 0xff) << 0;
          x += (b2 & 0xff) << 8;
          x += (b3 & 0xff) << 16;
          x += (b4 & 0xff) << 24;
          return x;
	  }

}
