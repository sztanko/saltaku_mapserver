package com.saltaku.tileserver.providers.translator.impl;

import com.saltaku.tileserver.providers.translator.TranslatorProvider;

public class DefaultTranslator implements TranslatorProvider{

	public int[] translateBaseMap(int[] baseMap, int[] mapping) {
		int[] out=new int[baseMap.length];
		for(int i=0;i<out.length;i++)
		{
			out[i]=mapping[baseMap[i]];
		}
		return out;
	}

}
