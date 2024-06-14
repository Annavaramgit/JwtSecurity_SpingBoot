package com.jwtSecurity.config;

import java.io.IOException;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwtSecurity.service.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
//@RequiredArgsConstructor
public class JwtFilter extends  OncePerRequestFilter{
	
	@Autowired
	private  JwtService jwtService;
	
	@Autowired
	private   UserDetailsService userDetailsService;
	
	
	

	

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		
		//Verify whether request has Authorization header and it has Bearer in it
         String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);
         String jwt;
         String email;
        
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
		
      //Extract jwt from the Authorization
        jwt = authHeader.substring(7);
        
        
      //Verify whether user is present in db
        //Verify whether token is valid
        System.out.println("Above extractUsername...............!");
        email = jwtService.extractUsername(jwt);
        System.out.println("this is return null......................!");
        
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
            //If valid set to security context holder
            if (jwtService.isTokenValid(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                    //null
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        }
        filterChain.doFilter(request, response);
	}
	
	
	 @Override
	    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
	        return request.getServletPath().contains("/login1");
	    }

}
