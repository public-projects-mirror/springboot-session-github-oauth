package com.example.controller;

import com.example.service.GitHubOAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/oauth")
@CrossOrigin
public class GitHubOAuthController {
    private static final String GITHUB_AUTHORIZE_URL = "https://github.com/login/oauth/authorize";

    @Autowired
    private GitHubOAuthService gitHubOAuthService;

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public void authorize(HttpServletResponse httpServletResponse, @RequestParam(required = false) String sessionId)
            throws IOException {
        String url = GITHUB_AUTHORIZE_URL
                + "?client_id=" + gitHubOAuthService.getClientId()
                + "&redirect_uri=" + gitHubOAuthService.getRedirectUri();

        if (sessionId != null && !sessionId.isEmpty()) {
            url += "&state=" + sessionId;
        }
        httpServletResponse.sendRedirect(url);
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public void redirect(@RequestParam("code") String requestToken,
                         @RequestParam(required = false, value = "state") String state,
                         HttpServletResponse httpServletResponse) throws Throwable {
        String accessToken = gitHubOAuthService.getAccessToken(requestToken);
        String sessionId = gitHubOAuthService.processUserInfo(accessToken, state);
        String redirectUrl = (state == null) ?
                "http://localhost:5173/home?sessionId=" + sessionId :
                "http://localhost:5173/home?state=" + state;
        httpServletResponse.sendRedirect(redirectUrl);
    }

}
