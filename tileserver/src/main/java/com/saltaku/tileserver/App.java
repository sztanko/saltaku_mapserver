package com.saltaku.tileserver;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.saltaku.tileserver.guice.DefaultModule;
import com.saltaku.tileserver.providers.basemaps.BasemapProvider;


/**
 * Hello world!
 *
 */
public class App 
{
    
	public static void main( String[] args ) throws Exception
    {

           App app=new App();
           app.startApp();
           
    }
	
	public void startApp() throws Exception
	{
		   Server  server = new Server(8081);
    	   Injector injector = Guice.createInjector(new DefaultModule());
    	   
           ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
           context.setContextPath("/");
           server.setHandler(context);
           HttpServlet tileServlet=injector.getInstance(HttpServlet.class);
           
           context.addServlet(new ServletHolder(tileServlet),"/tile/*");
           
           Runtime.getRuntime().addShutdownHook(new ShutdownHandler(server, injector.getInstance(BasemapProvider.class)));
           server.start();
           
           server.join();
           	
	}
    
    protected class ShutdownHandler extends Thread 
    {
    	Server s;
    	BasemapProvider bp;
    	public ShutdownHandler(Server s,BasemapProvider bp)
    	{
    		this.s=s;
    		this.bp=bp;
    	}
	    public void run() { try {
	    	System.out.println("Stopping");	
	    	s.stop();
			bp.stop();
			System.out.println("Stopped");	
		} catch (Exception e) {
			e.printStackTrace();
		} }
	}
}
