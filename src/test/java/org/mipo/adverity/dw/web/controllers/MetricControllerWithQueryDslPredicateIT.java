package org.mipo.adverity.dw.web.controllers;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mipo.adverity.dw.TestConstants.JSON_DATA;
import static org.mipo.adverity.dw.TestConstants.JSON_HEADERS;
import static org.mipo.adverity.dw.TestConstants.TEST;
import static org.mipo.adverity.dw.TestConstants.TEST_CAMPAIGN;
import static org.mipo.adverity.dw.TestConstants.TEST_DATE;
import static org.mipo.adverity.dw.web.bindings.TestMetricBindingsFixtures.testSelectionBindings;
import static org.mipo.adverity.dw.web.bindings.TestMetricBindingsFixtures.testSelectionBindingsAsString;
import static org.mipo.adverity.dw.web.controllers.MetricApi.API_METRICS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mipo.adverity.dw.ETLService;
import org.mipo.adverity.dw.TestConstants;
import org.mipo.adverity.dw.persistence.model.ModelConstants;
import org.mipo.adverity.dw.web.bindings.MetricBindings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MetricControllerWithQueryDslPredicateIT {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ETLService etlService;

	@Test
	public void shouldGetForMetricSelectionsWithPredicateAndRespondWithOk() throws Exception {
		this.mockMvc
				.perform(get(API_METRICS, select(testSelectionBindingsAsString(MetricBindings::isMetric)))
						.queryParam(ModelConstants.CAMPAIGN, TEST_CAMPAIGN))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath(JSON_HEADERS).value(hasItems(testSelectionBindings(MetricBindings::isMetric))))
				.andExpect(jsonPath(JSON_DATA).hasJsonPath());
	}

	@Test
	public void shouldGetForMetricSelectionsWithDatePredicateAndRespondWithOk() throws Exception {
		this.mockMvc
				.perform(get(API_METRICS, select(testSelectionBindingsAsString(MetricBindings::isMetric)))
						.queryParam(ModelConstants.DATE, TEST_DATE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath(JSON_HEADERS).value(hasItems(testSelectionBindings(MetricBindings::isMetric))))
				.andExpect(jsonPath(JSON_DATA).hasJsonPath());
	}

	@Test
	@Disabled
	public void shouldGetForMetricSelectionsWithBadDateFormatPredicateAndRespondWithBadRequest() throws Exception {
		this.mockMvc
		.perform(get(API_METRICS, select(testSelectionBindingsAsString(MetricBindings::isMetric)))
				.queryParam(ModelConstants.DATE, TEST))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath(JSON_HEADERS).value(hasItems(testSelectionBindings(MetricBindings::isMetric))))
		.andExpect(jsonPath(JSON_DATA).hasJsonPath());
	}

	private String select(String selection) {
		return format(TestConstants.FMT_SELECT, selection);
	}

}
