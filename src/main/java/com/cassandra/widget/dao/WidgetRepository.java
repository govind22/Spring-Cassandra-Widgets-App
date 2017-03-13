package com.cassandra.widget.dao;

import java.util.UUID;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cassandra.widget.domain.Widget;

public interface WidgetRepository extends CrudRepository<Widget, UUID> {

	@Query("select * from widget where id = ?0")
	public Widget findOne(UUID id);
	
}
