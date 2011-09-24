package com.saltaku.tileserver.render.impl;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.google.inject.Inject;
import com.saltaku.tileserver.render.BitmapRenderer;
import com.saltaku.tileserver.render.BitmapRendererException;

public class FastBitmapRenderer implements BitmapRenderer {

	@Inject
	public FastBitmapRenderer()
	{
	super();	
	}
	
	
	public void writeBitmap(int width, int height, int[] bitmap, OutputStream oStream) throws BitmapRendererException {
		BufferedImage bi=new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g=(Graphics2D)bi.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		bi.setRGB(0, 0, width, height, bitmap, 0, width);
		
		try {
			com.sun.imageio.plugins.png.PNGImageWriter writer=new com.sun.imageio.plugins.png.PNGImageWriter(null);
			writer.setOutput(oStream);
			writer.write(bi);
			oStream.flush();
			writer.dispose();
			//ImageIO.write(bi,"png",oStream);
		} catch (IOException e) {
			throw new BitmapRendererException(e);
		}
				//bi, "png", );
	}

}
