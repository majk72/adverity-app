package org.mipo.adverity.dw.persistence.dsl;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition.RepositoryFragments;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.RepositoryFragment;

public class ProjectingQueryDslJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID>
		extends JpaRepositoryFactoryBean<Repository<S, ID>, S, ID> {

	public ProjectingQueryDslJpaRepositoryFactoryBean(Class<? extends Repository<S, ID>> repositoryInterface) {
		super(repositoryInterface);
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new ProjectingQueryDslJpaRepositoryFactory(entityManager);
	}

	public static class ProjectingQueryDslJpaRepositoryFactory extends JpaRepositoryFactory {

		private EntityManager entityManager;
		private EntityPathResolver entityPathResolver;

		public ProjectingQueryDslJpaRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
			this.entityPathResolver = SimpleEntityPathResolver.INSTANCE;
		}

		@Override
		protected RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
			RepositoryComposition.RepositoryFragments fragments = RepositoryComposition.RepositoryFragments.empty();

			if (ProjectingQueryDslJpaRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
				fragments = fragments.append(RepositoryFragment
						.implemented(getTargetRepositoryViaReflection(ProjectingQueryDslJpaRepositoryImpl.class,
								getEntityInformation(metadata.getDomainType()), entityManager, entityPathResolver)));
			}
			return fragments;
		}

		public void setEntityPathResolver(EntityPathResolver entityPathResolver) {
			super.setEntityPathResolver(entityPathResolver);
			this.entityPathResolver = entityPathResolver;
		}

	}
}