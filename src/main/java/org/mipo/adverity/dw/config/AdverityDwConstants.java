package org.mipo.adverity.dw.config;

public interface AdverityDwConstants {

	String DATA_EXTRACT_SCRIPT = "${data.extract.script}";
	String DATA_LOADER_SCRIPT = "${data.loader.script:classpath:loader.sql}";
	String CSV_DATA_TEMP_FILE = "${csv.data.temp.file}";
	String CSV_DATA_URL = "${csv.data.url}";

}
