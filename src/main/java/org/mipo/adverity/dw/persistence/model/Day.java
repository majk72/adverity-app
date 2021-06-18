package org.mipo.adverity.dw.persistence.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "DAYS")
public class Day implements Dimension {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DAYS_SQ")
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "CALENDAR_DATE", nullable = false, unique = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@Column(name = "DAY_OF_WEEK", nullable = false)
	private int dayOfWeek;

	@Column(name = "DAY_MONTH", nullable = false)
	private int dayOfMonth;

	@Column(name = "MONTH", nullable = false)
	private int month;

	@Column(name = "QTR", nullable = false)
	private int quarter;

	@Column(name = "YEAR", nullable = false)
	private int year;

	public Day() {
		super();
	}

	public Day(LocalDate date, int dayOfWeek, int dayOfMonth, int month, int quarter, int year) {
		super();
		this.date = date;
		this.dayOfWeek = dayOfWeek;
		this.dayOfMonth = dayOfMonth;
		this.month = month;
		this.quarter = quarter;
		this.year = year;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getQuarter() {
		return quarter;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
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
		Day other = (Day) obj;
		return Objects.equals(id, other.id);
	}

}