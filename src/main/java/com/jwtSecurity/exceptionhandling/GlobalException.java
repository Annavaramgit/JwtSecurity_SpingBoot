package com.jwtSecurity.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalException {
	
	@ExceptionHandler(UserNotFound.class)
	public ResponseEntity<String> NosuchExeptionMethod(UserNotFound ex) {

		
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	

}
