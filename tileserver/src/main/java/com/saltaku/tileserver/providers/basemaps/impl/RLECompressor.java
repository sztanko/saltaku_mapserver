package com.saltaku.tileserver.providers.basemaps.impl;

import java.util.Arrays;

import org.geotools.feature.FeatureCollection;
import org.opengis.geometry.Envelope;

import com.saltaku.tileserver.providers.basemaps.BasemapCompressor;
import com.saltaku.tileserver.providers.basemaps.BasemapRenderer;
import com.saltaku.tileserver.providers.basemaps.CompressionUtil;

public class RLECompressor implements BasemapCompressor{

	public byte[] compress(int[] in) {  //speed-optimized compression
		int[] out=new int[1+(in.length<<2)];
		out[0]=in.length;
		//System.out.println("Length of input:" + out[0]);
		int i=0;
		int size=1;
		while(i<in.length){
			int value=in[i];
			int freq=0;
			while(i<in.length && in[i]==value) {
				freq++;
				i++;
			}
			//System.out.println(value+" is repeated "+freq+" times");
			out[size++]=freq;
			out[size++]=value;
			
		}
		
		return CompressionUtil.int2byte(Arrays.copyOfRange(out, 0, size));
	}

	public int[] decompress(byte[] in) {
		int len=CompressionUtil.byte2int(in[0], in[1], in[2], in[3]);
		int[] out=new int[len];
		int i=4;
		int index=0;
		while(i<in.length)
		{
			//two bytes of repetition
			int count=CompressionUtil.byte2int(in[i], in[i+1], in[i+2], in[i+3]);
			i+=4;
			
			//System.out.println("Count: "+count);
			int value=CompressionUtil.byte2int(in[i], in[i+1], in[i+2], in[i+3]);
			i+=4;
			for(int j=0;j<count;j++) out[index++]=value;
		//	System.out.println("Value: "+value);
		}
		return out;
	}

}
