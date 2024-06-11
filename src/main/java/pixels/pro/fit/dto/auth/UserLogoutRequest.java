package pixels.pro.fit.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogoutRequest {
    @NotBlank
    private String refreshToken;

    @NotEmpty
    private Long id;

}
