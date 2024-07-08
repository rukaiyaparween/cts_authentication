// RegistrationController.java
package com.authentication.controller;

import com.authentication.dto.RegistrationRequest;
import com.authentication.dto.RegistrationResponse;
import com.authentication.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Validated @RequestBody RegistrationRequest request) {
        RegistrationResponse response = registrationService.register(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        String result = registrationService.verifyToken(token);
        return ResponseEntity.ok(result);
    }
}
