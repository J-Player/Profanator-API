package api.domains.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record IngredientDTO (

        @NotEmpty(message = "The 'product' cannot be empty or null")
        String product,

        @NotEmpty(message = "The 'name' cannot be empty or null")
        String name,

        @NotNull(message = "The 'quantity' cannot be empty or null")
        @Min(value = 1, message = "The minimum value is 1.")
        Integer quantity

) {}
