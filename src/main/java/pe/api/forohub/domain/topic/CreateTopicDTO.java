package pe.api.forohub.domain.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateTopicDTO(
    @NotNull
    Long idUser,
    @NotNull
    Long isSubject,
    @NotBlank
    String title,
    @NotBlank
    String message
) {
}
