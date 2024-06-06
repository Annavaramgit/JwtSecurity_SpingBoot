package com.jwtSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jwtSecurity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	private UserRepository userRepository;
	
	
//the below is encoding the password,instead of store string password store this crypted
//so if some one hacks our database that guy can't get our password
@Bean
public PasswordEncoder pwdEncoder() {
	return new BCryptPasswordEncoder();	
}

//USerDetailsService,it will call repository findByEmail method 
@Bean
public UserDetailsService userDetailsService() {
	
	return username ->userRepository.findByEmail(username)
			.orElseThrow(()->new UsernameNotFoundException("userNotFoundInDB"));
			
}


//this is Authentication Provider
@Bean
public AuthenticationProvider authenticationProvider() {
	DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
	daoAuthenticationProvider.setUserDetailsService(userDetailsService());
	daoAuthenticationProvider.setPasswordEncoder(pwdEncoder());
	return daoAuthenticationProvider;
}



//this is AuthenticationManager
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
}


//security configuration,which url need to authenticate which not we mention here.
@Bean
public SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception
{
return	httpSecurity .csrf(AbstractHttpConfigurer::disable)
     .authorizeHttpRequests(req ->
             req.requestMatchers("/register")
                     .permitAll()     
                     .anyRequest()
                     .authenticated())
     .build();
            
                   
	
}
	
	
	
}
