package com.jwtSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtSecurity.model.AuthenticateRequest;
import com.jwtSecurity.model.RegisterRequest;
import com.jwtSecurity.model.User;
import com.jwtSecurity.repository.UserRepository;
import com.jwtSecurity.response.AuthenticationResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import lombok.Builder;
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

//public ResponseEntity<AuthenticationResponse> authenticateMethod(AuthenticateRequest authenticateRequest) {
//	
//	 authenticationManager.authenticate(
//             new UsernamePasswordAuthenticationToken(
//                     request.getEmail(),
//                     request.getPassword()
//             )
//     );
//	
//	
//	return null;
//}

}
