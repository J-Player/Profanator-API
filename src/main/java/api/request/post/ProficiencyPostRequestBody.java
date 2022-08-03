package api.request.post;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Hidden
public class ProficiencyPostRequestBody {

    @NotNull(message = "The 'name' cannot be empty or null")
    private String name;

}
