package com.saltaku.tileserver.providers.palette.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.inject.Inject;
import com.saltaku.tileserver.providers.palette.PaletteProvider;
import com.saltaku.tileserver.providers.translator.TranslatorUtils;

public class SimplePaletteProvider implements PaletteProvider {
private int[] defaultMapping;
	
	@Inject
	public SimplePaletteProvider()
	{
		try {
			defaultMapping=TranslatorUtils.translateRgb(new FileInputStream("resources/colours/standard.txt"));
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
	
	public int[] getPalette(String paletteName) {
		return defaultMapping;
	}

}
