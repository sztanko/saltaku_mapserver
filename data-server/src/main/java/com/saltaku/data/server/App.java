package com.saltaku.data.server;

import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.saltaku.api.WebAPI;
import com.saltaku.api.impl.SimpleWebAPI;
import com.saltaku.data.api.servlet.WebAPIServlets;
import com.saltaku.data.server.guice.DefaultModule;


 
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
    	   Injector injector = Guice.createInjector(new DefaultModule(new Properties()) );
    	   
           ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
           context.setContextPath("/");
           server.setHandler(context);
           WebAPI webApi=injector.getInstance(WebAPI.class);
           WebAPIServlets.addToContext(context, webApi);
           //HttpServlet tileServlet=injector.getInstance(HttpServlet.class);
           
           //context.addServlet(new ServletHolder(tileServlet),"/data/*");
           
           
           Runtime.getRuntime().addShutdownHook(new ShutdownHandler(server, injector.getInstance(WebAPI.class)));
           server.start();
           
           server.join();
           	
	}
    
    protected class ShutdownHandler extends Thread 
    {
    	Server s;
    	WebAPI api;
    	
    	public ShutdownHandler(Server s, WebAPI api)
    	{
    		this.s=s;
    		this.api=api;
    		
    	}
	    public void run() { try {
	    	System.out.println("Stopping");	
	    	s.stop();
			api.close();
			System.out.println("Stopped");	
		} catch (Exception e) {
			e.printStackTrace();
		} }
	}
}
