package com.example.practice.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.practice.entity.Users;
import com.example.practice.response.ResponseHandler;
import com.example.practice.service.UserService;

@RestController
@RequestMapping("/users/")
public class UserController {

	@Autowired
	private UserService userService;

//    @PostMapping("register")
//    public ResponseEntity<String> register(@RequestBody Users user) {
//        return ResponseEntity.ok(userService.registerUser(user));
//    }
//
//    @PostMapping("login")
//    public ResponseEntity<String> login(@RequestBody Users user) {
//        return ResponseEntity.ok(userService.loginUser(user));
//    }

	ResponseHandler response = new ResponseHandler();

	@PostMapping("register")
	public ResponseHandler register(@RequestBody Users user) {
		try {
			
			String result = userService.registerUser(user);

			response.setData(result);
			response.setMessage("success");
			response.setStatus(true);

		} catch (IllegalArgumentException ex) {
			response.setData(new ArrayList<>());
			response.setMessage(ex.getMessage());
			response.setStatus(false);

		} catch (Exception e) {
			response.setData(new ArrayList<>());
			response.setMessage(e.getMessage());
			response.setStatus(false);
		}
		return response;
	}

	@PostMapping("login")
	public ResponseHandler login(@RequestBody Users user) {
		try {
			
		String token = userService.loginUser(user);
			response.setMessage("success");
			response.setData(token);
			response.setStatus(true);

		} catch (IllegalArgumentException ex) {
			response.setData(new ArrayList<>());
			response.setMessage(ex.getMessage());
			response.setStatus(false);

		} catch (Exception e) {
			response.setData(new ArrayList<>());
			response.setMessage(e.getMessage());
			response.setStatus(false);
		}
		return response;	}
}
