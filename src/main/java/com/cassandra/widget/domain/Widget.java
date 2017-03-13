package com.cassandra.widget.domain;

import java.util.UUID;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table
public class Widget {

	@PrimaryKey
	private UUID id;
	
	private String owner_id;
	
	@Transient
	private String color;

	@Transient
	private int type;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Widget() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((owner_id == null) ? 0 : owner_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Widget other = (Widget) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (owner_id == null) {
			if (other.owner_id != null)
				return false;
		} else if (!owner_id.equals(other.owner_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Widget [id=" + id + ", owner_id=" + owner_id + "]";
	}
	
}
