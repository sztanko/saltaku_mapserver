package com.saltaku.data.serde;

public interface DataSerializer {
public byte[] serialize(double[] in);
public double[] deserialize(byte[] in);
}
