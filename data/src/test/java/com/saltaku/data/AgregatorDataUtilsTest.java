package com.saltaku.data;

import java.util.Arrays;

import org.junit.Test;

public class AgregatorDataUtilsTest {

	
	@Test
	public void testRemapTime()
	{
		int[] sizes={10, 100, 1000, 30000, 100000};
		for(int size: sizes){
			long t1=System.nanoTime();
			AggregateDataUtils.remap(TestUtils.generateData(size, 1, 100), this.genMap(size, size/10+5));
			long t2=System.nanoTime();
			System.out.println(size+" elements remapped in " +(t2-t1)/1000+" mks");
		}
	}
	
	@Test
	public void testCountAggregate()
	{
		double[] data={1.1, 1.2, 1.3, 2.1,2.2, 3.1,3.2,5.1};
		int map[]={0,0,0,1,1,2,2,3};
		double[][] remap=AggregateDataUtils.remap(data, map);
		for(int i=0;i<remap.length;i++)
		{
			System.out.println(Arrays.toString(remap[i]));
		}
		System.out.println(Arrays.toString(AggregateDataUtils.aggregate(remap, AggregateDataUtils.COUNT)));
		System.out.println(Arrays.toString(AggregateDataUtils.aggregate(remap, AggregateDataUtils.SUM)));
		System.out.println(Arrays.toString(AggregateDataUtils.aggregate(remap, AggregateDataUtils.AVG)));
		System.out.println(Arrays.toString(AggregateDataUtils.aggregate(remap, AggregateDataUtils.GEOMAVG)));
		System.out.println(Arrays.toString(AggregateDataUtils.aggregate(remap, AggregateDataUtils.MAD)));
		System.out.println(Arrays.toString(AggregateDataUtils.aggregate(remap, AggregateDataUtils.MEDIAN)));
	}
	
	@Test
	public void testCountSpeed()
	{
		int[] sizes={10, 100, 1000, 30000, 100000};
		Aggregator[] ags={AggregateDataUtils.COUNT, AggregateDataUtils.SUM, AggregateDataUtils.AVG, AggregateDataUtils.GEOMAVG, AggregateDataUtils.MAD, AggregateDataUtils.MEDIAN};
		System.out.print("#\t");
		for(Aggregator ag: ags)
		{
			System.out.print(ag.getClass().getSimpleName().replace("Aggregator", "")+"\t\t");
		}
		System.out.println();
		for(int size: sizes){
			System.out.print(size+"\t");
			double[][] remap=AggregateDataUtils.remap(TestUtils.generateData(size, 1, 100), this.genMap(size, size/10+5));
			for(Aggregator ag: ags)
			{
				long t1=System.nanoTime();
				for(int i=0;i<32;i++){
					AggregateDataUtils.aggregate(remap, ag);
				}
				long t2=System.nanoTime();
				System.out.print((t2-t1)/32+"\t\t");
			}
			System.out.println();
			
		}
	}
	
	public int[] genMap(int size, int maxValue)
	{
		int[] out=new int[size];
		for(int i=0;i<size;i++){
			out[i]=(int)(Math.random()*maxValue);
		}
		return out;
	}

}

