package com.saltaku.data;

import java.util.Arrays;

import org.junit.Test;

public class DataUtilsTest {

	public final double[] data1={Double.NEGATIVE_INFINITY,
16,90,71,8,48
,70,15,51,57,83, Double.NEGATIVE_INFINITY
,44,49,25,65,55
	};
	
	
	public DataUtilsTest(){
		
	}
	
	protected double[] generateData(int num, double min, double max){
		double[] out=new double[num];
		double r=max-min;
		for(int i=0;i<num;i++){ 
			out[i]=Math.random()*r+min;
		}
		return out;
	}
	
	protected double[] generateSquareData(int num, double min, double max){
		double[] out=new double[num];
		double r=max-min;
		for(int i=0;i<num;i++) { 
			out[i]=Math.random(); out[i]*=out[i]; out[i]=out[i]*r+min;
		}
		return out;
	}
	
	@Test
	public void testPartitionDataByEqualSize()
	{
		DataUtils du=new DataUtils();
		
	System.out.println(Arrays.toString(data1));
	this.performPartitionTest(du, data1, 2);
	this.performPartitionTest(du, data1, 3);
	this.performPartitionTest(du, data1, 4);
	/*System.out.println(Arrays.toString(du.partitionDataByEqualSize(data1, 3)));
	System.out.println(Arrays.toString(du.groupCount(du.partitionDataByEqualSize(data1, 3))));
	System.out.println(Arrays.toString(du.partitionDataByEqualSize(data1, 4)));
	System.out.println(Arrays.toString(du.groupCount(du.partitionDataByEqualSize(data1, 4))));
	System.out.println(Arrays.toString(du.partitionDataByEqualSize(data1, 2)));
	System.out.println(Arrays.toString(du.groupCount(du.partitionDataByEqualSize(data1, 2))));*/
	}
	
	@Test
	public void testlargeDataPartition()
	{
		DataUtils du=new DataUtils();
		this.performPartitionTest(du, this.generateData(100, 1, 100), 10);
		this.performPartitionTest(du, this.generateData(1000, 1, 100), 10);
		this.performPartitionTest(du, this.generateData(10000, 1, 100), 10);
		this.performPartitionTest(du, this.generateSquareData(30000, 1, 100), 11);
		this.performPartitionTest(du, this.generateData(30000, 1, 100), 11);
		//this.performPartitionTest(du, this.generateData(100000, 1, 1000), 10);
		this.performPartitionTest(du, this.generateData(400000, 1, 10000), 10);
		//this.performPartitionTest(du, this.generateData(15000000, 1, 10000), 10);
	}
	
	@Test
	public void testlargeDataValue()
	{
		DataUtils du=new DataUtils();
		this.performValuePartitionTest(du, this.generateData(100, 1, 100), 10);
		this.performValuePartitionTest(du, this.generateData(1000, 1, 100), 10);
		this.performValuePartitionTest(du, this.generateData(10000, 1, 100), 10);
		this.performValuePartitionTest(du, this.generateSquareData(30000, 1, 100), 11);
		this.performValuePartitionTest(du, this.generateData(30000, 1, 100), 11);
		//this.performValuePartitionTest(du, this.generateData(100000, 1, 1000), 10);
		this.performValuePartitionTest(du, this.generateData(400000, 1, 10000), 10);
		//this.performPartitionTest(du, this.generateData(15000000, 1, 10000), 10);
	}
	
	public void performPartitionTest(DataUtils du, double[] data, int numP)
	{
		long t1=System.nanoTime();
		int[] p=null;
		for(int i=0;i<10;i++)
			p=du.partitionDataByEqualSize(data, numP);
		long t2=System.nanoTime();
		System.out.println("Took "+(t2-t1)/10000+" mks to make "+numP+" partitions out of "+data.length+" elements: "+Arrays.toString(du.groupCount(p)));
		//System.out.println(Arrays.toString(du.groupCount(p)));
	}
	
	public void performValuePartitionTest(DataUtils du, double[] data, int numP)
	{
		long t1=System.nanoTime();
		int[] p=null;
		for(int i=0;i<10;i++)
			p=du.partitionByValues(data, numP);
		long t2=System.nanoTime();
		System.out.println("Took "+(t2-t1)/10000+" mks to make "+numP+" partitions out of "+data.length+" elements: "+Arrays.toString(du.groupCount(p)));
		
	}
	
	
	
}
