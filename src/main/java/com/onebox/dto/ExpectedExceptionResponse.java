package com.onebox.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpectedExceptionResponse {
	private String code;
	private String message;
	private List<String> details;
	
	public ExpectedExceptionResponse(String code, String message, List<String> details) {
		this.code = code;
		this.message = message;
		this.details = details;
	}
	
	public ExpectedExceptionResponse() {
		super();

	}
}
