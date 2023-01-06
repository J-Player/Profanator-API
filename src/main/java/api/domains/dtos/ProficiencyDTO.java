package api.domains.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ProficiencyDTO {

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private final String name;

}
