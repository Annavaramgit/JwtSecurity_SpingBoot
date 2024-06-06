package com.jwtSecurity.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	//this is the secret key we were used  
	private static final String SECRET = "9a2f8c4e6b0d71f3e8b925a45747f894a3d6bc70fa8d5e21a15a6d8c3b9a0e7c";
	
	//this method generates jwt_token 
	public String tokenGenerator(UserDetails  userDetails )
	{
		//in the jwt token 3 parts are there(components) they are header,playload,signature
		//in  the header type and algorithm used there
		//in the play load or claims subject,issuedDate,expirartionDate,role etc there
		//in the signature secret key will be there
		
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				//this is below line (claim) for add role in the token ,it call  populateAuthorities
				.claim("authorities",populateAuthorities(userDetails.getAuthorities()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+6000000))
				//the below line is for secret key it calls getSignInKey() method,that HS256 is algorithm
				.signWith(getSignInKey(),SignatureAlgorithm.HS256)
				.compact();
		
	}

	//this method is for secret key decodes and stores in array
	private Key getSignInKey() {
		 byte[] keyBytes = Decoders.BASE64.decode(SECRET);
	        return Keys.hmacShaKeyFor(keyBytes);
	}
	
	//this method is for add role claim also in the Generated jwt token
	public String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<String> authoritiesSet = new HashSet<>();//this is for remove duplicates  (for ex: if we pass USER,USER  then it take one USER)
		
		for(GrantedAuthority authority:authorities) {
			authoritiesSet.add(authority.getAuthority());
			
		}
		return String.join(",", authoritiesSet);
				
		
	}

}
