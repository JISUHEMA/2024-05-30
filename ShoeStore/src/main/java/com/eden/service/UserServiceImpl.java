package com.eden.service;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import com.eden.dao.UserDao;
import com.eden.entity.User;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;

	@Override
	public void register(User user) {
		User userDB= userDao.findByUserName(user.getUsername());
		
		if(!ObjectUtils.isEmpty(userDB)) throw new RuntimeException("このユーザー名は既に存在している！");
				
		String newpwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
		
		user.setPassword(newpwd);
		
		userDao.save(user);
	}

	@Override
	public User login(String username, String password) {
		User userDB= userDao.findByUserName(username);
		
		if(ObjectUtils.isEmpty(userDB)) throw new RuntimeException("このユーザー名が存在していません！");
		
		String pwdSecrete = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
		
		if(!userDB.getPassword().equals(pwdSecrete)) throw new RuntimeException("パスワードが間違っている！");
		
		return userDB;
	}

}
