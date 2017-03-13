package com.cassandra.widget.dao;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cassandra.widget.domain.OwnerWidgetCount;

public interface OwnerWidgetCountRepository extends CrudRepository<OwnerWidgetCount, String> {

	@Query("select * from owner_widget_counts where id = ?0")
	public OwnerWidgetCount findOne(String id);
	
}
