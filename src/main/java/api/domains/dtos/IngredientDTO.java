package api.domains.dtos;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientDTO {

    @NotEmpty(message = "The 'product' cannot be empty or null")
    private String product;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

    @NotNull(message = "The 'quantity' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    @Builder.Default
    private int quantity = 1;

}
