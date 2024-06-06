package com.jwtSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwtSecurity.model.AuthenticateRequest;
import com.jwtSecurity.model.RegisterRequest;
import com.jwtSecurity.response.AuthenticationResponse;
import com.jwtSecurity.service.AuthService_CntrlReqCameHere;

import jakarta.validation.Valid;

@RestController
public class AuthController_register_Login {

	@Autowired
	private AuthService_CntrlReqCameHere authService_CntrlReqCameHere;

	//this method is for register the user 
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> registeMethod(@Valid  @RequestBody RegisterRequest registerRequest)
	{
		System.out.println("------------controller--------------------------------------------------------");
		AuthenticationResponse authenticationResponse = authService_CntrlReqCameHere.registerMethod(registerRequest);
		
		return ResponseEntity.ok(authenticationResponse);
	}

//	@PostMapping("/authenticate")
//	public ResponseEntity<AuthenticationResponse> authenticationMethod(@RequestBody AuthenticateRequest AuthenticateRequest ){
//	
//		return authService_CntrlReqCameHere.authenticateMethod(AuthenticateRequest) ;
//	}

}
