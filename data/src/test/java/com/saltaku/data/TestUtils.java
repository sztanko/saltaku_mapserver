package com.saltaku.data;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class TestUtils {

	
	public static double[] generateData(int num, double min, double max){
		double[] out=new double[num];
		double r=max-min;
		for(int i=0;i<num;i++){ 
			out[i]=Math.random()*r+min;
		}
		return out;
	}
	
	public static double[] noiseData(double [] in, double delta){
		double[] out=new double[in.length];
		for(int i=0;i<out.length;i++) out[i]=in[i]*(1+(2*delta*Math.random()-delta));
		return out;
	}
	
	public static double[] generateSquareData(int num, double min, double max){
		double[] out=new double[num];
		double r=max-min;
		for(int i=0;i<num;i++) { 
			out[i]=Math.random(); out[i]*=out[i]*out[i]; out[i]=out[i]*r+min;
		}
		return out;
	}
	
	public static double[] readFile(String path)
	{
		double out[]=null;
		try {
			List<String> str=FileUtils.readLines(new File(path));
			out=new double[str.size()];
			int i=0;
			for(String s:str)
			{
				out[i++]=Double.parseDouble(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out;
	}

}
