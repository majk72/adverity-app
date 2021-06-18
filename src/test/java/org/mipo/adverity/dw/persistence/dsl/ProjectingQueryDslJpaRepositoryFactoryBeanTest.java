package org.mipo.adverity.dw.persistence.dsl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.spi.PersistenceProvider;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hibernate.metamodel.model.domain.spi.EntityTypeDescriptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mipo.adverity.dw.persistence.dsl.ProjectingQueryDslJpaRepositoryFactoryBean.ProjectingQueryDslJpaRepositoryFactory;
import org.mipo.adverity.dw.persistence.model.Metric;
import org.mipo.adverity.dw.persistence.model.MetricId;
import org.mipo.adverity.dw.persistence.repository.DslMetricQueryRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition.RepositoryFragments;

@ExtendWith(MockitoExtension.class)
public class ProjectingQueryDslJpaRepositoryFactoryBeanTest {

	@Mock
	private PersistenceProvider persistenceProvider;
	@Mock
	private EntityManager entityManager;
	@Mock
	private ProjectingQueryDslJpaRepositoryFactoryBean<Repository<Metric, MetricId>, Metric, MetricId> factoryBean;

	@Mock
	private ProjectingQueryDslJpaRepositoryFactory projectingQueryDslJpaRepositoryFactory;

	private DefaultRepositoryMetadata metadata = new DefaultRepositoryMetadata(DslMetricQueryRepository.class);
	@Mock
	private Metamodel metamodel;
	@Mock
	private EntityTypeDescriptor<Metric> managedType;

	@Test
	public void shouldCreateProjectingQueryDslJpaRepositoryFactory() {
		when(entityManager.getDelegate()).thenReturn(persistenceProvider);
		when(factoryBean.createRepositoryFactory(Mockito.any())).thenCallRealMethod();

		MatcherAssert.assertThat(factoryBean.createRepositoryFactory(entityManager), CoreMatchers
				.instanceOf(ProjectingQueryDslJpaRepositoryFactoryBean.ProjectingQueryDslJpaRepositoryFactory.class));
	}

	@Test
	public void shouldGetRepositoryFragmentImplementingProjectingQueryDslJpaRepositoryImpl() {
		when(entityManager.getDelegate()).thenReturn(persistenceProvider);
		when(entityManager.getMetamodel()).thenReturn(metamodel);
		when(metamodel.managedType(Mockito.<Class<Metric>>any())).thenReturn(managedType);

		RepositoryFragments fragments = new ProjectingQueryDslJpaRepositoryFactory(entityManager)
				.getRepositoryFragments(metadata);

		assertThat(fragments.map(rf -> rf.getImplementation()).filter(Optional::isPresent).map(Optional::get).toList(),
				hasItem(instanceOf(ProjectingQueryDslJpaRepositoryImpl.class)));
	}

}