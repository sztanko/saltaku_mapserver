package com.saltaku.data.compress;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.saltaku.data.TestUtils;
import com.saltaku.data.compress.impl.ZipDataCompressor;
import com.saltaku.data.serde.DataSerializer;
import com.saltaku.data.serde.impl.SimpleDataSerializer;

public class CompressorSpeedTest {
DataSerializer serde=new SimpleDataSerializer();
//DataCompressor comp=new ZipDataCompressor();
	
	@Test
	public void testCompressor(){
		int[] sizes={10, 100, 1000, 34000};
		double[] d=TestUtils.readFile("resources/data/imd2010.txt");
	for(int i=1;i<10;i++)
	{
		DataCompressor comp=new ZipDataCompressor(i);	
		System.out.print(i+"\t");
		for(int size: sizes)
		{
			this.performTest(Arrays.copyOf(d, size),comp);
		}
		System.out.println();
	}
	}

	protected void performTest(double[] data, DataCompressor comp)
	{
		//System.out.println("Testing serialization of "+size+" elements");
		//double[] data=TestUtils.generateData(size, 1, 100);
		//double[] data=new double[size];
		byte[] bb=serde.serialize(data);
		byte[] res=null;
		byte[] c=null;
		long t1=System.nanoTime();
		for(int i=0;i<32;i++)
		{
			c=comp.compress(bb);
		}
		long t2=System.nanoTime();
		long serTime=(t2-t1)/32;
		int serSize=c.length;
		t1=System.nanoTime();
		for(int i=0;i<32;i++)
		{
			res=comp.decompress(c);
		}
		t2=System.nanoTime();
		Assert.assertArrayEquals(bb,res);
		long deserTime=(t2-t1)/32;
		System.out.print(100*serSize/bb.length+"%("+serTime/1000+"/"+deserTime/1000+")\t");
		//System.out.print(serSize+"\t"+(100*serSize/bb.length)+"%\t"+serTime+"\t"+deserTime);
		}
}
