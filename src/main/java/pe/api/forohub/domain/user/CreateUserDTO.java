package pe.api.forohub.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(
    @NotBlank
    String name,
    @Email
    @NotBlank
    String email,
    @NotBlank
    String password
) {
}
