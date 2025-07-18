package com.example.YAPO.controlers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utility")
@CrossOrigin(origins = "https://localhost:4200/")
public class UtilityController {
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
    @GetMapping(value = "/session", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSession(HttpServletRequest request) {
        return "{\"Session ID\": " + request.getSession().getId() + "}";
    }
}
