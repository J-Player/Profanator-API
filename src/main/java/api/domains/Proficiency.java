package api.domains;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("Proficiency")
public class Proficiency {

    @Id
    private UUID id;

    @NotEmpty(message = "The 'name' cannot be empty or null")
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
