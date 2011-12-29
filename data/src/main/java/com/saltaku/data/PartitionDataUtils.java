package com.saltaku.data;

import java.util.Arrays;

public class PartitionDataUtils {

	/*
	 * returns an index of partitions for each of the element
	 * We also assume that cells that have no values have the value of Double.NEGATIVE_INFINITY
	 */
	public static int[] partitionDataByEqualSize(double[] in, int numPartitions)
	{
		double[] partitionLimits=PartitionDataUtils.getPartitionLimits(in, numPartitions);
		int[] out=new int[in.length];
		for(int i=0;i<in.length;i++)
		{
			if(in[i]!=Double.NEGATIVE_INFINITY){
				out[i]=PartitionDataUtils.findPartition(in[i], partitionLimits);
			}
		}
		//System.out.println(Arrays.toString(partitionLimits));
		return out;
	}
	
	public static double[] getPartitionLimits(double in[], int numPartitions)
	{
		double[] partitionLimits=new double[numPartitions];
		double[] data=Arrays.copyOf(in, in.length);
		Arrays.sort(data);
		int firstNonZero=0;
		while(firstNonZero<in.length && data[firstNonZero]==Double.NEGATIVE_INFINITY) firstNonZero++;
		int actualLength=in.length-firstNonZero;
		for(int i=0;i<numPartitions;i++)
		{
			int lowerLimit=i*actualLength/numPartitions+firstNonZero;
			partitionLimits[i]=data[lowerLimit];
		}
		return partitionLimits;
	}
	
	private  static int findPartition(double val, double[] partitionLimits)
	{
		int i=0;
		while(i<partitionLimits.length && val>=partitionLimits[i]) 
			{
			//System.out.println(val+" is smaller or equal then "+partitionLimits[i]);
			i++;
			
			}
		return i;
	}
	
	public static  double[] normalize(double[] in)
	{
		double min=Double.POSITIVE_INFINITY, max=Double.NEGATIVE_INFINITY;
		for(int i=0;i<in.length;i++)
		{
			if(in[i]!=Double.NEGATIVE_INFINITY)
			{
				if(in[i]<min) min=in[i];
				if(in[i]>max) max=in[i];
			}
		}
		double range=(max-min)*1.0000001;
		double[] out=new double[in.length];
		for(int i=0;i<in.length;i++)
		{
			if(in[i]==Double.NEGATIVE_INFINITY) out[i]=Double.NEGATIVE_INFINITY;
			else
			{
				out[i]=(in[i]-min)/range;
			}
		}
		return out;
	}
	
	public static  int[] partitionDataByValues(double in[], int numPartitions)
	{
		double data[]=PartitionDataUtils.normalize(in);
		int[] out=new int[data.length];
		for(int i=0;i<data.length;i++)
		{
			if(in[i]==Double.NEGATIVE_INFINITY) out[i]=0;
			else out[i]=(int)(data[i]*numPartitions+1);
		}
		return out;
	}
	
	public static  int[] groupCount(int[] in)
	{
		int min=Integer.MAX_VALUE;
		int max=Integer.MIN_VALUE;
		for(int i=0;i<in.length;i++)
		{
			if(min>in[i]) min=in[i];
			if(max<in[i]) max=in[i];
		}
		int[] gc=new int[max-min+1];
		for(int i=0;i<in.length;i++)
		{
			gc[in[i]-min]++;
		}
		return gc;
	}
	
	public static double[] groupCountNormalized(int[] in)
	{
		int[] out_int=PartitionDataUtils.groupCount(in);
		double[] out=new double[out_int.length];
		for(int i=0;i<out.length;i++)
		{
			out[i]=(double)out_int[i]/in.length;
		}
		return out;
	}
	
}
