package com.springsecurity.controller;

import com.springsecurity.constant.RoleName;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/public") // no authentication or authorization
    public String publicc() {
        return "Hello World Public";
    }

    @GetMapping("/hello") // only authenticated somebody
    public Authentication hello(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/user") // authenticated and only users are authorized to access
    public Authentication user(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/admin") // authenticated and only admins are authorized to access
    public Authentication admin(Authentication authentication) {
        return authentication;
    }

    @RolesAllowed({RoleName.Fields.MODERATOR})
    @GetMapping("/moderator") // authenticated and only moderators are authorized to access
    public Authentication moderator(Authentication authentication) {
        return authentication;
    }

    @Secured("SUPER_ADMIN")
    @GetMapping("/super-admin") // authenticated and only super admins are authorized to access
    public Authentication superAdmin(Authentication authentication) {
        return authentication;
    }

    @PreAuthorize("hasAuthority('USER') or hasRole('ADMIN')")
    @GetMapping("/user-or-admin") // authenticated and only user or admin are authorized to access
    public Authentication userOrAdmin(Authentication authentication) {
        return authentication;
    }
}
