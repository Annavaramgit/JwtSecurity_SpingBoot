package com.jwtSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwtSecurity.exceptionhandling.UserNotFound;
import com.jwtSecurity.model.AuthenticateRequest;
import com.jwtSecurity.model.RegisterRequest;
import com.jwtSecurity.response.AuthenticationResponse;
import com.jwtSecurity.service.AuthService_CntrlReqCameHere;

import jakarta.validation.Valid;

@RestController
public class AuthController_register_Login {

	@Autowired
	private AuthService_CntrlReqCameHere authService_CntrlReqCameHere;
	
	@Autowired
	private  AuthenticationManager authenticationManager ;

	//this method is for register the user 
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> registeMethod(@Valid  @RequestBody RegisterRequest registerRequest)
	{
		System.out.println("------------controller--------------------------------------------------------");
		AuthenticationResponse authenticationResponse = authService_CntrlReqCameHere.registerMethod(registerRequest);
		
		return ResponseEntity.ok(authenticationResponse);
	}


	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> loginMethod(@RequestBody AuthenticateRequest authenticateRequest) throws UserNotFound{
	
		AuthenticationResponse res = authService_CntrlReqCameHere.longinMethod(authenticateRequest);
		return ResponseEntity.ok(res);
	}
	
	@GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
		System.out.println("This is inside public");
        return ResponseEntity.ok("This is a public endpoint");
    }

}
