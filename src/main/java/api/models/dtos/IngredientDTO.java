package api.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {

<<<<<<< HEAD:src/main/java/api/models/dtos/IngredientDTO.java
    @NotEmpty(message = "The 'product' cannot be empty or null")
    private String product;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

    @NotNull(message = "The 'quantity' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    @Builder.Default
    private int quantity = 1;
=======
        @NotBlank(message = "The 'product' cannot be empty or null")
        private String product;

        @NotBlank(message = "The 'name' cannot be empty or null")
        private String name;

        @NotNull(message = "The 'quantity' cannot be empty or null")
        @Min(value = 1, message = "The minimum value is 1.")
        private Integer quantity;
>>>>>>> main:src/main/java/api/domains/dtos/IngredientDTO.java

}
