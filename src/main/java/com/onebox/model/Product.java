package com.onebox.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Builder
@Data
@EqualsAndHashCode
@Table(name = "PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	
	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "AMOUNT")
	private Long amount;
	
	@Column(name = "DESCRIPTION")
	private String description;

}
