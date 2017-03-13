package com.cassandra.widget.domain;

import java.util.UUID;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import com.datastax.driver.mapping.annotations.Column;

@Table
public class Owner {

	@PrimaryKeyColumn(name = "id", type = PrimaryKeyType.PARTITIONED)
	private String id;

	@PrimaryKeyColumn(name = "widget_id", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private UUID widget_id;

	@Column(name = "color")
	private String color;

	@Column(name = "type")
	private int type;

	public Owner() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UUID getWidget_id() {
		return widget_id;
	}

	public void setWidget_id(UUID widget_id) {
		this.widget_id = widget_id;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + type;
		result = prime * result + ((widget_id == null) ? 0 : widget_id.hashCode());
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
		Owner other = (Owner) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type != other.type)
			return false;
		if (widget_id == null) {
			if (other.widget_id != null)
				return false;
		} else if (!widget_id.equals(other.widget_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Owner [id=" + id + ", widget_id=" + widget_id + ", color=" + color + ", type=" + type + "]";
	}

}
