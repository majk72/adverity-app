package org.mipo.adverity.dw.web.bindings;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidMetricBindingsException extends RuntimeException {
	private static final long serialVersionUID = 3902196532502673786L;

	public InvalidMetricBindingsException(String message) {
		super(message);
	}

}
