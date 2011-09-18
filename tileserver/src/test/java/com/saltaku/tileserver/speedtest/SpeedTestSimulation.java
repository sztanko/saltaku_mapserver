package com.saltaku.tileserver.speedtest;

import java.util.Random;

import org.junit.Test;

import junit.framework.TestCase;

public class SpeedTestSimulation extends TestCase {

	public static final int TILESIZE=256;
	@Test
	public void testSpeed()
	{
		int sizes[] = {10,100,35000,35000,35000,35000,35000,35000,35000,35000,35000,8000000,8000000,8000000,8000000,8000000};
		long t1=System.currentTimeMillis();
		int[][] mappings=new int[sizes.length][];
		int[][] buffers=new int[sizes.length][];
		for(int i=0;i<sizes.length;i++)
		{
		long tt1=System.currentTimeMillis();
		mappings[i]=this.getRandomMapping(sizes[i]);
		buffers[i]=this.getRandomImgBuffer(sizes[i]);
		long tt2=System.currentTimeMillis();
		System.out.println("Random generation for "+sizes[i]+" took "+(tt2-tt1)+"ms");
		}
		long t2=System.currentTimeMillis();
		for(int i=0;i<sizes.length;i++){
			this.getOut(sizes[i],mappings[i],buffers[i]);
		}
		long t3=System.currentTimeMillis();
		System.out.println("Random generations took "+(t2-t1)+"ms");
		System.out.println("Mappings took "+(t3-t2)+"ms");
		System.out.println("The whole thing took "+(t3-t1)+"ms");
	}
	
	public int[] getOut(int mappingSize,int[] mapping, int[] buffer)
	{
	long t1=System.currentTimeMillis();
	int[] out=new int[TILESIZE*TILESIZE];
	for(int i=0;i<out.length;i++)
		{
			out[i]=mapping[buffer[i]];
		}
	long t2=System.currentTimeMillis();
	System.out.println("Mappings: "+mappingSize+", time:"+(t2-t1)+"ms");
	return out;
	
	}
	
	private int[] getRandomMapping(int size)
	{
		int[] out=new int[size];
		Random r=new Random();
		for(int i=0;i<out.length;i++)
		{
			out[i]=r.nextInt(10);
		}
		return out;
	}
	
	private int[] getRandomImgBuffer(int numMappings)
	{
		int[] out=new int[TILESIZE*TILESIZE];
		Random r=new Random();
		for(int i=0;i<out.length;i++) out[i]=r.nextInt(numMappings);
		return out;
	}
	
}
