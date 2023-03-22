package com.onebox.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartRequestDTO {
	
	private Long productId;
	
	private String description;
	
	private Long amount;

}
