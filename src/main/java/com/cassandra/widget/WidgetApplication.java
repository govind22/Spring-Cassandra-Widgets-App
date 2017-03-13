package com.cassandra.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

@SpringBootApplication
public class WidgetApplication {

	private static Cluster cluster;
    private static Session session;

    private static Logger logger = LoggerFactory.getLogger(WidgetApplication.class);
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(WidgetApplication.class, args);
	    
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("cwt");
		
		CassandraPaging cassandraPaging = ctx.getBean("cassandrapaging", CassandraPaging.class);
		cassandraPaging.setSession(session);
		
		logger.info("created cassandraPagin object " + cassandraPaging);
	}
}
