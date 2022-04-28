package api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("Ingredient")
public class Ingredient {

    @Id
    private Integer id;

    @NotEmpty(message = "The item field cannot be empty or null")
    private String item;

    @NotEmpty(message = "The ingredient field cannot be empty or null")
    private String ingredient;

    @NotEmpty(message = "The quantity cannot be empty or null")
    private int quantity;

}
