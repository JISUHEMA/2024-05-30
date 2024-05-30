package com.eden.service;

import com.eden.entity.User;

public interface UserService {

	void register(User user);

	User login(String username, String password);

	

}
