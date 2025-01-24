package org.app.controller;



import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.app.dto.LoginDTO;
import org.app.dto.SignupDTO;
import org.app.entity.Role;
import org.app.entity.User;
import org.app.repository.RoleRepository;
import org.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@Autowired
	private UserRepository userRepository;

	
	@Autowired
	private RoleRepository roleRepository;
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDto )
	{
		Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		Map<String, String> response=new HashMap<>();
		response.put("status","success");
		response.put("message","User singed-in successfully!!");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupDTO signUpDto)
	{
		if(userRepository.existsByUsername(signUpDto.getUserName()))
		{
			return new ResponseEntity<>("Username is already taken!!", HttpStatus.BAD_REQUEST);
		}
		
		if(userRepository.existsByEmail(signUpDto.getEmail()))
		{
			return new ResponseEntity<>("Email is already taken!!", HttpStatus.BAD_REQUEST);
		}
		
		User user=new User();
		user.setEmail(signUpDto.getEmail());
		user.setName(signUpDto.getName());
		user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
		user.setUsername(signUpDto.getUserName());
		
		
		Role roles=roleRepository.findByName("ROLE_ADMIN").get();
		user.setRoles(Collections.singleton(roles));
		
		
		userRepository.save(user);
		Map<String, String> response=new HashMap<>();
		response.put("status","success");
		response.put("message","User Registered Successfully");
		return new ResponseEntity<>(response,HttpStatus.CREATED);
		
	}
}
