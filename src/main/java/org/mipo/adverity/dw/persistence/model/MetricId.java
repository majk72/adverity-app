package org.mipo.adverity.dw.persistence.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class MetricId implements Serializable {
	private static final long serialVersionUID = 2951764926771738584L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "DAY_ID", nullable = false, updatable = false)
	private Day day;

	@ManyToOne(optional = false)
	@JoinColumn(name = "DATASOURCE_ID", nullable = false, updatable = false)
	private Datasource datasource;

	@ManyToOne(optional = false)
	@JoinColumn(name = "CAMPAIGN_ID", nullable = false, updatable = false)
	private Campaign campaign;

	public MetricId() {
		super();
	}

	public MetricId(Day day, Datasource datasource, Campaign campaign) {
		super();
		this.day = day;
		this.datasource = datasource;
		this.campaign = campaign;
	}

	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	@Override
	public int hashCode() {
		return Objects.hash(campaign, datasource, day);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetricId other = (MetricId) obj;
		return Objects.equals(campaign, other.campaign) && Objects.equals(datasource, other.datasource)
				&& Objects.equals(day, other.day);
	}

}