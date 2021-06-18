package org.mipo.adverity.dw.app.persistence.repository;

import static com.querydsl.core.types.Projections.tuple;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mipo.adverity.dw.TestConstants.TEST_CAMPAIGN;
import static org.mipo.adverity.dw.web.bindings.TestMetricBindingsFixtures.expressions;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mipo.adverity.dw.AbstractSpringBootBaseIT;
import org.mipo.adverity.dw.persistence.model.QMetric;
import org.mipo.adverity.dw.persistence.repository.DslMetricQueryRepository;
import org.mipo.adverity.dw.web.bindings.MetricBindings;
import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;

public class DslMetricQueryRepositoryIT extends AbstractSpringBootBaseIT {

	@Autowired
	private DslMetricQueryRepository metricQueryRepository;

	@Test
	public void shouldSelectAggregates() {
		shouldExecQuery(tuple(expressions(MetricBindings::isAggregate)), null);
	}

	@Test
	public void shouldSelectDimensionAndAggregates() {
		shouldExecQueryAndReturnAtLeastOneRec(tuple(expressions(m -> m.isDimension() || m.isAggregate())),
				asList(expressions(m -> m.isDimension())), null);
	}

	@Test
	public void shouldSelectMetrics() {
		shouldExecQuery(tuple(expressions(MetricBindings::isMetric)),
				new BooleanBuilder(QMetric.metric.id.campaign.name.eq(TEST_CAMPAIGN)));
	}

	private void shouldExecQuery(Expression<Tuple> projections, Predicate predicate) {
		shouldExecQueryAndReturnAtLeastOneRec(projections, emptyList(), predicate);
	}

	private void shouldExecQueryAndReturnAtLeastOneRec(Expression<Tuple> projections, List<Expression<?>> groupBy,
			Predicate predicate) {
		shouldReturnAtLeastOneRec(metricQueryRepository.findAllWithProjection(projections, groupBy, predicate));
	}

	private void shouldReturnAtLeastOneRec(Iterable<Tuple> res) {
		assertNotNull(res);
		assertNotNull(res.iterator().next());
	}
}
