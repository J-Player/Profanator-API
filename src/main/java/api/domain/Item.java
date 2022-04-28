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
@Table("Item")
public class Item implements Serializable {

    @Id
    private Integer id;

    private String proficiency;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

    @NotEmpty(message = "The 'qtbyproduction' cannot be empty or null")
    private int qtByProduction;

}
