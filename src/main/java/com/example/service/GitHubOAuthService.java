package com.example.service;

import com.example.exception.OAuth2TokenRetrievalException;
import com.example.exception.UserInfoProcessingException;
import com.example.manager.BaseAuthManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.transaction.Transactional;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Service
public class GitHubOAuthService {
    private static final Logger logger = LoggerFactory.getLogger(GitHubOAuthService.class);
    private static final String GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String GITHUB_USER_API_URL = "https://api.github.com/user";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    @Value("${github.redirect-uri}")
    private String redirectUri;

    @Autowired
    private BaseAuthManager authManager;

    @Autowired
    private UserService userService;

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    @Transactional
    public String getAccessToken(String requestToken) {
        OkHttpClient client = new OkHttpClient();

        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("client_id", clientId);
        bodyMap.put("client_secret", clientSecret);
        bodyMap.put("code", requestToken);
        bodyMap.put("redirect_uri", redirectUri);
        bodyMap.put("grant_type", "authorization_code");

        String reqBodyJson = new Gson().toJson(bodyMap);
        RequestBody requestBody = RequestBody.create(reqBodyJson, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(GITHUB_TOKEN_URL)
                .post(requestBody)
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new OAuth2TokenRetrievalException("Failed to get access token");
            }

            assert response.body() != null;
            String responseBody = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            return jsonObject.get("access_token").getAsString();
        } catch (IOException e) {
            logger.error("Error during OAuth2 token retrieval", e);
            throw new OAuth2TokenRetrievalException("Error during OAuth2 token retrieval", e);
        }
    }

    @Transactional
    public String processUserInfo(String accessToken, String state) {
        OkHttpClient client = new OkHttpClient();
        Request userInfoRequest = new Request.Builder()
                .url(GITHUB_USER_API_URL)
                .header("Authorization", "token " + accessToken)
                .build();

        try (Response userInfoResponse = client.newCall(userInfoRequest).execute()) {
            if (!userInfoResponse.isSuccessful()) {
                throw new UserInfoProcessingException("Failed to get user info");
            }

            assert userInfoResponse.body() != null;
            String userInfoResponseBody = userInfoResponse.body().string();
            String githubLoginName = findTextFromBody(userInfoResponseBody, "login");

            if (state == null) {
                String name = findTextFromBody(userInfoResponseBody, "name");
                String sessionId = findTextFromBody(userInfoResponseBody, "id");
                String userId = UUID.randomUUID().toString();
                authManager.saveSession(sessionId, userId);
                userService.saveGitHubUser(userId, name, githubLoginName);
                return sessionId;
            } else {
                String userId = authManager.getUserId(state);
                userService.addGithubLoginName(userId, githubLoginName);
                return null;
            }
        } catch (IOException e) {
            logger.error("Error during OAuth2 token retrieval", e);
            throw new UserInfoProcessingException("Error during user info handling", e);
        }
    }

    private String findTextFromBody(String body, String text) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(body);
            JsonNode nameNode = rootNode.path(text);
            return !nameNode.isMissingNode() ? nameNode.asText() : null;
        } catch (Exception e) {
            logger.error("Error parsing JSON response", e);
            return null;
        }
    }
}
