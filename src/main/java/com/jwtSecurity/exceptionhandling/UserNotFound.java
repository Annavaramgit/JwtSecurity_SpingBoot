package com.jwtSecurity.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor

@Data
public class UserNotFound extends RuntimeException {
	
	
	private static final long serialVersionUID = 1L;
	public UserNotFound(String message)
	{
		super(message);
	}

}
