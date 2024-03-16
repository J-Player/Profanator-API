package api.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeDTO {

    @NotEmpty(message = "The 'item' cannot be empty or null")
    private String item;

    @NotNull(message = "The 'quantity' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    private Integer quantity;

    @NotNull(message = "The 'price' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    private Integer price;

    @NotEmpty(message = "The 'seller' cannot be empty or null")
    private String seller;

}

