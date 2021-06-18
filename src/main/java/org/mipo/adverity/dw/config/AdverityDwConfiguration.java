package org.mipo.adverity.dw.config;

import static org.mipo.adverity.dw.config.AdverityDwConstants.CSV_DATA_TEMP_FILE;
import static org.mipo.adverity.dw.config.AdverityDwConstants.CSV_DATA_URL;
import static org.mipo.adverity.dw.config.AdverityDwConstants.DATA_EXTRACT_SCRIPT;
import static org.mipo.adverity.dw.config.AdverityDwConstants.DATA_LOADER_SCRIPT;

import java.io.IOException;

import javax.sql.DataSource;

import org.mipo.adverity.dw.ETLService;
import org.mipo.adverity.dw.services.FileToDbLoader;
import org.mipo.adverity.dw.services.HttpDownloader;
import org.mipo.adverity.dw.services.MetricsETLService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AdverityDwConfiguration {

	@Value(CSV_DATA_URL)
	private String sourceUrl;
	@Value(CSV_DATA_TEMP_FILE)
	private String tempFile;
	@Value(DATA_EXTRACT_SCRIPT)
	private Resource dataExtractScript;
	@Value(DATA_LOADER_SCRIPT)
	private Resource dataLoaderScript;

	@Bean
	public HttpDownloader fileDownloader() {
		return new HttpDownloader(sourceUrl, tempFile);
	}

	@Bean
	public FileToDbLoader dbLoader(DataSource datasource) throws IOException {
		return new FileToDbLoader(new JdbcTemplate(datasource), dataExtractScript, dataLoaderScript, tempFile);
	}

	@Bean
	public ETLService importService(HttpDownloader fileDownloader, FileToDbLoader dbLoader) {
		return new MetricsETLService(fileDownloader, dbLoader);
	}

}
