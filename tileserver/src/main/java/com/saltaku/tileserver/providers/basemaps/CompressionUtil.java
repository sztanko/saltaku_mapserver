package com.saltaku.tileserver.providers.basemaps;

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
	  
	
}
