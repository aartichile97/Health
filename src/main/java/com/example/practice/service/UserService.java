package com.example.practice.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

	public String registerUser(Users users) {
		if (userRepository.findByUsername(users.getUsername()).isPresent()) {
			return "Username already exists";
		}
		userRepository.save(users);
		return "User registered successfully";
	}

//    public String loginUser(Users users) {
//        Optional<Users> existingUser = userRepository.findByUsername(users.getUsername());
//
//        if (existingUser.isPresent()) {
//            if (existingUser.get().getPassword().equals(users.getPassword())) {
//                return "Login successful";
//            } else {
//                return "Incorrect password";
//            }
//        } else {
//            return "User not found";
//        }
//    }
	public String loginUser(Users user) {
		Optional<Users> existingUser = userRepository.findByUsername(user.getUsername());
		if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
			return jwtUtil.generateToken(user.getUsername());
		}
		return "Invalid username or password";
	}

}
