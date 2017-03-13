package com.cassandra.widget.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cassandra.widget.CassandraPaging;
import com.cassandra.widget.dao.OwnerRepository;
import com.cassandra.widget.domain.Owner;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

@RestController
public class OwnerController {

private static Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    private OwnerRepository ownerRepository;
    
    @Autowired
    private CassandraPaging cassandraPaging;

	@RequestMapping("/owners/{id}/widgets")
	public Response getAllWidgetsByOwner(@PathVariable String id, @QueryParam("page") String page,
			                             @QueryParam("per_page") String per_page) {
		
		 Select s = QueryBuilder.select().from("owner"); 
		 Statement select = s.where(QueryBuilder.eq("id", id)); 
		
		 int start =  1, pageSize = 10;
		 
		 // validate page and per_page is valid integer
		 if(page != null && page.matches("\\d+")){
			 start = Integer.parseInt(page);
			 if(start <= 0){
				 return Response.status(Status.BAD_REQUEST.getStatusCode()).entity("Invalid paging parameters, Should be greater than 0").build();
			 }
		 }else if(page != null && !page.matches("\\d+")){
			 return Response.status(Status.BAD_REQUEST.getStatusCode()).entity("Invalid paging parameters, Should be greater than 0").build();
		 }
		 
		 if(per_page != null && per_page.matches("\\d+")){
			 pageSize = Integer.parseInt(per_page);
			 if(pageSize <= 0){
				 return Response.status(Status.NOT_FOUND.getStatusCode()).entity("Invalid paging parameters, Should be greater than 0").build();
			 }
		 }else if(per_page != null && !per_page.matches("\\d+")){
			 return Response.status(Status.BAD_REQUEST.getStatusCode()).entity("Invalid paging parameters, Should be greater than 0").build();
		 }
		 
		 List<Row> pageRows = cassandraPaging.fetchRowsWithPage(select, start, pageSize);
	     logger.info("fetched given page size row " + pageRows);
	        
	     List<Owner> widgets = OwnerController.extractRows(pageRows);
			
		 return Response.status(Status.OK.getStatusCode()).entity(widgets).build();				
	}
	
	@RequestMapping("/owners/{id}")
	public Response getOwnerById(@PathVariable String id) {
				
		Iterable<Owner> p = ownerRepository.findByOwnerId(id);		
		logger.info("in /owners/{id}");
		return Response.status(Status.OK.getStatusCode()).entity(p).build();			
	}
	
	// Convert Result set to entities 
	 private static List<Owner> extractRows(final List<Row> inRows){        
	     
		 List<Owner> result = new ArrayList<Owner>();
		 for (Row row : inRows) {
			    Owner o = new Owner();
			    o.setId(row.getString("id"));
			    o.setColor(row.getString("color"));
			    o.setType(row.getInt("type"));
			    o.setWidget_id(row.getUUID("widget_id"));
			    result.add(o);
	        }
			return result;
	    }
}
