package com.saltaku.tileserver.providers.translator;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class TranslatorUtils {

	public static int[] translateRgb(InputStream str) throws IOException
	{
		List<Color> cList=new ArrayList<Color>();
		BufferedReader r=new BufferedReader(new InputStreamReader(str));
		String s;
		while((s=r.readLine())!=null)
		{
			String[] ss=s.split("\\s+");
			cList.add(new Color(Integer.parseInt(ss[0]),Integer.parseInt(ss[1]),Integer.parseInt(ss[2])));
		}
		r.close();
		int[] out=new int[cList.size()];
		for(int i=0;i<cList.size();i++) out[i]=cList.get(i).getRGB();
		return out;
	}
	
}
