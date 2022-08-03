package api.request.put;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Hidden
public class IngredientPutRequestBody {

    @Id
    private Integer id;

    @NotEmpty(message = "The 'product' cannot be empty or null")
    private String product;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

    @NotNull(message = "The 'quantity' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    @Builder.Default
    private int quantity = 1;

}
