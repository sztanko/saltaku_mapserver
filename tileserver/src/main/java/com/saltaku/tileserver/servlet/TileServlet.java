package com.saltaku.tileserver.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.saltaku.tileserver.providers.basemaps.BasemapCompressor;
import com.saltaku.tileserver.providers.basemaps.BasemapProvider;
import com.saltaku.tileserver.providers.basemaps.BasemapProviderException;
import com.saltaku.tileserver.providers.mappings.MappingProvider;
import com.saltaku.tileserver.providers.mappings.MappingProviderException;
import com.saltaku.tileserver.providers.palette.PaletteProvider;
import com.saltaku.tileserver.providers.translator.TranslatorProvider;
import com.saltaku.tileserver.render.BitmapRenderer;
import com.saltaku.tileserver.render.BitmapRendererException;

public class TileServlet extends HttpServlet {

	private BasemapProvider basemapProvider;
	private MappingProvider mappingProvider;
	private BitmapRenderer bitmapRenderer;
	private TranslatorProvider translatorProvider;
	private PaletteProvider paletteProvider;
	
	final int CACHE_DURATION_IN_SECOND = 60 * 60 * 24 * 90; // 90 days
	final long   CACHE_DURATION_IN_MS = CACHE_DURATION_IN_SECOND  * 1000;
	
	
	@Inject
	public TileServlet(BasemapProvider basemapProvider, MappingProvider mappingProvider, BitmapRenderer bitmapRenderer, 
			TranslatorProvider translatorProvider, PaletteProvider paletteProvider) {
		super();
		this.basemapProvider = basemapProvider;
		this.mappingProvider = mappingProvider;
		this.bitmapRenderer = bitmapRenderer;
		this.translatorProvider = translatorProvider;
		this.paletteProvider = paletteProvider;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		long t1=System.currentTimeMillis();
		String shapeId=(String)request.getParameter("id");
		String mappingName=(String)request.getParameter("map");
		int x=Integer.parseInt((String)request.getParameter("x"));
		int y=Integer.parseInt((String)request.getParameter("y"));
		int z=Integer.parseInt((String)request.getParameter("z"));
		//System.out.println(Thread.currentThread().getId()+" " +request.getQueryString());
		int[] baseMap;
		try {
			baseMap = basemapProvider.getBasemapForTile(shapeId, x, y, z);
			int[] mapping = mappingProvider.getMapping(mappingName);
			int[] bitmap = translatorProvider.translateBaseMap(baseMap, mapping);
			int[] palette = paletteProvider.getPalette("default");
			response.setContentType("image/png");
			response.addHeader("Cache-Control", "max-age=" + CACHE_DURATION_IN_SECOND);
			response.addHeader("Cache-Control", "must-revalidate");//optional
			response.setDateHeader("Last-Modified", t1);
			response.setDateHeader("Expires", t1+CACHE_DURATION_IN_MS);
			response.setStatus(HttpServletResponse.SC_OK);

			this.bitmapRenderer.writeBitmap(256, 256, bitmap, palette, response.getOutputStream());
		} catch (BasemapProviderException e) {
			e.printStackTrace();
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace(new PrintStream(response.getOutputStream()));
			throw new ServletException(e);
		} catch (MappingProviderException e) {
			e.printStackTrace();
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace(new PrintStream(response.getOutputStream()));
			throw new ServletException(e);
		} catch (BitmapRendererException e) {
			e.printStackTrace();
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace(new PrintStream(response.getOutputStream()));
			throw new ServletException(e);
		}
		finally{
			long t3=System.currentTimeMillis();
			this.doLog(request, t3-t1);
		}
		
	}
	
	protected void doLog(HttpServletRequest request, long ms)
	{
		StringBuilder sb=new StringBuilder(request.getQueryString());
		sb.append("\t");
		sb.append(request.getLocalAddr());
		sb.append("\t");
		sb.append(ms).append("ms");
		System.out.println(sb.toString());
	}
	
	
}
