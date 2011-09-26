package com.saltaku.tileserver.providers.basemaps;

import java.awt.Color;

public class CompressionUtil {

	 public static byte[] int2byte(int[]src) {
		    int srcLength = src.length;
		    byte[]dst = new byte[srcLength << 2];
		    
		    for (int i=0; i<srcLength; i++) {
		        int x = src[i];
		        int j = i << 2;
		        dst[j++] = (byte) ((x >>> 0) & 0xff);           
		        dst[j++] = (byte) ((x >>> 8) & 0xff);
		        dst[j++] = (byte) ((x >>> 16) & 0xff);
		        dst[j++] = (byte) ((x >>> 24) & 0xff);
		    }
		    return dst;
		}
	 
	 public static byte[] fourbitint2byte(int[]src) {
		    int srcLength = src.length;
		    byte[]dst = new byte[srcLength >> 2];
		    int i=0;
		    int j=0;
		    while(i<srcLength) {
		        byte x = (byte)(src[i++] & 0x0f);
		        x+=(byte)((src[i++] & 0x0f) << 4);
		        dst[j++]=x;
		    }
		    return dst;
		}
	
	 
	 
	   public static int[] byte2int(byte[]src) {
	        int dstLength = src.length >> 2;
	        int[]dst = new int[dstLength];
	        
	        for (int i=0; i<dstLength; i++) {
	            int j = i << 2;
	            int x = 0;
	            x += (src[j++] & 0xff) << 0;
	            x += (src[j++] & 0xff) << 8;
	            x += (src[j++] & 0xff) << 16;
	            x += (src[j++] & 0xff) << 24;
	            dst[i] = x;
	        }
	        return dst;
	    }
	  
	  public static int byte2int(byte b1, byte b2,byte b3, byte b4)
	  {
		  int x = 0;
          x += (b1 & 0xff) << 0;
          x += (b2 & 0xff) << 8;
          x += (b3 & 0xff) << 16;
          x += (b4 & 0xff) << 24;
          return x;
	  }
	  
	  
	  public static byte[] int2byte(int x)
	  {
		  byte[] out=new byte[4];
		  out[0] = (byte) ((x >>> 0) & 0xff);           
		  out[1] = (byte) ((x >>> 8) & 0xff);
		  out[2] = (byte) ((x >>> 16) & 0xff);
		  out[3]= (byte) ((x >>> 24) & 0xff);
		  return out;
	  }
	  
	  public static byte[] int2rgb(int x)
	  {
		  byte[] out=new byte[3];
		  //out[0] = (byte) ((x >>> 0) & 0xff);           
		  out[0] = (byte) ((x >>> 8) & 0xff);
		  out[0] = (byte) ((x >>> 16) & 0xff);
		  out[0]= (byte) ((x >>> 24) & 0xff);
		  return out;
	  }

	  public static Color int2color(int x)
	  {
//		  int[] out=new int[4];
		  Color out=new Color(((x >>> 16) & 0xff),((x >>> 8) & 0xff),((x >>> 0) & 0xff),(~(x >>> 24) & 0xff));
		/*  out[3] =  ((x >>> 0) & 0xff); //b           
		  out[2] =  ((x >>> 8) & 0xff);  //g
		  out[1] =  ((x >>> 16) & 0xff);  //r
		  out[0]=  ((x >>> 24) & 0xff);   //alpha */
		  return out;
	  }
	  public static int color2int(Color x)
	  {
		  int out=0;
		  out+=(x.getBlue()  & 0xff) << 0;
		  out+=(x.getGreen()  & 0xff) << 8;
		  out+=(x.getRed()  & 0xff) << 16;
		  out+= ~(x.getAlpha() & 0xff) <<24;
		  return out;
	  }
	
	  /*
	   * x y r
	   * 1 0 1
	   * 1 1 0
	   * 0 0 0
	   * 0 1 1
	   */
	  
	  public static int color2int(int x)
	  {
		  
		  return x==0?-1:(x ^ 0xFF000000);
	  }
}
