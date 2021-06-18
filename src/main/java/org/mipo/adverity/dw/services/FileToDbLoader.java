package org.mipo.adverity.dw.services;

import static org.mipo.adverity.dw.config.AdverityDwConstants.CSV_DATA_TEMP_FILE;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.sql.Connection;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.FileCopyUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class FileToDbLoader {

	private static final String MSG_DBLOADER_CREATED = "Executing script loader: '%s','%s' for file %s";
	private static final String MSG_DBLOADER_EXEC_SUCCESS = "Successfully executed script '%s', '%s'";

	private JdbcTemplate jdbc;
	private Resource extractScript;
	private Resource loaderScript;
	private String sourceFileName;

	public void load() {
		log.info(String.format(MSG_DBLOADER_CREATED, extractScript.getDescription(), loaderScript.getDescription(),
				sourceFileName));
		jdbc.execute((Connection connection) -> {
			ScriptUtils.executeSqlScript(connection, filtered(extractScript, sourceFileName));
			ScriptUtils.executeSqlScript(connection, loaderScript);
			return null;
		});
		log.info(String.format(MSG_DBLOADER_EXEC_SUCCESS, extractScript.getDescription(),
				loaderScript.getDescription()));
	}

	private Resource filtered(Resource resource, String tempFile) {
		try (Reader reader = new InputStreamReader(resource.getInputStream())) {
			return new ByteArrayResource(
					FileCopyUtils.copyToString(reader).replace(CSV_DATA_TEMP_FILE, tempFile).getBytes());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
