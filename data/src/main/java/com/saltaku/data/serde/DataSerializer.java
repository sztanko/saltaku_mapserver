package com.saltaku.data.serde;

public interface DataSerializer {
public byte[] serialize(double[] in);
public double[] deserialize(byte[] in);

public byte[] serialize(int[] in);
public int[] deserializeAsIntArray(byte[] in);
}
