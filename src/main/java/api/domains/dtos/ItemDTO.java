package api.domains.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ItemDTO (

        String proficiency,

        @NotEmpty(message = "The 'name' cannot be empty or null")
        String name,

        @NotNull(message = "The 'qtByProduction' cannot be empty or null")
        @Min(value = 1, message = "The minimum value is 1.")
        Integer qtByProduction

) {}
