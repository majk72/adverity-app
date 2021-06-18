package org.mipo.adverity.dw.persistence.dsl;

import java.util.List;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;

import lombok.NonNull;

@NoRepositoryBean
public interface ProjectingQueryDslJpaRepository<T, E extends EntityPath<T>> extends QuerydslPredicateExecutor<T> {

	<P> @NonNull List<P> findAllWithProjection(@NonNull Expression<P> factoryProjectionExpression,
			List<Expression<?>> groupBy, Predicate predicate);

}