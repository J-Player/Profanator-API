package api.domains;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("Item")
public class Item {

    @Id
    private UUID id;

    private String proficiency;

    @NotEmpty(message = "The 'name' cannot be empty or null")
    private String name;

    @Column("qtbyproduction")
    @NotNull(message = "The 'qtByProduction' cannot be empty or null")
    @Min(value = 1, message = "The minimum value is 1.")
    @Builder.Default
    private int qtByProduction = 1;

    @Column("created_at")
    @CreatedDate
    private Instant createdAt;

    @Column("updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @Column("version")
    @Version
    private Long version;

}
