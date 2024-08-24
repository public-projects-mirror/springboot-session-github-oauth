package com.example.dto;

import com.example.entity.UserDO;

import java.util.function.Function;

public class UserDTO {
    private String userId;
    private String username;
    private String password;
    private String githubLoginName;

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGithubLoginName() {
        return githubLoginName;
    }

    public static Function<UserDO, UserDTO> newFromDO() {
        return new Function<UserDO, UserDTO>() {
            @Override
            public UserDTO apply(UserDO userDO) {
                UserDTO userDTO = new UserDTO();
                userDTO.userId = userDO.getUserId();
                userDTO.username = userDO.getUsername();
                userDTO.password = userDO.getPassword();
                userDTO.githubLoginName = userDO.getGithubLoginName();
                return userDTO;
            }
        };
    }
}
