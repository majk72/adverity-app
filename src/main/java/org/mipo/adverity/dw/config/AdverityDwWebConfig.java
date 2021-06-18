package org.mipo.adverity.dw.config;

import org.mipo.adverity.dw.web.bindings.MetricBindingsValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AdverityDwWebConfig implements WebMvcConfigurer {

	private static final String MSG_PATH_HELPER_CONFIGURED = "Configured Path Helper with removed semicolon content";

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		urlPathHelper.setRemoveSemicolonContent(false);
		configurer.setUrlPathHelper(urlPathHelper);
		log.info(MSG_PATH_HELPER_CONFIGURED);
	}

	@Bean
	public MetricBindingsValidator metricBindingsValidator() {
		return new MetricBindingsValidator();
	}

}