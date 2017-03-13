package com.cassandra.widget.controller;

import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassandra.widget.CassandraPaging;
import com.cassandra.widget.dao.OwnerRepository;
import com.cassandra.widget.dao.WidgetRepository;
import com.cassandra.widget.domain.Owner;
import com.cassandra.widget.domain.Widget;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;
import com.datastax.driver.core.utils.UUIDs;

@RestController
public class WidgetController {

	private static Logger logger = LoggerFactory.getLogger(WidgetController.class);

	@Autowired
	private WidgetRepository widgetRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private CassandraPaging cassandraPaging;

	@RequestMapping("/widgets/{id}")
	public Response getWidgetById(@PathVariable UUID id) {

		Widget p = widgetRepository.findOne(id);
		
		if(p == null){
			return Response.status(Status.NOT_FOUND.getStatusCode()).entity("widget does not exists").build();		
		}	
		
		// Fetch widget metadata from owner partiton from owner table
			Owner o = ownerRepository.findByOwnerAndWidgetId(p.getOwner_id(), id);
			p.setColor(o.getColor());
			p.setType(o.getType());		
		
		
		logger.info("in GET /widgets/{id} resource");
		return Response.status(Status.OK.getStatusCode()).entity(p).build();
	}

	@RequestMapping(value = "/widgets", method = RequestMethod.POST)
	public Response addWidget(@RequestBody Widget p) {

		logger.info("in POST /widgets resource");

		if (p.getOwner_id() == null || p.getOwner_id().isEmpty()) {
			return Response.status(Status.BAD_REQUEST.getStatusCode()).entity("invalid owner").build();
		}

		if (p.getColor() == null || p.getColor().isEmpty()) {
			return Response.status(Status.BAD_REQUEST.getStatusCode()).entity("invalid color").build();
		}

		if (p.getType() < 0) {
			return Response.status(Status.BAD_REQUEST.getStatusCode()).entity("invalid type").build();
		}

		p.setId(UUIDs.timeBased());
		
		widgetRepository.save(p);

		logger.info("created widget with Id :" + p.getId());
		
		// store widget meta data in its owner partition in owner table
		Owner widgetOwner = new Owner();
		widgetOwner.setId(p.getOwner_id());
		widgetOwner.setColor(p.getColor());
		widgetOwner.setType(p.getType());
		widgetOwner.setWidget_id(p.getId());
		
		// create/update owner partition for new widget id
		ownerRepository.save(widgetOwner);

		logger.info("created new widget Id for owner:" + p.getOwner_id());
		
		    // update widget counter for owner
			// Increment owners widget count
			Update updateOp = QueryBuilder.update("owner_widget_counts");
			Statement update = updateOp.with(QueryBuilder.incr("counter_value"))
					.where(QueryBuilder.eq("id", p.getOwner_id())); 
																
			// This is an update query, so it will update record and fetch updated row
			// so does not need to store updated rows because this operation is only meant for update
			// not return anything to user its internal table to store widget count for user.
			cassandraPaging.fetchRowsWithPage(update, 1, 1);
			logger.info("updated widget counter for owner: " + p.getOwner_id());

		return Response.status(Status.CREATED.getStatusCode()).entity("created widget with id : " + p.getId()).build();
	}

}
