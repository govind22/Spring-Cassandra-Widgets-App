package com.cassandra.widget.controller;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cassandra.widget.dao.OwnerWidgetCountRepository;
import com.cassandra.widget.domain.OwnerWidgetCount;

@RestController
public class OwnerWidgetCounterController {

private static Logger logger = LoggerFactory.getLogger(OwnerWidgetCounterController.class);

    @Autowired
    private OwnerWidgetCountRepository ownerWidgetCountRepository;
    
	@RequestMapping("/owner/{id}/widgetcount")
	public Response getWidgetCountsByOwnerId(@PathVariable String id) {
		
		if(!ownerWidgetCountRepository.exists(id)){
			return Response.status(Status.NOT_FOUND.getStatusCode()).entity("owner does not exists").build();
		}
		
		OwnerWidgetCount p = ownerWidgetCountRepository.findOne(id);	
		logger.info("in /owners/{id}/widgetcounts");
		return Response.status(Status.OK.getStatusCode()).entity(p.getCounter_value()).build();		
		
	}
}
