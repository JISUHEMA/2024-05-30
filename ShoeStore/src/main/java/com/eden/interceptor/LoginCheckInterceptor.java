package com.eden.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.eden.utils.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//1.リクエストURLを取得します。
		System.out.println("preHandle run");
		String url = request.getRequestURI().toString();
		System.out.println(url);
		
		//２.リクエストURLに「login」などが含まれているかどうかを判断する。含まれている場合、ログイン操作であると判断し、許可します。
		if(url.contains("login")|| url.contains("regist")||url.contains("img")||url.contains("script")||url.contains("css")||url.contains("Image")) {
			log.info("登録操作、許可します。");	
			return true;
		}
		
		//３，リクエストにより、セッションからトークンを取得します。
		HttpSession session = request.getSession();
		
		String jwt=null;
		if(session.getAttribute("token")!=null) {
			jwt = session.getAttribute("token").toString();
		}		
		
		//４．トークンの存在を確認し、存在しない場合はエラー結果を返します（未ログイン）
		if(!StringUtils.hasLength(jwt)) {
			log.info("リクエストのtoken空になる、未登録のメッセージを返します");	
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}
		
		//５．トークンを解析し、解析に失敗した場合はエラー結果を返します（未ログイン）
		try {
			JwtUtils.parseJWT(jwt);
		} catch (Exception e) {
			log.info("トークンを解析できない、未登録のメッセージを返します");
			e.printStackTrace();
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}		
		
		return true;
	}	

}
