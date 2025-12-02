package ee.news.app.service.user;

import ee.news.app.infrastructure.exception.EmailExistsException;
import ee.news.app.infrastructure.exception.InvalidCredentialsException;
import ee.news.app.infrastructure.exception.UsernameExistsException;
import ee.news.app.persistence.role.Role;
import ee.news.app.persistence.role.RoleRepository;
import ee.news.app.persistence.user.User;
import ee.news.app.persistence.user.UserRepository;
import ee.news.app.security.jwt.JwtUtil;
import ee.news.app.service.user.dto.LoginDto;
import ee.news.app.service.user.dto.LoginResponseDto;
import ee.news.app.service.user.dto.RegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public ResponseEntity<?> register(RegistrationDto registrationDto) {
        if (userRepository.existsBy(registrationDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UsernameExistsException("Username already exists"));
        }
        if (userRepository.existsBy(registrationDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EmailExistsException("Email already exists"));
        }

        User newUser = userMapper.registrationToUser(registrationDto);
        newUser.setRole(getDefaultRole());
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        newUser.setStatus("A");
        userRepository.save(newUser);

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    public LoginResponseDto login(LoginDto loginDto) {
        User loginUser = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new InvalidCredentialsException("No such user exists!"));

        if (!passwordEncoder.matches(loginDto.getPassword(), loginUser.getPassword())) {
            throw new InvalidCredentialsException("Password does not match");
        }

        UserDetails userDetails = new UserDetailsImpl(loginUser);
        String token = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        LoginResponseDto loginResponse = userMapper.toLoginResponse(loginUser);
        loginResponse.setToken(token);
        loginResponse.setRefreshToken(refreshToken);
        return loginResponse;
    }

    private Role getDefaultRole() {
        return roleRepository.findByName("DEFAULT").orElseGet(() -> {
            Role role = new Role("DEFAULT");
            return roleRepository.save(role);
        });
    }

}
