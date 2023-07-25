package com.api.note.controllers.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.note.dtos.auth.LoginDTORequest;
import com.api.note.dtos.auth.LoginDTOResponse;
import com.api.note.interceptor.AllowAnnonymous;
import com.api.note.services.auth.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @AllowAnnonymous
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTORequest loginDTORequest) {
        try {
            LoginDTOResponse loginDTOResponse = this.authService.login(loginDTORequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(loginDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
