package api.domains.dtos;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProficiencyDTO {

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

}
