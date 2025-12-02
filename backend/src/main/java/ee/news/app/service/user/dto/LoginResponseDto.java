package ee.news.app.service.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginResponseDto implements Serializable {
    @Size(max = 255)
    @NotNull
    private Integer userId;
    @Size(max = 255)
    @NotNull
    private String roleName;
    private String token;
    private String refreshToken;

    @NotNull
    private String username;

    @NotNull
    private String email;
}
