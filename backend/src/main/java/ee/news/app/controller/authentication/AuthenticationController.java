package ee.news.app.controller.authentication;

import ee.news.app.service.user.dto.LoginDto;
import ee.news.app.service.user.dto.LoginResponseDto;
import ee.news.app.service.user.dto.RegistrationDto;
import ee.news.app.service.user.UserService;
import ee.news.app.service.user.dto.StatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Registration. Returns userId and roleName", description = """
            A new user is created in the system.
            If the username already exists, an error with error code 409 (CONFLICT) is thrown.""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "409", description = "Username already exists")})
    public ResponseEntity<?> userRegistration(@RequestBody RegistrationDto registrationDto) {
        return userService.register(registrationDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Login. Returns userId and roleName", description = """
            Searches the system for a user by username and password.
            If no match is found, an error with error code 403 (FORBIDDEN) is thrown.""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "403", description = "Invalid username or password")})
    public LoginResponseDto login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {

        LoginResponseDto loginResponse = userService.login(loginDto);

        Cookie tokenCookie = new Cookie("token", loginResponse.getToken());
        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(false);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(60 * 60);

        Cookie refreshCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(tokenCookie);
        response.addCookie(refreshCookie);
        return loginResponse;
    }

    @GetMapping("/status")
    @Operation(summary = "Checks user authentication status", description = "checks user authentication status and invalidates old cookies if they are not valid.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "403", description = "Invalid username or password")})
    public StatusDto status(HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);

        StatusDto status = new StatusDto();

        if (isAuthenticated) {
            status.setAuthenticated(true);
            status.setMessage("Session is active.");
            return status;
        } else {
            Cookie tokenCookie = new Cookie("token", null);
            tokenCookie.setHttpOnly(true);
            tokenCookie.setSecure(false);
            tokenCookie.setPath("/");
            tokenCookie.setMaxAge(0);

            Cookie refreshCookie = new Cookie("refreshToken", null);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(0);

            response.addCookie(tokenCookie);
            response.addCookie(refreshCookie);

            status.setAuthenticated(false);
            status.setMessage("Token expired or missing. Cookies cleared.");
        }
        return status;
    }

    @GetMapping("/logout")
    @Operation(summary = "Registration. Returns userId and roleName", description = """
            A new user is created in the system.
            If the username already exists, an error with error code 409 (CONFLICT) is thrown.""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "409", description = "Username already exists")})
    public StatusDto logout(HttpServletResponse response) {
        StatusDto status = new StatusDto();

        Cookie tokenCookie = new Cookie("token", null);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(false);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(0);

        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);

        response.addCookie(tokenCookie);
        response.addCookie(refreshCookie);

        status.setAuthenticated(false);
        status.setMessage("User logged out!");
        return status;
    }
}
