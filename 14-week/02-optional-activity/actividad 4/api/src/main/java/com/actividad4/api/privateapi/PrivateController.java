package com.actividad4.api.privateapi;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/private")
public class PrivateController {

    @GetMapping("/hello")
    public Map<String, Object> hello(Authentication authentication) {
        return Map.of(
                "message", "Hola, acceso protegido con JWT",
                "user", authentication.getName(),
                "authorities", authentication.getAuthorities()
        );
    }
}