package org.mipo.adverity.dw.web.controllers;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mipo.adverity.dw.TestConstants.JSON_DATA;
import static org.mipo.adverity.dw.TestConstants.JSON_HEADERS;
import static org.mipo.adverity.dw.TestConstants.TEST;
import static org.mipo.adverity.dw.web.bindings.TestMetricBindingsFixtures.testSelectionBindings;
import static org.mipo.adverity.dw.web.bindings.TestMetricBindingsFixtures.testSelectionBindingsAsString;
import static org.mipo.adverity.dw.web.controllers.MetricApi.API_METRICS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mipo.adverity.dw.ETLService;
import org.mipo.adverity.dw.TestConstants;
import org.mipo.adverity.dw.persistence.repository.DslMetricQueryRepository;
import org.mipo.adverity.dw.web.bindings.InvalidMetricBindingsException;
import org.mipo.adverity.dw.web.bindings.MetricBindings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MissingMatrixVariableException;

@WebMvcTest(MetricController.class)
public class MetricControllerIT {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ETLService etlService;
	@MockBean
	private DslMetricQueryRepository repository;

	@Test
	public void shouldGetForAggregateSelectionsAndRespondWithOk() throws Exception {
		this.mockMvc.perform(get(API_METRICS, select(testSelectionBindingsAsString(MetricBindings::isAggregate))))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath(JSON_HEADERS).value(hasItems(testSelectionBindings(MetricBindings::isAggregate))))
				.andExpect(jsonPath(JSON_DATA).hasJsonPath());
	}

	@Test
	public void shouldRespondWithBadRequestForEmptySelect() throws Exception {
		shoulResponseWithBadRequestAndException(select(EMPTY), MissingMatrixVariableException.class);
	}

	@Test
	public void shouldRespondWithBadRequestForUnknownSelect() throws Exception {
		shoulResponseWithBadRequestAndException(select(TEST), InvalidMetricBindingsException.class);
	}

	@Test
	public void shouldRespondWithBadRequestForDimensionOnlySelections() throws Exception {
		shoulResponseWithBadRequestAndException(select(testSelectionBindingsAsString(MetricBindings::isDimension)),
				InvalidMetricBindingsException.class);
	}

	private void shoulResponseWithBadRequestAndException(String select, Class<? extends Exception> exception)
			throws Exception {
		assertThat(this.mockMvc.perform(get(API_METRICS, select)) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResolvedException(), instanceOf(exception));
	}

	private String select(String selection) {
		return format(TestConstants.FMT_SELECT, selection);
	}

}
