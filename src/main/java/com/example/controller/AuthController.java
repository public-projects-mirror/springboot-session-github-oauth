package com.example.controller;

import com.example.dto.UserDTO;
import com.example.manager.BaseAuthManager;
import com.example.model.UserRequest;
import com.example.response.ApiResponse;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    private final UserService userService;
    private final BaseAuthManager authManager;

    @Autowired
    public AuthController(UserService userService, BaseAuthManager authManager) {
        this.userService = userService;
        this.authManager = authManager;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResponse<String> loginUser(@RequestBody UserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        userService.authenticate(username, password);
        UserDTO userDTO = userService.findUserByUsername(username);
        String userId = userDTO.getUserId();
        String sessionId = authManager.createSession(userId);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setMessage("Successfully logged in");
        apiResponse.setData(sessionId);
        return apiResponse;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResponse<String> registerUser(@RequestBody UserRequest userRequest) {
        String username = userRequest.getUsername();
        userService.checkUserExists(username);
        UserDTO userDTO = userService.saveUser(userRequest);
        String userId = userDTO.getUserId();
        String sessionId = authManager.createSession(userId);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setMessage("User registered successfully");
        apiResponse.setData(sessionId);
        return apiResponse;
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ApiResponse<String> home(@RequestParam String sessionId) {
        String userId = authManager.getUserId(sessionId);
        UserDTO userDTO = userService.findUserByUserId(userId);
        String username = userDTO.getUsername();
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setMessage("Welcome, " + username);
        apiResponse.setData(username);
        return apiResponse;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ApiResponse<String> logout(String sessionId) {
        authManager.deleteSessionBySessionId(sessionId);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setMessage("Successfully logged out");
        apiResponse.setData(null);
        return apiResponse;
    }
}
