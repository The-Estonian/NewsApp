package ee.news.app.service.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class StatusDto implements Serializable {

    @NotNull
    private boolean authenticated;

    @Size(max = 20)
    @NotNull
    private String message;

    @NotNull
    private String username;

    @NotNull
    private String email;
}
