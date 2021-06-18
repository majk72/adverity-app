package org.mipo.adverity.dw.web.bindings;

import static java.lang.String.format;
import static org.assertj.core.util.Arrays.array;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mipo.adverity.dw.TestConstants.TEST;
import static org.mipo.adverity.dw.TestConstants.TEST_YEAR;
import static org.mipo.adverity.dw.web.bindings.MetricBindingsValidator.ERR_CANNOT_HAVE_BOTH_AGGREGATES_AND_METRICS;
import static org.mipo.adverity.dw.web.bindings.MetricBindingsValidator.ERR_CANNOT_HAVE_ONLY_DIMENSIONS;
import static org.mipo.adverity.dw.web.bindings.MetricBindingsValidator.ERR_FOR_METRICS_A_FILTER_IS_REQUIRED;
import static org.mipo.adverity.dw.web.bindings.MetricBindingsValidator.ERR_INVALID_SELECT_EXPRESSIONS;
import static org.mipo.adverity.dw.web.bindings.TestMetricBindingsFixtures.testSelectionBindings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mipo.adverity.dw.persistence.model.QMetric;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class MetricBindingsValidatorTest {

	@Test
	public void shouldThrowOnUnknownBindings() {
		shouldThrowOnInvalidBindings(array(TEST), format(ERR_INVALID_SELECT_EXPRESSIONS, TEST));
	}

	@Test
	public void shouldThrowOnHavingAggregatesAndMetrics() {
		shouldThrowOnInvalidBindings(array(testSelectionBindings(m -> m.isAggregate() || m.isMetric())),
				ERR_CANNOT_HAVE_BOTH_AGGREGATES_AND_METRICS);
	}

	@Test
	public void shouldThrowOnHavingDimensionsOnly() {
		shouldThrowOnInvalidBindings(array(testSelectionBindings(m -> m.isDimension())), ERR_CANNOT_HAVE_ONLY_DIMENSIONS);
	}

	@Test
	public void shouldThrowOnMissingPredicateForMetrics() {
		shouldThrowOnInvalidBindings(array(testSelectionBindings(m -> m.isMetric())), ERR_FOR_METRICS_A_FILTER_IS_REQUIRED);
	}

	@Test
	public void shouldPassOnValidAggregateBindings() {
		callValidate(array(testSelectionBindings(m -> m.isAggregate())), testEmptyPredicate());
	}

	@Test
	public void shouldPassOnValidMetricsBindingsAndPredicate() {
		callValidate(array(testSelectionBindings(m -> m.isMetric())),
				testEmptyPredicate().and(QMetric.metric.id.day.year.eq(TEST_YEAR)));
	}

	private void shouldThrowOnInvalidBindings(String[] bindings, String expectedErrorMsg) {
		assertThat(assertThrows(InvalidMetricBindingsException.class, callValidate(bindings, testEmptyPredicate()))
				.getMessage(), equalTo(expectedErrorMsg));
	}

	private BooleanBuilder testEmptyPredicate() {
		return new BooleanBuilder();
	}

	private Executable callValidate(String[] params, Predicate predicate) {
		return () -> {
			new MetricBindingsValidator().validate(params, predicate);
		};
	}

}
