package org.mipo.adverity.dw.persistence.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name = "METRICS")
public class Metric {
	@EmbeddedId
	private MetricId id;

	@Column(name = "CLICKS", nullable = false)
	private long clicks;

	@Column(name = "IMPRESSIONS", nullable = false)
	private long impressions;

	@Formula("ROUND(100*CLICKS/IMPRESSIONS,2)")
	private long ctr;

	public Metric() {
		super();
	}

	public Metric(MetricId id, long clicks, long impressions, long ctr) {
		super();
		this.id = id;
		this.clicks = clicks;
		this.impressions = impressions;
		this.ctr = ctr;
	}

	public MetricId getId() {
		return id;
	}

	public void setId(MetricId id) {
		this.id = id;
	}

	public long getClicks() {
		return clicks;
	}

	public void setClicks(long clicks) {
		this.clicks = clicks;
	}

	public long getImpressions() {
		return impressions;
	}

	public void setImpressions(long impressions) {
		this.impressions = impressions;
	}

	public long getCtr() {
		return ctr;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Metric other = (Metric) obj;
		return Objects.equals(id, other.id);
	}

}