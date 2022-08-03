package api.request.put;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Hidden
public class ProficiencyPutRequestBody {

    @NotNull(message = "The 'id' cannot be empty or null")
    private Integer id;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

}
