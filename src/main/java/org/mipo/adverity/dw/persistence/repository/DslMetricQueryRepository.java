package org.mipo.adverity.dw.persistence.repository;

import org.mipo.adverity.dw.persistence.dsl.ProjectingQueryDslJpaRepository;
import org.mipo.adverity.dw.persistence.model.Metric;
import org.mipo.adverity.dw.persistence.model.MetricId;
import org.mipo.adverity.dw.persistence.model.QMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DslMetricQueryRepository extends JpaRepository<Metric, MetricId>,
		ProjectingQueryDslJpaRepository<Metric, QMetric>, MetricQuerydslBinderCustomizer {

}