package com.eden.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	private Integer id;
	private String username;
	private String realname;
	private String password;
	private boolean gender;

}
