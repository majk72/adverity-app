package org.mipo.adverity.dw.web.bindings;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class MetricBindingsValidator {

	protected static final String ERR_INVALID_SELECT_EXPRESSIONS = "Invalid select expressions '%s'";
	protected static final String ERR_CANNOT_HAVE_BOTH_AGGREGATES_AND_METRICS = "Cannot have both metrics and aggregates in one query (e.g. clicks,avg_impressions)";
	protected static final String ERR_CANNOT_HAVE_ONLY_DIMENSIONS = "Cannot have only dimensions, at least one metric or aggregate is required";
	protected static final String ERR_FOR_METRICS_A_FILTER_IS_REQUIRED = "Missing query filter for metrics, some query params should be added after '?' in url";;

	public void validate(String[] bindings, Predicate predicate) {
		throwOnNonEmptyUnknownBindings(collectInvalidBindings(bindings));

		validateBindingTypes(contains(bindings, MetricBindings::isAggregate),
				contains(bindings, MetricBindings::isMetric), contains(bindings, MetricBindings::isDimension),
				predicate);
	}

	private void validateBindingTypes(boolean hasAggregates, boolean hasMetrics, boolean hasDimensions,
			Predicate predicate) {
		if (hasAggregates && hasMetrics) {
			throw new InvalidMetricBindingsException(ERR_CANNOT_HAVE_BOTH_AGGREGATES_AND_METRICS);
		}
		if (!hasAggregates && !hasMetrics && hasDimensions) {
			throw new InvalidMetricBindingsException(ERR_CANNOT_HAVE_ONLY_DIMENSIONS);
		}
		if (hasMetrics && predicate instanceof BooleanBuilder && !((BooleanBuilder) predicate).hasValue()) {
			throw new InvalidMetricBindingsException(ERR_FOR_METRICS_A_FILTER_IS_REQUIRED);
		}
	}

	private void throwOnNonEmptyUnknownBindings(List<String> invalid) {
		if (!invalid.isEmpty()) {
			throw new InvalidMetricBindingsException(
					format(ERR_INVALID_SELECT_EXPRESSIONS, collectionToCommaDelimitedString(invalid)));
		}
	}

	private boolean contains(String[] bindings, java.util.function.Predicate<? super MetricBindings> filter) {
		return !MetricBindings.toMetricBindings(bindings, filter).isEmpty();
	}

	private static List<String> collectInvalidBindings(String[] bindings) {
		return stream(bindings).filter(b -> !MetricBindings.ALL_BINDING_NAMES.contains(b)).collect(toList());
	}

}
