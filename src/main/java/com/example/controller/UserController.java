package com.example.controller;

import com.example.dto.UserDTO;
import com.example.manager.BaseAuthManager;
import com.example.model.AddPasswordRequest;
import com.example.response.ApiResponse;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin

public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private BaseAuthManager authManager;

    @RequestMapping(value = "/state", method = RequestMethod.GET)
    public ApiResponse<String> getUserState(@RequestParam String sessionId) {
        String userId = authManager.getUserId(sessionId);
        UserDTO userDTO = userService.findUserByUserId(userId);
        String password = userDTO.getPassword();
        String githubLoginName = userDTO.getGithubLoginName();
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK);
        if (password == null) {
            apiResponse.setMessage("User password is empty");
            apiResponse.setData("password");
            return apiResponse;
        }
        if (githubLoginName == null) {
            apiResponse.setMessage("User dont have github login name");
            apiResponse.setData("githubLoginName");
            return apiResponse;
        }
        apiResponse.setMessage("User already exists");
        apiResponse.setData("user");
        return apiResponse;
    }

    @RequestMapping(value = "/addPassword", method = RequestMethod.POST)
    public ApiResponse<String> addGithubUser(@RequestParam String sessionId, @RequestBody AddPasswordRequest addPasswordRequest) {
        String userId = authManager.getUserId(sessionId);
        String password = addPasswordRequest.getPassword();
        userService.addPassword(userId, password);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK);
        if (password == null) {
            apiResponse.setMessage("User password is empty");
        } else {
            apiResponse.setMessage("User password is added successfully");
        }
        return apiResponse;
    }

}
