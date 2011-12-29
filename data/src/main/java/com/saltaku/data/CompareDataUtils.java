package com.saltaku.data;

public class CompareDataUtils {

	public static int[][] partitionDataByValues(int numPartitions, double[] ...data )
	{
		int numItems=data[0].length;
		int[][] out=new int[numItems][data.length];
		//for(int i=0;i<numItems;i++) out[i]=new int[data.length];
		for(int i=0;i<data.length;i++)
		{
			int[] partitions=PartitionDataUtils.partitionDataByValues(data[i], numPartitions);
			for(int j=0;j<partitions.length;j++)
			{
				out[j][i]=partitions[j];
			}
		}
		return out;
	}
	
	public static int[][] partitionDataByEqualSize(int numPartitions, double[] ...data )
	{
		int numItems=data[0].length;
		int[][] out=new int[numItems][data.length];
		//for(int i=0;i<numItems;i++) out[i]=new int[data.length];
		for(int i=0;i<data.length;i++)
		{
			int[] partitions=PartitionDataUtils.partitionDataByEqualSize(data[i], numPartitions);
			for(int j=0;j<partitions.length;j++)
			{
				out[j][i]=partitions[j];
			}
		}
		return out;
	}
	
	public static double[][] distribution(int numPartitions, int[][] data)
	{
		double[][] out=new double[numPartitions][numPartitions];
		for(int [] r: data)
		{
			if(r[0]>0 && r[1]>0){
			out[r[0]-1][r[1]-1]++;
			}
		}
		for(int i=0;i<numPartitions;i++)
		{
			for(int j=0;j<numPartitions;j++)
					out[i][j]=out[i][j]/(double)data.length;
		}
		return out;
	}
	
	public static double correlation(int numPartitions, double[] data1,double[] data2)
	{
		double[][] d=CompareDataUtils.distribution(numPartitions,CompareDataUtils.partitionDataByValues(numPartitions, data1, data2));
		double []s1=new double[numPartitions];
		double []s2=new double[numPartitions];
		for(int i=0;i<numPartitions;i++)
		{
			for(int j=0;j<numPartitions;j++){
					s1[i]+=d[i][j];
					s2[j]+=d[i][j];
			}
		}
		double delta,correlation=0;
		/*double sum=0;
		for(int i=0;i<numPartitions;i++) sum+=s1[i]+s2[i];
		System.out.println("SUM IS "+sum);*/
		for(int i=0;i<numPartitions;i++)
		{
			for(int j=0;j<numPartitions;j++){
				delta=(s1[i]*s2[j])-d[i][j];
				correlation+=delta*delta;
			}
		}
			
		return Math.sqrt(correlation);
	
	}
	
	public static double pearsonCorrelation(double[] d1, double[] d2, int numPartitions)
	{
		double[] scores1=PartitionDataUtils.groupCountNormalized(PartitionDataUtils.partitionDataByValues(d1,numPartitions));
		double[] scores2=PartitionDataUtils.groupCountNormalized(PartitionDataUtils.partitionDataByValues(d2,numPartitions));
		double result = 0;
	        double sum_sq_x = 0;
	        double sum_sq_y = 0;
	        double sum_coproduct = 0;
	        double mean_x = scores1[0];
	        double mean_y = scores2[0];
	        for(int i=2;i<scores1.length+1;i+=1){
	            double sweep =Double.valueOf(i-1)/i;
	            double delta_x = scores1[i-1]-mean_x;
	            double delta_y = scores2[i-1]-mean_y;
	            sum_sq_x += delta_x * delta_x * sweep;
	            sum_sq_y += delta_y * delta_y * sweep;
	            sum_coproduct += delta_x * delta_y * sweep;
	            mean_x += delta_x / i;
	            mean_y += delta_y / i;
	        }
	        double pop_sd_x = (double) Math.sqrt(sum_sq_x/scores1.length);
	        double pop_sd_y = (double) Math.sqrt(sum_sq_y/scores1.length);
	        double cov_x_y = sum_coproduct / scores1.length;
	        result = cov_x_y / (pop_sd_x*pop_sd_y);
	        return result;
	}
	
}
