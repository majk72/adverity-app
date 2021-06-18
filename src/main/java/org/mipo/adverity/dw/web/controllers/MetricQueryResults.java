package org.mipo.adverity.dw.web.controllers;

import java.util.List;

import lombok.Data;

@Data
public class MetricQueryResults {

	private final String headers[];
	private final List<Object[]> data;

}
