package kz.aparking.authservice.logout;

import jakarta.servlet.http.HttpServletRequest;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LogoutController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        String jwtToken = request.getHeader("Authorization").substring(7);
        tokenBlacklistService.addToBlacklist(jwtToken);
        return ResponseEntity.ok().build();
    }
}

