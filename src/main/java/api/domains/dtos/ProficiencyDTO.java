package api.domains.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProficiencyDTO {

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

}
