package api.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table("Proficiency")
public class Proficiency implements Serializable {

    @Id
    private Integer id;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

}
