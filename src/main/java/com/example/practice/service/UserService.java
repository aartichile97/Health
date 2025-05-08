package com.example.practice.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.practice.config.JwtUtil;
import com.example.practice.entity.Users;
import com.example.practice.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public String registerUser(Users users) {
		if (users == null || users.getUsername() == null || users.getUsername().trim().isEmpty()
				|| users.getPassword() == null || users.getPassword().trim().isEmpty()) {
			throw new IllegalArgumentException("Username and password must not be null or empty");
		}

		if (userRepository.findByUsername(users.getUsername()).isPresent()) {
			throw new IllegalArgumentException("Username already exists");
		}
		String hashedPassword = passwordEncoder.encode(users.getPassword());
		users.setPassword(hashedPassword);

		userRepository.save(users);
		return "User registered successfully";
	}

	public String loginUser(Users user) {
		Optional<Users> existingUser = userRepository.findByUsername(user.getUsername());
		if (existingUser.isPresent()) {
			Users dbUser = existingUser.get();

			if (passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
				return jwtUtil.generateToken(dbUser.getUsername(), dbUser);

			}
		}
		throw new IllegalArgumentException("Invalid username or password");
	}

}
