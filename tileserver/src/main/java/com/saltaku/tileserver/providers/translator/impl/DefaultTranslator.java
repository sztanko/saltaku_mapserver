package com.saltaku.tileserver.providers.translator.impl;

import com.saltaku.tileserver.providers.basemaps.CompressionUtil;
import com.saltaku.tileserver.providers.translator.TranslatorProvider;

public class DefaultTranslator implements TranslatorProvider{

	public int[] translateBaseMap(int[] baseMap, int[] mapping) {
		int[] out=new int[baseMap.length];
		for(int i=0;i<out.length;i++)
		{
			int b=baseMap[i];
			int c=CompressionUtil.color2int(b);
			if(c<0)
				out[i]=0;
			else
				out[i]=mapping[c];
		}
		return out;
	}

}
