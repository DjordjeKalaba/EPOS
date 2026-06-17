package com.epos.auth_service.api;

import com.epos.auth_service.api.dto.LoginRequest;
import com.epos.auth_service.api.dto.LoginResponse;
import com.epos.auth_service.domain.Role;
import com.epos.auth_service.domain.User;
import com.epos.auth_service.repo.TenantRepository;
import com.epos.auth_service.repo.UserRepository;
import com.epos.auth_service.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final TenantRepository tenantRepo;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(
            UserRepository userRepo,
            TenantRepository tenantRepo,
            JwtUtil jwtUtil
    ) {
        this.userRepo = userRepo;
        this.tenantRepo = tenantRepo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest req) {

        // 🔎 1. potvrda da je request stigao
        System.out.println("AUTH: /login HIT, email=" + req.email());

        User user = userRepo.findByEmail(req.email())
                .orElseThrow(() -> {
                    System.out.println("AUTH: USER NOT FOUND");
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
                });

        // 🔎 2. ispis role i tenantId
        System.out.println("AUTH: user role=" + user.getRole()
                + ", tenantId=" + user.getTenantId());

        // 🔎 3. KRITIČNI DEBUG (OVO NAM SVE GOVORI)
        System.out.println("AUTH: HASH FROM DB = [" + user.getPasswordHash() + "]");
        System.out.println("AUTH: PASSWORD MATCH = " +
                encoder.matches(req.password(), user.getPasswordHash()));

        if (!encoder.matches(req.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }

        // 👉 Samo TENANT_ADMIN mora imati READY tenant
        if (user.getRole() == Role.ROLE_TENANT_ADMIN) {

            Long tenantId = user.getTenantId();
            if (tenantId == null) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Missing tenantId"
                );
            }

            var tenant = tenantRepo.findById(tenantId)
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.FORBIDDEN, "Tenant not found"
                            )
                    );

            if (!"READY".equalsIgnoreCase(tenant.getStatus())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Tenant is not READY"
                );
            }
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getTenantId()
        );

        return new LoginResponse(
                token,
                user.getRole().name(),
                user.getTenantId() == null ? 0 : user.getTenantId()
        );
    }

}
