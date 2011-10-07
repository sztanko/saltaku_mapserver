package com.saltaku.tileserver.render;

import java.io.OutputStream;

public interface BitmapRenderer {
   public void writeBitmap(int width, int height, int[] bitmap,int[] palette, OutputStream oStream) throws BitmapRendererException;
}
