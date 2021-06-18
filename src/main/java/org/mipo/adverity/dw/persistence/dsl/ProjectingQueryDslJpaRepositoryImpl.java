package org.mipo.adverity.dw.persistence.dsl;

import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.querydsl.EntityPathResolver;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

import lombok.NonNull;

public class ProjectingQueryDslJpaRepositoryImpl<T, ID extends Serializable> extends QuerydslJpaPredicateExecutor<T>
		implements ProjectingQueryDslJpaRepository<T, EntityPath<T>> {

	private final EntityPath<T> path;

	public ProjectingQueryDslJpaRepositoryImpl(@NonNull JpaEntityInformation<T, ID> entityInformation,
			@NonNull EntityManager entityManager, @NonNull EntityPathResolver resolver) {
		super(entityInformation, entityManager, resolver, null);

		this.path = resolver.createPath(entityInformation.getJavaType());
	}

	@Override
	public <P> @NonNull List<P> findAllWithProjection(@NonNull Expression<P> factoryProjectionExpression,
			@NonNull List<Expression<?>> groupBy, Predicate predicate) {
		return createQuery(predicate).select(factoryProjectionExpression).from(path)
				.groupBy(groupBy.toArray(new Expression[] {})).orderBy(orderBy(groupBy)).fetch();
	}

	private OrderSpecifier<?>[] orderBy(List<Expression<?>> groupBy) {
		return groupBy.stream().filter(exp -> exp instanceof ComparableExpressionBase<?>)
				.map(exp -> new OrderSpecifier<>(Order.ASC, (ComparableExpressionBase<?>) exp)).collect(toList())
				.toArray(new OrderSpecifier[] {});
	}

}