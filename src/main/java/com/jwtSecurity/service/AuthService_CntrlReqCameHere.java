package com.jwtSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.jwtSecurity.exceptionhandling.UserNotFound;
import com.jwtSecurity.model.AuthenticateRequest;
import com.jwtSecurity.model.RegisterRequest;
import com.jwtSecurity.model.User;
import com.jwtSecurity.repository.UserRepository;
import com.jwtSecurity.response.AuthenticationResponse;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService_CntrlReqCameHere {

@Autowired
private UserRepository userRepository;
@Autowired
private JwtService jwtService;
@Autowired
private  PasswordEncoder pwdEncoder;

private final AuthenticationManager authenticationManager; 

public AuthenticationResponse registerMethod(RegisterRequest registerRequest)
{
	System.out.println("------------registerMethod-------------------------------------------------------------");
	 var user = User.builder()   //builder means it craetes obj
             .firstName(registerRequest.getFirstName())
             .lastName(registerRequest.getLastName())
             .email(registerRequest.getEmail())
             .password(pwdEncoder.encode(registerRequest.getPassword()))
             .role(registerRequest.getRole())
             .build();
	 var savedUser = userRepository.save(user);
	 String jwtToken = jwtService.tokenGenerator(user);
	 
	 
	 return AuthenticationResponse.builder().accessToken(jwtToken).build();
}




public AuthenticationResponse longinMethod(AuthenticateRequest authenticateRequest) throws UserNotFound {

	authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
            		authenticateRequest.getEmail(),
            		authenticateRequest.getPassword()
            )
    );
	User user = userRepository.findByEmail(authenticateRequest.getEmail())
            .orElseThrow(()-> new UserNotFound("No Student Found At::"+authenticateRequest.getEmail()+" Please Check Once Agin"));
	
	 String jwtToken = jwtService.tokenGenerator(user);
	
	 return AuthenticationResponse.builder().accessToken(jwtToken).build();
}

}
