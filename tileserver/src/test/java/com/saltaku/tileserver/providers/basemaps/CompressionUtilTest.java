package com.saltaku.tileserver.providers.basemaps;

import java.awt.Color;

import junit.framework.Assert;

import org.junit.Test;


public class CompressionUtilTest {

	
	@Test
	public void int2rgbTest()
	{
		this.printValue(12345);
		this.printValue(05335456);
		this.printValue(Color.GREEN.getRGB());
	}
	
	@Test
	public void testExtreme()
	{
		System.out.println(CompressionUtil.color2int(-16777216));
	}
	
	@Test
	public void color2intTest()
	{
		Color col=Color.blue;
		System.out.println(Integer.toBinaryString(col.getRGB()));
		System.out.println(Integer.toBinaryString(0xFF000000));
		System.out.println(Integer.toBinaryString(col.getRGB() ^ 0xFF000000));
		Assert.assertEquals(255,CompressionUtil.color2int(col.getRGB()));
		for(int i=0;i< 16777216 ;i+=10000)
		{
			Assert.assertEquals(i,CompressionUtil.color2int(new Color(i).getRGB()));
		}
	}
	
	private void printValue(int value)
	{
		Color c=CompressionUtil.int2color(value);
		System.out.println("Value: "+value+" - "+Integer.toBinaryString(value));
		System.out.println("A: "+c.getAlpha()+" - "+Integer.toBinaryString(c.getAlpha()));
		System.out.println("R: "+c.getRed()+" - "+Integer.toBinaryString(c.getRed()));
		System.out.println("G: "+c.getGreen()+" - "+Integer.toBinaryString(c.getGreen()));
		System.out.println("B: "+c.getBlue()+" - "+Integer.toBinaryString(c.getBlue()));
		
		Assert.assertEquals("Color conversion", value, CompressionUtil.color2int(c));
	}
}
