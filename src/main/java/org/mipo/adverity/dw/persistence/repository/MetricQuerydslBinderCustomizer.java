package org.mipo.adverity.dw.persistence.repository;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Optional;

import org.mipo.adverity.dw.persistence.model.ModelConstants;
import org.mipo.adverity.dw.persistence.model.QMetric;
import org.springframework.data.querydsl.binding.MultiValueBinding;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

public interface MetricQuerydslBinderCustomizer extends QuerydslBinderCustomizer<QMetric> {

	public static final String PCT = "%";
	public static final String ASTERISK = "*";

	@Override
	default public void customize(final QuerydslBindings bindings, final QMetric root) {
		bindings.excludeUnlistedProperties(true);

		bindings.bind(root.id.campaign.name).as(ModelConstants.CAMPAIGN).first(likeExpression());
		bindings.bind(root.id.datasource.name).as(ModelConstants.DATASOURCE).first(likeExpression());
		bindings.bind(root.id.day.year).as(ModelConstants.YEAR).all(equalOrBetween());
		bindings.bind(root.id.day.month).as(ModelConstants.MONTH).all(equalOrBetween());
		bindings.bind(root.id.day.quarter).as(ModelConstants.QUARTER).all(equalOrBetween());
		bindings.bind(root.id.day.dayOfMonth).as(ModelConstants.DAY_OF_MONTH).all(equalOrBetween());
		bindings.bind(root.id.day.date).as(ModelConstants.DATE).all((path, value) -> {
			Iterator<? extends LocalDate> it = value.iterator();
			return Optional.of(value.size() == 1 ? path.eq(it.next()) : path.between(it.next(), it.next()));
		});

		bindings.bind(root.id.day.dayOfWeek).as(ModelConstants.DAY_OF_WEEK).all((path, val) -> {
			return Optional.of(path.in(val.toArray(new Integer[] {})));
		});
	}

	default SingleValueBinding<StringPath, String> likeExpression() {
		return (path, val) -> {
			return path.like(val.replace(ASTERISK, PCT));
		};
	}

	default <T extends Number & Comparable<?>> MultiValueBinding<NumberPath<T>, T> equalOrBetween() {
		return (path, value) -> {
			Iterator<? extends T> it = value.iterator();
			return Optional.of(value.size() == 1 ? path.eq(it.next()) : path.between(it.next(), it.next()));
		};
	}

}