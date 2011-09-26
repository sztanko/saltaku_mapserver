package com.saltaku.tileserver.render.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;

import com.saltaku.tileserver.providers.basemaps.CompressionUtil;
import com.saltaku.tileserver.providers.basemaps.impl.ZipCompressor;


public class FastPngWriter {
	protected static final byte PLTE[] = { 80, 76, 84, 69 };
    
	/** IHDR tag. */
    protected static final byte IHDR[] = {73, 72, 68, 82};
    
    /** IDAT tag. */
    protected static final byte IDAT[] = {73, 68, 65, 84};
    
    /** IEND tag. */
    protected static final byte IEND[] = {73, 69, 78, 68};
    
    
    protected static final byte TRNS[] = {116, 82, 78, 83}; 
	//private static final 

    protected CRC32 crc = new CRC32();
    
    protected ZipCompressor compressor=new ZipCompressor();
    
	public void writePng(int[] data, int[] palette,OutputStream os) throws IOException
	{
		
		// Write png id
		
		//Write header
		
		writeHeader(os);
	}
	
	protected void writeHeader(OutputStream os) throws IOException
	{
		
		os.write(13);  // Should be the length of hdr in bytes
		os.write(IHDR);
		
		os.write(256);  //width
		os.write(256);  //height
		os.write((byte)4); // bit depth - 4 bits to encode 16 colors of palette
		os.write((byte)3); // colour type - indexed colour
		os.write((byte)0);  // compression method - Only compression method 0 (deflate/inflate compression with a sliding window of at most 32768 bytes) is defined in this International Standard.
		os.write((byte)0);  // filter method - Only filter method 0 (adaptive filtering with five basic filter types) is defined
		os.write((byte)0);  // interlacing - no interlace

		//TODO: write crc
		
	}
	
	protected void writePalette(OutputStream os,int[] palette) throws IOException
	{
		os.write(33);  // (10 colors + 1 transparent color )*3 bytes = 33 bytes
		os.write(PLTE);
		os.write(CompressionUtil.int2rgb(0));
		for(int c: palette)
		{
			os.write(CompressionUtil.int2rgb(c));  // r,g,b
		}
		
		//TODO: write crc
		
	}
	
	protected void writeTransparency(OutputStream os) throws IOException
	{
		os.write(1);
		os.write(TRNS);
		os.write((byte)0);  // write transparency for first element of the palette
		
		//TODO write crc
	}
	
    protected void filterSub(byte[] pixels) {
        byte leftByte, a;//new byte[16];
        int k=0;
        for(int row=0;row<256;row++){
        	a=0;
        	leftByte=0;
		        for (int i = 1; i < 256; i++) {
		        	a=pixels[k];
		        	pixels[k] = (byte) ((pixels[k] - leftByte) % 256);
		            leftByte =  a;
		            k++;
		        }
        }
    }
    
	protected void writeData(OutputStream os, int[] data) throws IOException
	{
	//	byte[] d=data;
		byte[] d=CompressionUtil.fourbitint2byte(data);
		this.filterSub(d);
		byte[] c=compressor.compress(d,4);
		os.write(c.length);
		os.write(IDAT);
		os.write(c);
		//TODO write crc
	}
	
	protected void writeEnd(OutputStream os) throws IOException
	{
		os.write(0);
		os.write(IEND);
	}
}
