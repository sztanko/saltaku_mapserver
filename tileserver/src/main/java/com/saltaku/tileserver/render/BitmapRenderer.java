package com.saltaku.tileserver.render;

import java.io.OutputStream;

public interface BitmapRenderer {
   public void writeBitmap(int[] bitmap, OutputStream oStream);
}
