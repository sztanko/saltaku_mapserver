package com.saltaku.data;

import java.util.Arrays;

import org.junit.Test;

public class PartitionDataUtilsTest {

	public final double[] data1={Double.NEGATIVE_INFINITY,
16,90,71,8,48
,70,15,51,57,83, Double.NEGATIVE_INFINITY
,44,49,25,65,55
	};
	
	
	public PartitionDataUtilsTest(){
		
	}
	
	
	
	@Test
	public void testPartitionDataByEqualSize()
	{
		
	System.out.println(Arrays.toString(data1));
	this.performPartitionTest( data1, 2);
	this.performPartitionTest( data1, 3);
	this.performPartitionTest(data1, 4);
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
		PartitionDataUtils du=new PartitionDataUtils();
		this.performPartitionTest( TestUtils.generateData(100, 1, 100), 10);
		this.performPartitionTest(TestUtils.generateData(1000, 1, 100), 10);
		this.performPartitionTest( TestUtils.generateData(10000, 1, 100), 10);
		this.performPartitionTest( TestUtils.generateSquareData(30000, 1, 100), 11);
		this.performPartitionTest(TestUtils.generateData(30000, 1, 100), 11);
		//this.performPartitionTest(du, this.generateData(100000, 1, 1000), 10);
		this.performPartitionTest(TestUtils.generateData(400000, 1, 10000), 10);
		//this.performPartitionTest(du, this.generateData(15000000, 1, 10000), 10);
	}
	
	@Test
	public void testlargeDataValue()
	{

		this.performValuePartitionTest(TestUtils.generateData(100, 1, 100), 10);
		this.performValuePartitionTest( TestUtils.generateData(1000, 1, 100), 10);
		this.performValuePartitionTest(TestUtils.generateData(10000, 1, 100), 10);
		this.performValuePartitionTest( TestUtils.generateSquareData(30000, 1, 100), 11);
		this.performValuePartitionTest( TestUtils.generateData(30000, 1, 100), 11);
		//this.performValuePartitionTest(du, this.generateData(100000, 1, 1000), 10);
		this.performValuePartitionTest(TestUtils.generateData(400000, 1, 10000), 10);
		//this.performPartitionTest(du, this.generateData(15000000, 1, 10000), 10);
	}
	
	public void performPartitionTest( double[] data, int numP)
	{
		long t1=System.nanoTime();
		int[] p=null;
		for(int i=0;i<10;i++)
			p=PartitionDataUtils.partitionDataByEqualSize(data, numP);
		long t2=System.nanoTime();
		System.out.println("Took "+(t2-t1)/10000+" mks to make "+numP+" partitions out of "+data.length+" elements: "+Arrays.toString(PartitionDataUtils.groupCount(p)));
		System.out.println("Normalized form: "+Arrays.toString(PartitionDataUtils.groupCountNormalized(p)));
	}
	
	public void performValuePartitionTest(double[] data, int numP)
	{
		long t1=System.nanoTime();
		int[] p=null;
		for(int i=0;i<10;i++)
			p=PartitionDataUtils.partitionDataByValues(data, numP);
		long t2=System.nanoTime();
		System.out.println("Took "+(t2-t1)/10000+" mks to make "+numP+" partitions out of "+data.length+" elements: "+Arrays.toString(PartitionDataUtils.groupCount(p)));
		System.out.println("Normalized form: "+Arrays.toString(PartitionDataUtils.groupCountNormalized(p)));
	}
	
	
	
}
