package api.domains.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientDTO {

    @NotEmpty(message = "The 'product' cannot be empty or null")
    private final String product;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private final String name;

    @NotNull(message = "The 'quantity' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    @Builder.Default
    private final int quantity = 1;

}
