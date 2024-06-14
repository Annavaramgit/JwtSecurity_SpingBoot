
package com.jwtSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


import com.jwtSecurity.exceptionhandling.UserNotFound;
import com.jwtSecurity.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfiguration {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	
	
	
//the below is encoding the password,instead of store string password store this crypted
//so if some one hacks our database that guy can't get our password
@Bean
public PasswordEncoder pwdEncoder() {
	return new BCryptPasswordEncoder();	
}

//USerDetailsService,it will call repository findByEmail method 
@Bean
public UserDetailsService userDetailsService() {
    return username -> userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFound("User Not Found With This Email:"+username+""));
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
                     .requestMatchers("/login")
                     .permitAll()
                     .anyRequest()
                     .authenticated())
     .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
     .authenticationProvider(authenticationProvider())
     .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
     .build();
            
                   
	
}
	
	
	
}
