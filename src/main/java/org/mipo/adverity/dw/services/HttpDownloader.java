package org.mipo.adverity.dw.services;

import static java.lang.String.format;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpDownloader {

	private static final String MSG_INIT_DOWNLOADER_FMT = "FileDownloader initialized with url '%s' as source and temporary file '%s' ";
	private static final String MSG_DOWNLOADER_STATUS_FMT = "FileDownloader from HTTP GET returned status code %s";
	private static final int REQUEST_TIMEOUT = 10;

	private String sourceUrl;
	private HttpClient httpClient;
	private String fileName;

	public HttpDownloader(String sourceUrl, String fileName) {
		this(sourceUrl, fileName, HttpClient.newHttpClient());
	}

	public HttpDownloader(String url, String fileName, HttpClient httpClient) {
		this.sourceUrl = url;
		this.fileName = fileName;
		this.httpClient = httpClient;
		log.info(format(MSG_INIT_DOWNLOADER_FMT, url, fileName));
	}

	public int download() throws Exception {
		return withLog(httpClient.send(requestFrom(sourceUrl), lineBodyHandler()).statusCode());
	}

	private int withLog(int statusCode) {
		log.info(format(MSG_DOWNLOADER_STATUS_FMT, statusCode));
		return statusCode;
	}

	private HttpRequest requestFrom(String url) throws URISyntaxException {
		return HttpRequest.newBuilder().uri(new URI(url)).timeout(Duration.of(REQUEST_TIMEOUT, ChronoUnit.SECONDS))
				.version(HttpClient.Version.HTTP_2).GET().build();
	}

	private BodyHandler<Path> lineBodyHandler() {
		return (responseInfo) -> responseInfo.statusCode() == HttpStatus.OK.value()
				? BodySubscribers.ofFile(Path.of(fileName))
				: BodySubscribers.replacing(Path.of(fileName));
	}

}
