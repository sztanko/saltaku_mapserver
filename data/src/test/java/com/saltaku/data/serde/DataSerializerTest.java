package com.saltaku.data.serde;

import org.junit.Test;

import com.saltaku.data.serde.impl.SimpleDataSerializer;

public class DataSerializerTest {

	
	protected double[] generateData(int num, double min, double max){
		double[] out=new double[num];
		double r=max-min;
		for(int i=0;i<num;i++){ 
			out[i]=Math.random()*r+min;
		}
		return out;
	}
	
	protected void performTest(int size, DataSerializer ... serializers)
	{
		System.out.println("Testing serialization of "+size+" elements");
		double[] data=this.generateData(size, 1, 1000);
		for(DataSerializer serde: serializers)
		{
			byte[] bb=null;
			try{
			long t1=System.nanoTime();
			for(int i=0;i<100;i++)
			{
				bb=serde.serialize(data);
			}
			long t2=System.nanoTime();
			long serTime=(t2-t1)/100;
			int serSize=bb.length;
			t1=System.nanoTime();
			for(int i=0;i<100;i++)
			{
				bb=serde.serialize(data);
			}
			t2=System.nanoTime();
			long deserTime=(t2-t1)/100;
			System.out.println(serde.getClass().getSimpleName()+"\t"+serSize+"\t"+serTime+"\t"+deserTime);
			}catch(Exception e){
				long serTime=-1;
				long deserTime=-1;
				int serSize=-1;
				System.out.println(serde.getClass().getSimpleName()+"\t"+serSize+"\t"+serTime+"\t"+deserTime);
			}
			
		}
	}
	
	@Test
	public void compareDataSerializers()
	{
		int[] sizes={10, 100, 1000, 30000, 400000};
		DataSerializer[] sers={ new SimpleDataSerializer(),new SimpleDataSerializer()};
		for(int size: sizes)
		{
			this.performTest(size, sers);
		}
	}
	
}
