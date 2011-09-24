package com.saltaku.tileserver.providers.translator;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;



public class TranslatorUtilsTest {

	@Test
	public void testTranslateColors() throws FileNotFoundException, IOException
	{
		for(int i :TranslatorUtils.translateRgb(new FileInputStream("resources/colours/standard.txt")))
				{
			System.out.println(new Color(i).toString());
				}
	}
}
