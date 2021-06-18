package org.mipo.adverity.dw.web.bindings;

import static java.util.Arrays.stream;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.util.function.Predicate;
import java.util.stream.Stream;

import com.querydsl.core.types.Expression;

public class TestMetricBindingsFixtures {


	public static String[] testSelectionBindings(Predicate<? super MetricBindings> predicate) {
		return filterMetricBindings(predicate).map(MetricBindings::getBinding).toArray(String[]::new);
	}

	public static String testSelectionBindingsAsString(Predicate<? super MetricBindings> predicate) {
		return arrayToCommaDelimitedString(testSelectionBindings(predicate));
	}


	public static Expression<?>[] expressions(Predicate<? super MetricBindings> predicate) {
		return filterMetricBindings(predicate).map(MetricBindings::getExpression)
				.toArray(Expression[]::new);
	}

	private static Stream<MetricBindings> filterMetricBindings(Predicate<? super MetricBindings> predicate) {
		return stream(MetricBindings.values()).filter(predicate);
	}

}
