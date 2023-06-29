package api.domains.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProficiencyDTO {

    @NotBlank(message = "The 'name' cannot be empty or null")
    private String name;

}
