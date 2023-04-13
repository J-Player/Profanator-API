package api.domains;

import jakarta.validation.constraints.NotEmpty;
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
@Table("Proficiency")
public class Proficiency {

    @Id
    private Long id;

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
