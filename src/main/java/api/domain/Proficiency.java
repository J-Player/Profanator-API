package api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("Proficiency")
public class Proficiency implements Serializable {

    @Id
    @NotNull(message = "The 'id' cannot be empty or null")
    private Integer id;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

}
