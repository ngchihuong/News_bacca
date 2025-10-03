package com.newsroom.controller.auth;

import com.newsroom.commons.ApiPrefixConstants;
import com.newsroom.commons.Constants;
import com.newsroom.dto.ResponseDTO.BaseOutput;
import com.newsroom.dto.UserDTO;
import com.newsroom.dto.auth.JwtResponse;
import com.newsroom.dto.auth.LoginRequest;
import com.newsroom.enums.ResponseStatus;
import com.newsroom.model.User;
import com.newsroom.service.auth.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPrefixConstants.API_MAPPING_PREFIX +"/auth")
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseOutput<?>> login(@Valid @RequestBody LoginRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest()
                    .body(
                            BaseOutput.builder()
                                    .status(ResponseStatus.FAILED)
                                    .errors(List.of(Constants.ERROR.REQUEST.INVALID_BODY))
                                    .build()
                    );
        }
        JwtResponse responses = Optional.of(this.authService.login(request)).orElse(null);


        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseOutput<?>> register(@Valid @RequestBody User user) {
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(
                            BaseOutput.builder()
                                    .status(ResponseStatus.FAILED)
                                    .errors(List.of(Constants.ERROR.REQUEST.INVALID_BODY))
                                    .build()
                    );
        }
        UserDTO userDTO =this.authService.register(user);
        return ResponseEntity.ok()
                .body(
                        BaseOutput.<UserDTO>builder()
                                .status(ResponseStatus.SUCCESS)
                                .data(userDTO)
                                .message(HttpStatus.OK.toString())
                                .build()
                );
    }
}
