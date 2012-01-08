package com.saltaku.data.serde.impl;

import com.google.inject.Inject;
import com.saltaku.data.serde.DataSerializer;

public class SimpleDataSerializer implements DataSerializer {
	
	@Inject
	public SimpleDataSerializer()
	{
		
	}
	
	public byte[] serialize(double[] in) {
		return toByta(in);
	}

	public double[] deserialize(byte[] in) {
		return toDoubleA(in);
	}

	public byte[] serialize(int[] in) {
		return toByta(in);
	}

	public int[] deserializeAsIntArray(byte[] in) {
		return toIntA(in);
	}

	public static byte[] toByta(long data) {
	    return new byte[] {
	        (byte)((data >> 56) & 0xff),
	        (byte)((data >> 48) & 0xff),
	        (byte)((data >> 40) & 0xff),
	        (byte)((data >> 32) & 0xff),
	        (byte)((data >> 24) & 0xff),
	        (byte)((data >> 16) & 0xff),
	        (byte)((data >> 8) & 0xff),
	        (byte)((data >> 0) & 0xff),
	    };
	}
	
	public  byte[] toByta(double data) {
	    return toByta(Double.doubleToRawLongBits(data));
	}
	 
	public  byte[] toByta(double[] data) {
	    if (data == null) return null;
	    // ----------
	    byte[] byts = new byte[data.length * 8];
	    for (int i = 0; i < data.length; i++)
	        System.arraycopy(toByta(data[i]), 0, byts, i * 8, 8);
	    return byts;
	}
	
	public  double[] toDoubleA(byte[] data) {
	    if (data == null) return null;
	    // ----------
	    if (data.length % 8 != 0) return null;
	    double[] dbls = new double[data.length / 8];
	    for (int i = 0; i < dbls.length; i++) {
	        dbls[i] = toDouble( new byte[] {
	            data[(i*8)],
	            data[(i*8)+1],
	            data[(i*8)+2],
	            data[(i*8)+3],
	            data[(i*8)+4],
	            data[(i*8)+5],
	            data[(i*8)+6],
	            data[(i*8)+7],
	        } );
	    }
	    return dbls;
	}
	public  double toDouble(byte[] data) {
	    if (data == null || data.length != 8) return 0x0;
	    // ---------- simple:
	    return Double.longBitsToDouble(toLong(data));
	}
	public  long toLong(byte[] data) {
	    if (data == null || data.length != 8) return 0x0;
	    // ----------
	    return (long)(
	            // (Below) convert to longs before shift because digits
	            //         are lost with ints beyond the 32-bit limit
	            (long)(0xff & data[0]) << 56  |
	            (long)(0xff & data[1]) << 48  |
	            (long)(0xff & data[2]) << 40  |
	            (long)(0xff & data[3]) << 32  |
	            (long)(0xff & data[4]) << 24  |
	            (long)(0xff & data[5]) << 16  |
	            (long)(0xff & data[6]) << 8   |
	            (long)(0xff & data[7]) << 0
	            );
	}

	
	 public  byte[] toByta(int data) {
	      return new byte[] {
	      (byte)((data >> 24) & 0xff),
	      (byte)((data >> 16) & 0xff),
	      (byte)((data >> 8) & 0xff),
	      (byte)((data >> 0) & 0xff),
	      };
	      }
	       
	      public  byte[] toByta(int[] data) {
	      if (data == null) return null;
	      // ----------
	      byte[] byts = new byte[data.length * 4];
	      for (int i = 0; i < data.length; i++)
	      System.arraycopy(toByta(data[i]), 0, byts, i * 4, 4);
	      return byts;
	      }
	      
	      public  int toInt(byte[] data) {
		      if (data == null || data.length != 4)
			      return 0x0;

		      return (0xff & data[0]) << 24 | (0xff & data[1]) << 16 | (0xff & data[2]) << 8 | (0xff & data[3]) << 0;
	      }
	       
	      public  int[] toIntA(byte[] data) {
	      if (data == null || data.length % 4 != 0) return null;
	      // ----------
	      int[] ints = new int[data.length / 4];
	      for (int i = 0; i < ints.length; i++)
	      ints[i] = toInt( new byte[] {
	      data[(i*4)],
	      data[(i*4)+1],
	      data[(i*4)+2],
	      data[(i*4)+3],
	      } );
	      return ints;
	      }
	
	
}
