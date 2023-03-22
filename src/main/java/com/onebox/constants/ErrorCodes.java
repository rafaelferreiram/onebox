package com.onebox.constants;

public enum ErrorCodes {
	INTERNAL_SERVER_ERROR("Internal server error"),
	NOT_FOUND("Not found"),
	INVALID_REQUEST("Invalid request"),
	VALIDATION_FAILED("Validation failed");
    private final String message;

    ErrorCodes(String message) {
    	this.message = message;
    }

	public String getMessage() {
    	return message;
	}
}

