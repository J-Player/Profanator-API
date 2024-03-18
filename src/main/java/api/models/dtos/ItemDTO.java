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
public class ItemDTO {

        private String proficiency;

        @NotBlank(message = "The 'name' cannot be empty or null")
        private String name;

        @Builder.Default
        @NotNull(message = "The 'qtByProduction' cannot be empty or null")
        @Min(value = 1, message = "The minimum value is 1.")
        private Integer qtByProduction = 1;

}
