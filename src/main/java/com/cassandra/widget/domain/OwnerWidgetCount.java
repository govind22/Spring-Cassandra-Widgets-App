package com.cassandra.widget.domain;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "owner_widget_counts")
public class OwnerWidgetCount {

	@PrimaryKey
	private String id;

	private long counter_value;

	public OwnerWidgetCount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCounter_value() {
		return counter_value;
	}

	public void setCounter_value(long counter_value) {
		this.counter_value = counter_value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (counter_value ^ (counter_value >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		OwnerWidgetCount other = (OwnerWidgetCount) obj;
		if (counter_value != other.counter_value)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OwnerWidgetCount [id=" + id + ", counter_value=" + counter_value + "]";
	}
	
}
