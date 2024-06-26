package com.jwtSecurity.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	 @NotBlank(message = "First name is required")
	    private String firstName;

	    @NotBlank(message = "Last name is required")
	    private String lastName;

	    @Email(message = "Email should be valid")
	    @NotBlank(message = "Email is required")
	    private String email;

	    @NotNull(message = "Role is required")
	    private Role role;

	    @NotBlank(message = "Password is required")
	    private String password;
	
	
	
	
}
