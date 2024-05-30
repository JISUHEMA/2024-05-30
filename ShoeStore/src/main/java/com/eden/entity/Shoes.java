package com.eden.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shoes {
	
	private Integer id;
	private String name;
	private Double price;
	private String size;
	private String imageUrl;

}
