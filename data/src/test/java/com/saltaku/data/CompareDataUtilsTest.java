package com.saltaku.data;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.saltaku.data.serde.impl.SimpleDataSerializer;

public class CompareDataUtilsTest {

	public final double[] data1={Double.NEGATIVE_INFINITY,
16,90,71,8,48
,70,15,51,57,83, Double.NEGATIVE_INFINITY
,44,49,25,65,55
	};
	
	private SimpleDataSerializer serde=new SimpleDataSerializer();
	
	
	public CompareDataUtilsTest(){
		
	}
	
	
	
	@Test
	public void testCompareLargeDataValue()
	{

		this.performCorrelateTest(TestUtils.generateData(100, 1, 100),TestUtils.generateData(100, 1, 100), 10);
		this.performCorrelateTest( TestUtils.generateData(1000, 1, 100),TestUtils.generateData(1000, 1, 100), 100);
		this.performCorrelateTest(TestUtils.generateData(10000, 1, 100),TestUtils.generateData(10000, 1, 100), 100);
		this.performCorrelateTest( TestUtils.generateSquareData(30000, 1, 100),TestUtils.generateData(3000, 1, 100), 100);
		this.performCorrelateTest( TestUtils.generateData(30000, 1, 100),TestUtils.generateData(30000, 1, 100), 100);
		//this.performValuePartitionTest(du, TestUtils.generateData(100000, 1, 1000), 10);
		this.performCorrelateTest(TestUtils.generateData(400000, 1, 10000),TestUtils.generateData(400000, 1, 10000), 1000);
		//this.performPartitionTest(du, TestUtils.generateData(15000000, 1, 10000), 10);
	}
	
	@Test
	public void testCompareLargeDataValueIdentity()
	{

		double[] data=TestUtils.generateData(30000, 1, 100);
		System.out.println("Calculating correlation on two identical sets");
		this.performCorrelateTest( data, data, 11);
	}
	
	@Test
	public void testCompareCorrelationPartitions()
	{

		System.out.println("Checking correlation for different partition number");
		int[] numP={2,5,10,20,50,100,500,1000,3000};
		double[] d1=readBinaryArray("resources/data/barriers.binary.txt");
		double[] d2=readBinaryArray("resources/data/crime.binary.txt");
		for(int p: numP){
			System.out.println(p);
			this.performCorrelateTest( d1, d2, p);
			//System.out.println(p+"\t"+CompareDataUtils.correlation(p, d1, d2));
		}
		for(int i=2;i<200;i+=5)
		{
			System.out.println(i+"\t"+d1.length/i+"\t"+CompareDataUtils.correlation(i, d1, d2));
		}
	}
	
	@Test
	public void testCompareCorrelationOnRandomSets()
	{
		
		System.out.print("\n");
		System.out.print("Testing optimal sizes for correlation: \n\n");
		int[] sizes={10, 15, 30, 60, 100, 200, 500, 1000, 5000, 30000, 100000,400000};
		int[] itemsPerPartition={1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 50, 100, 1000};
		int[] numPartitions={2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 50, 100, 500, 1000};
		
			for(int j=0; j < sizes.length;j++){
				double[] d1=TestUtils.generateData(sizes[j], 1, 100);
				//double[] d2=TestUtils.generateData(sizes[j], 1, 100);
				double[] d2=TestUtils.noiseData(d1, 0.1);
				
				for(int i=0; i< numPartitions.length;i++)
				{
				String c="-";
				if(sizes[j]>=numPartitions[i]){
				
				//c= Integer.toString((int)Math.round(10000*CompareDataUtils.correlation(sizes[j]/itemsPerPartition[i], d1, d2)));
				c= Integer.toString((int)Math.round(10000*CompareDataUtils.correlation(numPartitions[i], d1, d2)));
//				c= Integer.toString((int)Math.round(10000*CompareDataUtils.pearsonCorrelation(d1, d2, numPartitions[i])));
				}
				System.out.print(c+",");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	@Test
	public void testCompareDatasets()
	{

		System.out.println("Comparing datasets");
		double[] d1=TestUtils.generateData(30000, 1, 100);
		double[] d2=TestUtils.generateData(30000, 1, 100);
		this.performCompareTest( d1, d2, 5);
		this.performCompareTest( d1, TestUtils.noiseData(d1,0.1), 5);
		this.performCompareTest( d1, d1, 5);
	}
	
	@Test
	public void testNoise()
	{

		System.out.println("Comparing datasets");
		double[] d1=TestUtils.generateData(30000, 1, 100);
		for(double delta=0.0;delta<=1;delta+=0.05)
		{
		this.performCorrelateTest(d1, TestUtils.noiseData(d1,delta), 20);
		}
		this.performCorrelateTest( d1, TestUtils.generateData(30000, 1, 100),20);
	}
	
	@Test
	public void testReadDatasets()
	{

		System.out.println("Comparing crime and housing barriers ");
		long t1=System.nanoTime();
		double[] d1=TestUtils.readFile("resources/data/barriers.txt");
		double[] d2=TestUtils.readFile("resources/data/crime.txt");
		double[] d3=TestUtils.readFile("resources/data/imd2010.txt");
		double[] d4=TestUtils.readFile("resources/data/imd2007.txt");
		/*double[] d1=readBinaryArray("resources/data/barriers.binary.txt");
		double[] d2=readBinaryArray("resources/data/crime.binary.txt");*/
		long t2=System.nanoTime();
		/*saveAsBinary("resources/data/barriers.binary.txt", d1);
		saveAsBinary("resources/data/crime.binary.txt", d2);*/
		System.out.println("Datasets loaded from file in "+(t2-t1)/1000000+"ms");
		this.performCompareTest( d1, d2, 10);
		this.performCompareTest( d3, d4, 10);
	}
	
	@Test
	public void testReadDatasetsIterations()
	{

		System.out.println("Comparing imd2010 and imd2007 ");
		long t1=System.nanoTime();
		double[] d1=TestUtils.readFile("resources/data/barriers.txt");
		double[] d2=TestUtils.readFile("resources/data/crime.txt");
		double[] d3=TestUtils.readFile("resources/data/imd2010.txt");
		double[] d4=TestUtils.readFile("resources/data/imd2007.txt");
		/*double[] d1=readBinaryArray("resources/data/barriers.binary.txt");
		double[] d2=readBinaryArray("resources/data/crime.binary.txt");*/
		long t2=System.nanoTime();
		/*saveAsBinary("resources/data/barriers.binary.txt", d1);
		saveAsBinary("resources/data/crime.binary.txt", d2);*/
		System.out.println("");
		System.out.println("#\tmy\tpearson\tmy\tpearson");
		for(int i=2;i<30;i++){
			//this.performCorrelateTest(d3, d4, i);
			String c1=Integer.toString((int)Math.round(10000*CompareDataUtils.correlation(i, d1, d2)));
			String c2=Integer.toString((int)Math.round(10000*CompareDataUtils.pearsonCorrelation(d1, d2,i)));
			String c3=Integer.toString((int)Math.round(10000*CompareDataUtils.correlation(i, d3, d4)));
			String c4=Integer.toString((int)Math.round(10000*CompareDataUtils.pearsonCorrelation(d3, d4,i)));
			System.out.println(i+"\t"+c1+"\t"+c2+"\t"+c3+"\t"+c4);
		}
	}
	
	
	
	@Test
	public void testCorrelation()
	{

		System.out.println("Testing the correlation between crime and housing barriers ");
		long t1=System.nanoTime();
		/*double[] d1=this.readFile("resources/data/barriers.txt");
		double[] d2=this.readFile("resources/data/crime.txt");*/
		double[] d1=readBinaryArray("resources/data/barriers.binary.txt");
		double[] d2=readBinaryArray("resources/data/crime.binary.txt");
		long t2=System.nanoTime();
		/*saveAsBinary("resources/data/barriers.binary.txt", d1);
		saveAsBinary("resources/data/crime.binary.txt", d2);*/
		System.out.println("Datasets loaded from file in "+(t2-t1)/1000000+"ms");
		this.performCorrelateTest( d1, d2, 5);
	}
	
	
	public void performCompareTest(double[] data1, double[] data2, int numP)
	{
		long t1=System.nanoTime();
		int[][] d=CompareDataUtils.partitionDataByValues(numP, data1, data2);
		double[][] p=CompareDataUtils.distribution(numP, d);
		long t2=System.nanoTime();
		for(int i=0;i<numP;i++){
			for(int j=0;j<numP;j++)
			{
				System.out.print(Math.round(p[i][j]*10000)/10000.0+"\t");
			}
		System.out.println();
		}
		System.out.println(d.length+" sized datasets compared in "+(t2-t1)/1000+"mks");
		this.performCorrelateTest(data1, data2, numP);
	}
	
	public void performCorrelateTest(double[] data1, double[] data2, int numP)
	{
		long t1=System.nanoTime();
		double c=CompareDataUtils.correlation(numP, data1, data2);
		long t2=System.nanoTime();
		System.out.println("Correlation of "+c+" is calculated in "+(t2-t1)/1000+"mks");
	}
	
	
	public void performPearsonCorrelationTest( double[] data1, double[] data2, int numP)
	{
		long t1=System.nanoTime();
		double correlation=0;
		for(int i=0;i<10;i++){
			correlation=CompareDataUtils.pearsonCorrelation(data1, data2, numP);
		}
		long t2=System.nanoTime();
		System.out.println("d1: "+Arrays.toString(PartitionDataUtils.groupCountNormalized(PartitionDataUtils.partitionDataByValues(data1,4))));
		System.out.println("d2: "+Arrays.toString(PartitionDataUtils.groupCountNormalized(PartitionDataUtils.partitionDataByValues(data2,4))));
		System.out.println("Took "+(t2-t1)/10000+" mks to realize correlation is "+Math.round(correlation*1000)/1000.0 +" for "+data1.length+" elements");
	}
	
	
	
	private double[] readBinaryArray(String path)
	{
		try {
			return (serde.deserialize(FileUtils.readFileToByteArray(new File(path))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void saveAsBinary(String path, double[] data)
	{
		try {
			FileUtils.writeByteArrayToFile(new File(path), serde.serialize((data)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 
	
	
	
}
