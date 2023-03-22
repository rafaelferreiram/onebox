package com.onebox.exceptions;

import com.onebox.constants.ErrorCodes;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final ErrorCodes errorCodes;

	public NotFoundException(ErrorCodes errorCodes, String msg) {
		super(msg);
		this.errorCodes = errorCodes;
	}

	public ErrorCodes getErrorCode() {
		return this.errorCodes;
	}

}