package api.models.dtos;

<<<<<<< HEAD:src/main/java/api/models/dtos/ProficiencyDTO.java
import jakarta.validation.constraints.NotEmpty;
=======
import jakarta.validation.constraints.NotBlank;
>>>>>>> main:src/main/java/api/domains/dtos/ProficiencyDTO.java
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProficiencyDTO {

    @NotBlank(message = "The 'name' cannot be empty or null")
    private String name;

}
