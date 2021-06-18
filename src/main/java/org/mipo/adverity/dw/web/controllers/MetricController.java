package org.mipo.adverity.dw.web.controllers;

import static java.util.stream.Collectors.toList;
import static org.mipo.adverity.dw.web.controllers.MetricApi.API_METRICS;
import static org.mipo.adverity.dw.web.controllers.MetricApi.VAR_SELECT;

import java.util.List;

import org.mipo.adverity.dw.persistence.model.Metric;
import org.mipo.adverity.dw.persistence.repository.DslMetricQueryRepository;
import org.mipo.adverity.dw.web.bindings.MetricBindings;
import org.mipo.adverity.dw.web.bindings.MetricBindingsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

@RestController
class MetricController {

	@Autowired
	private DslMetricQueryRepository dslMetricQueryRepository;
	@Autowired
	private MetricBindingsValidator metricBindingsValidator;

	@GetMapping(API_METRICS)
	public MetricQueryResults findForSelection(@MatrixVariable(pathVar = VAR_SELECT) String[] select,
			@QuerydslPredicate(root = Metric.class) Predicate predicate) {
		metricBindingsValidator.validate(select, predicate);
		return new MetricQueryResults(select, findAll(select, predicate));
	}

	private List<Object[]> findAll(String[] selectionBindings, Predicate predicate) {
		return dslMetricQueryRepository
				.findAllWithProjection(MetricBindings.toProjectionExpression(selectionBindings),
						MetricBindings.toGroupExpression(selectionBindings), predicate)
				.stream().map(t -> t.toArray()).collect(toList());
	}

}
