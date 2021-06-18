package org.mipo.adverity.dw.services;

import org.mipo.adverity.dw.ETLService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class MetricsETLService implements ETLService {

	private HttpDownloader httpDownloader;
	private FileToDbLoader fileToDbLoader;

	public void importMetrics()  {
		try {
			httpDownloader.download();
			fileToDbLoader.load();
			log.info("Metrics imported successfully");
		} catch (Exception e) {
			log.error("Error importing metrics", e);
		}
	}

}
