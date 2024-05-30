package com.eden.aop;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eden.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LogAspect {
		
	
	@Autowired
	private HttpServletRequest request;
	
	@Around("@annotation(com.eden.anno.Log)")
	public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
		
		//操作者のID
		HttpSession session = request.getSession();
		
		String jwt=null;
		if(session.getAttribute("token")!=null) {
			jwt = session.getAttribute("token").toString();
		}		
		
		Claims claims = JwtUtils.parseJWT(jwt);//{name=tom, id=1, exp=1714629760}
		
		Integer operateUser=(Integer)claims.get("id");
		
		//操作の時間
		LocalDateTime operateTime = LocalDateTime.now();
		
		//操作のクラス名
		String className = joinPoint.getTarget().getClass().getName();
		
		//そうさのメソッド名
		String methodName = joinPoint.getSignature().getName();
		
		//操作のパラメータ
		Object[] args = joinPoint.getArgs();		
		String methodParams = Arrays.toString(args);
		
		//メソッドの返す値		
		long begin=System.currentTimeMillis();
		
		Object result = joinPoint.proceed();
		
		long end=System.currentTimeMillis();
		
		String returnValue = result.toString();
		
		//操作掛かる時間
		Long costTime=end-begin;		
		
		log.info("AOP操作のレコード：{},{},{},{},{},{},{}",operateUser,operateTime,className,methodName,methodParams,returnValue,costTime);
		
		return result;
	}

}
