package api.domains.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDTO {

    private String proficiency;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

    @NotNull(message = "The 'qtByProduction' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    @Builder.Default
    private int qtByProduction = 1;

}
