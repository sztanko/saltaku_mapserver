package com.saltaku.api.servlet.webapi;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.saltaku.api.impl.TestWebAPI;
import com.saltaku.data.api.servlet.WebAPIServlets;


public class ServletTest {

	@Test
	public void run() throws Exception
	{
		Server  server = new Server(8081);
 	   
 	   
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        WebAPIServlets.addToContext(context, new TestWebAPI());
        server.start();
        
        server.join();
       
	}
	
}
