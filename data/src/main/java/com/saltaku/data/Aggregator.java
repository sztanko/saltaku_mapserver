package com.saltaku.data;

public interface Aggregator {
	public double aggregate(double[] in);
	public String getName();
}
