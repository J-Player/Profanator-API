package api.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProficiencyDTO {

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

}
