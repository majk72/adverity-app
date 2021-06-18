package org.mipo.adverity.dw;

import org.mipo.adverity.dw.persistence.dsl.ProjectingQueryDslJpaRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {
		org.mipo.adverity.dw.persistence.repository.Pkg.class }, repositoryFactoryBeanClass = ProjectingQueryDslJpaRepositoryFactoryBean.class)
public class AdverityDwApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdverityDwApplication.class, args);
	}

	@Autowired
	private ETLService etlService;

	@EventListener(ApplicationReadyEvent.class)
	public void importMetricsAfterStartup() {
		etlService.importMetrics();
	}
}
