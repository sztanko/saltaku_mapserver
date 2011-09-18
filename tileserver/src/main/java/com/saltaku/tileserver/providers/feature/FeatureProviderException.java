package com.saltaku.tileserver.providers.feature;

import java.io.IOException;

public class FeatureProviderException extends Exception {

	public FeatureProviderException(IOException e) {
		super(e);
	}

}
