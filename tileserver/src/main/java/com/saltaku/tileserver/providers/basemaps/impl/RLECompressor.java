package com.saltaku.tileserver.providers.basemaps.impl;

import java.util.Arrays;

import org.geotools.feature.FeatureCollection;
import org.opengis.geometry.Envelope;

import com.saltaku.tileserver.providers.basemaps.BasemapCompressor;
import com.saltaku.tileserver.providers.basemaps.BasemapRenderer;

public class RLECompressor implements BasemapCompressor{

	public int[] compress(int[] in) {  //speed-optimized compression
		int[] out=new int[in.length*2];
		int i=0;
		int size=0;
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
		
		return Arrays.copyOfRange(out, 0, size);
	}

	public int[] decompress(int[] in) {
		int[] out=new int[65536];
		int i=0;
		int index=0;
		while(i<in.length)
		{
			//two bytes of repetition
			int count=in[i++];
			
			//System.out.println("Count: "+count);
			//4 bytes of value
			int value=in[i++];
			for(int j=0;j<count;j++) out[index++]=value;
			//System.out.println("Value: "+value);
		}
		return out;
	}

}
