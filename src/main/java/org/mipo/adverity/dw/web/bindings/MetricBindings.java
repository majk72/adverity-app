package org.mipo.adverity.dw.web.bindings;

import static com.querydsl.core.types.dsl.MathExpressions.round;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.mipo.adverity.dw.persistence.model.ModelConstants;
import org.mipo.adverity.dw.persistence.model.QMetric;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;

import lombok.NonNull;

public enum MetricBindings {

	CLICKS(ModelConstants.CLICKS, QMetric.metric.clicks, Type.METRIC), //
	IMPRESSIONS(ModelConstants.IMPRESSIONS, QMetric.metric.impressions, Type.METRIC), //
	CTR(ModelConstants.CTR, round(QMetric.metric.ctr, 2), Type.METRIC), //
	TOTAL_CLICKS(ModelConstants.TOTAL_CLICKS, QMetric.metric.clicks.sum(), Type.AGGREGATE), //
	TOTAL_IMPRESSIONS(ModelConstants.TOTAL_IMPRESSIONS, QMetric.metric.impressions.sum(), Type.AGGREGATE), //
	AVG_CLICKS(ModelConstants.AVG_CLICKS, round(QMetric.metric.clicks.avg(), 2), Type.AGGREGATE), //
	AVG_IMPRESSIONS(ModelConstants.AVG_IMPRESSIONS, round(QMetric.metric.impressions.avg(), 2), Type.AGGREGATE), //
	AVG_CTR(ModelConstants.AVG_CTR, round(QMetric.metric.ctr.avg(), 2), Type.AGGREGATE), //
	COUNT(ModelConstants.COUNT, QMetric.metric.count(), Type.AGGREGATE), //
	DATASOURCE(ModelConstants.DATASOURCE, QMetric.metric.id.datasource.name), //
	CAMPAIGN(ModelConstants.CAMPAIGN, QMetric.metric.id.campaign.name), //
	YEAR(ModelConstants.YEAR, QMetric.metric.id.day.year), //
	QUARTER(ModelConstants.QUARTER, QMetric.metric.id.day.quarter), //
	MONTH(ModelConstants.MONTH, QMetric.metric.id.day.month), //
	DATE(ModelConstants.DATE, QMetric.metric.id.day.date), //
	DAY_OF_MONTH(ModelConstants.DAY_OF_MONTH, QMetric.metric.id.day.dayOfMonth), //
	DAY_OF_WEEK(ModelConstants.DAY_OF_WEEK, QMetric.metric.id.day.dayOfWeek);

	public static final Set<String> ALL_BINDING_NAMES = stream(values()).map(MetricBindings::getBinding)
			.collect(toSet());

	public static final Map<String, MetricBindings> ALL_SELECTION_BINDINGS = stream(values())
			.collect(toMap(MetricBindings::getBinding, identity()));

	private static enum Type {
		AGGREGATE, DIMENSION, METRIC;
	}

	public static @NonNull List<Expression<?>> toGroupExpression(String[] bindings) {
		return toMetricBindings(bindings, m -> m.isMetric()).isEmpty()
				? toExpressions(toMetricBindings(bindings, m -> m.isDimension()))
				: emptyList();
	}

	public static @NonNull Expression<Tuple> toProjectionExpression(String[] bindings) {
		return Projections.tuple(toExpressions(toMetricBindings(bindings)).toArray(new Expression<?>[] {}));
	}

	public static @NonNull List<MetricBindings> toMetricBindings(String[] bindings,
			Predicate<? super MetricBindings> filter) {
		return stream(bindings).map(binding -> bindingFor(binding).orElseThrow(() -> {
			throw new InvalidMetricBindingsException(binding);
		})).filter(filter).collect(toList());
	}

	private static List<Expression<?>> toExpressions(List<MetricBindings> bindings) {
		return bindings.stream().map(MetricBindings::getExpression).collect(toList());
	}

	private static @NonNull List<MetricBindings> toMetricBindings(String[] bindings) {
		return toMetricBindings(bindings, m -> true);
	}

	private static Optional<MetricBindings> bindingFor(String binding) {
		return Optional.ofNullable(ALL_SELECTION_BINDINGS.get(binding));
	}

	private MetricBindings(String binding, Expression<?> expression) {
		this(binding, expression, Type.DIMENSION);
	}

	private MetricBindings(String binding, Expression<?> expression, Type type) {
		this.binding = binding;
		this.expression = expression;
		this.type = type;
	}

	private String binding;
	private Expression<?> expression;
	private Type type;

	public Expression<?> getExpression() {
		return expression;
	}

	public String getBinding() {
		return binding;
	}

	public boolean isDimension() {
		return Type.DIMENSION.equals(this.type);
	}

	public boolean isAggregate() {
		return Type.AGGREGATE.equals(this.type);
	}

	public boolean isMetric() {
		return Type.METRIC.equals(this.type);
	}
}
