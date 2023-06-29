package api.domains;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("Proficiencies")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Proficiency {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    @NotBlank(message = "The 'name' cannot be empty or null")
    private String name;

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
