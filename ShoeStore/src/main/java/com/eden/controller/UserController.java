package com.eden.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eden.entity.User;
import com.eden.service.UserService;
import com.eden.utils.JwtUtils;
import com.eden.utils.VerifyCodeUtils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
	
	@RequestMapping("login")
	public String login(String username,String password,HttpSession session) throws UnsupportedEncodingException {
		log.info("ユーザー名：{},パスワード：{}",username,password);
		
		try {
			User user= userService.login(username,password);
			
			session.setAttribute("user", user);
			
			if(user!=null) {
				Map<String,Object> claims=new HashMap<>();
				claims.put("id",user.getId());
				claims.put("username", user.getUsername());
				
				String jwt = JwtUtils.generateJwt(claims);
				
				session.setAttribute("token", jwt);
				
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return "redirect:/login?msg="+URLEncoder.encode(e.getMessage(),"UTF-8");
		}
		
		return "redirect:/shoes/lists";
	}
	
	@RequestMapping("register")
	public String register(User user,String code,HttpSession session) throws UnsupportedEncodingException {
		//System.out.println("Hello register");
		log.info("ユーザー名：{},名前：{},パスワード：{},性別：{},検証コード：{}",
				user.getUsername(),user.getRealname(),user.getPassword(),user.isGender(),code);
		
		try {
			String sessionCode = session.getAttribute("code").toString();
			
			if(!sessionCode.equalsIgnoreCase(code)) throw new RuntimeException("検証コードは間違っている");
			
			userService.register(user);
		} catch (Exception e) {
			
			e.printStackTrace();
			return "redirect:/register?msg="+URLEncoder.encode(e.getMessage(),"UTF-8");
			
		}
		
		return "redirect:/login";
	}
	
	@GetMapping("generateImageCode")
	public void generateImageCode(HttpSession session,HttpServletResponse response) throws IOException {
		String code = VerifyCodeUtils.generateVerifyCode(4);
		
		session.setAttribute("code", code);
		
		response.setContentType("image/png");
		
		ServletOutputStream os = response.getOutputStream();
		
		VerifyCodeUtils.outputImage(180, 50, os, code);
	}

}
