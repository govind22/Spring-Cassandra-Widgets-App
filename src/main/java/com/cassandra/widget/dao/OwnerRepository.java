package com.cassandra.widget.dao;

import java.util.UUID;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cassandra.widget.domain.Owner;

public interface OwnerRepository extends CrudRepository<Owner, String> {

	@Query("select * from owner where id = ?0")
	public Iterable<Owner> findByOwnerId(String id);
	
	@Query("select * from owner where id = ?0 and widget_id=?1")
    public Owner findByOwnerAndWidgetId(String id, UUID widget_id);
	
}
