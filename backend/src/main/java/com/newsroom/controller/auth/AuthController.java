package com.newsroom.controller.auth;

import com.newsroom.commons.ApiPrefixConstants;
import com.newsroom.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPrefixConstants.API_MAPPING_PREFIX + "/auth")
public class AuthController {
    private final IAuthService authService;

}
