package com.saltaku.tileserver.render.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.util.zip.Deflater;

import jj2000.j2k.entropy.encoder.ByteOutputBuffer;

import com.saltaku.tileserver.providers.basemaps.CompressionUtil;
import com.saltaku.tileserver.providers.basemaps.impl.ZipCompressor;


public class FastPngWriter {
	
	protected static final int SIZE=256;
	protected static final byte PLTE[] = { 80, 76, 84, 69 };
	protected static final byte PLTE_FULL[] = { 80, 76, 84, 69, 0, 0, 0, 0, 84, -1, 50, -103, -1, 101, -52, -1, -103, -19, -1, -52, -1, -1, -1, -1, -52, -1, -18, -103, -1, -52, 101, -1, -103, 50, -1, 85, 0, -121, -64, 70, 15, }; 
    
	/** IHDR tag. */
    protected static final byte IHDR[] = {73, 72, 68, 82};
    protected static final byte IHDR_FULL[] = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 1, 0, 0, 0, 1, 0, 4, 3, 0, 0, 0, -82, 92, -75, 85, }; 
    
    /** IDAT tag. */
    protected static final byte IDAT[] = {73, 68, 65, 84};
    
    /** IEND tag. */
    protected static final byte IEND[] = {73, 69, 78, 68};
    
    
    protected static final byte TRNS[] = {116, 82, 78, 83};
    protected static final byte TRNS_FULL[] = {  0, 0, 0, 1, 116, 82, 78, 83, 0, 64, -26, -40, 102, };
    
    protected static final  byte[]  pngIdBytes = {-119, 80, 78, 71, 13, 10, 26, 10};
	//private static final 

    protected CRC32 crc = new CRC32();
    
    public void writePng(int[] data, int[] palette,OutputStream os) throws IOException
	{
		writeHeader(os);
		writePalette(os,palette);
		writeTransparency(os);
		writeData(os, data);
		writeEnd(os);
	}
	
	protected void writeHeader(OutputStream os) throws IOException
	{
		os.write(IHDR_FULL);
		/*
		os.write(pngIdBytes);
		os.write(CompressionUtil.int2bytele(13));  // Should be the length of hdr in bytes
		printData(pngIdBytes);
		printData(CompressionUtil.int2bytele(13));
		ByteArrayOutputStream bos=new ByteArrayOutputStream(13+4);
		bos.write(IHDR);
		//System.out.println(CompressionUtil.int2bytele(256)[0],CompressionUtil.int2bytele(256)[1])
		bos.write(CompressionUtil.int2bytele(SIZE));  //width
		bos.write(CompressionUtil.int2bytele(SIZE));  //height
		bos.write(4); // bit depth - 4 bits to encode 16 colors of palette
		bos.write(3); // colour type - indexed colour
		bos.write(0);  // compression method - Only compression method 0 (deflate/inflate compression with a sliding window of at most 32768 bytes) is defined in this International Standard.
		bos.write(0);  // filter method - Only filter method 0 (adaptive filtering with five basic filter types) is defined
		bos.write(0);  // interlacing - no interlace
		writeDataAndChecksum(os,bos.toByteArray());
		bos.close();
		*/
	}
	
	protected void writePalette(OutputStream os,int[] palette) throws IOException
	{
		//System.out.println("Palette length is "+palette.length);
		//os.write(PLTE_FULL);
		
		os.write(CompressionUtil.int2bytele(3*palette.length+3));  // (10 colors + 1 transparent color )*3 bytes = 33 bytes
		ByteArrayOutputStream bos=new ByteArrayOutputStream(3*palette.length+3+4);
		bos.write(PLTE);
		bos.write(CompressionUtil.int2rgb(0));
		for(int c: palette)
		{
			bos.write(CompressionUtil.int2rgb(c));  // r,g,b
		}
		writeDataAndChecksum(os,bos.toByteArray());
		bos.close();
		
		
	}
	
	protected void writeTransparency(OutputStream os) throws IOException
	{
		os.write(TRNS_FULL);
		/*os.write(CompressionUtil.int2bytele(1));
		printData(CompressionUtil.int2bytele(1));
		ByteArrayOutputStream bos=new ByteArrayOutputStream(1+4);
		bos.write(TRNS);
		bos.write(0);  // write transparency for first element of the palette
		writeDataAndChecksum(os,bos.toByteArray());
		bos.close();*/
	}
	
    protected void mfilterSub(byte[] pixels) {
        byte leftByte, a;//new byte[16];
        int k=0;
        int i=0;
        for(int row=0;row<SIZE;row++){
        	a=0;
        	leftByte=0;
        	k++;
		        for (i = 1; i < (SIZE>>1); i++) {
		        	a=pixels[k];
		        	pixels[k] = (byte) ((pixels[k] - leftByte) % 256);
		            leftByte =  a;
		            k++;
		        }
        }
    }
    
    protected byte[] filterSub(byte[] pixels) {
        byte[] out=new byte[pixels.length+SIZE];
    	byte leftByte, a;//new byte[16];
        int k=0;
        int i=0;
        int j=0;
        for(int row=0;row<256;row++){
        	a=0;
        	out[k++]=1;
        	out[k++]=pixels[j++];
		        for (i = 1; i < 128; i++) {
		        	out[k]=(byte) ((pixels[j]-pixels[j-1]) % 256);
		        	j++;
		            k++;
		        }
        }
        return out;
    }
    
    protected byte[] filterNone(byte[] pixels) {
//        byte[] out=new byte[pixels.length+SIZE];
        byte[] out=new byte[(SIZE*SIZE>>1)+SIZE];
    	//byte leftByte, a;//new byte[16];
        int k=0;
        int i=0;
        int j=0;
        //out[k++]=0;
        for(int row=0;row<SIZE;row++){
        	//a=0;
        	out[k++]=0;
        	for (i = 0; i < (SIZE>>1); i++) {
        	    //	System.out.print(" "+(pixels[j]>>>4)+" "+pixels[j] % 16 );
		        	out[k++]=pixels[j++];
        		//out[k++]=18;
		        }
        	   // System.out.println();
        }
        
        return out;
    }
    
    
	protected void writeData(OutputStream os, int[] data) throws IOException
	{
	//	byte[] d=data;
		byte[] d=CompressionUtil.fourbitint2byte(data);
		d=this.filterNone(d);
		//d=this.filterSub(d);
		ByteArrayOutputStream bos=new ByteArrayOutputStream(d.length >>> 2);
		//bos.write(IDAT);
		bos.write(IDAT);
		this.compressAndWrite(d,4,bos);//Deflater.BEST_COMPRESSION);
		byte[] b=bos.toByteArray();
		os.write(CompressionUtil.int2bytele(b.length-4));
		
		this.writeDataAndChecksum(os, b);
		
		//bos.write(d);
		//writeDataAndChecksum(os,bos.toByteArray());
	}
	
	protected void writeEnd(OutputStream os) throws IOException
	{
		os.write(CompressionUtil.int2bytele(0));
		os.write(IEND);
		os.write(CompressionUtil.int2bytele(-1371381630));
	}
	
	protected long getChecksum(byte[] bytes)
	{
		CRC32 ch=new CRC32();
		ch.update(bytes);
		return ch.getValue();
		
	}
	
	protected void writeDataAndChecksum(OutputStream os, byte[] data) throws IOException
	{
		os.write(data);
		//System.out.println("Length of data is "+data.length);
		os.write(CompressionUtil.int2bytele((int)this.getChecksum(data)));
		//printData(data);
		//printData(CompressionUtil.int2bytele((int)this.getChecksum(data)));
	}
	
	
	protected void compressAndWrite(byte[] input, int compressionLevel, OutputStream os) throws IOException {
		//byte[] input=(in);
		// Create the compressor with highest level of compression
		Deflater compressor = new Deflater();
		compressor.setLevel(compressionLevel);// BEST_COMPRESSION);
		//compressor.setLevel(Deflater.BEST_COMPRESSION);
		compressor.setInput(input);
		compressor.finish();

		//ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
		
		byte[] buf = new byte[10000];
		while (!compressor.finished()) {
		    int count = compressor.deflate(buf);
		    os.write(buf, 0, count);
		}
	}
	/*
	protected void printData(byte[] input)
	{
	System.out.print("{ ");
	for(byte b: input) {System.out.print(b+", "); };
	System.out.println("} ");
	}*/
}
