package com.jwtSecurity.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j

public class JwtService {
	
	//this is the secret key we were used  
	private static final String SECRET = "9a2f8c4e6b0d71f3e8b925a45747f894a3d6bc70fa8d5e21a15a6d8c3b9a0e7c";
	
	
	public void validateToken(final String token) {
		Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);

	}
	
	//this method generates jwt_token 
	public String tokenGenerator(UserDetails  userDetails )
	{
		//in the jwt token 3 parts are there(components) they are header,playload,signature
		//in  the header type and algorithm used there
		//in the play load or claims subject,issuedDate,expirartionDate,role etc there
		//in the signature secret key will be there
		
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				//this is below line (claim) for add role in the token ,it call  populateAuthorities() method
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

//	//this is for extracting body from token
//	 private Claims extractAllClaims(String token) {
//	        return Jwts
//	                .parserBuilder()
//	                .setSigningKey(getSignInKey())
//	                .build()
//	                .parseClaimsJws(token)
//	                .getBody();
//	    }
//	 
//	 //username extracting
//	 public String extractUsername(String token) {
//		 System.out.println("Jwt service :"+token);
//	        return extractClaim(token, Claims::getSubject);
//	    }
	
	public String extractEmailId(String token) {
		log.info("------inside extractEmailid JwtService-----");
		return (String) extractClaims(token).get("username");
	}

	public Claims extractClaims(String token) {
		log.info("------inside extractclaims JwtService----");
		Claims clims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
		log.info(clims.toString());
		return clims;
	}
	 
	 //extracting calaims
//	 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//	        final Claims claims = extractAllClaims(token);
//	        return claimsResolver.apply(claims);
//	    }
	
//	 public boolean isTokenValid(String token, UserDetails userDetails) {
//	        final String username = extractUsername(token);
//	        return (username.equals(userDetails.getUsername()));
//	    }
	

	
	
}
