package com.saltaku.data;

import java.util.Arrays;

public class AggregateDataUtils {

	public static final Aggregator AVG=new AvgAggregator();
	public static final Aggregator GEOMAVG=new GeomAvgAggregator();
	public static final Aggregator MEDIAN=new MedianAggregator();
	public static final Aggregator COUNT=new CountAggregator();
	public static final Aggregator MAD=new MadGeomAggregator();
	public static final Aggregator SUM=new SumAggregator();
	
	
	public static double[][] remap(double[] in, int[] map)
	{
		int maxValue=AggregateDataUtils.maxValue(map);
		double out[][]=new double[maxValue+1][];
		int[] sizes=new int[maxValue+1];
		
		for(int i=0;i<in.length;i++)
		{
			sizes[map[i]]++;
		}
		for(int i=0;i<=maxValue;i++)
		{
			out[i]=new double[sizes[i]];
		}
		sizes=new int[maxValue+1];
		for(int i=0;i<in.length;i++)
		{
			out[map[i]][sizes[map[i]]]=in[i];
			sizes[map[i]]++;
		}
		return out;
	}
	
	public static int maxValue(int[] map)
	{
		int max=0;
		for(int v:map) if(max<v) max=v;
		return max;
	}
	
	public static double[] aggregate(double[][] in, Aggregator agg)
	{
		double[] out=new double[in.length];
		for(int i=0;i<in.length;i++) out[i]=agg.aggregate(in[i]);
		return out;
	}
	

	public static class CountAggregator implements Aggregator{
		public double aggregate(double[] in) {
			return in.length;
		}
	}
	
	public static class MedianAggregator implements Aggregator{
		public double aggregate(double[] in) {
			double[] m=Arrays.copyOf(in, in.length);
			Arrays.sort(m);
			if(m.length>1)
			return m[m.length/2];
			else
				return m.length>0?m[m.length-1]:0;
		}
	}
	
	public static class GeomAvgAggregator implements Aggregator{
		public double aggregate(double[] in) {
			double sum=0;
			for(double d: in) sum+=Math.log(d);
			return Math.exp(sum/in.length);
		}
	}
	
	public static class MadGeomAggregator implements Aggregator{
		static private GeomAvgAggregator gagg=new GeomAvgAggregator();
		static private MedianAggregator magg=new MedianAggregator();
	public double aggregate(double[] in) {
		double gavg=gagg.aggregate(in);
		double median=magg.aggregate(in);
		double[] dev=new double[in.length];
		for(int i=0;i<in.length;i++)
		{
			dev[i]=Math.abs(in[i]-median);
		}
		double mad=magg.aggregate(dev);
		
		int count=0;
		for(int i=0;i<in.length;i++)
		{
			if(Math.abs(in[i]-gavg)<=mad)
			{
				count++;
			}
		}
		double[] out=new double[count];
		count=0;
		for(int i=0;i<in.length;i++)
		{
			if(Math.abs(in[i]-gavg)<=mad)
			{
				out[count++]=in[i];
			}
		}
		return gagg.aggregate(out);
	}
	
}
	
	public static class SumAggregator implements Aggregator{

		public double aggregate(double[] in) {
			double sum=0;
			for(double d: in) sum+=d;
			return sum;
		}
	}
	
	public static class AvgAggregator implements Aggregator{
		public double aggregate(double[] in) {
			double sum=0;
			for(double d: in) sum+=d;
			return sum/in.length;
		}
	}
	
	
	
	
}
