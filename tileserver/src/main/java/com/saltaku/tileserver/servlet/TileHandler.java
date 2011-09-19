package com.saltaku.tileserver.servlet;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.saltaku.tileserver.providers.basemaps.BasemapProvider;
import com.saltaku.tileserver.providers.basemaps.BasemapProviderException;
import com.saltaku.tileserver.providers.feature.FeatureProviderException;
import com.saltaku.tileserver.providers.mappings.MappingProvider;
import com.saltaku.tileserver.providers.mappings.MappingProviderException;
import com.saltaku.tileserver.providers.translator.TranslatorProvider;
import com.saltaku.tileserver.render.BitmapRenderer;
import com.saltaku.tileserver.render.BitmapRendererException;

public class TileHandler extends AbstractHandler {
	private BasemapProvider basemapProvider;
	private MappingProvider mappingProvider;
	private BitmapRenderer bitmapRenderer;
	private TranslatorProvider translatorProvider;
	
	
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String shapeId=(String)request.getAttribute("id");
		int x=Integer.parseInt((String)request.getAttribute("x"));
		int y=Integer.parseInt((String)request.getAttribute("y"));
		int z=Integer.parseInt((String)request.getAttribute("z"));
				
		int[] baseMap;
		try {
			baseMap = basemapProvider.getBasemapForTile(shapeId, x, y, z);

		int[] mapping = mappingProvider.getMapping(shapeId,null);
		int[] bitmap = translatorProvider.translateBaseMap(baseMap, mapping);

		response.setContentType("img/png;");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
		bitmapRenderer.writeBitmap(256,256,bitmap, response.getOutputStream());
		} catch (MappingProviderException e) {
			throw new ServletException(e);
		} catch (BasemapProviderException e) {
			e.printStackTrace();
		} catch (BitmapRendererException e) {
			e.printStackTrace();
		}
		
	}

}
